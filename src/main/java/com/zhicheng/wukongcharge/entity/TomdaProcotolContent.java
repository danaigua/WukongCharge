package com.zhicheng.wukongcharge.entity;

import com.google.protobuf.MessageLite;

public class TomdaProcotolContent {
	private int dataType;//ָ���ʶ
	private int msgId;//ϵ�к�
    private MessageLite messageLite;// proto����
	private String frame;//ָ������
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
