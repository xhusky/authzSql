# authzSql


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
                .permissionsProvider(new SamplePermissionProvider())
                .build()
                .transformSql();
        System.out.println(sql);
    }
```