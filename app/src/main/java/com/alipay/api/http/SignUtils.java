package com.alipay.api.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 2017/12/25.
 */
public class SignUtils {

    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        ArrayList keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for(int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            String value = (String)sortedParams.get(key);
            if(areNotEmpty(new String[]{key, value})) {
                content.append((index == 0?"":"&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    public static String rsaSign(String content, String privateKey, String charset, String signType) throws Exception {
        if("RSA".equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if("RSA2".equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new Exception("Sign Type is Not Support : signType=" + signType);
        }
    }

    public static String rsa256Sign(String content, String privateKey, String charset) throws Exception {
        try {
            PrivateKey e = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(e);
            if(isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception var6) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, var6);
        }
    }

    public static String rsaSign(String content, String privateKey, String charset) throws Exception {
        try {
            PrivateKey e = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(e);
            if(isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException var6) {
            throw new Exception("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", var6);
        } catch (Exception var7) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, var7);
        }
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if(ins != null && !isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm, "BC");
            byte[] encodedKey = readText(ins).getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if(value != null && (strLen = value.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if(values != null && values.length != 0) {
            String[] var2 = values;
            int var3 = values.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String value = var2[var4];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void io(InputStream in, OutputStream out) throws IOException {
        io((InputStream)in, (OutputStream)out, -1);
    }

    public static void io(InputStream in, OutputStream out, int bufferSize) throws IOException {
        if(bufferSize == -1) {
            bufferSize = 8192;
        }

        byte[] buffer = new byte[bufferSize];

        int amount;
        while((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

    }

    public static void io(Reader in, Writer out) throws IOException {
        io((Reader)in, (Writer)out, -1);
    }

    public static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if(bufferSize == -1) {
            bufferSize = 4096;
        }

        char[] buffer = new char[bufferSize];

        int amount;
        while((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

    }

    public static OutputStream synchronizedOutputStream(OutputStream out) {
        return new SynchronizedOutputStream(out);
    }

    public static OutputStream synchronizedOutputStream(OutputStream out, Object lock) {
        return new SynchronizedOutputStream(out, lock);
    }

    public static String readText(InputStream in) throws IOException {
        return readText(in, (String)null, -1);
    }

    public static String readText(InputStream in, String encoding) throws IOException {
        return readText(in, encoding, -1);
    }

    public static String readText(InputStream in, String encoding, int bufferSize) throws IOException {
        InputStreamReader reader = encoding == null?new InputStreamReader(in):new InputStreamReader(in, encoding);
        return readText(reader, bufferSize);
    }

    public static String readText(Reader reader) throws IOException {
        return readText(reader, -1);
    }

    public static String readText(Reader reader, int bufferSize) throws IOException {
        StringWriter writer = new StringWriter();
        io((Reader)reader, (Writer)writer, bufferSize);
        return writer.toString();
    }

    private static class SynchronizedOutputStream extends OutputStream {
        private OutputStream out;
        private Object lock;

        SynchronizedOutputStream(OutputStream out) {
            this(out, out);
        }

        SynchronizedOutputStream(OutputStream out, Object lock) {
            this.out = out;
            this.lock = lock;
        }

        public void write(int datum) throws IOException {
            Object var2 = this.lock;
            synchronized(this.lock) {
                this.out.write(datum);
            }
        }

        public void write(byte[] data) throws IOException {
            Object var2 = this.lock;
            synchronized(this.lock) {
                this.out.write(data);
            }
        }

        public void write(byte[] data, int offset, int length) throws IOException {
            Object var4 = this.lock;
            synchronized(this.lock) {
                this.out.write(data, offset, length);
            }
        }

        public void flush() throws IOException {
            Object var1 = this.lock;
            synchronized(this.lock) {
                this.out.flush();
            }
        }

        public void close() throws IOException {
            Object var1 = this.lock;
            synchronized(this.lock) {
                this.out.close();
            }
        }
    }

}
