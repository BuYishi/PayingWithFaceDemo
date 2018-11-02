package com.alipay.api;

import java.util.HashMap;

/**
 * Created by bruce on 2017/12/25.
 */
public class WebUtils {

    public static String buildQuery(HashMap<String, String> protocalOptParams, String charset) {
        StringBuilder queryString = new StringBuilder();

        for (String item : protocalOptParams.keySet()) {
            if (queryString.length() == 0) {
                queryString.append(item + "=" + protocalOptParams.get(item));
            } else {
                queryString.append("&" + item + "=" + protocalOptParams.get(item));
            }

        }
        return queryString.toString();
    }

}
