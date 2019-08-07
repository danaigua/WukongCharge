package com.zhicheng.wukongcharge.model;

/**
 * Tomda协议抽象实现
 * <p>
 * Created by jiafeng on 2017/2/17.
 */
public abstract class TomdaProtocolModel implements ITomdaProtocolModel {

    private String frame;               // 完整帧
    private long msgId;                 // 序列号
    private String chargerSerialNum;    // 充电桩序列号

    public String getProtocolId() {
        return TomdaProtocol.PROTOCOL_ID;
    }

    public int getProtocolVersion() {
        return TomdaProtocol.PROTOCOL_VERSION;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getChargerSerialNum() {
        return chargerSerialNum;
    }

    public void setChargerSerialNum(String chargerSerialNum) {
        this.chargerSerialNum = chargerSerialNum;
    }
}
