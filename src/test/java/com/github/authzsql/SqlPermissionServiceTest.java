package com.github.authzsql; 

import org.junit.Test;


/** 
* SqlPermissionService Tester. 
* 
* @author Think Wong
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
    /** 
    * 
    * Method: transformSql() 
    * 
    */ 
    @Test
    public void testTransformSql() throws Exception { 
        String sql = new SqlPermissionService.Builder().sql(ORIGINAL_SQL).build().transformSql();
        System.out.println(sql);

        System.out.println("-------------------------------------------------------------------");

        String sql2 = new SqlPermissionService.Builder().sql(ORIGINAL_SQL2).build().transformSql();
        System.out.println(sql2);

        System.out.println("-------------------------------------------------------------------");

        String sql3 = new SqlPermissionService.Builder().sql(ORIGINAL_SQL3).build().transformSql();
        System.out.println(sql3);
    }

} 
