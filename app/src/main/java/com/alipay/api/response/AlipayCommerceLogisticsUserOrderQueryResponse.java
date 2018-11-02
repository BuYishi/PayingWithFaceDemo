package com.alipay.api.response;

import com.alibaba.fastjson.annotation.JSONField;

import com.alipay.api.AlipayResponse;

/**
 * Created by bruce on 2017/12/25.
 */

public class AlipayCommerceLogisticsUserOrderQueryResponse extends AlipayResponse {

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    @JSONField(name = "order_info")
    String orderInfo = "";
}
