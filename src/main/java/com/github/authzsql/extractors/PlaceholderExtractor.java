package com.github.authzsql.extractors;

import com.github.authzsql.model.SqlPlaceholder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class PlaceholderExtractor {
    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile("'K2AUTH/(.*?)/(.*?)'\\s*<>\\s*'\\s*'");

    private static class InstanceHolder {
        private static final PlaceholderExtractor INSTANCE = new PlaceholderExtractor();
    }

    public static PlaceholderExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    private static SqlPlaceholder extract(String placeholder) {
        final Matcher matcher = PATTERN_PLACEHOLDER.matcher(placeholder);
        if (matcher.find()) {
            SqlPlaceholder sqlPlaceholder = new SqlPlaceholder();
            sqlPlaceholder.setText(matcher.group(0));
            sqlPlaceholder.setType(matcher.group(1));
            sqlPlaceholder.setColumn(matcher.group(2));
        }
        return null;
    }
}
