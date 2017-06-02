package com.github.authzsql;

import com.github.authzsql.model.SqlPlaceholder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class ResourcePlaceholderProvider {

    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile("'K2AUTH/(.*?)/(.*?)'\\s*=\\s*'K2AUTH/(.*?)/(.*?)'");
    private static final String ORIGINAL_SQL = "SELECT\n" +
            " a.id , a.windfarmid , a.windfarmname, a.longitude, a.latitude, a.windturbinetype,\n" +
            " a.projectname, a.projectid, a.region, a.assemblycapacity, a.assemblyquantity\n" +
            " FROM gw_basic_windfarm a\n" +
            " WHERE 'K2AUTH/windFarmId/a.windFarmId'='K2AUTH/windFarmId/a.windFarmId'";

    private static List<SqlPlaceholder> resource(String originalSql) {
        final Matcher matcher = PATTERN_PLACEHOLDER.matcher(originalSql);
        List<SqlPlaceholder> placeholders = new ArrayList<>();
        while (matcher.find()) {
            SqlPlaceholder sqlPlaceholder = new SqlPlaceholder();
            sqlPlaceholder.setText(matcher.group(0));
            sqlPlaceholder.setType(matcher.group(1));
            sqlPlaceholder.setColumn(matcher.group(2));

            placeholders.add(sqlPlaceholder);
        }

        return placeholders;
    }

    public static void main(String[] args) {

        System.out.println(resource(ORIGINAL_SQL));
    }

}
