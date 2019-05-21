package com.huaruan.csshop.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer orderId;

    private String orderNum;

    private BigDecimal orderTotalprice;

    private Date orderCreatedate;

    private Date orderPaydate;

    private Integer userId;

    private String orderPaystatus;

    private String orderValid;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public BigDecimal getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(BigDecimal orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public Date getOrderCreatedate() {
        return orderCreatedate;
    }

    public void setOrderCreatedate(Date orderCreatedate) {
        this.orderCreatedate = orderCreatedate;
    }

    public Date getOrderPaydate() {
        return orderPaydate;
    }

    public void setOrderPaydate(Date orderPaydate) {
        this.orderPaydate = orderPaydate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderPaystatus() {
        return orderPaystatus;
    }

    public void setOrderPaystatus(String orderPaystatus) {
        this.orderPaystatus = orderPaystatus == null ? null : orderPaystatus.trim();
    }

    public String getOrderValid() {
        return orderValid;
    }

    public void setOrderValid(String orderValid) {
        this.orderValid = orderValid == null ? null : orderValid.trim();
    }
}