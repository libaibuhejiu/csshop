package com.huaruan.csshop.service;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.huaruan.csshop.bean.Product;

/**
 * 
 * @Param
 * 		Integer 为实体类主键类型
 * @author 
 *
 */
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {

}
