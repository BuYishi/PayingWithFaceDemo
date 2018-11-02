package com.alipay.api.http;

/**
 * Created by yueweizyw on 17/9/26.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by bruce on 2017/12/25.
 */
public class HttpUtils {

    public static String postData(final String url, final Map<String, String> paramsValue) {

        //用HttpClient发送请求，分为五步
        //第一步：创建HttpClient对象
        HttpClient httpCient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        String response = "";
        try {
            //第三步：执行请求，获取服务器发还的相应对象

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : paramsValue.keySet()) {
                nvps.add(new BasicNameValuePair(key, paramsValue.get(key)));
            }

            String str = EntityUtils.toString(new UrlEncodedFormEntity(nvps, "utf-8"));

            String absoluteURL = url + "?" + str;
            Log.i("Test", "absoluteURL:" + absoluteURL);

            HttpPost httpPost = new HttpPost(absoluteURL);
            //.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse httpResponse = httpCient.execute(httpPost);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串

                Log.i("Test", "response:" + response);
            } else {
                //TODO
            }

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        
        return response;

    }
}

