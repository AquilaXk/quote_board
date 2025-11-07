package com.back;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Rq {
    private final String actionName;
    private final Map<String, String> paramsMap;


    public Rq(String input) {
        String[] cmdBits = input.split("\\?", 2);
        actionName = cmdBits[0];

        String queryString = cmdBits.length == 2 ? cmdBits[1].trim() : "";

        String[] querStringBits = queryString.split("&");

        paramsMap = Arrays.stream(querStringBits)
                .map(queryParm -> queryParm.split("=", 2))
                .filter(bits -> bits.length == 2 && !bits[1].trim().isEmpty())
                .collect(Collectors.toMap(
                        bits -> bits[0].trim(),
                        bits -> bits[1].trim()
                ));
    }

    public String getActionName() {
        return actionName;
    }

    public String getParam(String paramName, String defaultValue) {
        return paramsMap.getOrDefault(paramName, defaultValue);
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        String value = getParam(paramName, "");

        if (value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {return defaultValue;}
    }
}
