package com.zhicheng.wukongcharge.communicate;

import com.zhicheng.wukongcharge.communicate.processor.TomdaChargeActionProcessor;
import com.zhicheng.wukongcharge.tcp.KeyAttach;
import com.zhicheng.wukongcharge.model.TomdaProtocol;
import com.zhicheng.wukongcharge.models.*;
import com.zhicheng.wukongcharge.tcp.KeyAttach;
import com.zhicheng.wukongcharge.tool.PTool;
import com.zhicheng.wukongcharge.tool.Tool;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * Tomda协议解码类
 * Created by jiafeng on 2018/09/09.
 */
public class TomdaChargerDecoder extends ByteToMessageDecoder {
    private Logger logger = Logger.getLogger(TomdaChargerDecoder.class);

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.toString().equals("EmptyByteBufBE")) {
            logger.info("终端已断开连接！");
            return;
        }
        // 如果写入数据的下标大于读取的下标，就释放一下缓冲
        if (buffer.readerIndex() < buffer.writerIndex()) {
            buffer.discardReadBytes();
        }

        // 复制一个buffer来判断数据协议，如果读取了原buffer，就不能进行断包处理
        ByteBuf copyBuf = buffer.copy();
        String content = decode(copyBuf.nioBuffer());
        int length = content.length();

        // 判断终端是否在缓存中
        String chargerInfo = "";
        KeyAttach keyAttach = (KeyAttach) ctx.channel().attr(TomdaChargeActionProcessor.NETTY_CHANNEL_KEY).get();
        if (keyAttach != null && keyAttach.getRouterSerialNum() != null) {
            chargerInfo = "[" + keyAttach.getRouterSerialNum() + "]";
        }
        logger.info((Tool.formatDateTime(new Date()) + "Tomda协议,终端" + chargerInfo + "上报的命令length = " + length + ", show content = " + Tool
                .showData(content)));
        try {
            // 判断协议开头
            if (!content.startsWith(TomdaProtocol.PREFIX_TEXT)) {
                // 判断内容中是否包含协议头,可能是终端组包错误
                int protocolHeaderIndex = content.indexOf(TomdaProtocol.PREFIX_TEXT);
                if (protocolHeaderIndex > 0) {
                    int discardLength = protocolHeaderIndex;
                    ByteBuf discardBuf = buffer.readBytes(discardLength);
                    logger.info("Tomda协议,服务接收到终端" + chargerInfo + "的指令协议头不对,但指令中有包含协议头，丢弃内容长度[" + discardLength + "],丢弃内容：" + Tool.showData(decode(discardBuf
                            .nioBuffer())));
                    discardBuf.release();  // 用完即释放
                } else {
                    logger.info("Tomda协议,服务接收到终端" + chargerInfo + "的指令不符合规范，指令内容：" + Tool.showData(content));
                    buffer.clear();
                }
                return;
            }
            int validLength = PTool.count(content.charAt(TomdaProtocol.BIT_DATA_LENGTH + 1), content.charAt(TomdaProtocol.BIT_DATA_LENGTH));
            if (length >= validLength) {
                // 读取有效长度的buffer
                ByteBuf validLengthBuf = copyBuf.readBytes(validLength);
                copyBuf.release();  // 用完即释放

                // 把ByteBuf转为逻辑处理中所需的String
                String validLengthContent = decode(validLengthBuf.nioBuffer());
                validLengthBuf.release();     // 用完即释放

                //获取校验和
                int checksumRead = PTool.count(content.charAt(validLength - 1), content.charAt(validLength - 2));
                // 计算校验和，计算范围包含从 协议头 到 数据域
                int checksumCalculate = PTool.calc_crc16(validLengthContent.substring(0, validLength - 2));
//                logger.info("校验和，checksum为 " + checksumRead + ", checksumCalculate为 " + checksumCalculate);
                if (checksumCalculate == checksumRead) {    // 检测校验和是否合格

                    // 读取ByteBuf中的流数据，读出字节数组
                    ByteBuf validBuf = buffer.readBytes(validLength);

                    String validContent = decode(validBuf.nioBuffer());
                    validBuf.release();     // 用完即释放

                    int msgId = PTool.count(validContent.charAt(9), validContent.charAt(8));
                    // 获取指令类型
                    int dataType = PTool.count(validContent.charAt(7), validContent.charAt(6));

                    // 数据域
                    String dataContent = validContent.substring(TomdaProtocol.BIT_DATA, validLength - 2);
                    byte[] bytes = new byte[dataContent.length()];
                    for (int i = 0; i < dataContent.length(); ++i) {
                        bytes[i] = (byte) dataContent.charAt(i);
                    }
                    MessageLite messageLite = decodeBody(dataType, bytes);
                    TomdaProcotolContent procotolContent = new TomdaProcotolContent();
                    procotolContent.setDataType(dataType);
                    procotolContent.setMsgId(msgId);
                    procotolContent.setMessageLite(messageLite);
                    procotolContent.setFrame(validContent);
                    out.add(procotolContent);
                } else {
                    logger.error("Tomda协议,终端" + chargerInfo + "上报的非法帧：校验和校验失败，checksum为 " + checksumRead + ", checksumCalculate为 " + checksumCalculate);
                    // 判断内容中是否包含协议头,可能是终端包错误
                    int protocolHeaderIndex = validLengthContent.substring(2).indexOf(TomdaProtocol.PREFIX_TEXT);
                    if (protocolHeaderIndex > 0) {
                        int discardLength = protocolHeaderIndex + 2;
                        ByteBuf discardBuf = buffer.readBytes(discardLength);
                        logger.info("Tomda协议,服务接收到终端" + chargerInfo + "的指令不符合规范，丢弃内容长度[" + validLength + "],丢弃内容：" + Tool.showData(decode(discardBuf
                                .nioBuffer())));
                        discardBuf.release(); // 用完即释放
                    } else {
                        buffer.readBytes(validLength).release();    // 释放资源
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Tomda协议,处理终端" + chargerInfo + "数据包异常 : " + e.getMessage(), e);
            buffer.clear();
        } finally {
//            ReferenceCountUtil.release(buffer);
        }
    }

    private Charset charset = Charset.forName("ISO-8859-1");

    public String decode(ByteBuffer buffer) {
        try {
            return charset.newDecoder().decode(buffer).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public MessageLite decodeBody(int dataType, byte[] data1) throws Exception {
        ByteArrayInputStream data = new ByteArrayInputStream(data1);
        switch (dataType) {
            case 101:
                return BootNotificationProto.bootNotificationReq.getDefaultInstance().getParserForType().parseFrom(data);                       // EVSE登录请求
            case 102:
                return BootNotificationProto.bootNotificationResp.getDefaultInstance().getParserForType().parseFrom(data);                      // EVSE登录应答
            case 103:
                return BeatHeartProto.beatHeartReq.getDefaultInstance().getParserForType().parseFrom(data);                                     // EVSE心跳请求
            case 104:
                return BeatHeartProto.beatHeartResp.getDefaultInstance().getParserForType().parseFrom(data);                                    // Server心跳应答
            case 201:
                return AuthorizeProto.authorizeReq.getDefaultInstance().getParserForType().parseFrom(data);                                     // EVSE本地授权应答
            case 202:
                return AuthorizeProto.authorizeResp.getDefaultInstance().getParserForType().parseFrom(data);                                    // EVSE本地授权请求
            case 203:
                return StartTransactionProto.startTransactionResp.getDefaultInstance().getParserForType().parseFrom(data);                       // EVSE远程授权充电应答
            case 204:
                return StartTransactionProto.startTransactionReq.getDefaultInstance().getParserForType().parseFrom(data);                      // 远程授权充电请求
            case 205:
                return StopTransactionProto.stopTransactionResp.getDefaultInstance().getParserForType().parseFrom(data);                         // 后台停止充电应答
            case 206:
                return StopTransactionProto.stopTransactionReq.getDefaultInstance().getParserForType().parseFrom(data);                        // 后台停止充电请求
            case 207:
                return CostUpdatedProto.costUpdatedResp.getDefaultInstance().getParserForType().parseFrom(data);                                 // 更新充电金额/时间应答
            case 208:
                return CostUpdatedProto.costUpdatedReq.getDefaultInstance().getParserForType().parseFrom(data);                                // 更新充电金额/时间请求
            case 301:
                return TransactionEventProto.transactionEventReq.getDefaultInstance().getParserForType().parseFrom(data);                       // 事务事件上报
            case 302:
                return TransactionEventProto.transactionEventResp.getDefaultInstance().getParserForType().parseFrom(data);                      // 事务事件应答
            case 303:
                return ChargingDataReportProto.chargingDataReportReq.getDefaultInstance().getParserForType().parseFrom(data);                   // 工作数据上报
            case 304:
                return ChargingDataReportProto.chargingDataReportResp.getDefaultInstance().getParserForType().parseFrom(data);                  // 工作数据上报应答
            case 305:
                return NotifyEventProto.notifyEventReq.getDefaultInstance().getParserForType().parseFrom(data);                                 // 事件上报
            case 306:
                return NotifyEventProto.notifyEventResp.getDefaultInstance().getParserForType().parseFrom(data);                                // 事件上报应答
            case 307:
                return StatusNotificationProto.statusNotificationReq.getDefaultInstance().getParserForType().parseFrom(data);                   // 插头状态上报
            case 308:
                return StatusNotificationProto.statusNotificationResp.getDefaultInstance().getParserForType().parseFrom(data);                  // 插头状态上报应答
            case 401:
                return SetVariablesProto.setVariablesResp.getDefaultInstance().getParserForType().parseFrom(data);                              // 设置组件变量应答
            case 402:
                return SetVariablesProto.setVariablesReq.getDefaultInstance().getParserForType().parseFrom(data);                               // 设置组件变量
            case 403:
                return GetVariableProto.getVariableResp.getDefaultInstance().getParserForType().parseFrom(data);                                // 获取组件变量应答
            case 404:
                return GetVariableProto.getVariableReq.getDefaultInstance().getParserForType().parseFrom(data);                                 // 获取组件变量
            case 501:
                return UpdateFirmwareEventProto.updateFirmwareEventResp.getDefaultInstance().getParserForType().parseFrom(data);                // 固件更新请求应答
            case 502:
                return UpdateFirmwareEventProto.updateFirmwareEventReq.getDefaultInstance().getParserForType().parseFrom(data);                 // 固件更新请求
            case 503:
                return FirmwareStatusNotificationProto.firmwareStatusNotificationReq.getDefaultInstance().getParserForType().parseFrom(data);   // 固件更新状态上报
            case 504:
                return FirmwareStatusNotificationProto.firmwareStatusNotificationResp.getDefaultInstance().getParserForType().parseFrom(data);  // 固件更新状态上报应答
            case 505:
                return DataTransferProto.dataTransferResp.getDefaultInstance().getParserForType().parseFrom(data);                               // 数据传输请求应答
            case 506:
                return DataTransferProto.dataTransferReq.getDefaultInstance().getParserForType().parseFrom(data);                              // 数据传输请求
            case 601:
                return TriggerMessageProto.triggerMessageResp.getDefaultInstance().getParserForType().parseFrom(data);                          // 触发消息应答
            case 602:
                return TriggerMessageProto.triggerMessageReq.getDefaultInstance().getParserForType().parseFrom(data);                           // 触发消息请求
        }

        return null; // or throw exception
    }

}
