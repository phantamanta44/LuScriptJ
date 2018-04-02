package io.github.phantamanta44.lsj.util;

public class Utils {

    public static String parseEscapes(String s) {
        // TODO write an escaping algo that doesn't suck
        return s.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\\\\r", "\r");
    }

}
