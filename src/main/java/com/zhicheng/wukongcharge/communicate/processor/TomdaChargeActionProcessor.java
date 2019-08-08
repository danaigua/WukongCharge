package com.zhicheng.wukongcharge.communicate.processor;

import java.util.Arrays;

import com.zhicheng.wukongcharge.entity.Charger;
import com.zhicheng.wukongcharge.model.TomdaProtocol;
import com.zhicheng.wukongcharge.models.BootNotificationProto;
import com.zhicheng.wukongcharge.models.BootNotificationProto.bootNotificationReq;
import com.zhicheng.wukongcharge.models.BootNotificationProto.bootNotificationReq.Builder;
import com.zhicheng.wukongcharge.models.BootNotificationProto.bootNotificationReq.bootReasonEnumType;
import com.zhicheng.wukongcharge.tcp.KeyAttach;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class TomdaChargeActionProcessor {

//	private String routerSerialNum;         //网关序列号
//	 private String routerCompanyCode;        //网关所属公司编码
//	 private Long routerStationId;         //网关关联电站的ID
//	 private boolean login;           //当前网关是否已经登录
//	 private Protocol protocol;          //协议版本
//	 private boolean needResetAuthKey;        //是否需要重新设置网关登录Key
//	 private long loginTime;           //登陆时间
//	 private long syncTime;                //网关同步后10秒后才去设置网关的登录Key
//	 private long ioExceptionTime;         //首次报IO异常的时间，超过2分钟，则断开TCP连接
//	 private List<String> commands = new ArrayList<String>();
//	 private String protocolId;          //协议的ID
//	 private String remoteConnectInfo;        //远端连接判定参数
	KeyAttach keyAttach = new KeyAttach();
    public static final AttributeKey<KeyAttach> NETTY_CHANNEL_KEY = AttributeKey.valueOf("tomdanetty.channel");
    
    /**
     * 充电桩登录
     */
    public boolean wukongChargeLogin(ChannelHandlerContext ctx) {
    	Charger charger = new Charger();
    	charger.setCompanyCode("智诚");
    	charger.setStationId((long) 123);
    	Attribute<KeyAttach> attr = ctx.channel().attr(NETTY_CHANNEL_KEY);
    	KeyAttach keyAttach = attr.get();
    	//未识别（未知的序列号） 的Socket， 全新的socket连接
    	if(null == keyAttach) {
    		keyAttach = new KeyAttach();
    	}
    	String chargerSerialNum = "112233";
		keyAttach.setRouterSerialNum(chargerSerialNum);
    	keyAttach.setRouterCompanyCode(charger.getCompanyCode());
    	keyAttach.setProtocolId(this.getProtocolID());
    	attr.setIfAbsent(keyAttach);
    	
    	//成功登录
    	keyAttach.setRouterStationId(charger.getStationId());
    	keyAttach.setLogin(true);
    	
    	TomdaProtocol tomdaProtocol = new TomdaProtocol();
    	tomdaProtocol.loadModels();
    	
    	Builder builder2 = BootNotificationProto.bootNotificationReq.newBuilder();
    	builder2.setReason(bootReasonEnumType.ApplicationReset).setSerialNumber("10101010").setKey("10101").setPcbID(111).setVendorName("aaa").setFirmwareVersion("111");
    	bootNotificationReq req = builder2.build();
    	byte[] byteArray = req.toByteArray();
    	System.out.println(Arrays.toString(byteArray));
    	System.out.println(builder2);
    	System.out.println(tomdaProtocol.organizeFrame(101, 10101, req));
    	ctx.channel().writeAndFlush(tomdaProtocol.organizeFrame(101, 10101, req));
//    	tomdaProtocol.organizeFrame(101, 10101, req);
    	
    	return true;
    }

    /**
     * 登录应答
     * @return
     */
	private String getProtocolID() {
		
		return null;
	}

	
}
