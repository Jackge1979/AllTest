package com.sql.parse.stored.procedure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lcc on 2018/8/23.
 */
public class RegexUtil {
    public RegexUtil() {
    }

    public static String regexSelect(String text, String regexStr) {
        Pattern p = Pattern.compile(regexStr);
        Matcher m = p.matcher(text);
        return m.find() ? m.group() : null;
    }

    public static List<String> regexSelectList(String text, String regexStr) {
        Pattern p = Pattern.compile(regexStr);
        Matcher m = p.matcher(text);
        ArrayList results = new ArrayList();

        while (m.find()) {
            results.add(m.group());
        }

        return results;
    }

    public static Set<String> regexSelectSet(String text, String regexStr) {
        Pattern p = Pattern.compile(regexStr);
        Matcher m = p.matcher(text);
        HashSet results = new HashSet();

        while (m.find()) {
            results.add(m.group());
        }

        return results;
    }

}