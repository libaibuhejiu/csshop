package com.huaruan.csshop.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.huaruan.csshop.bean.ActiveUser;


/**
 * shiro工具类
 * @author Administrator
 *
 */
public class ShiroUtils {
	
	/**
	 * 获取ActiveUser
	 */
	public static ActiveUser getActiveUser() {
		Subject subject = SecurityUtils.getSubject();
		ActiveUser activeUser = (ActiveUser)subject.getPrincipal();
		return activeUser;
	}

}
