package com.alipay.api;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;

import android.util.Base64;
import com.alipay.api.http.SignUtils;

import static com.alipay.api.AlipayConstants.SIGN_ALGORITHMS;
/**
 * Created by bruce on 2017/12/25.
 */
public class AlipaySignature {
    public static Object rsaSign(String signContent, String privateKey, String charset, String sign_type)
        throws Exception {
        return SignUtils.rsaSign(signContent, privateKey, charset, sign_type);
    }

    public static String getSignatureContent(RequestParametersHolder requestHolder) {

        return getSignContent(getSortedMap(requestHolder));
    }

    public static Map<String, String> getSortedMap(RequestParametersHolder requestHolder) {
        Map<String, String> sortedParams = new TreeMap();
        AlipayHashMap appParams = requestHolder.getApplicationParams();
        if ((appParams != null) && (appParams.size() > 0)) {
            sortedParams.putAll(appParams);
        }
        AlipayHashMap protocalMustParams = requestHolder.getProtocalMustParams();
        if ((protocalMustParams != null) && (protocalMustParams.size() > 0)) {
            sortedParams.putAll(protocalMustParams);
        }
        AlipayHashMap protocalOptParams = requestHolder.getProtocalOptParams();
        if ((protocalOptParams != null) && (protocalOptParams.size() > 0)) {
            sortedParams.putAll(protocalOptParams);
        }
        return sortedParams;
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String)keys.get(i);
            String value = (String)sortedParams.get(key);
            if (areNotEmpty(new String[] {key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    private static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            String[] var2 = values;
            int var3 = values.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String value = var2[var4];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }

    private static boolean isEmpty(String value) {
        int strLen;
        if (value != null && (strLen = value.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

}
