package com.alipay.api.response;

import com.alibaba.fastjson.annotation.JSONField;

import com.alipay.api.AlipayResponse;

/**
 * Created by bruce on 2017/12/26.
 */

public class AlipayTradePayResponse extends AlipayResponse {
    private static final long serialVersionUID = 4447185575179442381L;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "seller_id")
    private String sellerId;

    @JSONField(name = "total_amount")
    private String totalAmount;

    @JSONField(name = "trade_no")
    private String tradeNo;

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTradeNo()
    {
        return this.outTradeNo;
    }

    public void setSellerId(String sellerId)
    {
        this.sellerId = sellerId;
    }

    public String getSellerId()
    {
        return this.sellerId;
    }

    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount()
    {
        return this.totalAmount;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo()
    {
        return this.tradeNo;
    }
}
