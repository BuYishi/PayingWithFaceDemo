package com.example.wolf.paying_with_face_demo;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayCallBack;
import com.alipay.api.AlipayConstants;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZolozAuthenticationCustomerSmilepayInitializeRequest;
import com.alipay.api.response.ZolozAuthenticationCustomerSmilepayInitializeResponse;

public class ZimDataGetter {
    private Activity activity;

    public ZimDataGetter(Activity activity) {
        this.activity = activity;
    }

    public void getZimData(String appId, String privateKey, String metaInfo, final Callback callback) {
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(AlipayConstants.SERVER_URL, appId, privateKey, "json", "utf-8",
                null, "RSA2");
        ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
        request.setBizContent(metaInfo);
        alipayClient.execute(request, new AlipayCallBack() {
            @Override
            public <T extends AlipayResponse> T onResponse(final T response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response == null)
                            callback.onFail(null, "Cannot get server response, please check network");
                        else {
                            String code = response.getCode();
                            if (code.equals(AlipayResponse.ERROR_CODE_SUCCESS)) {
                                ZolozAuthenticationCustomerSmilepayInitializeResponse zolozResponse =
                                        (ZolozAuthenticationCustomerSmilepayInitializeResponse) response;
                                String result = zolozResponse.getResult();
                                JSONObject resultJson = JSON.parseObject(result);
                                String zimId = resultJson.getString("zimId");
                                String zimInitClientData = resultJson.getString("zimInitClientData");
                                callback.onSuccess(zimId, zimInitClientData);
                            } else
                                callback.onFail(code, null);
                        }
                    }
                });
                return null;
            }
        });
    }

    public interface Callback {
        void onSuccess(String zimId, String zimInitClientData);

        void onFail(String errorCode, String errorMessage);
    }
}