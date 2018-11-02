package com.alipay.api;


/**
 * Created by bruce on 2017/12/25.
 */
public class RequestParametersHolder {
    public AlipayHashMap protocalMustParams = null;
    public AlipayHashMap protocalOptParams = null;
    public AlipayHashMap applicationParams = new AlipayHashMap();

    public void setApplicationParams(AlipayHashMap appParams) {
        applicationParams.putAll(appParams);
    }

    public void setProtocalMustParams(AlipayHashMap protocalMustParams) {
        this.protocalMustParams = protocalMustParams;
    }

    public void setProtocalOptParams(AlipayHashMap protocalOptParams) {
        this.protocalOptParams = protocalOptParams;
    }

    public AlipayHashMap getProtocalMustParams() {
        return this.protocalMustParams;
    }

    public AlipayHashMap getProtocalOptParams() {
        return this.protocalOptParams;
    }

    public AlipayHashMap getApplicationParams() {
        return applicationParams;
    }
}
