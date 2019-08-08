package com.zhicheng.wukongcharge.communicate;

import cn.jpush.api.utils.StringUtils;
import com.zhicheng.wukongcharge.tcp.KeyAttach;
import com.zhicheng.wukongcharge.tool.Tool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.util.Date;

/**
 * Tomda协议编码类 Created by jiafeng on 2018/09/09.
 */
//@Component
public class TomdaChargerEncoder extends MessageToByteEncoder<Object> {
	private Logger logger = Logger.getLogger(TomdaChargerEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) {
		try {
			String commonde = (String) message;
//			System.out.println(commonde);
//			System.out.println(message);
//			System.out.println(TomdaChargerServerHandler.NETTY_CHANNEL_KEY);
//			System.out.println(ctx.channel().attr(TomdaChargerServerHandler.NETTY_CHANNEL_KEY).get());
			KeyAttach keyAttach = ctx.channel().attr(TomdaChargerServerHandler.NETTY_CHANNEL_KEY).get();
			System.out.println(keyAttach);
			out.writeBytes(commonde.getBytes("ISO-8859-1"));
			System.out.println(" - ^_^ Write back, " + keyAttach.getRouterSerialNum() + ", "
					+ Tool.formatDateTime(new Date()) + " = " + Tool.showData(commonde));
			logger.info(" - ^_^ Write back, " + keyAttach.getRouterSerialNum() + ", " + Tool.formatDateTime(new Date())
					+ " = " + Tool.showData(commonde));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对下发的命令进行编码
	 *
	 * @param commonde
	 * @param out
	 * @param ctx
	 * @throws CharacterCodingException
	 */
	public void encodeMessage(String commonde, ByteBuf out, ChannelHandlerContext ctx) throws Exception {
		if (StringUtils.isNotEmpty(commonde)) {
			KeyAttach keyAttach = ctx.channel().attr(TomdaChargerServerHandler.NETTY_CHANNEL_KEY).get();
			out.writeBytes(commonde.getBytes("ISO-8859-1"));
			logger.info(" - ^_^ Write back, " + keyAttach.getRouterSerialNum() + ", " + Tool.formatDateTime(new Date())
					+ " = " + Tool.showData(commonde));
		}
	}
}
