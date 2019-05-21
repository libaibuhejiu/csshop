package com.huaruan.csshop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;

import org.elasticsearch.index.query.QueryStringQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huaruan.csshop.bean.ActiveUser;
import com.huaruan.csshop.bean.Category2;
import com.huaruan.csshop.bean.Product;
import com.huaruan.csshop.bean.User;
import com.huaruan.csshop.service.ProductRepository;
import com.huaruan.csshop.service.ProductService;
import com.huaruan.csshop.service.UserService;
import com.huaruan.csshop.util.ShiroUtils;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@RequestMapping("/index")
	public ModelAndView goIndexPage(HttpServletRequest request) {
		//ActiveUser信息传到前台
		ActiveUser activeUser = ShiroUtils.getActiveUser();
		request.setAttribute("activeUser", activeUser);
		//二级分类列表
		List<Category2> c2List = productService.findAllCategory2List();
		//热门商品列表
		List<Product> hotList = productService.findAllHotProduct();
		//最新商品列表（暂时用热门代替）
		ModelAndView mav = new ModelAndView();
		mav.addObject("c2List", c2List);
		mav.addObject("hotList", hotList);
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping("/login")
	public String goLoginPage() {
		return "login";
	}
	
	@RequestMapping(value="/loginCheck",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loginCheck(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean rememberMe = Boolean.valueOf(request.getParameter("rememberMe"));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if (rememberMe == true) {
			token.setRememberMe(rememberMe);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			subject.login(token);
		} catch (IncorrectCredentialsException ice) {
			map.put("status","2");//密码错误！
			return map;
		} catch (UnknownAccountException uae) {
			map.put("status","3");//用户名不存在！
			return map;
		}
		map.put("status","1");//登录验证成功
		
		return map;
	}
	
	@RequestMapping("/register")
	public String goRegisterPage() {
		return "register";
	}
	
	//注册
	@RequestMapping(value="/registerNewUser",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> registerNewUser(HttpServletRequest request) {
		Map<String,String> resultMap = new HashMap<String, String>();
		User user = new User();
		user.setUserName(request.getParameter("username"));
		// 使用SimpleHash加密
		String password = request.getParameter("password");
		String salt = new SecureRandomNumberGenerator().nextBytes().toString(); // 盐
		int times = 2; // 加盐次数
		String algorithmName = "md5"; // MD5 加密
		// 加密 密码
		String encodedPassword = new SimpleHash(algorithmName,password, salt, times).toString();
		user.setUserPassword(encodedPassword);
		user.setUserSalt(salt);
		user.setUserAddress(request.getParameter("address"));
		user.setUserEmail(request.getParameter("email"));
		user.setUserPhone(request.getParameter("phone"));
		user.setUserSex(request.getParameter("sex"));
		try {
			userService.insertNewUser(user);
		} catch (Exception e) {
			resultMap.put("RegisterResult","2");//发生异常，注册失败
			e.printStackTrace();
		}
		resultMap.put("RegisterResult","1");//注册成功

		return resultMap;
	}
	
	//用户名是否已存在
	@RequestMapping("/checkUserName")
	@ResponseBody
	public Map<String,String> checkUserName(HttpServletRequest request) {
		String username = request.getParameter("username");
		int count = userService.isHaveUsername(username);
		Map<String,String> resultMap = new HashMap<String, String>();
		if (count == 0) {
			resultMap.put("status","1");//用户名可用
		}
		if (count > 0) {
			resultMap.put("status","2");//已存在
		}		
		return resultMap;
	}
	
	
	@GetMapping("/conditionSelectProduct/{q}")
	@ResponseBody
	public ModelAndView conditionSelectProduct(@PathVariable String q) {
		QueryStringQueryBuilder qsqb = new QueryStringQueryBuilder(q);
		Iterable<Product> searchResult = productRepository.search(qsqb);
		Iterator<Product> iterator = searchResult.iterator();
		List<Product> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		ModelAndView mav = new ModelAndView();
		//ActiveUser信息传到前台
		ActiveUser activeUser = ShiroUtils.getActiveUser();
		mav.addObject("activeUser", activeUser);
		mav.addObject("productList", list);
		mav.addObject("currCondition", q);
		mav.setViewName("searchResult");
		return mav;
	}

}
