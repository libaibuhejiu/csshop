package com.huaruan.csshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huaruan.csshop.bean.Permission;
import com.huaruan.csshop.bean.Role;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.bean.UserRole;
import com.huaruan.csshop.dao.UserDao;
import com.huaruan.csshop.service.UserService;


@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDao userDao;

	@Override
	public User findByUsername(String username) {
		User user = userDao.findByUsername(username);
		return user;
	}

	@Override
	public List<Permission> findPermissionList(int userId) {
		//根据id找到角色列表
		List<Integer> roleIdList = userDao.findRoleIdListByUserId(userId);
		List<Role> roleList = new ArrayList<>();
		for (Integer roleId : roleIdList) {
			Role role = userDao.findRoleByRoleId(roleId);
			roleList.add(role);
		}
		List<Permission> permissionList = new ArrayList<Permission>();
		//根据角色列表查找出权限列表
		for (Role role : roleList) {
			//根据角色id找到对应的权限id列表
			List<Integer> permissionIdList = userDao.findPermissionIdListByRoleId(role.getRoleId());
			for (Integer permissionId : permissionIdList) {
				//根据权限id找出权限
				Permission permission = userDao.findPermissionByPermissionId(permissionId);
				permissionList.add(permission);
			}
		}
		return permissionList;
	}

	@Override
	public List<Integer> findRoleIdListByUserId(int userId) {
		return userDao.findRoleIdListByUserId(userId);
	}

	@Override
	public Role findRoleByRoleId(Integer roleId) {
		return userDao.findRoleByRoleId(roleId);
	}

	

	@Override
	public int isHaveUsername(String username) {
		return userDao.isHaveUsername(username);
	}

	@Override
	public void insertNewUser(User user) {
		userDao.insertUser(user);		
	}

	@Override
	public List<User> findUserlistByUsername(String username) {
		return userDao.findUserlistByUsername(username);
	}

	@Override
	public User findUserById(Integer userid) {
		return userDao.findUserById(userid);
	}

	@Override
	@Transactional
	public String updateUserMessage(HttpServletRequest request) {
		User user = new User();
		if (request.getParameter("id") != null) {
			user.setUserId(Integer.parseInt(request.getParameter("id")));
		}
		if (request.getParameter("password") != null) {
			String salt = new SecureRandomNumberGenerator().nextBytes().toString(); // 盐
			int times = 2; // 加盐次数
			String algorithmName = "md5"; // MD5 加密
			// 加密 密码
			String encodedPassword = new SimpleHash(algorithmName,request.getParameter("password"), salt, times).toString();
			user.setUserPassword(encodedPassword);
		}
		if (request.getParameter("email") != null) {
			user.setUserEmail(request.getParameter("email"));
		}
		if (request.getParameter("phone") != null) {
			user.setUserPhone(request.getParameter("phone"));
		}
		if (request.getParameter("address") != null) {
			user.setUserAddress(request.getParameter("address"));
		}
		if (request.getParameter("sex") != null) {
			user.setUserSex(request.getParameter("sex"));
		}
		if (request.getParameter("age") != null) {
			user.setUserAge(Integer.parseInt(request.getParameter("age")));
		}
		try {
			userDao.updateUserByUser(user);
		} catch (Exception e) {			
			e.printStackTrace();
			return "2";//发生异常
		}
 		return "1";//修改成功
	}

	@Override
	public void deleteUserById(int userId) {
		userDao.deleteUserById(userId);
	}

	@Override
	public List<UserRole> findUserRoleListByUsername(String string) {
		return userDao.findUserRoleListByUsername(string);
	}

	@Override
	public String updateUserRole(UserRole userRole) {
		try {
			userDao.updateUserRole(userRole);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	@Override
	public List<Permission> findPermissionListByRoleIdAndEnable(Integer roleId, String enable) {
		List<Permission> permissionList = userDao.findPermissionListByRoleIdAndEnable(roleId,enable);
		return permissionList;
	}

	@Override
	public String updateRolePermissionEnable(Integer roleId, Integer permissionId,String enable) {
		try {
			userDao.updateRolePermissionEnable(roleId, permissionId, enable);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	@Override
	public String updatePermissionByPermission(Permission permission) {
		//先看permission是否已存在
		int count = userDao.isHavePermission(permission.getPermission());
		if (count > 0) {
			return "3";//permission已存在
		} else {
			try {
				userDao.updatePermissionByPermission(permission);
			} catch (Exception e) {
				e.printStackTrace();
				return "2";//发生异常
			}
			return "1";//更新成功
		}
		
	}

	@Override
	public String deletePermissionByPermissionId(Integer permissionId) {
		try {
			userDao.deletePermissionFromPermissionByPermissionId(permissionId);
			userDao.deletePermissionFromRolePermissionByPermisisonId(permissionId);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";//发生异常
		}
		return "1";//成功
	}

	@Override
	public String insertNewPermissionInPermission(Permission permission) {
		if (userDao.isHavePermission(permission.getPermission()) > 0) {
			return "3";//权限已存在
		}
		try {
			userDao.insertNewPermissionInPermission(permission);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";//发生异常
		}
		return "1";
	}

	@Override
	public Integer getLastPermissionId() {
		Integer permisisonId = userDao.getLastPermissionId();
		return permisisonId;
	}

	@Override
	public String insertIntoRolePermission(Integer roleId, Integer permissionId, String enable) {
		try {
			userDao.insertIntoRolePermission(roleId,permissionId,enable);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";//异常
		}
		return "1";//成功
	}

	

}
