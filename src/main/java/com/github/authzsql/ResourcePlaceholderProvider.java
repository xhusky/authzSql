package com.github.authzsql;

import com.github.authzsql.model.ResourcePlaceholder;

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
    /**
     * Extract sql condition map. key is placeholder, value is where condition
     */
    private static List<ResourcePlaceholder> resource(String originalSql) {
        final Matcher matcher = PATTERN_PLACEHOLDER.matcher(originalSql);
        List<ResourcePlaceholder> placeholders = new ArrayList<>();
        while (matcher.find()) {
            ResourcePlaceholder resourcePlaceholder = new ResourcePlaceholder();
            resourcePlaceholder.setText(matcher.group(0));
            resourcePlaceholder.setType(matcher.group(1));
            resourcePlaceholder.setColumn(matcher.group(2));

            placeholders.add(resourcePlaceholder);
        }

        return placeholders;
    }

    public static void main(String[] args) {

        System.out.println(resource(ORIGINAL_SQL));
    }

}
