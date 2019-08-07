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

    private static final SimpleDateFormat minuteFormat = new SimpleDateFormat("mm��");

    private static final SimpleDateFormat timeMillis = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static final Random random = new Random();
    
    public static boolean isWjDevice(String chargerSerialNum) {
        return chargerSerialNum != null && chargerSerialNum.startsWith("W") && chargerSerialNum.length() == 14;
    }
    /**
           * ��ʽ����������
     *
     * @return
     */
    public synchronized static String formatTodayDate() {
        return dateFormat.format(new Date()).replaceAll("-", "");
    }
    /**
           * ����һ��uuid
     *
     * @return
     */
    public static String createUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**
     * �õ���Դ�ļ��ĸ�·��
     * @return
     */
    public static String getResourceRoot() { 
    	return System.getProperty("user.home") + "/"; 
    }
    /**
     * ���ͼƬ�ĺ�׺��
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
     * ����yyyy-MM-dd��ʽ��yyyy_MM_dd��ʽ�������ִ�
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
     * ����yyyy-MM-dd HH:mm:ss��ʽ������ʱ���ִ�
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
     * �����޷��ŵ�yyyyMMddHHmmss��ʽ������ʱ���ִ�
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
     * ��Date���ڸ�ʽ����yyyy-MM��ʽ
     *
     * @param date
     * @return
     */
    public synchronized static String formatMonthDate(Date date) {
        return monthDateFormat.format(date);
    }
    /**
     * ��Date���ڸ�ʽ����yyyy-MM-dd��ʽ
     *
     * @param date
     * @return
     */
    public synchronized static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * ��Date���ڸ�ʽ����yyyy-MM-dd HH:mm:ss��ʽ
     *
     * @param date
     * @return
     */
    public synchronized static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * ��Date���ڸ�ʽ����yyyyMMddHHmmss��ʽ
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
     * ��Dateʱ���ʽ����HH:mm:ss��ʽ
     *
     * @param date
     * @return
     */
    public synchronized static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    /**
     * ��Dateʱ���ʽ����mm:ss��ʽ
     *
     * @param date
     * @return
     */
    public synchronized static String formatMinute(Date date) {
        return minuteFormat.format(date);
    }

    /**
     * ����ַ����Ƿ�Ϊ��
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
     * ��ʽ��float����,����һλС��
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
     * ��ǰ�߳�����
     *
     * @param seconds ��
     */
    public static void delay(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��Booleanֵת����js�����ж�
     *
     * @param result
     * @return
     */
    public static String toJsBoolean(boolean result) {
        return result ? "true" : "";
    }

    /**
     * ������Ȩ�޴���ʧ�ܵ�Json����ֵ
     *
     * @return
     */
    public static String getJsonNoPermissionFalseReturn() {
        return getJsonMsgReturn(false, "noPermission");
    }

    /**
     * ����easyui��datagrid�齨������Json����ֵ
     *
     * @return
     */
    public static String getJsonNoDataGridDataReturn() {
        return "{\"total\":0,\"rows\":[]}";
    }

    /**
     * ��ȡһ��successΪtrue��JSONObject����
     *
     * @return
     */
    public static JSONObject getAppSuccessReturnJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        return obj;
    }

    /**
     * ��ȡһ��successΪtrue��JSONObject����
     *
     * @return
     */
    public static JSONObject getAppFailureReturnJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        return obj;
    }

    /**
     * ��ȡһ��Json�ظ�����msg��
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
     * ���������еĴ��ںŸ�С�ںš�˫����
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
     * ��int����ת���ɶ�ӦASCII�ַ�
     *
     * @param data
     * @return
     */
    public static String getStringFromHex(long data) {
        return new Character((char) data).toString();
    }

    /**
     * ת���ַ���Ϊ�����Ķ���16��������
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
     * ȥ������ǰ���0
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
     * ��ȡ����λ
     *
     * @param data
     * @return 16�����ַ���
     */
    public static String getHeight4(int data) {
        int height = ((data & 0xf0) >> 4);
        return Integer.toHexString(height);
    }

    /**
     * ��ȡ����λ
     *
     * @param data
     * @return 16�����ַ���
     */
    public static String getLow4(int data) {
        int low = (data & 0x0f);
        return Integer.toHexString(low);
    }

    /**
     * ����6λ���������֤��
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
     * ����Token��6��֮����ڣ����ڱ��ش���token�Ŀͻ��ˣ�����ʹ�ù�����Token��ʱ��
     *
     * @param tokenCreateTime
     * @return
     */
    public static synchronized boolean isTokenForSaveTimeout(Date tokenCreateTime) {
        Calendar now = Calendar.getInstance();
        // �û�token��Ч�ڸ�Ϊһ��
        now.add(Calendar.DATE, -6);
        return now.getTime().after(tokenCreateTime);
    }

    /**
     * ����Token��7��֮�����
     *
     * @param tokenCreateTime
     * @return
     */
    public static synchronized boolean isTokenTimeout(Date tokenCreateTime) {
        Calendar now = Calendar.getInstance();
        // �û�token��Ч�ڸ�Ϊһ��
        now.add(Calendar.DATE, -7);
        return now.getTime().after(tokenCreateTime);
    }

    /**
     * ��ȡtoken����ʱ���
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
     * ��ȡtokenʣ�����ʱ��(��λΪ��)
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
     * У��1�������Ƿ�ִ�л�ʱ����ɨ��������
     *
     * @param latestExecutionTime �ϴ�ɨ��ʱ��
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
     * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
     *
     * @param dir ��Ҫɾ�����ļ�Ŀ¼
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //�ݹ�ɾ��Ŀ¼�е���Ŀ¼��
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Ŀ¼��ʱΪ�գ�����ɾ��
        return dir.delete();
    }

    /**
     * ��ͨѶ�����л�ȡ�豸ʱ��
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
     * MD5����
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
     * ����ָ�����ȵ�����ַ�����������Сд��ĸ�����֣�
     *
     * @param length ��ʾ�����ַ����ĳ���
     * @return �����ַ������ͽ��
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
     * ����ָ�����ȵ���������ַ���
     *
     * @param length ��ʾ�����ַ����ĳ���
     * @return �����ַ������ͽ��
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
     * ��ȡһ��0��max-1֮����������
     *
     * @param max
     * @return
     */
    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    @SuppressWarnings("rawtypes")
    public static Object getObjectFromXML(String xml, Class clazz) {
        //����API���ص�XML����ӳ�䵽Java����
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", clazz);
        xStreamForResponseData.ignoreUnknownElements();//��ʱ���Ե�һЩ�������ֶ�
        return xStreamForResponseData.fromXML(xml);
    }

    /**
     * ������к��Ƿ�Ϸ��������ڵ���0x30������Ϊ�Ϸ����ݣ�
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
     * ���app�ϴ�����������Ƿ���Ҫ�������
     * Ϊ�ղ����㡢Ϊ�㲻����
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
     * ��λ����
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
     * ��ʽ������Ϊ����-ʱ-�֡��ĸ�ʽ
     *
     * @param totalSeconds
     * @return
     */
    public static String formatSeconds(long totalSeconds) {
        String result = "";
        if (0L == totalSeconds) {
            return "0����";
        }

        long hours = totalSeconds / 3600;
        if (0L != hours) {
            result += hours + "Сʱ";
        }

        long minutes = (totalSeconds - hours * 3600) / 60;
        if (0L != minutes) {
            result += minutes + "����";
        }
        if (result == "") {
            return "0����";
        }

        return result;
    }

    /**
     * �ж��Ƿ���������
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
     * ���ֽ�ת��Ϊʮ�������ַ���
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
     * ���ֽ�����ת��Ϊʮ�������ַ���
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
     * ���ݳ��ʱ������絥������
     *
     * @param chargeSeconds
     * @return
     */
    public static int calcElecFeeByTime(int chargeSeconds, int formula) {
        int elecFee = 0;
        if (chargeSeconds <= 180) { // 3�����ڲ��շ�
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
     * �����ֻ������м���λ
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
     * ����Ƿ�ǿ�
     *
     * eg. null��""��"  "��"null"��"NULL"��"   NULL  "��"NU  LL" ����false
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
     * ����ַ����Ƿ�Ϸ���ȡֵ��ΧΪ���ֺ���ĸ��
     * @param content
     * @return
     */
    public static boolean check09AZStringValid(String content) {
        // ASCII�е���ĸ���ִ�Сд
        content = content.toUpperCase();

        for(int i = 0; i < content.length(); i++) {
            // ȡֵ��Χ0-9��A-Z
            if (content.charAt(i) < 0x30 || (content.charAt(i) > 0x39 && content.charAt(i) < 0x41) || content.charAt(i) > 0x5a) {
                return false;
            }
        }
        return true;
    }

    /**
     * ����ַ������ݺͳ����Ƿ�Ϸ���ȡֵ��ΧΪ���ֺ���ĸ��
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
            diffDesc.append(datePeriod.getYears()).append("��");
        }
        if (datePeriod.getMonths() > 0) {
            diffDesc.append(datePeriod.getMonths()).append("��");
        }

        if (datePeriod.getDays() > 0) {
            diffDesc.append(datePeriod.getDays()).append("��");
        }

        if (datePeriod.getHours() > 0) {
            diffDesc.append(datePeriod.getHours()).append("Сʱ");
        }
        if (datePeriod.getMinutes() > 0) {
            diffDesc.append(datePeriod.getMinutes()).append("����");
        }
        if (datePeriod.getSeconds() > 0) {
            diffDesc.append(datePeriod.getSeconds()).append("��");
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