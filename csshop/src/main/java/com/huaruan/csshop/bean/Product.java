package com.huaruan.csshop.bean;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName="product")
public class Product {

	@Id
    private Integer productId;

    private String productName;

    private BigDecimal productPriceShop;

    private BigDecimal productPriceMarket;

    private Integer category2Id;

    private String ishot;

    private String productImageUrl;
    
    private String productDescription;
    
    private Integer productQuantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public BigDecimal getProductPriceShop() {
        return productPriceShop;
    }

    public void setProductPriceShop(BigDecimal productPriceShop) {
        this.productPriceShop = productPriceShop;
    }

    public BigDecimal getProductPriceMarket() {
        return productPriceMarket;
    }

    public void setProductPriceMarket(BigDecimal productPriceMarket) {
        this.productPriceMarket = productPriceMarket;
    }

    public Integer getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Integer category2Id) {
        this.category2Id = category2Id;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot == null ? null : ishot.trim();
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl == null ? null : productImageUrl.trim();
    }

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPriceShop="
				+ productPriceShop + ", productPriceMarket=" + productPriceMarket + ", category2Id=" + category2Id
				+ ", ishot=" + ishot + ", productImageUrl=" + productImageUrl + ", productDescription="
				+ productDescription + ", productQuantity=" + productQuantity + "]";
	}
	
	
}