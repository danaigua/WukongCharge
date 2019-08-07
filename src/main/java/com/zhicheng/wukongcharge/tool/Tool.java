package com.zhicheng.wukongcharge.tool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.joda.time.Interval;
import org.joda.time.Period;

import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;

public final class Tool {

    private static final SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat noSymbolDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private static final SimpleDateFormat minuteFormat = new SimpleDateFormat("mm分");

    private static final SimpleDateFormat timeMillis = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static final Random random = new Random();
    
    public static boolean isWjDevice(String chargerSerialNum) {
        return chargerSerialNum != null && chargerSerialNum.startsWith("W") && chargerSerialNum.length() == 14;
    }
    /**
           * 格式化当天日期
     *
     * @return
     */
    public synchronized static String formatTodayDate() {
        return dateFormat.format(new Date()).replaceAll("-", "");
    }
    /**
           * 创建一个uuid
     *
     * @return
     */
    public static String createUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**
     * 得到资源文件的根路径
     * @return
     */
    public static String getResourceRoot() { 
    	return System.getProperty("user.home") + "/"; 
    }
    /**
     * 获得图片的后缀名
     * @param path
     * @return
     */
    public static String getImageExtension(String path) {
    	int lastIndex = path.lastIndexOf(".");
        if (lastIndex > 0 && lastIndex > path.lastIndexOf("/")) {
            String extension = path.substring(lastIndex).toLowerCase();
            if (".jpg".equals(extension) || ".jpeg".equals(extension) || ".png".equals(extension) || ".gif".equals(extension) || ".bmp".contains(extension)) {
                return extension;
            }
        }
        return ".jpg";
    }
    /**
     * 解析yyyy-MM-dd格式或yyyy_MM_dd格式的日期字串
     *
     * @param dateText
     * @return
     */
    public synchronized static Date parseDate(String dateText) {
        try {
            return isEmpty(dateText) ? null : dateFormat.parse(dateText.replace("_", "-"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解析yyyy-MM-dd HH:mm:ss格式的日期时间字串
     *
     * @param datetimeText
     * @return
     */
    public synchronized static Date parseDatetime(String datetimeText) {
        try {
            return isEmpty(datetimeText) ? null : dateTimeFormat.parse(datetimeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解析无符号的yyyyMMddHHmmss格式的日期时间字串
     *
     * @param noSymbolDatetimeText
     * @return
     */
    public synchronized static Date parseNoSymbolDatetime(String noSymbolDatetimeText) {
        try {
            return isEmpty(noSymbolDatetimeText) ? null : noSymbolDateTimeFormat.parse(noSymbolDatetimeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将Date日期格式化成yyyy-MM格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatMonthDate(Date date) {
        return monthDateFormat.format(date);
    }
    /**
     * 将Date日期格式化成yyyy-MM-dd格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 将Date日期格式化成yyyy-MM-dd HH:mm:ss格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 将Date日期格式化成yyyyMMddHHmmss格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatNoSymbolDateTime(Date date) {
        return noSymbolDateTimeFormat.format(date);
    }

    public static String showDate(Date date) {
        return date != null ? Tool.formatDate(date) : "";
    }

    public static String showDateTime(Date date) {
        return date != null ? Tool.formatDateTime(date) : "";
    }

    /**
     * 将Date时间格式化成HH:mm:ss格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 将Date时间格式化成mm:ss格式
     *
     * @param date
     * @return
     */
    public synchronized static String formatMinute(Date date) {
        return minuteFormat.format(date);
    }

    /**
     * 检测字符串是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj) {
        if (obj == null || "".equals(obj.trim())) {
            return true;
        }
        return false;
    }

    public static float formatFloat(float data, int digits) {
        return Float.valueOf(formatData(data, digits)).floatValue();
    }

    /**
     * 格式化float数据,保留一位小数
     *
     * @param data
     * @return
     */
    public static float formatFloat(float data) {
        return formatFloat(data, 1);
    }

    public static String formatData(double data) {
        return formatData(data, 1);
    }

    public static String formatData(double data, int digits) {
        NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(digits);
        return nf.format(data);
    }

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前线程休眠
     *
     * @param seconds 秒
     */
    public static void delay(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Boolean值转换成js可用判断
     *
     * @param result
     * @return
     */
    public static String toJsBoolean(boolean result) {
        return result ? "true" : "";
    }

    /**
     * 返回无权限处理失败的Json返回值
     *
     * @return
     */
    public static String getJsonNoPermissionFalseReturn() {
        return getJsonMsgReturn(false, "noPermission");
    }

    /**
     * 返回easyui的datagrid组建无数据Json返回值
     *
     * @return
     */
    public static String getJsonNoDataGridDataReturn() {
        return "{\"total\":0,\"rows\":[]}";
    }

    /**
     * 获取一个success为true的JSONObject对象
     *
     * @return
     */
    public static JSONObject getAppSuccessReturnJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        return obj;
    }

    /**
     * 获取一个success为true的JSONObject对象
     *
     * @return
     */
    public static JSONObject getAppFailureReturnJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        return obj;
    }

    /**
     * 获取一个Json回复（带msg）
     *
     * @param success
     * @param msg
     * @return
     */
    public static String getJsonMsgReturn(boolean success, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("success", success);
        obj.put("msg", msg);
        return obj.toString();
    }

    /**
     * 过滤文字中的大于号跟小于号、双引号
     *
     * @param showText
     * @return
     */
    public static String filterShowText(String showText) {
        if (Tool.isEmpty(showText)) {
            return showText;
        }
        return showText.replace("<", "").replace(">", "").replace("\"", "").replace("\'", "");
    }

    /**
     * 将int数字转换成对应ASCII字符
     *
     * @param data
     * @return
     */
    public static String getStringFromHex(long data) {
        return new Character((char) data).toString();
    }

    /**
     * 转换字符串为容易阅读的16进制数据
     *
     * @param command
     * @return
     */
    public static String showData(String command) {
        if(command == null){
            return "";
        }
        String str = "";
        for (char c : command.toCharArray()) {
            String hexString = Integer.toHexString(c);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            str += hexString + " ";
        }
        return str.toUpperCase().trim();
    }

    /**
     * 去掉卡号前面的0
     *
     * @param text
     * @return
     */
    public static String trimZeroFromBegin(String text) {
        while (text.length() > 1 && text.charAt(0) == 0x30) {
            text = text.substring(1);
        }
        return text;
    }

    /**
     * 获取高四位
     *
     * @param data
     * @return 16进制字符串
     */
    public static String getHeight4(int data) {
        int height = ((data & 0xf0) >> 4);
        return Integer.toHexString(height);
    }

    /**
     * 获取低四位
     *
     * @param data
     * @return 16进制字符串
     */
    public static String getLow4(int data) {
        int low = (data & 0x0f);
        return Integer.toHexString(low);
    }

    /**
     * 创建6位随机数字验证码
     *
     * @return
     */
    public static String createRendomVerifyCode() {
        String verifyCode = "";
        for (int i = 0; i < 6; i++) {
            verifyCode += random.nextInt(10);
        }
        return verifyCode;
    }

    /**
     * 配置Token在6天之后过期（用于本地储存token的客户端，避免使用过程中Token超时）
     *
     * @param tokenCreateTime
     * @return
     */
    public static synchronized boolean isTokenForSaveTimeout(Date tokenCreateTime) {
        Calendar now = Calendar.getInstance();
        // 用户token有效期改为一周
        now.add(Calendar.DATE, -6);
        return now.getTime().after(tokenCreateTime);
    }

    /**
     * 配置Token在7天之后过期
     *
     * @param tokenCreateTime
     * @return
     */
    public static synchronized boolean isTokenTimeout(Date tokenCreateTime) {
        Calendar now = Calendar.getInstance();
        // 用户token有效期改为一周
        now.add(Calendar.DATE, -7);
        return now.getTime().after(tokenCreateTime);
    }

    /**
     * 获取token过期时间戳
     *
     * @param createDatetime
     * @return
     */
    public static long getTokenTimelimit(Date createDatetime) {
        Calendar createDate = Calendar.getInstance();
        createDate.setTime(createDatetime);
        createDate.add(Calendar.DATE, +7);
        return createDate.getTimeInMillis() / 1000;
    }

    /**
     * 获取token剩余过期时间(单位为秒)
     *
     * @param tokenCreateTime
     * @return
     */
    public static long getTokenRemainTime(Date tokenCreateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tokenCreateTime);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        int secondsBeforeTokenTimeout = (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000) - 1;
        return secondsBeforeTokenTimeout;
    }

    /**
     * 校验1分钟内是否执行或超时订单扫描清理工作
     *
     * @param latestExecutionTime 上次扫描时间
     * @return
     */
    public static boolean isExecuted(String latestExecutionTime, int period) {
        if (TextUtils.isBlank(latestExecutionTime)) {
            return true;
        } else {
            Date latestDate = Tool.parseDatetime(latestExecutionTime);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, period);
            return now.getTime().after(latestDate);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 从通讯数据中获取设备时间
     *
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
     * MD5加密
     *
     * @param password
     * @return
     */
    public static String md5(String password) {
        if (isEmpty(password)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成指定长度的随机字符串（包含大小写字母、数字）
     *
     * @param length 表示生成字符串的长度
     * @return 返回字符串类型结果
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机数字字符串
     *
     * @param length 表示生成字符串的长度
     * @return 返回字符串类型结果
     */
    public static String getRandomInt(int length) {
        String base = "1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
        if (key == "" || key == null) {
            return defaultValue;
        }
        String result = (String) map.get(key);
        if (result == null) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public static int getIntFromMap(Map<String, Object> map, String key) {
        if (key == "" || key == null) {
            return 0;
        }
        if (map.get(key) == null) {
            return 0;
        }
        return Integer.parseInt((String) map.get(key));
    }

    public static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }

    public synchronized static String generateTradeNo() {
        String time = "CD" + timeMillis.format(new Date());
        time += getRandomInt(3);
        return time;
    }

    public static String generateRefundNo(String tradeNo) {
        return tradeNo + "-" + getRandomInt(4);
    }

    /**
     * 获取一个0到max-1之间的随机整数
     *
     * @param max
     * @return
     */
    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    @SuppressWarnings("rawtypes")
    public static Object getObjectFromXML(String xml, Class clazz) {
        //将从API返回的XML数据映射到Java对象
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", clazz);
        xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
        return xStreamForResponseData.fromXML(xml);
    }

    /**
     * 检测序列号是否合法（不存在低于0x30的数据为合法数据）
     *
     * @param serialNum
     * @return
     */
    public static boolean checkSerialNumValid(String serialNum) {
        for (int i = 0; i < serialNum.length(); i++) {
            if (serialNum.charAt(i) < 0x30) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查app上传的坐标参数是否需要计算距离
     * 为空不计算、为零不计算
     *
     * @param lonLat
     * @return
     */
    public static boolean checkLonLat(String lonLat) {
        if (!TextUtils.isBlank(lonLat) && !",".equals(lonLat)) {
            String gps[] = lonLat.split(",");
            Double lon = Double.parseDouble(gps[0]);
            Double lat = Double.parseDouble(gps[1]);

            if (lon > 0 || lat > 0) {
                return true;
            }
        }

        return false;
    }

    public static String convertCardNum(String cardNum) {
        String fixCardNum = "";
        for (int i = 0; i < 16 - cardNum.length(); i++) {
            fixCardNum += "0";
        }

        fixCardNum += cardNum;
        String data = "";
        for (int i = 0; i < 8; i++) {
            data += getStringFromHex(Integer.parseInt(fixCardNum.substring(i * 2, i * 2 + 2), 16));
        }

        return data;
    }

    /**
     * 高位补零
     *
     * @param number
     * @param length
     * @return
     */
    public static String fillZeros(int number, int length) {
        String text = String.valueOf(number);
        while (text.length() < length) {
            text = "0" + text;
        }
        return text;
    }

    /**
     * get version numbers
     *
     * @param version
     * @return
     */
    public static int getVersionNumber(final String version) {
        if (isEmpty(version)) {
            return 0;
        }

        String versionText = version;

        versionText = versionText.replace(".", "");
        try {
            return Integer.parseInt(versionText);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    /**
     * 格式化秒数为“天-时-分”的格式
     *
     * @param totalSeconds
     * @return
     */
    public static String formatSeconds(long totalSeconds) {
        String result = "";
        if (0L == totalSeconds) {
            return "0分钟";
        }

        long hours = totalSeconds / 3600;
        if (0L != hours) {
            result += hours + "小时";
        }

        long minutes = (totalSeconds - hours * 3600) / 60;
        if (0L != minutes) {
            result += minutes + "分钟";
        }
        if (result == "") {
            return "0分钟";
        }

        return result;
    }

    /**
     * 判断是否是正整数
     *
     * @param text
     * @return
     */
    public static boolean isPositiveNumber(String text) {
        String reg = "[1-9]\\d*";
        return text.matches(reg);
    }

    public static Calendar getHCDeviceCalendar(String timeText) {
        Calendar deviceCalendar = Calendar.getInstance();
        deviceCalendar.set(1, Integer.parseInt(new StringBuilder().append(Integer.toHexString(timeText.charAt(0))).append(Integer.toHexString(timeText.charAt(1))).toString()));
        deviceCalendar.set(2, Integer.parseInt(Integer.toHexString(timeText.charAt(2))) - 1);
        deviceCalendar.set(5, Integer.parseInt(Integer.toHexString(timeText.charAt(3))));
        deviceCalendar.set(11, Integer.parseInt(Integer.toHexString(timeText.charAt(4))));
        deviceCalendar.set(12, Integer.parseInt(Integer.toHexString(timeText.charAt(5))));
        deviceCalendar.set(13, Integer.parseInt(Integer.toHexString(timeText.charAt(6))));
        deviceCalendar.set(14, 0);
        return deviceCalendar;
    }

    public static String getCurrentTimeStr10() {
        return getCurrentTimeStr10(new Date());
    }

    public static String getCurrentTimeStr10(Date date) {
        String dateStr = formatNoSymbolDateTime(date);
        StringBuffer dateBuffer = new StringBuffer();
        char[] dateChars = dateStr.toCharArray();
        for (int i = 0; i < dateChars.length; i += 2) {
            dateBuffer.append(getStringFromHex(Integer.valueOf(new StringBuilder().append(String.valueOf(dateChars[i])).append(String.valueOf(dateChars[(i + 1)])).toString(), 16).intValue()));
        }
        return dateBuffer.toString();
    }

    public static String getDateStrFromISO8601(String isoDate) {
        return Tool.formatDateTime(getDateFromISO8601(isoDate));
    }

    public static Date getDateFromISO8601(String isoDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        try {
            return sdf.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getISO8601Timestamp() {
//        Date date = Tool.parseDatetime("2018-10-10 21:12:06");
        return getISO8601Timestamp(new Date());
    }

    public static String getISO8601Timestamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return sdf.format(date);
    }

    /**
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param byteArray
     * @return
     */
    public static String byteToStr(byte[] byteArray) {

        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;

    }

    public static boolean isInteger(String str) {
        if (str == null || str == "") {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据充电时长计算电单车费用
     *
     * @param chargeSeconds
     * @return
     */
    public static int calcElecFeeByTime(int chargeSeconds, int formula) {
        int elecFee = 0;
        if (chargeSeconds <= 180) { // 3分钟内不收费
            elecFee = 0;
        } else {
            int hours = chargeSeconds / 3600;
            if (0L != hours) {
                elecFee = hours * formula;
            }
            int minutes = (chargeSeconds - hours * 3600) / 60;
            if (0L != minutes) {
                elecFee += formula;
            }
        }
        return elecFee;
    }

    public static List<Long> changeListTypeToLong(List<String> stringList) {
        List<Long> resultList = new ArrayList<Long>();
        for (String str : stringList) {
            resultList.add(Long.parseLong(str));
        }

        return resultList;
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param mobile
     * @return
     */
    public static String hiddenMobile(String mobile) {
        if (mobile != null && !mobile.equals("")) {
            return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return mobile;
    }

    /**
     * 检测是否非空
     *
     * eg. null、""、"  "、"null"、"NULL"、"   NULL  "、"NU  LL" 返回false
     * @param content
     * @return
     */
    public static boolean checkNotNullOrNullStr(String content) {
        final String nullValue = "null";

        if (TextUtils.isBlank(content)) {
            return false;
        }

        if (nullValue.equalsIgnoreCase(StringUtils.deleteWhitespace(content))) {
            return false;
        }

        return true;
    }

    /**
     * 检测字符串是否合法（取值范围为数字和字母）
     * @param content
     * @return
     */
    public static boolean check09AZStringValid(String content) {
        // ASCII中的字母区分大小写
        content = content.toUpperCase();

        for(int i = 0; i < content.length(); i++) {
            // 取值范围0-9，A-Z
            if (content.charAt(i) < 0x30 || (content.charAt(i) > 0x39 && content.charAt(i) < 0x41) || content.charAt(i) > 0x5a) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测字符串内容和长度是否合法（取值范围为数字和字母）
     * @param content
     * @param length
     * @return
     */
    public static boolean check09AZStringValidWithLength(String content, int length) {
        if (!checkNotNullOrNullStr(content)) {
            return false;
        }

        if (content.length() != length) {
            return false;
        }

        return check09AZStringValid(content);
    }

    public static String calcDateDiff(Date startDatetime, Date stopDatetime) {
        if (startDatetime == null || stopDatetime == null) {
            return "";
        }

        Interval interval = new Interval(startDatetime.getTime(), stopDatetime.getTime());
        Period datePeriod = interval.toPeriod();

        StringBuilder diffDesc = new StringBuilder();
        if (datePeriod.getYears() > 0) {
            diffDesc.append(datePeriod.getYears()).append("年");
        }
        if (datePeriod.getMonths() > 0) {
            diffDesc.append(datePeriod.getMonths()).append("月");
        }

        if (datePeriod.getDays() > 0) {
            diffDesc.append(datePeriod.getDays()).append("日");
        }

        if (datePeriod.getHours() > 0) {
            diffDesc.append(datePeriod.getHours()).append("小时");
        }
        if (datePeriod.getMinutes() > 0) {
            diffDesc.append(datePeriod.getMinutes()).append("分钟");
        }
        if (datePeriod.getSeconds() > 0) {
            diffDesc.append(datePeriod.getSeconds()).append("秒");
        }
        return diffDesc.toString();
    }
    public static int mysqlObject2Int(Object value){
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf((Object) value));
    }
}