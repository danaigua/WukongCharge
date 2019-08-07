package com.zhicheng.wukongcharge.model;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.zhicheng.wukongcharge.base.charger.IChargeActionProcessor;
import com.zhicheng.wukongcharge.base.charger.IChargeProtocol;
import com.zhicheng.wukongcharge.base.charger.IChargeProtocolException;
import com.zhicheng.wukongcharge.base.charger.IChargeProtocolModel;
import com.zhicheng.wukongcharge.communicate.TomdaChargerDecoder;
import com.zhicheng.wukongcharge.models.AuthorizeProto;
import com.zhicheng.wukongcharge.models.BeatHeartProto;
import com.zhicheng.wukongcharge.models.BootNotificationProto;
import com.zhicheng.wukongcharge.models.ChargingDataReportProto;
import com.zhicheng.wukongcharge.models.CostUpdatedProto;
import com.zhicheng.wukongcharge.models.DataTransferProto;
import com.zhicheng.wukongcharge.models.FirmwareStatusNotificationProto;
import com.zhicheng.wukongcharge.models.GetVariableProto;
import com.zhicheng.wukongcharge.models.NotifyEventProto;
import com.zhicheng.wukongcharge.models.SetVariablesProto;
import com.zhicheng.wukongcharge.models.StartTransactionProto;
import com.zhicheng.wukongcharge.models.StatusNotificationProto;
import com.zhicheng.wukongcharge.models.StopTransactionProto;
import com.zhicheng.wukongcharge.models.TransactionEventProto;
import com.zhicheng.wukongcharge.models.TriggerMessageProto;
import com.zhicheng.wukongcharge.models.UpdateFirmwareEventProto;
import com.zhicheng.wukongcharge.tool.PTool;

/**
 * Tomda协议
 * <p>
 * Created by jiafeng on 2018/1/18.
 */
@Component
public class TomdaProtocol implements IChargeProtocol {
    private Logger log = Logger.getLogger(TomdaProtocol.class);

    public static final String PROTOCOL_ID = "TOMDA"; // TOMDA协议
    
//    public static final int PROTOCOL_VERSION = 1;
    
    public static final int PROTOCOL_VERSION = 6;        // TOMDA协议版本

    private Map<Integer, Class<? extends MessageLite>> protocolModelMap = new LinkedHashMap<Integer, Class<? extends MessageLite>>();
//    public static  Map<Integer, Class<? extends MessageLite>> protocolModelMap = new LinkedHashMap<Integer, Class<? extends MessageLite>>();

    private Map<String, IChargeActionProcessor> processorMap = new LinkedHashMap<String, IChargeActionProcessor>();

    // 通讯端口
    private int protocolPort;

    // 帧头
//    public static final String PREFIX_TEXT = "0xAA0x55";
//    public static final String PREFIX_TEXT = "1010101001010101";
    public static final String PREFIX_TEXT = PTool.getStringFromHex(0xAA) + PTool.getStringFromHex(0x55);

    public static final int BIT_DATA_LENGTH = 4;    //数据长度起始位置
    public static final int BIT_DATA = 10;           //数据起始位置

    public void loadModels() {
        protocolModelMap.put(101, BootNotificationProto.bootNotificationReq.class);                         // EVSE登录请求
        protocolModelMap.put(102, BootNotificationProto.bootNotificationResp.class);                        // EVSE登录应答
        protocolModelMap.put(103, BeatHeartProto.beatHeartReq.class);                                       // EVSE心跳请求
        protocolModelMap.put(104, BeatHeartProto.beatHeartResp.class);                                      // Server心跳应答
        protocolModelMap.put(201, AuthorizeProto.authorizeReq.class);                                       // EVSE本地授权应答
        protocolModelMap.put(202, AuthorizeProto.authorizeResp.class);                                      // EVSE本地授权请求
        protocolModelMap.put(203, StartTransactionProto.startTransactionReq.class);                         // EVSE远程授权充电应答
        protocolModelMap.put(204, StartTransactionProto.startTransactionResp.class);                        // 远程授权充电请求
        protocolModelMap.put(205, StopTransactionProto.stopTransactionReq.class);                           // 后台停止充电应答
        protocolModelMap.put(206, StopTransactionProto.stopTransactionResp.class);                          // 后台停止充电请求
        protocolModelMap.put(207, CostUpdatedProto.costUpdatedReq.class);                                   // 更新充电金额/时间应答
        protocolModelMap.put(208, CostUpdatedProto.costUpdatedResp.class);                                  // 更新充电金额/时间请求
        protocolModelMap.put(301, TransactionEventProto.transactionEventReq.class);                         // 事务事件上报
        protocolModelMap.put(302, TransactionEventProto.transactionEventResp.class);                        // 事务事件应答
        protocolModelMap.put(303, ChargingDataReportProto.chargingDataReportReq.class);                     // 工作数据上报
        protocolModelMap.put(304, ChargingDataReportProto.chargingDataReportResp.class);                    // 工作数据上报应答
        protocolModelMap.put(305, NotifyEventProto.notifyEventReq.class);                                   // 事件上报
        protocolModelMap.put(306, NotifyEventProto.notifyEventResp.class);                                  // 事件上报应答
        protocolModelMap.put(307, StatusNotificationProto.statusNotificationReq.class);                     // 插头状态上报
        protocolModelMap.put(308, StatusNotificationProto.statusNotificationResp.class);                    // 插头状态上报应答
        protocolModelMap.put(401, SetVariablesProto.setVariablesResp.class);                                // 设置组件变量应答
        protocolModelMap.put(402, SetVariablesProto.setVariablesReq.class);                                 // 设置组件变量
        protocolModelMap.put(403, GetVariableProto.getVariableResp.class);                                  // 获取组件变量应答
        protocolModelMap.put(404, GetVariableProto.getVariableReq.class);                                   // 获取组件变量
        protocolModelMap.put(501, UpdateFirmwareEventProto.updateFirmwareEventResp.class);                  // 固件更新请求应答
        protocolModelMap.put(502, UpdateFirmwareEventProto.updateFirmwareEventReq.class);                   // 固件更新请求
        protocolModelMap.put(503, FirmwareStatusNotificationProto.firmwareStatusNotificationReq.class);     // 固件更新状态上报
        protocolModelMap.put(504, FirmwareStatusNotificationProto.firmwareStatusNotificationResp.class);    // 固件更新状态上报应答
        protocolModelMap.put(505, DataTransferProto.dataTransferReq.class);                                 // 数据传输请求应答
        protocolModelMap.put(506, DataTransferProto.dataTransferResp.class);                                // 数据传输请求
        protocolModelMap.put(601, TriggerMessageProto.triggerMessageResp.class);                            // 触发消息应答
        protocolModelMap.put(602, TriggerMessageProto.triggerMessageReq.class);                             // 触发消息请求

    }

