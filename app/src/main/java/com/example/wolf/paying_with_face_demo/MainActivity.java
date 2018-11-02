package com.example.wolf.paying_with_face_demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private AlipayZoloz alipayZoloz;
    private HashMap merchantInfo;
    private Activity currentContext = this;
    private static final String MERCHANT_ID = "Your merchant id";
    private static final String APP_ID = "Your app id";
    private static final String PRIVATE_KEY = "Your private key";
    private static final String DEVICE_NUMBER = "Your device number";
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.payWithFaceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipayZoloz.getMetaInfo(merchantInfo, new AlipayZoloz.Callback() {
                    @Override
                    public void onSuccess(String metaInfo) {
                        new ZimDataGetter(currentContext).getZimData(APP_ID, PRIVATE_KEY, metaInfo, new ZimDataGetter.Callback() {
                            @Override
                            public void onSuccess(String zimId, String zimInitClientData) {
                                HashMap params = new HashMap();
                                params.put("zim.init.resp", zimInitClientData);
                                alipayZoloz.verify(zimId, params, new AlipayZoloz.Callback() {
                                    @Override
                                    public void onSuccess(String ftoken) {
                                        Log.d(TAG, "ftoken: " + ftoken);
                                        Toast.makeText(currentContext, "ftoken: " + ftoken, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFail(String errorCode) {
                                        Log.e(TAG, "verify --- errorCode: " + errorCode);
                                        switch (errorCode) {
                                            case ZolozConstants.ZOLOZ_ERROR_CODE_EXIT:
                                                Toast.makeText(currentContext, "已退出刷脸支付", Toast.LENGTH_SHORT).show();
                                                break;
                                            case ZolozConstants.ZOLOZ_ERROR_CODE_TIMEOUT:
                                                Toast.makeText(currentContext, "刷脸支付超时", Toast.LENGTH_SHORT).show();
                                                break;
                                            case ZolozConstants.ZOLOZ_ERROR_CODE_PAYMENT_METHOD_CHANGE:
                                                Toast.makeText(currentContext, "已退出刷脸支付，更改支付方式", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFail(String errorCode, String errorMessage) {
                                Log.e(TAG, "getZimData --- errorCode: " + errorCode);
                                Log.e(TAG, "getZimData --- errorMessage: " + errorMessage);
                                if (errorCode == null)
                                    Toast.makeText(currentContext, "无法获取服务器回应，请检查网络", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(currentContext, "刷脸支付失败，错误码：" + errorCode, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFail(String errorCode) {
                        Log.e(TAG, "errorCode: " + errorCode);
                        Toast.makeText(currentContext, "刷脸支付失败，错误码：" + errorCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alipayZoloz = AlipayZoloz.getInstance(this);
        merchantInfo = getMerchantInfo();
        alipayZoloz.install(merchantInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alipayZoloz.uninstall();
    }

    private HashMap getMerchantInfo() {
        HashMap merchantInfo = new HashMap();
        merchantInfo.put("merchantId", MERCHANT_ID);
        merchantInfo.put("partnerId", MERCHANT_ID);
        merchantInfo.put("appId", APP_ID);
        merchantInfo.put("deviceNum", DEVICE_NUMBER);
        return merchantInfo;
    }
}