package com.hyde.gateway.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    /**
     * 过滤xss代码
     * @param input
     * @return
     */
    public static String antixss(String input) {
        StringBuilder sb = new StringBuilder();
        char[] arr = input.toCharArray();
        for (int i = 0, l = arr.length; i < l; i++) {
            switch (arr[i]) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#x27;");
                    break;
                case '/':
                    sb.append("&#x2F;");
                    break;
                default:
                    sb.append(arr[i]);
                    break;
            }
        }
        return sb.toString();
    }

    public static String filterHTMLTag(String str) {
        String result ="";
        try {
            //第一步先进行style标签的过滤
            //第二步 将所有的空格去掉
            str = str.replaceAll("\\s*", "");
            //第三步 判断结尾
            if(str.endsWith("</p>")){
                str = str.substring(0, str.length()-4);
            }
            if(str.endsWith("<br/>")){
                str = str.substring(0, str.length()-5);
            }
            //第四步 替换字符串
            str = str.replaceAll("<p[^>]*?>", "");
            str = str.replaceAll("</p>", "\n");
            str = str.replaceAll("&nbsp;", " ");
            str = str.replaceAll("<span[^>]*?>", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("target=\"_blank\"", "");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("<br/>", "\n");
            str = str.replaceAll("<h[^>]*?>", "");
            str = str.replaceAll("</h[^>]*?>", "");
            //将a标签还原
            str= str.replaceAll("<ahref", "<a href");
            //任务号1530 moshifei start
            str= str.replaceAll("href=\"", "href=\'");
            str= str.replaceAll("target=\"_blank\"", "");
            str= str.replaceAll("\">", "\'>");
            //任务号1530 moshifei end
            result = str;
        }catch(Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return result;//返回文本字符串
    }

    public static String getStrSHA1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest [] = digest.digest();
            StringBuffer sb = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    sb.append(0);
                }
                sb.append(shaHex);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = getNonceStr();
        String timestamp = getTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String getNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String getTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString().toUpperCase();
    }
}
