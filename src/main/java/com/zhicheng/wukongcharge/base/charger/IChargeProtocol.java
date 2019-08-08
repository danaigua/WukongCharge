package com.zhicheng.wukongcharge.base.charger;

import com.zhicheng.wukongcharge.entity.ActionProcessor;

/**
 * 电池活动控制器
 * 业务代码
 * @author 章家宝
 *
 */
public interface IChargeProtocol {

	public IChargeActionProcessor getActionProcessor(String actionId);

}
