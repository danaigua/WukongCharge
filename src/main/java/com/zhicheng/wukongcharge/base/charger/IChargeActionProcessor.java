package com.zhicheng.wukongcharge.base.charger;

import com.zhicheng.wukongcharge.communicate.TomdaProcotolContent;

import io.netty.channel.ChannelHandlerContext;

public interface IChargeActionProcessor {

	public void processModel(TomdaProcotolContent procotolContent, ChannelHandlerContext context);

}
