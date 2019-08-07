package com.zhicheng.wukongcharge.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.zhicheng.wukongcharge.communicate.processor.ProConstants;
import com.zhicheng.wukongcharge.model.TomdaCrc16;

public class PTool {

	/**
	 * 将数字转换成字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String getStringFromHex(long data) {
		return new Character((char) (int) data).toString();
	}
	
	public static int count(int dataH, int dataL) {
		return dataH * 256 + dataL;
	}
	
	/**
	 * 考虑有符号位
	 * @param dataH
	 * @param dataL
	 * @return
	 */
	public static int countN(int dataH, int dataL) {
		int data = dataH * 256 + dataL;
		if((data & 0x8000) > 0) {
			data = -(0xFFFF - data + 1);
		}
		return data;
	}

	public static int count(int dataHL, int dataLH, int dataLL) {
		return dataHL * 65536 + dataLH * 256 + dataLL;
	}

	public static int count(int dataHH, int dataHL, int dataLH, int dataLL) {
		return (dataHH * 256 + dataHL) * 65536 + dataLH * 256 + dataLL;
	}

	public static double count(int dataHH, int dataHL, int dataLH, int dataLL, int divi) {
		return ((dataHH * 256 + dataHL) * 65536 + dataLH * 256 + dataLL) / divi;
	}

	public static int[] count2(int data) {
		int[] rs = new int[2];
		rs[0] = (data / 256);
		rs[1] = Math.abs(data % 256);
		return rs;
	}

	/**
	 * 将数据转换成二进制命令，2个Byte，高位数据在前
	 * 
	 * @param data
	 * @return
	 */
	public static String count2TextHigh(int data) {
		int[] rs = count2(data);
		return getStringFromHex(rs[0]) + getStringFromHex(rs[1]);
	}

	/**
	 * 将数据转换成二进制命令，2个Byte，低位数据在前
	 * 
	 * @param data
	 * @return
	 */
	public static String count2TextLow(int data) {
		int[] rs = count2(data);
		return getStringFromHex(rs[1]) + getStringFromHex(rs[0]);
	}

	/**
	 * 将数据转换成二进制命令，4个Byte，高位数据在前
	 * 
	 * @param data
	 * @return
	 */
	public static String count4TextHigh(long data) {
		long[] rs = new long[4];
		long highData = data / 65536L;
		long lowData = Math.abs(data % 65536L);
		rs[0] = (lowData % 256L);
		rs[1] = (lowData / 256L);
		rs[2] = (highData % 256L);
		rs[3] = (highData / 256L);
		return getStringFromHex(rs[3]) + getStringFromHex(rs[2]) + getStringFromHex(rs[1]) + getStringFromHex(rs[0]);
	}

    /**
     * 将数据转换成二进制命令，6个byte，高位数据在前
     *
     * @param data
     * @return
     */
    public static String count6TextHigh(long data) {
        long[] rs = new long[6];
        long highData = data / 4294967296L;
        long lowData = Math.abs(data % 4294967296L);
        rs[0] = ((lowData % 65536L) % 256L);
        rs[1] = ((lowData % 65536L) / 256L);
        rs[2] = ((lowData / 65536L) % 256L);
        rs[3] = ((lowData / 65536L) / 256L);
        rs[4] = ((highData % 65536L) % 256L);
        rs[5] = ((highData % 65536L) / 256L);
        return getStringFromHex(rs[5]) + getStringFromHex(rs[4]) + getStringFromHex(rs[3]) + getStringFromHex(rs[2])
                + getStringFromHex(rs[1]) + getStringFromHex(rs[0]);
    }

	/**
	 * 将数据转换成二进制命令，4个Byte，低位数据在前
	 * 
	 * @param data
	 * @return
	 */
	public static String count4TextLow(long data) {
		long[] rs = new long[4];
		long highData = data / 65536L;
		long lowData = Math.abs(data % 65536L);
		rs[0] = (lowData % 256L);
		rs[1] = (lowData / 256L);
		rs[2] = (highData % 256L);
		rs[3] = (highData / 256L);
		return getStringFromHex(rs[0]) + getStringFromHex(rs[1]) + getStringFromHex(rs[2]) + getStringFromHex(rs[3]);
	}
	
	/**
	 * 将数据转换成二进制命令，16个Byte，高位数据在前
	 *
	 * @param data
	 * @return
	 */
	public static String count16TextHigh(String data) {
		int hex = Integer.parseInt(data, 16);
		String str = Integer.toBinaryString(hex);
		// 对16位的2进制进行高低位反转
		StringBuffer strBuffer = new StringBuffer(str).reverse();
		// 不够16位后边补0
		while (strBuffer.length() < 16) {
			strBuffer.append("0");
		}
		return strBuffer.toString();
	}

	public static String balanceTo16(int balance) {
		if (balance >= 0) {
			return PTool.count4TextLow(balance);
		} else {
			String hex = Integer.toHexString(balance);
			StringBuffer balanceStr = new StringBuffer();
			char[] chars = hex.toCharArray();
			for (int i = 0, len = chars.length; i < len; i += 2) {
				String hexString = chars[i] + "" + chars[i + 1];
				balanceStr.insert(0, PTool.getStringFromHex(Integer.parseInt(hexString, 16)));
			}
			return balanceStr.toString();
		}
	}

