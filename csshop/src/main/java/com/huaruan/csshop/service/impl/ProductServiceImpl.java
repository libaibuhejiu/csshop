package com.huaruan.csshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huaruan.csshop.bean.Category2;
import com.huaruan.csshop.bean.Product;
import com.huaruan.csshop.dao.ProductDao;
import com.huaruan.csshop.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	
	@Override
	public List<Product> findProductListByProductName(String productName) {
		return productDao.findProductListByProductName(productName);
	}

	@Override
	public String insertNewProduct(Product product) {
		try {
			productDao.insertNewProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	@Override
	public List<Category2> findAllCategory2List() {
		return productDao.findAllCategory2List();
	}

	@Override
	public Product findProductByProductId(Integer productId) {
		return productDao.findProductByProductId(productId);		
	}

	@Override
	public String updateProductByProduct(Product product) {
		try {
			productDao.updateProductByProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	@Override
	public String deleteProductById(int productId) {
		try {
			productDao.deleteProductById(productId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	@Override
	public List<Product> findAllHotProduct() {
		return productDao.findAllHotProduct();
	}

}
