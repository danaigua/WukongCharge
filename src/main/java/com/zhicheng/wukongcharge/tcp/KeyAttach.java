package com.zhicheng.wukongcharge.tcp;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
/**
 * 充电桩的钥匙开关
 * @author 章家宝
 *
 */
public class KeyAttach {
	private String routerSerialNum;         //网关序列号
	 private String routerCompanyCode;        //网关所属公司编码
	 private Long routerStationId;         //网关关联电站的ID
	 private boolean login;           //当前网关是否已经登录
	 private Protocol protocol;          //协议版本
	 private boolean needResetAuthKey;        //是否需要重新设置网关登录Key
	 private long loginTime;             //登陆时间
	 private long syncTime;                //网关同步后10秒后才去设置网关的登录Key
	 private long ioExceptionTime;         //首次报IO异常的时间，超过2分钟，则断开TCP连接
	 private List<String> commands = new ArrayList<String>();
	 private String protocolId;          //协议的ID
	 private String remoteConnectInfo;        //远端连接判定参数
	public String getRouterSerialNum() {
		return routerSerialNum;
	}
	public void setRouterSerialNum(String routerSerialNum) {
		this.routerSerialNum = routerSerialNum;
	}
	public String getRouterCompanyCode() {
		return routerCompanyCode;
	}
	public void setRouterCompanyCode(String routerCompanyCode) {
		this.routerCompanyCode = routerCompanyCode;
	}
	public Long getRouterStationId() {
		return routerStationId;
	}
	public void setRouterStationId(Long routerStationId) {
		this.routerStationId = routerStationId;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public Protocol getProtocol() {
		return protocol;
	}
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	public boolean isNeedResetAuthKey() {
		return needResetAuthKey;
	}
	public void setNeedResetAuthKey(boolean needResetAuthKey) {
		this.needResetAuthKey = needResetAuthKey;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}
	public long getIoExceptionTime() {
		return ioExceptionTime;
	}
	public void setIoExceptionTime(long ioExceptionTime) {
		this.ioExceptionTime = ioExceptionTime;
	}
	public List<String> getCommands() {
		return commands;
	}
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	public String getRemoteConnectInfo() {
		return remoteConnectInfo;
	}
	public void setRemoteConnectInfo(String remoteConnectInfo) {
		this.remoteConnectInfo = remoteConnectInfo;
	}
	
}
