package com.huaruan.csshop.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.huaruan.csshop.bean.ActiveUser;
import com.huaruan.csshop.bean.Permission;
import com.huaruan.csshop.bean.Role;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.service.UserService;



public class MyShiroRealm extends AuthorizingRealm {
	
	
	@Autowired
	UserService userService;
	
	@Override
	public String getName() {
		return "MyShiroRealm";
	}
	
	//支持什么类型的token
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}
	

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//从token中获取用户身份信息
		String username = token.getPrincipal().toString();
		User sysUser = null;
		try {
			sysUser = userService.findByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询不到返回null
		if (sysUser == null) {
			return null;
		}
		//根据用户id取出权限列表
		List<Permission> permissionList = null;
		try {
			permissionList = userService.findPermissionList(sysUser.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//从数据库中获取用户的password
		String sysPassword = sysUser.getUserPassword();
		//盐
		String salt = sysUser.getUserSalt();
		//构建用户身份信息
		ActiveUser activeUser = new ActiveUser();
		activeUser.setId(sysUser.getUserId());
		activeUser.setUserName(sysUser.getUserName());
		activeUser.setPermissionList(permissionList);
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(activeUser, sysPassword, ByteSource.Util.bytes(salt), getName());
		return simpleAuthenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//身份信息
		ActiveUser activeUser = (ActiveUser)principals.getPrimaryPrincipal();
		int userId = activeUser.getId();
		//获取用户权限
		List<Permission> permissionList = null;
		try {
			permissionList = userService.findPermissionList(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取用户角色
		List<Integer> roleIdList = userService.findRoleIdListByUserId(userId);
		List<Role> roleList = new ArrayList<>();
		for (Integer roleId : roleIdList) {
			Role role = userService.findRoleByRoleId(roleId);
			roleList.add(role);
		}
		//构建shiro授权信息
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		for (Permission permission : permissionList) {//添加权限列表
			simpleAuthorizationInfo.addStringPermission(permission.getPermission());	
		}
		for (Role role : roleList) {//添加角色列表
			simpleAuthorizationInfo.addRole(role.getRoleName());
		}
		return simpleAuthorizationInfo;
	}

}
