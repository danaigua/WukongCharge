package com.zhicheng.wukongcharge.communicate;

import com.zhicheng.wukongcharge.base.charger.IChargeActionProcessor;
import com.zhicheng.wukongcharge.base.charger.IChargeProtocol;
import com.zhicheng.wukongcharge.model.TomdaProtocol;
/**
 * 电池活动管理类
 * @author 章家宝
 *
 */
public class ProtocolManager{

	//通过协议ID查找协议
	public TomdaProtocol findProtocolByProtocolID(String procotolId) {
		TomdaProtocol tomdaProtocol = new TomdaProtocol();
		tomdaProtocol.loadModels();
		if(procotolId.equals(tomdaProtocol.getProtocolID())) {
			return tomdaProtocol;
		}else {
			System.out.println("找不到协议，丢弃！！！");
			return null;
		}
	}
}
