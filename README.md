# authzSql

**常用操作类型**

VIEW, EDIT, DELETE, EXECUTE, CANCEL, DOWNLOAD

**常用匹配类型**

LIKE, NOT LIKE, IN, NOT IN, =, <>

**资源存储**

- LIKE存储： LIKE:122%
- IN存储： IN:310230,320602,320624
- EQUAL存储： EQUAL:620302 
- NOT LIKE存储： NOT_LIKE:122%
- NOT IN存储： NOT_IN:310230,320602,320624
- NOT EQUAL存储： NOT_EQUAL:620302
- ALL存储： *


`注意`：资源存储值为除`*`外，如果不加前缀则认为是`EQUAL`，比如：资源存储值为`620302`，则等价于`EQUAL:620302`，对应sql语句为 wfid = '620302'，资源存储值为`310230,320602,320624`, 则等价于`EQUAL:310230,320602,320624`，即整个值认为是一个资源，对应sql语句为wfid = '310230,320602,320624'。


**SQL占位符**

|权限占位符格式：|固定前缀|资源类型|操作类型|具体列名|
|-----------:|:-----:|:----:|:----:|:-----:|
|权限占位符实例：|K2AUTH|windfarm|EDIT|a.windFarmId|

完整占位符（包括单引号）：
 
- 操作权限： `'K2AUTH/windfarm/EDIT/a.windFarmId'`
- 数据查看权限： `'K2AUTH/windfarm/VIEW/a.windFarmId'=0`


`注意1`：`固定前缀` **必须为字母开头**，否则` 'K2AUTH/windfarm/VIEW/a.windFarmId'=0 ` 为false。

`注意2`：`具体列名` 如果存在表别名则必须加上别名，比如这里的 `a.windFarmId` 不能写成`windFarmId`，否则替换后的sql语句有语法错误。


**SQL填入权限实例**

需要替换的sql语句：
```sql
SELECT
    a.id,
    a.windFarmId,
    a.windFarmName,
    'K2AUTH/windfarm/EDIT/a.windFarmId', -- 操作权限占位符
    'K2AUTH/windfarm/DELETE/a.windFarmId' -- 操作权限占位符
 
FROM
    gw_basic_windfarm a
WHERE 'K2AUTH/windfarm/VIEW/a.windFarmId'=0 -- 数据查看权限占位符

```

权限数据为

resourceType | resourceInfo | operation
------------ | ------------ | ---------
windfarm|NOT_LIKE:100060%|VIEW
windfarm|LIKE:100050%|VIEW
windfarm|IN:100070,100071|VIEW
windfarm|LIKE:100040%|EDIT

则执行权限替换后：

```sql
SELECT
    a.id,
    a.windFarmId,
    a.windFarmName,
    (SELECT COUNT(1) FROM DUAL WHERE (a.windFarmId LIKE '100040%')) AS `windfarm_edit`,
    (SELECT COUNT(1) FROM DUAL WHERE 1 = 0) AS `windfarm_delete`
 
FROM
    gw_basic_windfarm a
WHERE (a.windFarmId NOT LIKE '100060%') AND (
    a.windFarmId LIKE '100050%' OR a.windFarmId IN ('100070','100071') OR 1 = 1
)

```


因为没有DELETE权限，所以`'K2AUTH/windfarm/DELETE/a.windFarmId'`被替换成``(SELECT COUNT(1) FROM DUAL WHERE 1 = 0) AS`windfarm_delete` ``

**使用实例**


```java
public class SqlPermissionServiceTest {
    @Test
    public void testTransformSql() throws Exception {
        static final String ORIGINAL_SQL = "SELECT\n" +
                "    a.id,\n" +
                "    a.windFarmId,\n" +
                "    a.windFarmName\n" +
                " \n" +
                "FROM\n" +
                "    gw_basic_windfarm a\n" +
                "WHERE 'K2AUTH/windfarm/VIEW/a.windFarmId'=0";
        
        String sql = new SqlPermissionService.Builder()
                .sql(ORIGINAL_SQL)
                .permissionsProvider(new YourPermissionProvider())
                .build()
                .transformSql();
        System.out.println(sql);
    }
}
```

`注意`： `YourPermissionProvider`为自己PermissionProvider接口的实现


**mybatis拦截器中使用**

```java

/**
 * 权限拦截，只拦截查询语句.权限控制条件筛选，动态修改sql where语句
 *
 * @author wsg
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (Global.TRUE.equals(Md4xConstants.REQUIRES_PERMISSIONS)) {

            final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

            Object parameter = invocation.getArgs()[1];
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);

            if (!StringUtils.isBlank(boundSql.getSql())) {
                String originalSql = boundSql.getSql().trim();
                
                SqlPermissionService sqlPermissionService = new SqlPermissionService.Builder()
                    .sql(originalSql)
                    .permissionsProvider(new CachePermissionsProvider())
                    .build();
                
                // 判断是否需要做sql权限转换
                if (sqlPermissionService.support()) {
                    String permissionSql = sqlPermissionService.transformSql();

                    boundSql = new BoundSql(mappedStatement.getConfiguration(), permissionSql,
                        boundSql.getParameterMappings(), boundSql.getParameterObject());
                    MappedStatement newMs = copyFromMappedStatement(mappedStatement,
                        new BoundSqlSqlSource(boundSql));
                    invocation.getArgs()[0] = newMs;
                }
            }
        }

        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
        SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
            ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {

        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
```

这里的`CachePermissionsProvider`就是你自己实现的`PermissionsProvider`

```java
SqlPermissionService sqlPermissionService = new SqlPermissionService.Builder()
    .sql(originalSql)
    .permissionsProvider(new CachePermissionsProvider())
    .build();
```

**配置mybatis拦截器 mybatis-config.xml**

plugin 的执行顺序为先执行后方配置，即PermissionInterceptor -> PaginationInterceptor

    <!-- 插件配置 -->
    <plugins>
        <plugin interceptor="com.k2data.platform.common.persistence.interceptor.PaginationInterceptor"/>
        <plugin interceptor="com.k2data.platform.common.persistence.interceptor.PermissionInterceptor"/>
    </plugins>


> 具体参考 gw-md4x, com.k2data.platform.common.authzsql