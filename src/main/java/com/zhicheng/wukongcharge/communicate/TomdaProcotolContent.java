package com.zhicheng.wukongcharge.communicate;


import com.zhicheng.wukongcharge.base.charger.IChargeProtocolModel;
import com.zhicheng.wukongcharge.model.TomdaProtocol;
import com.google.protobuf.MessageLite;

/**
 * Tomda解码后数据传输内容
 * Created by jiafeng on 2018/09/09.
 */
public class TomdaProcotolContent implements IChargeProtocolModel {

    private int dataType;                           // 指令标识
    private int msgId;                              // 序列号
    private MessageLite messageLite;                // proto内容
    private String frame;                           // 指令内容

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

    public String getProtocolId() {
        return TomdaProtocol.PROTOCOL_ID;
    }

    public String getActionID() {
        return null;
    }

    public int getProtocolVersion() {
        return TomdaProtocol.PROTOCOL_VERSION;
    }

    public void initWithFrame(String frame) {

    }

    public String organizeFrame() {
        return null;
    }
}
