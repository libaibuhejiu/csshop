package com.huaruan.csshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import com.huaruan.csshop.bean.Permission;
import com.huaruan.csshop.bean.Role;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.bean.UserRole;

public interface UserService {

	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return 用户
	 */
	User findByUsername(String username);
	
	/**
	 * 根据输入的用户名模糊查询用户列表
	 * @param username
	 * @return
	 */
	List<User> findUserlistByUsername(String username);
	
	/**
	 * 根据userid查出对应的权限列表
	 * @param userId
	 * @return
	 */
	List<Permission> findPermissionList(int userId);

	/**
	 * 根据userid查出对应的role_id list
	 * @param userId
	 * @return
	 */
	List<Integer> findRoleIdListByUserId(int userId);

	/**
	 * 根据roleid找出对应的role
	 * @param roleId
	 * @return
	 */
	Role findRoleByRoleId(Integer roleId);
	
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	void insertNewUser(User user);
	
	/**
	 * 查看是否已存在用户名
	 * @param username
	 * @return 已存在的该用户名个数
	 */
	int isHaveUsername(String username);

	/**
	 * 根据用户id查找用户
	 * @param userid
	 * @return
	 */
	User findUserById(Integer userid);

	/**
	 * 更新用户信息
	 * @param request
	 * @return "1"成功，"2"失败
	 */
	@Transactional
	String updateUserMessage(HttpServletRequest request);

	/**
	 * 根据用户id删除用户
	 * @param parseInt
	 */
	void deleteUserById(int userId);

	/**
	 * 根据用户名模糊查询UserRole list
	 * @param string
	 */
	List<UserRole> findUserRoleListByUsername(String string);

	/**
	 * 根据UserRole更新用户角色
	 * @param userRole
	 * @return "1"更新成功，“2”失败
	 */
	String updateUserRole(UserRole userRole);

	/**
	 * 根据roleid查出所有权限列表
	 * @param roleId
	 * @param enable是否有效
	 * @return
	 */
	List<Permission> findPermissionListByRoleIdAndEnable(Integer roleId, String enable);

	/**
	 * 根据roleId permissionId修改对应权限的enable字段，即将role_permission表中enable置为“0”，无效
	 * 或者“1”有效
	 * @param roleId
	 * @param permissionId
	 * @return "1"成功，“2”失败
	 */
	String updateRolePermissionEnable(Integer roleId, Integer permissionId,String enable);

	/**
	 * 根据Permission对象更新permission表
	 * @param permission
	 * @return
	 */
	String updatePermissionByPermission(Permission permission);

	/**
	 * 根据permissionid删除权限，完全删除，从permission表和role_permission表
	 * @param permissionId
	 * @return "1"成功，“2”失败
	 */
	String deletePermissionByPermissionId(Integer permissionId);

	/**
	 * 在permission 表中插入一个新的permission记录，根据传入的permission
	 * @param permission
	 * @return 1成功2异常3已存在permission
	 */
	String insertNewPermissionInPermission(Permission permission);

	/**
	 * 获取permission表中最后增加的序列值
	 * @return
	 */
	Integer getLastPermissionId();

	/**
	 * 在role_permission表中插入新纪录
	 * @param roleId
	 * @param permissionId
	 * @param string
	 * @return 1成功2异常
	 */
	String insertIntoRolePermission(Integer roleId, Integer permissionId, String string);
}
