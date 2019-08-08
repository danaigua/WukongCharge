package com.zhicheng.wukongcharge.communicate;

import com.zhicheng.wukongcharge.tcp.KeyAttach;
import com.zhicheng.wukongcharge.communicate.ProtocolManager;
import com.zhicheng.wukongcharge.base.charger.IChargeActionProcessor;
import com.zhicheng.wukongcharge.base.charger.IChargeProtocol;
import com.zhicheng.wukongcharge.base.charger.impl.ChargeActionProcessorImpl;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Tomda协议数据处理类
 * Created by jiafeng on 2018/09/09.
 */
@Component
@Sharable   //Sharable表示此对象在channel间共享
public class TomdaChargerServerHandler extends SimpleChannelInboundHandler<TomdaProcotolContent> {
    public class NETTY_CHANNEL_KEY {

	}

	private Logger log = Logger.getLogger(TomdaChargerServerHandler.class);

//    @Autowired
//    private ProtocolManager protocolManager;

    public static final AttributeKey<KeyAttach> NETTY_CHANNEL_KEY = AttributeKey.valueOf("tomdanetty.channel");
	@Override
    public void channelRead0(ChannelHandlerContext context, TomdaProcotolContent procotolContent) throws Exception {
        try {
        	ProtocolManager protocolManager = new ProtocolManager();
            String procotolId = procotolContent.getProtocolId();
            System.out.println(procotolId);
            System.out.println(protocolManager);
            //在这里的时候没有--------------------------通过协议ID查找协议
            IChargeProtocol protocol = protocolManager.findProtocolByProtocolID(procotolId);
            if (protocol == null) {
                log.info("Tomda Protocol未找到" + procotolId + "对应的协议");
                System.out.println("Tomda Protocol未找到" + procotolId + "对应的协议");
                return;
            }
            String actionId = String.valueOf(procotolContent.getDataType());
            System.out.println("actionId:" + actionId);
            //通过活动id查找操作处理器
             IChargeActionProcessor processor = protocol.getActionProcessor(actionId);
//            if (processor == null) {
//                log.info("Tomda Protocol未找到" + actionId + "对应的解析器");
//                System.out.println("Tomda Protocol未找到" + actionId + "对应的解析器");
//                return;
//            }
            ChargeActionProcessorImpl processorTest = new ChargeActionProcessorImpl();
            processorTest.processModel(procotolContent, context);
//            processor.processModel(procotolContent, context);
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("###Tomda Protocol 收到消息处理异常: " + e.getMessage(), e);
            System.out.println("###Tomda Protocol 收到消息处理异常: " + e.getMessage());
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel is active，Tomda Protocol打开时客户端连接的地址：" + ctx.channel().remoteAddress());
        System.out.println("Channel is active，Tomda Protocol打开时客户端连接的地址：" + ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel is disconnected，Tomda Protocol关闭时客户端连接的地址：" + ctx.channel().remoteAddress());
        System.out.println("Channel is disconnected，Tomda Protocol关闭时客户端连接的地址：" + ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //此处对断网进行了处理
    	System.out.println("此处对断网进行了处理");
        ctx.close();
    }


}
