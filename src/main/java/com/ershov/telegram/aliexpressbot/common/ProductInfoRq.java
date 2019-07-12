package com.ershov.telegram.aliexpressbot.common;

import java.io.Serializable;
import java.util.Objects;

public class ProductInfoRq implements Serializable {

    private String productId;
    private String currency;
    private String locale;

    public ProductInfoRq(String productId, String currency, String locale) {
        this.productId = productId;
        this.currency = currency;
        this.locale = locale;
    }

    public ProductInfoRq() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInfoRq that = (ProductInfoRq) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productId, currency, locale);
    }

    @Override
    public String toString() {
        return "ProductInfoRq{" +
                "productId='" + productId + '\'' +
                ", currency='" + currency + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
