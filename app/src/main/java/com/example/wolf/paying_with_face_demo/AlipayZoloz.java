package com.example.wolf.paying_with_face_demo;

import android.app.Activity;
import android.util.Log;

import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.alipay.zoloz.smile2pay.service.ZolozCallback;

import java.util.Map;

public class AlipayZoloz {
    private Zoloz zoloz;
    private Activity activity;
    private static AlipayZoloz instance;
    private static final String TAG = AlipayZoloz.class.getName();

    private AlipayZoloz(Activity activity) {
        zoloz = Zoloz.getInstance(activity);
        this.activity = activity;
    }

    public void install(Map merchantInfo) {
        zoloz.zolozInstall(merchantInfo);
    }

    public void uninstall() {
        zoloz.zolozUninstall();
    }

    public void getMetaInfo(Map merchantInfo, final Callback callback) {
        zoloz.zolozGetMetaInfo(merchantInfo, new AlipayZolozCallback(activity) {
            @Override
            public void onResponse(Map response) {
                String code = (String) response.get("code");
                Log.d(TAG, "ZolozCallback --- code: " + code);
                String metaInfo = (String) response.get("metainfo");
                if (code.equals(ZolozConstants.ZOLOZ_ERROR_CODE_SUCCESS))
                    callback.onSuccess(metaInfo);
                else
                    callback.onFail(code);
            }
        });
    }

    public void verify(String zimId, Map params, final Callback callback) {
        zoloz.zolozVerify(zimId, params, new AlipayZolozCallback(activity) {
            @Override
            public void onResponse(Map response) {
                String code = (String) response.get("code");
                if (code.equals(ZolozConstants.ZOLOZ_ERROR_CODE_SUCCESS)) {
                    String ftoken = (String) response.get("ftoken");
                    callback.onSuccess(ftoken);
                } else
                    callback.onFail(code);
            }
        });
    }

    public static AlipayZoloz getInstance(Activity activity) {
        if (instance == null)
            instance = new AlipayZoloz(activity);
        return instance;
    }

    public interface Callback {
        void onSuccess(String data);

        void onFail(String errorCode);
    }

    private abstract class AlipayZolozCallback implements ZolozCallback {
        private Activity activity;

        public AlipayZolozCallback(Activity activity) {
            this.activity = activity;
        }

        public abstract void onResponse(Map response);

        @Override
        public void response(final Map map) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onResponse(map);
                }
            });
        }
    }
}