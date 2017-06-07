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
```