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
            " WHERE 'K2AUTH/windfarm/a.windFarmId'<>''";

    /**
    * 
    * Method: needAuthorization(String sql)
    * 
    */ 
    @Test
    public void testNeedAuthorization() throws Exception {
        Assert.assertEquals(true, SqlPermissionHelper.needAuthorization(ORIGINAL_SQL));
    }

    /** 
    * 
    * Method: generateAuthorizationSql()
    * 
    */ 
    @Test
    public void testGenerateAuthorizationSql() throws Exception {
        String sql = new SqlPermissionHelper.Builder()
                .sql(ORIGINAL_SQL)
                .build()
                .generateAuthorizationSql();
        System.out.println(sql);

    }

} 
