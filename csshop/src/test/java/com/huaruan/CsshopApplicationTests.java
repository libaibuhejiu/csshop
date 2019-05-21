package com.huaruan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.huaruan.csshop.bean.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsshopApplicationTests {

	 @Autowired
	 private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void contextLoads() {
		elasticsearchTemplate.createIndex(Product.class);
		elasticsearchTemplate.putMapping(Product.class);
	}

}
