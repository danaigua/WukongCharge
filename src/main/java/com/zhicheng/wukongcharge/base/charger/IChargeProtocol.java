package com.zhicheng.wukongcharge.base.charger;

public interface IChargeProtocol {

	public IChargeActionProcessor getActionProcessor(String actionId);

}
