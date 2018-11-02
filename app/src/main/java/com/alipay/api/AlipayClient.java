package com.alipay.api;


/**
 * Created by bruce on 2017/12/25.
 */
public interface AlipayClient {

    public <T extends AlipayResponse> void execute(AlipayRequest<T> request, AlipayCallBack callBack);

}
