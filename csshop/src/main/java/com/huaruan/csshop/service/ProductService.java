package com.huaruan.csshop.service;

import java.util.List;

import com.huaruan.csshop.bean.Category2;
import com.huaruan.csshop.bean.Product;

public interface ProductService {

	/**
	 * 根据产品名称模糊查询出产品列表
	 * @param string
	 * @return
	 */
	List<Product> findProductListByProductName(String productName);

	/**
	 * 插入新商品
	 * @param product
	 * @return "1"成功"2"异常
	 */
	String insertNewProduct(Product product);

	/**
	 * 获取全部商品二级分类列表
	 * @return
	 */
	List<Category2> findAllCategory2List();

	/**
	 * 根据商品ID查找商品
	 * @param productId
	 * @return
	 */
	Product findProductByProductId(Integer productId);

	/**
	 * 根据商品对象修改商品信息
	 * @param product
	 * @return 1成功 2异常 
	 */
	String updateProductByProduct(Product product);

	/**
	 * 根据Id删除PRODUCT
	 * @param parseInt
	 * @return 1成功2异常
	 */
	String deleteProductById(int productId);

	/**
	 * 全部热门商品列表
	 * @return
	 */
	List<Product> findAllHotProduct();

}
