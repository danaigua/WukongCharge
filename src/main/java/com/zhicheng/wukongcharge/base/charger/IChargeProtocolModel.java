package com.zhicheng.wukongcharge.base.charger;

public interface IChargeProtocolModel {

	String getProtocolId();

	String getActionID();

	int getProtocolVersion();

	void initWithFrame(String frame);

}