    public int getProtocolPort() {
        return protocolPort;
    }

    public void setProtocolPort(int protocolPort) {
        this.protocolPort = protocolPort;
    }

    public boolean hasPossibleFrame(String frame, int protocolPort) {
        if (frame.startsWith(PREFIX_TEXT) && this.protocolPort == protocolPort) {
            return true;
        }
        return false;
    }

    public List<IChargeProtocolModel> parseModels(String frames, StringBuffer remaining)
            throws IChargeProtocolException {
        return null;
    }

    public IChargeProtocolModel parseModel(String completeFrame) throws IChargeProtocolException {
        return null;
    }

    public String getProtocolID() {
        return PROTOCOL_ID;
    }

    public void addActionProcessor(String actionID, IChargeActionProcessor processor) {
        processorMap.put(actionID, processor);
    }

    public void removeActionProcessor(String actionID) {
        processorMap.remove(actionID);
    }

    public IChargeActionProcessor getActionProcessor(String actionID) {
        if (null != processorMap.get(actionID)) {
            return processorMap.get(actionID);
        }
        return null;
    }

    /**
     * 组织协议帧
     *
     * @return 完整帧内容
     */
    public static String organizeFrame(int actionId, long msgId, MessageLite messageLite) {
        int dataLength = 12 + messageLite.getSerializedSize();
        StringBuffer cmd = new StringBuffer();
        cmd.append(PREFIX_TEXT);                                                // 起始
        cmd.append(PTool.getStringFromHex(1));                                  // 协议版本号
        cmd.append(PTool.getZeros(1));                                          // 加密子
        cmd.append(PTool.count2TextLow(dataLength));                            // 总包长度
        cmd.append(PTool.count2TextLow(actionId));                              // 协议ID
        cmd.append(PTool.count2TextLow((int) msgId));                           // 序列号
//        cmd.append(bytesToString(messageLite.toByteString(), "ISO-8859-1"));  // 数据域
        cmd.append(encode(messageLite));                                        // 数据域
        int crc16Value = PTool.calc_crc16(cmd.toString());
        cmd.append(PTool.count2TextLow(crc16Value));                            // CRC16校验码
        return cmd.toString();
    }

    public static String encode(MessageLite messageLite) {
        try {
            byte[] bytes = messageLite.toByteArray();
            return new String(bytes,"ISO-8859-1");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String bytesToString(ByteString src, String charSet) {
        if (StringUtils.isEmpty(charSet)) {
            charSet = "ISO-8859-1";
        }
        return bytesToString(src.toByteArray(), charSet);
    }

    public static String bytesToString(byte[] input, String charSet) {
        if (input == null || input.length == 0) {
            return "";
        }

        ByteBuffer buffer = ByteBuffer.allocate(input.length);
        buffer.put(input);
        buffer.flip();

        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;

        try {
            charset = Charset.forName(charSet);
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public static void main(String args[]) {
//    	Builder builder2 = BootNotificationProto.bootNotificationReq.newBuilder();
//    	builder2.setReason(bootReasonEnumType.ApplicationReset).setSerialNumber("10101010").setKey("10101").setPcbID(111).setVendorName("aaa").setFirmwareVersion("111");
//    	bootNotificationReq req = builder2.build();
//    	byte[] byteArray = req.toByteArray();
//    	System.out.println(Arrays.toString(byteArray));
//    	System.out.println(builder2);
//    	organizeFrame(1, 1, req);
//    	System.out.println(organizeFrame(1, 1, req));
//    	TomdaChargerDecoder t = new TomdaChargerDecoder();
//    	t.decodeBody(101, data1);
    }
}
