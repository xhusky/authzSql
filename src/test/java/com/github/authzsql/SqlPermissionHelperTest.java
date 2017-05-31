package com.github.authzsql;


import org.junit.Assert;
import org.junit.Test;

/**
* SqlPermissionHelper Tester. 
* 
* @author Think Wong
*/ 
public class SqlPermissionHelperTest {

    private static final String ORIGINAL_SQL = "SELECT\n" +
            " a.id , a.windfarmid , a.windfarmname, a.longitude, a.latitude, a.windturbinetype,\n" +
            " a.projectname, a.projectid, a.region, a.assemblycapacity, a.assemblyquantity\n" +
            " FROM gw_basic_windfarm a\n" +
            " WHERE 'IAmPlaceholder-a.windFarmId'='IAmPlaceholder-a.windFarmId'" +
            " AND 'IAmPlaceholder-a.windfarmname'='IAmPlaceholder-a.windfarmname'";
    /** 
    * 
    * Method: needAuthz(String sql) 
    * 
    */ 
    @Test
    public void testNeedAuthz() throws Exception {
        Assert.assertEquals(true, SqlPermissionHelper.needAuthz(ORIGINAL_SQL));
    }

    /** 
    * 
    * Method: generateAuthzSql() 
    * 
    */ 
    @Test
    public void testGenerateAuthzSql() throws Exception {
        String sql = new SqlPermissionHelper.Builder()
                .sql(ORIGINAL_SQL)
                .build()
                .generateAuthzSql();
        System.out.println(sql);
    } 


} 
