package com.huaruan.csshop.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huaruan.csshop.bean.ActiveUser;
import com.huaruan.csshop.bean.Category2;
import com.huaruan.csshop.bean.Permission;
import com.huaruan.csshop.bean.Product;
import com.huaruan.csshop.bean.RolePermission;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.bean.UserRole;
import com.huaruan.csshop.service.ProductRepository;
import com.huaruan.csshop.service.ProductService;
import com.huaruan.csshop.service.UserService;
import com.huaruan.csshop.util.OssFileUtils;
import com.huaruan.csshop.util.ShiroUtils;

@Controller
@RequestMapping("/manage")
public class ManageController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;
	
	private int pageSize = 5;
	
	@RequestMapping("/login")
	public String goManageLoginPage() {
		return "manageLogin";
	}
	
	@RequestMapping("/index")
	public String goManageIndexPage(HttpServletRequest request) {
		//ActiveUser信息传到前台
		ActiveUser activeUser = ShiroUtils.getActiveUser();
		request.setAttribute("activeUser", activeUser);
		return "manageIndex";
	}
	
	@RequestMapping(value="/allUserlist")
	public ModelAndView goManageUserlist() {
		
		// 分页
		PageHelper.startPage(1,pageSize);
		List<User> userList = userService.findUserlistByUsername("");
		PageInfo<User> userPageInfo = new PageInfo<User>(userList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition", "");
		mav.setViewName("manageUserlist");
		return mav;
	}
	
	
	@RequestMapping(value="/findUsersByUsername")
	public ModelAndView findUsersByUsername(HttpServletRequest request) {
		PageHelper.startPage(1,pageSize);
		List<User> userList = userService.findUserlistByUsername(request.getParameter("username"));
		PageInfo<User> userPageInfo = new PageInfo<User>(userList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("username"));
		mav.setViewName("manageUserlist");
		return mav;
	}
	
	//翻页
	@RequestMapping("/findUsersByPage")
	public ModelAndView findUsersByPage(HttpServletRequest request) {
		PageHelper.startPage(Integer.parseInt(request.getParameter("pageNum")),pageSize);
		List<User> userList = userService.findUserlistByUsername(request.getParameter("username"));
		PageInfo<User> userPageInfo = new PageInfo<User>(userList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("username"));
		mav.setViewName("manageUserlist");
		return mav;
	}
	
	//前往用户信息修改页
	@RequestMapping(value="/toModifyUserMessageWin")
	public ModelAndView goToModifyUserPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		User oldUser = userService.findUserById(userId);
		mav.addObject("oldUser", oldUser);
		mav.setViewName("modifyUser");
		return mav;
	}
	
	@RequestMapping(value="/modifyUser",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> modifyUser(HttpServletRequest request){
		String resultStatus = userService.updateUserMessage(request);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", resultStatus);
		return resultMap;
	}
	
	@RequestMapping(value="/deleteUserById",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> deleteUserById(HttpServletRequest request){
		Map<String,String> resultMap = new HashMap<>();
		try {
			userService.deleteUserById(Integer.parseInt(request.getParameter("userId")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			resultMap.put("status","2");//删除失败
		}
		resultMap.put("status", "1");//删除成功
		return resultMap;
	}
	
	//前往用户角色管理页
	@RequestMapping("/roleManage")
	public ModelAndView goRoleManagePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PageHelper.startPage(1,pageSize);
		List<UserRole> list = userService.findUserRoleListByUsername("");
		PageInfo<UserRole> userPageInfo = new PageInfo<UserRole>(list);
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition","");
		mav.setViewName("manageUserRole");
		return mav;
	}
	
	@RequestMapping(value="/findUserRoleListByUsername")
	public ModelAndView findUserRoleListByUsername(HttpServletRequest request) {
		PageHelper.startPage(1,pageSize);
		List<UserRole> list = userService.findUserRoleListByUsername(request.getParameter("username"));
		PageInfo<UserRole> userPageInfo = new PageInfo<UserRole>(list);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("username"));
		mav.setViewName("manageUserRole");
		return mav;
	}
	
	@RequestMapping("/findUserRoleListByPage")
	public ModelAndView findUserRoleListByPage(HttpServletRequest request) {
		PageHelper.startPage(Integer.parseInt(request.getParameter("pageNum")),pageSize);
		List<UserRole> list = userService.findUserRoleListByUsername(request.getParameter("username"));
		PageInfo<UserRole> userPageInfo = new PageInfo<UserRole>(list);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("username"));
		mav.setViewName("manageUserRole");
		return mav;
	}
	
	//前往用户角色修改页
	@RequestMapping(value="/goModifyUserRole")
	public ModelAndView goModifyUserRole(HttpServletRequest request) {
		UserRole userRole = new UserRole();
		userRole.setUserId(Integer.parseInt(request.getParameter("userId")));
		userRole.setUserName(request.getParameter("userName"));
		userRole.setRoleId(Integer.parseInt(request.getParameter("roleId")));
		ModelAndView mav = new ModelAndView();
		mav.addObject("userRole",userRole);
		mav.setViewName("modifyUserRole");
		return mav;
	}
	
	@RequestMapping(value="/updateUserRole",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updateUserRole(HttpServletRequest request) {
		UserRole userRole = new UserRole();
		userRole.setUserId(Integer.parseInt(request.getParameter("userId")));
		userRole.setUserName(request.getParameter("userName"));
		userRole.setRoleId(Integer.parseInt(request.getParameter("roleId")));
		String resultStatus = userService.updateUserRole(userRole);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", resultStatus);
		return resultMap;
	}
	
	@RequestMapping(value="/permissionManage")
	public ModelAndView goManagePermissionPage() {
		Integer roleId = 2;//2代表用户，暂时只能改用户权限，以后再考虑管理员权限
		List<Permission> permissionList = userService.findPermissionListByRoleIdAndEnable(roleId,"1");//有效权限列表
		List<Permission> unPermissionList = userService.findPermissionListByRoleIdAndEnable(roleId,"0");//无效权限列表
		ModelAndView mav = new ModelAndView();
		mav.addObject("permissionList", permissionList);
		mav.addObject("unPermissionList", unPermissionList);
		mav.setViewName("manageRolePermission");
		return mav;
	}
	
	@RequestMapping(value="/deletePermission",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> deletePermission(HttpServletRequest request){
		Integer roleId = Integer.parseInt(request.getParameter("roleId"));
		Integer permissionId = Integer.parseInt(request.getParameter("permissionId"));
		String status = userService.updateRolePermissionEnable(roleId,permissionId,"0");
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", status);
		return resultMap;
	}

	@RequestMapping(value="/addPermission",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> addPermission(HttpServletRequest request){
		Integer roleId = Integer.parseInt(request.getParameter("roleId"));
		Integer permissionId = Integer.parseInt(request.getParameter("permissionId"));
		String status = userService.updateRolePermissionEnable(roleId,permissionId,"1");
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", status);
		return resultMap;
	}
	
	@RequestMapping("/toUpdatePermission")
	public ModelAndView goUpdatePermissionPage(HttpServletRequest request) {
		RolePermission rolePermission = new RolePermission();
		rolePermission.setRoleId(Integer.parseInt(request.getParameter("roleId")));
		rolePermission.setPermissionId(Integer.parseInt(request.getParameter("permissionId")));
		rolePermission.setPermission(request.getParameter("permission"));
		rolePermission.setDescription(request.getParameter("description"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("rolePermission",rolePermission);
		mav.setViewName("modifyRolePermission");
		return mav;
	}
	
	@RequestMapping(value="/updatePermission",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> updatePermission(HttpServletRequest request){
		Permission permission = new Permission();
		permission.setPermissionId(Integer.parseInt(request.getParameter("permissionId")));
		permission.setPermission(request.getParameter("permission"));
		permission.setDescription(request.getParameter("description"));
		String resultStatus = userService.updatePermissionByPermission(permission);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", resultStatus);
		return resultMap;
	}
	
	@RequestMapping(value="/deletePermissionTotally")
	@ResponseBody
	public Map<String,String> deletePermissionTotally(HttpServletRequest request){
		Integer permissionId = Integer.parseInt(request.getParameter("permissionId"));
		String resultStatus = userService.deletePermissionByPermissionId(permissionId);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", resultStatus);
		return resultMap;
	}
	
	@RequestMapping("/createPermission")
	@ResponseBody
	public Map<String,String> createPermission(HttpServletRequest request){
		Map<String,String> resultMap = new HashMap<>();
		String permission = request.getParameter("permission");
		String description = request.getParameter("description");
		Integer roleId = 2;//用户，暂时写死
		Permission newpermission = new Permission();
		newpermission.setPermission(permission);
		newpermission.setDescription(description);
		String resultStatus1 = userService.insertNewPermissionInPermission(newpermission);
		Integer permissionId = userService.getLastPermissionId();
		//在role_permission表中插入新纪录
		String resultStatus2 = userService.insertIntoRolePermission(roleId,permissionId,"1");
		if (resultStatus1.equals("1") && resultStatus2.equals("1")) {
			resultMap.put("status", "1");//成功
		}
		if (resultStatus1.equals("2") || resultStatus2.equals("2")) {
			resultMap.put("status", "2");//异常
		}
		if (resultStatus1.equals("3")) {
			resultMap.put("status", "3");//permission已存在
		}
		return resultMap;
	}
	
	//商品管理
	@RequestMapping("/productManage")
	public ModelAndView goProductManagePage() {
		// 分页
		PageHelper.startPage(1,pageSize);
		List<Product> productList = productService.findProductListByProductName("");
		PageInfo<Product> userPageInfo = new PageInfo<Product>(productList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo", userPageInfo);
		mav.addObject("currCondition","");
		mav.setViewName("manageProduct");
		return mav;
	}
	
	@RequestMapping(value="/findProductListByProductname")
	public ModelAndView findProductListByProductname(HttpServletRequest request) {
		PageHelper.startPage(1,pageSize);
		List<Product> list = productService.findProductListByProductName(request.getParameter("productname"));
		PageInfo<Product> userPageInfo = new PageInfo<Product>(list);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("productname"));
		mav.setViewName("manageProduct");
		return mav;
	}
	
	@RequestMapping("/findProductListByPage")
	public ModelAndView findProductListByPage(HttpServletRequest request) {
		PageHelper.startPage(Integer.parseInt(request.getParameter("pageNum")),pageSize);
		List<Product> list = productService.findProductListByProductName(request.getParameter("productname"));
		PageInfo<Product> userPageInfo = new PageInfo<Product>(list);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageInfo",userPageInfo);
		mav.addObject("currCondition",request.getParameter("productname"));
		mav.setViewName("manageProduct");
		return mav;
	}
	
	@RequestMapping("/addProduct")
	public ModelAndView goProductAddPage() {
		ModelAndView mav = new ModelAndView();
		//获取2级商品分类
		List<Category2> list = productService.findAllCategory2List();
		mav.addObject("categoryList", list);
		mav.setViewName("insertProduct");
		return mav;
	}
	
	@RequestMapping(value="/insertProduct",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> insertProduct(HttpServletRequest request,@RequestParam("imgFile")MultipartFile file){
		Product product = new Product();
		String resultStatus = "";
		product.setProductName(request.getParameter("productName"));
		product.setProductQuantity(Integer.parseInt(request.getParameter("productQuantity")));
		product.setProductPriceShop(BigDecimal.valueOf(Double.parseDouble(request.getParameter("productShopPrice"))));
		product.setProductPriceMarket(BigDecimal.valueOf(Double.parseDouble(request.getParameter("productMarketPrice"))));
		product.setIshot(request.getParameter("ishot"));
		product.setCategory2Id(Integer.parseInt(request.getParameter("category2")));
		product.setProductDescription(request.getParameter("productDescription"));
		//上传图片到OSS
		String imgUrl = "";		
		try {
			imgUrl = OssFileUtils.uploadFile(file.getOriginalFilename(), file);
		} catch (OSSException e) {
			e.printStackTrace();
			resultStatus = "3";//图片上传失败
		} catch (ClientException e) {
			e.printStackTrace();
			resultStatus = "3";//图片上传失败
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			resultStatus = "3";//图片上传失败
		}
		if (imgUrl != null && !"".equals(imgUrl)) {
			product.setProductImageUrl(imgUrl);
		}
		resultStatus = productService.insertNewProduct(product);
		//product索引中添加数据
		productRepository.save(product);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("status", resultStatus);
		return resultMap;
	}
	
	@RequestMapping("/toModifyProductPage")
	public ModelAndView goModifyProductPage(HttpServletRequest request) {
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		Product product = productService.findProductByProductId(productId);
		ModelAndView mav = new ModelAndView();
		//获取2级商品分类
		List<Category2> list = productService.findAllCategory2List();
		mav.addObject("categoryList", list);
		mav.addObject("product", product);
		mav.setViewName("modifyProduct");
		return mav;
	}
	
	@RequestMapping("/updateProduct")
	@ResponseBody
	public Map<String,String> updateProduct(HttpServletRequest request,@RequestParam(value="imgFile",required=false)MultipartFile file){
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		String resultStatus = "";
		Map<String,String> resutlMap = new HashMap<>();
		Product product = new Product();
		product.setProductId(productId);
		if (request.getParameter("productName") != null) {
			product.setProductName(request.getParameter("productName"));
		}
		if (request.getParameter("productQuantity") != null) {
			product.setProductQuantity(Integer.parseInt(request.getParameter("productQuantity")));
		}
		if (request.getParameter("productShopPrice") != null) {
			product.setProductPriceShop(BigDecimal.valueOf(Double.parseDouble(request.getParameter("productShopPrice"))));
		}
		if (request.getParameter("productMarketPrice") != null) {
			product.setProductPriceMarket(BigDecimal.valueOf(Double.parseDouble(request.getParameter("productMarketPrice"))));
		}
		if (request.getParameter("ishot") != null) {
			product.setIshot(request.getParameter("ishot"));
		}
		if (request.getParameter("category2") != null) {
			product.setCategory2Id(Integer.parseInt(request.getParameter("category2")));
		}
		if (request.getParameter("productDescription") != null) {
			product.setProductDescription(request.getParameter("productDescription"));
		}
		if (file != null) {
			//上传图片到OSS
			String imgUrl = "";		
			try {
				imgUrl = OssFileUtils.uploadFile(file.getOriginalFilename(), file);
			} catch (OSSException e) {
				e.printStackTrace();
				resultStatus = "3";//图片上传失败
			} catch (ClientException e) {
				e.printStackTrace();
				resultStatus = "3";//图片上传失败
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				resultStatus = "3";//图片上传失败
			}
			if (imgUrl != null && !"".equals(imgUrl)) {
				product.setProductImageUrl(imgUrl);
			}
		}
		System.out.println(""+product);
		resultStatus = productService.updateProductByProduct(product);
		
		//product索引中更新对应的产品
		Product productAfterUpdate = productService.findProductByProductId(productId);
		productRepository.save(productAfterUpdate);
		resutlMap.put("status", resultStatus);
		return resutlMap;
	}
	
	@RequestMapping("/deleteProduct")
	@ResponseBody
	public Map<String,String> deleteProduct(HttpServletRequest request){
		String resultStatus = productService.deleteProductById(Integer.parseInt(request.getParameter("productId")));
		Map<String, String> resultMap = new HashMap<>();
		//product索引删除商品
		productRepository.delete(Integer.parseInt(request.getParameter("productId")));
		resultMap.put("status",resultStatus);
		return resultMap;
	}
}
