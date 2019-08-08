package com.zhicheng.wukongcharge.service;

import org.springframework.stereotype.Service;

import cn.jpush.api.report.UsersResult.User;

/**
 * 用户service接口
 * @author 章家宝
 *
 */
@Service("userService")
public interface IUserService {

	public User getByUserName(String userName);

}
