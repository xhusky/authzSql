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


**SQL占位符**

|权限占位符格式：|固定前缀|资源类型|操作类型|具体列名你|
|-----------:|:-----:|:----:|:----:|:-----:|
|权限占位符实例：|K2AUTH|windfarm|EDIT|a.windFarmId|

完整占位符（包括单引号）： `'K2AUTH/windfarm/EDIT/a.windFarmId'`

**SQL填入权限实例**

```sql
SELECT
    a.id,
    a.windFarmId,
    a.windFarmName,
    'K2AUTH/windfarm/EDIT/a.windFarmId',
    'K2AUTH/windfarm/DELETE/a.windFarmId'
 
FROM
    gw_basic_windfarm a
WHERE 'K2AUTH/windfarm/VIEW/a.windFarmId'=0

```
执行权限替换后：

```sql
SELECT
    a.id,
    a.windFarmId,
    a.windFarmName,
    (SELECT COUNT(1) FROM DUAL WHERE (a.windFarmId LIKE '100040%')) AS `windfarm_edit`,
    (SELECT COUNT(1) FROM DUAL WHERE 1 = 0) AS `windfarm_delete`
 
FROM
    gw_basic_windfarm a
WHERE (a.windFarmId NOT LIKE '100060%') AND (a.windFarmId LIKE '100050%' OR a.windFarmId IN ('100070') OR 1 = 1)

```

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

```java

/**
 * SqlPermissionService Tester.
 *
 * @author wsg
 */
public class SqlPermissionServiceTest {

    private static final String ORIGINAL_SQL = "SELECT\n" +
            "    a.id,\n" +
            "    a.windFarmId,\n" +
            "    a.windFarmName\n" +
            " \n" +
            "FROM\n" +
            "    gw_basic_windfarm a\n" +
            "WHERE 'K2AUTH/windfarm/VIEW/a.windFarmId'=0";

    private static final String ORIGINAL_SQL2 = "SELECT\n" +
            "    a.id,\n" +
            "    a.windFarmId,\n" +
            "    a.windFarmName,\n" +
            "    'K2AUTH/windfarm/ALL/a.windFarmId',\n" +
            "    'K2AUTH/windfarm/EDIT/a.windFarmId',\n" +
            "    'K2AUTH/windfarm/DELETE/a.windFarmId'\n" +
            " \n" +
            "FROM\n" +
            "    gw_basic_windfarm a\n";


    private static final String ORIGINAL_SQL3 = "SELECT\n" +
            "    a.id,\n" +
            "    a.windFarmId,\n" +
            "    a.windFarmName,\n" +
            "    'K2AUTH/windfarm/ALL/a.windFarmId',\n" +
            "    'K2AUTH/windfarm/EDIT/a.windFarmId',\n" +
            "    'K2AUTH/windfarm/DELETE/a.windFarmId'\n" +
            " \n" +
            "FROM\n" +
            "    gw_basic_windfarm a\n" +
            "WHERE 'K2AUTH/windfarm/VIEW/a.windFarmId'=0";

    private static final String SQL3 = "SELECT\n" +
            "    a.id,\n" +
            "    a.windFarmId,\n" +
            "    a.windFarmName,\n" +
            "    (SELECT COUNT(1) FROM DUAL WHERE (a.windFarmId LIKE '100040%')) AS `windfarm_edit`,\n" +
            "(SELECT COUNT(1) FROM DUAL WHERE (a.windFarmId NOT LIKE '100060%') AND (a.windFarmId LIKE '100050%' OR a.windFarmId IN ('100070') OR 1 = 1)) AS `windfarm_view`,\n" +
            "    (SELECT COUNT(1) FROM DUAL WHERE (a.windFarmId LIKE '100040%')) AS `windfarm_edit`,\n" +
            "    (SELECT COUNT(1) FROM DUAL WHERE 1 = 0) AS `windfarm_delete`\n" +
            " \n" +
            "FROM\n" +
            "    gw_basic_windfarm a\n" +
            "WHERE (a.windFarmId NOT LIKE '100060%') AND (a.windFarmId LIKE '100050%' OR a.windFarmId IN ('100070') OR 1 = 1)";

    /**
     * Method: transformSql()
     */
    @Test
    public void testTransformSql() throws Exception {
        PermissionsProvider permissionsProvider = new SamplePermissionProvider();
        String sql = new SqlPermissionService.Builder()
            .sql(ORIGINAL_SQL)
            .permissionsProvider(permissionsProvider)
            .build()
            .transformSql();
        System.out.println(sql);

        System.out.println("-------------------------------------------------------------------");

        String sql2 = new SqlPermissionService.Builder()
            .sql(ORIGINAL_SQL2)
            .permissionsProvider(permissionsProvider)
            .build()
            .transformSql();
        System.out.println(sql2);

        System.out.println("-------------------------------------------------------------------");

        String sql3 = new SqlPermissionService.Builder()
            .sql(ORIGINAL_SQL3)
            .permissionsProvider(permissionsProvider)
            .build()
            .transformSql();
        System.out.println(sql3);

        Assert.assertEquals(sql3, SQL3);
    }

} 
```

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

这里的CachePermissionsProvider就是你自己实现的PermissionsProvider

```java
SqlPermissionService sqlPermissionService = new SqlPermissionService.Builder()
    .sql(originalSql)
    .permissionsProvider(new CachePermissionsProvider())
    .build();
```