	public static int modbus_Caluation_CRC16(String data) {
		int crc = 0;
		for(int i = 0; i < data.length(); i++) {
			char pData = data.charAt(i);
			crc ^= pData;
			for(int j = 0; j < 8; j++) {
				if((crc & 0x01) > 0) {
					crc = (crc >> 1) ^ 0xa001;
				} else {
					crc = crc >> 1;
				}
			}
		}
		return crc;
	}
	
	public static boolean validateCrcCode(String content) {
		//获取报文中的数据长度
		int len = PTool.count(content.charAt(ProConstants.BIT_DATA_LENGTH), content.charAt(ProConstants.BIT_DATA_LENGTH + 1)) + 11;
		// 根据数据长度截取真实数据及报文中的CRC码
		String realData = content.substring(0, len - 2);
		String crcCodeStr = content.substring(len - 2);
		int crcCode = count(crcCodeStr.charAt(0), crcCodeStr.charAt(1));
		// CRC校验接收到的报文内容
		int realCrcCode = modbus_Caluation_CRC16(realData);
		
		return crcCode == realCrcCode;
	}
	
	/**
	 * 计算校验和
	 *
	 * @param content
	 * @return
	 */
	public static int calc_checksum(String content) {
		byte result = 0;
		for (int i = 0; i < content.length(); i++) {
			result += Integer.valueOf(Integer.toHexString(content.charAt(i)),16).byteValue();
		}
		return result;
	}

	/**
	 * CRC16校验
	 *
	 * @param content
	 * @return
	 */
	public static int calc_crc16(String content) {
		int[] ints = new int[content.length()];
		for (int i = 0; i < content.length(); ++i) {
			ints[i] = content.charAt(i);
		}
		return TomdaCrc16.compute16(ints);
	}
	
	/**
	 * 转换字符串为容易阅读的16进制数据
	 *
	 * @param command
	 * @return
	 */
	public static String showData(String command) {
		String str = "";
		for (char c : command.toCharArray()) {
			String hexString = Integer.toHexString(c).toUpperCase();
			if (hexString.length() == 1) {
				hexString = "0" + hexString;
			}
			str += hexString + " ";
		}
		return str.trim();
	}
	
	/**
	 * ASCII码转字符串
	 *
	 * @param content
	 * @return
	 */
	public static String asc2Str(String content) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0, len = content.length(); i < len; i++) {
			if (content.charAt(i) > 30) {
				stringBuffer.append(content.substring(i, i + 1));
			} else {
				continue;
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 获取16进制的0
	 *
	 * @param length 需要获取多少位的0
	 * @return
	 */
	public static String getZeros(int length) {
		StringBuffer buffer = new StringBuffer();
		while (buffer.length() < length) {
			buffer.append(PTool.getStringFromHex(0));
		}
		return buffer.toString();
	}

    /**
     * 从通讯数据中获取设备时间
     * @param timeText
     * @return
     */
    public static Calendar getDeviceCalendar(String timeText) {
        Calendar deviceCalendar = Calendar.getInstance();
        deviceCalendar.set(Calendar.YEAR, timeText.charAt(0) + 2000);
        deviceCalendar.set(Calendar.MONTH, timeText.charAt(1) - 1);
        deviceCalendar.set(Calendar.DAY_OF_MONTH, timeText.charAt(2));
        deviceCalendar.set(Calendar.HOUR_OF_DAY, timeText.charAt(3));
        deviceCalendar.set(Calendar.MINUTE, timeText.charAt(4));
        deviceCalendar.set(Calendar.SECOND, timeText.charAt(5));
        deviceCalendar.set(Calendar.MILLISECOND, 0);
        return deviceCalendar;
    }

	/**
	 * 获取高四位
	 * @param data
	 * @return 16进制字符串
	 */
	public static int getHeight4(int data){
		return ((data & 0xf0) >> 4);
	}

	/**
	 * 获取低四位
	 * @param data
	 * @return 16进制字符串
	 */
	public static int getLow4(int data){
		return (data & 0x0f);
	}

	/**
	 * 获取当前时间的10进制字符串
	 *
	 * @return
	 */
	public static String getCurrentTimeStr10() {
		return getDateTimeStr10(new Date());
	}

	/**
	 * 获取时间的10进制字符串
	 *
	 * @param inputDate
	 * @return
	 */
	public static String getDateTimeStr10(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(inputDate);
		StringBuffer dateBuffer = new StringBuffer();
		char[] dateChars = date.toCharArray();
		for (int i = 0; i < dateChars.length; i += 2) {
			dateBuffer.append(PTool.getStringFromHex(Integer.valueOf(String.valueOf(dateChars[i]) + String.valueOf
					(dateChars[i + 1]), 16)));
		}
		return dateBuffer.toString();
	}

	/**
	 * 将数据转换成二进制命令，N个Byte，高位数据在前
	 *
	 * @param data
	 * @param hexByte
	 * @return
	 */
	public static String decimalToBinary(String data, int hexByte) {
		int hex = Integer.parseInt(data, hexByte);
		String str = Integer.toBinaryString(hex);
		// 对2进制进行高低位反转
		StringBuffer strBuffer = new StringBuffer(str).reverse();
		// 不够位后边补0
		while (strBuffer.length() < hexByte) {
			strBuffer.append("0");
		}
		return strBuffer.toString();
	}

}
