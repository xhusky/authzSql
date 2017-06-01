package com.github.authzsql;


import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            " WHERE 'K2AUTH/windFarmId/a.windFarmId'='K2AUTH/windFarmId/a.windFarmId'";

    private static final String X = "'K2AUTH/(.*?)/(.*?)'\\s*=\\s*'K2AUTH/(.*?)/(.*?)'";
    private static final String Y = "('K2AUTH/.*?/.*?')\\s*=\\s*('K2AUTH/.*?/.*?')";

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

        Pattern pattern = Pattern.compile(Y);
        Matcher matcher = pattern.matcher(ORIGINAL_SQL);

        while (matcher.find()) {
            String part1 = matcher.group(1);
            String part2 = matcher.group(2);

            System.out.println(part1);
            System.out.println(part2);
            System.out.println("---" + matcher.group(0));

            System.out.println(part1.equals(part2));
        }
    } 


} 
