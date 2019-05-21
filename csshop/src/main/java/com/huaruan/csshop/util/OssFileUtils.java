package com.huaruan.csshop.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;

public class OssFileUtils {
	
	private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";

	private static String accessKeyId = "LTAI9deh5wbN3yLS";

	private static String accessKeySecret = "ninyltan0vh4uoXU4cX91wNvGkm5HF";

	private static String bucketName = "childrenpalace-data";

	   /**
	    * 上传文件到阿里云OSS
	    * @param objectKey 文件名
	    * @param multipartFile 文件
	    * @return 该文件的下载地址
	    * @throws OSSException
	    * @throws ClientException
	    * @throws FileNotFoundException
	    */
	   public static String uploadFile(String objectKey, MultipartFile multipartFile)
	           throws OSSException, ClientException, FileNotFoundException {

	       // 创建OSSClient的实例
	       OSSClient ossClient  = new OSSClient(endpoint,accessKeyId,accessKeySecret);

	       // 上传的文件不是空，并且文件的名字不是空字符串
	       if (multipartFile.getSize() != 0 && !"".equals(multipartFile.getName())) {
	           ObjectMetadata om = new ObjectMetadata();
	           om.setContentLength(multipartFile.getSize());
	           // 设置文件上传到服务器的名称
	           om.addUserMetadata("filename", objectKey);
	           
	           try {
	               ossClient.putObject(bucketName, objectKey, new ByteArrayInputStream(multipartFile.getBytes()), om);
	           } catch (IOException e) {
	        	   e.printStackTrace();
	           }
	       }
	       // 设置这个文件地址的有效时间
	       Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
	       String url = ossClient.generatePresignedUrl(bucketName, objectKey, expiration).toString();
	       return url;
	   }


}
