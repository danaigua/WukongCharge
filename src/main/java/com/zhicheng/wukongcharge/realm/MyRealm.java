package com.zhicheng.wukongcharge.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.zhicheng.wukongcharge.service.IUserService;

import cn.jpush.api.report.UsersResult.User;

/**
 * 后台自定义realm
 * @author 章家宝
 *
 */
public class MyRealm extends AuthorizingRealm{

	@Resource
	private IUserService userService;
	
	/**
	 * 为当前的登录的用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 验证当前登录的用户
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName=(String) token.getPrincipal();
		User blogger=userService.getByUserName(userName);
		if(blogger!=null){
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger); // 把当前用户信息存到session中
			AuthenticationInfo authcInfo= null;
//					new SimpleAuthenticationInfo(user.getUserName(), blogger.getPassword(), "xxx");
			return authcInfo;
		}else{
			return null;			
		}
	}


}
