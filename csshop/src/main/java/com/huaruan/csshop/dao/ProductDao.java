package com.huaruan.csshop.dao;

import java.util.List;

import com.huaruan.csshop.bean.Category2;
import com.huaruan.csshop.bean.Product;

public interface ProductDao {

	List<Product> findProductListByProductName(String productName);

	void insertNewProduct(Product product);

	List<Category2> findAllCategory2List();

	Product findProductByProductId(Integer productId);

	void updateProductByProduct(Product product);

	void deleteProductById(int productId);

	List<Product> findAllHotProduct();

}
