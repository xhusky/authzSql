package com.github.authzsql;

import com.github.authzsql.provider.PermissionsProvider;
import com.github.authzsql.provider.SamplePermissionProvider;

import org.junit.Assert;
import org.junit.Test;


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
