package com.zhicheng.wukongcharge.entity;

import com.google.protobuf.MessageLite;

public class TomdaProcotolContent {
	private int dataType;//指令标识
	private int msgId;//系列号
    private MessageLite messageLite;// proto内容
	private String frame;//指令内容
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public MessageLite getMessageLite() {
		return messageLite;
	}
	public void setMessageLite(MessageLite messageLite) {
		this.messageLite = messageLite;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}

}
