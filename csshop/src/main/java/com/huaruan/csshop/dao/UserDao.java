package com.huaruan.csshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huaruan.csshop.bean.Permission;
import com.huaruan.csshop.bean.Role;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.bean.UserRole;

public interface UserDao {

	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return 用户
	 */
	User findByUsername(String username);
	
	/**
	 *  根据输入的用户名模糊查询用户列表
	 * @param username
	 * @return
	 */
	List<User> findUserlistByUsername(String username);

	/**
	 * 根据用户id查出该用户的角色ID List
	 * @param userId
	 * @return 
	 */
	List<Integer> findRoleIdListByUserId(int userId);

	/**
	 * 根据角色id查找对应的角色
	 * @param roleId
	 * @return
	 */
	Role findRoleByRoleId(Integer roleId);

	/**
	 * 根据roleId查找权限id列表
	 * @param roleId
	 * @return
	 */
	List<Integer> findPermissionIdListByRoleId(Integer roleId);

	/**
	 * 根据permissionid查出Permission
	 * @param permissionId
	 * @return
	 */
	Permission findPermissionByPermissionId(Integer permissionId);
	
	int isHaveUsername(String username);
	
	void insertUser(User user);

	User findUserById(Integer userid);

	void updateUserByUser(User user);

	void deleteUserById(int userId);

	List<UserRole> findUserRoleListByUsername(String string);

	void updateUserRole(UserRole userRole);

	List<Permission> findPermissionListByRoleIdAndEnable(@Param("roleId")Integer roleId, @Param("enable")String enable);

	void updateRolePermissionEnable(@Param("roleId")Integer roleId, @Param("permissionId")Integer permissionId,@Param("enable")String enable);

	int isHavePermission(String permission);

	void updatePermissionByPermission(Permission permission);

	void deletePermissionFromPermissionByPermissionId(Integer permissionId);

	void deletePermissionFromRolePermissionByPermisisonId(Integer permissionId);

	void insertNewPermissionInPermission(Permission permission);

	Integer getLastPermissionId();

	void insertIntoRolePermission(@Param("roleId")Integer roleId, @Param("permissionId")Integer permissionId, @Param("enable")String enable);
}
