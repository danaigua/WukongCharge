package com.zhicheng.wukongcharge.base.charger.impl;

import com.zhicheng.wukongcharge.base.charger.IChargeActionProcessor;
import com.zhicheng.wukongcharge.communicate.TomdaProcotolContent;
import com.zhicheng.wukongcharge.communicate.processor.TomdaChargeActionProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
/**
 * 电池活动控制器的实现类
 * @author 章家宝
 * IChargeActionProcessor
 *
 */
public class ChargeActionProcessorImpl implements IChargeActionProcessor {

	public void processModel(TomdaProcotolContent procotolContent, ChannelHandlerContext context) {
		System.out.println(procotolContent.getFrame());
		TomdaChargeActionProcessor t = new TomdaChargeActionProcessor();
		t.wukongChargeLogin(context);
//		context.channel().writeAndFlush(procotolContent.getFrame());
	}

}
