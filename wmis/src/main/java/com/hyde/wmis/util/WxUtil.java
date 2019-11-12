package com.hyde.wmis.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

public class WxUtil {

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
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
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getJsapiTicket(String accessToken) {
        try {
            return httpJsapiTicket(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpJsapiTicket(String accessToken) {
        Gson gson = new Gson();
        String url2 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        String json2 = httpGet(url2);
        Map<String, Object> jsapiTicket = gson.fromJson(json2, new TypeToken<Map<String, String>>() {
        }.getType());
        return (String) jsapiTicket.get("ticket");
    }

    public static String getAccessToken(String appId, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        String json = httpGet(url);
        Gson gson = new Gson();
        Map<String, Object> accessToken = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        return (String) accessToken.get("access_token");
    }

    public static String httpGet(String url) {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        String result = null;
        try {
            String params = "";
            // 拼接地址
            if (params.length() > 0) {
                url = url + "?" + params;
            }
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "utf-8");
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String post(String url, String body, String mimeType, String charset) throws IOException {
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        // 设置无缓存
        post.setHeader("Pragma", "no-cache");
        post.setHeader("cache-control", "no-cache");
        String result = "";
        try {
            HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
            post.setEntity(entity);
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            customReqConf.setConnectTimeout(5000);
            customReqConf.setSocketTimeout(120000);
            post.setConfig(customReqConf.build());
            HttpResponse res;
            // 执行 Http 请求.
            client = HttpClientBuilder.create().build();
            res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = res.getEntity();
                result = EntityUtils.toString(httpEntity, "utf-8");// 取出应答字符串
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }

    /**
     * 获取用户基本信息
     *
     */
    public static Map<String, String> getUser(String token, String openId) {

        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
        String respJson = httpGet(url);
        Gson gson = new Gson();
        if (StringUtils.isNotBlank(respJson)) {
            if (!respJson.contains("errcode")) {
                respJson = respJson.replace("[", "\"[");
                respJson = respJson.replace("]", "]\"");
                return gson.fromJson(respJson, new TypeToken<Map<String, String>>() {
                }.getType());
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 网页授权，根据code获取用户信息
     */
    public static Map<String, String> getRightUserBycode(String code) {
        Gson gson = new Gson();
        // 网页授权access_token(特殊)
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + CacheUtil.getSysConfig("app_id") + "&secret=" + CacheUtil.getSysConfig("app_secret") + "&code=" + code + "&grant_type=authorization_code";
        String respJson1 = httpGet(url);
        Map<String, String> tokenMap = gson.fromJson(respJson1, new TypeToken<Map<String, String>>() {
        }.getType());
        // 获取用户信息
        Map<String, String> userMap = getWebUser(tokenMap.get("access_token"), tokenMap.get("openid"));
        userMap.put("refresh_token", tokenMap.get("refresh_token"));
        return userMap;
    }

    /**
     * 网页授权，根据code获取用户信息
     */
    public static String getOpenIdBycode(String code) {
        Gson gson = new Gson();
        // 网页授权access_token(特殊)
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + CacheUtil.getSysConfig("app_id") + "&secret=" + CacheUtil.getSysConfig("app_secret") + "&code=" + code + "&grant_type=authorization_code";
        String respJson1 = httpGet(url);
        Map<String, String> tokenMap = gson.fromJson(respJson1, new TypeToken<Map<String, String>>() {
        }.getType());
        System.out.println(tokenMap);
        return tokenMap.get("openid");
    }

    /**
     * 网页授权，根据refresh_token获取用户信息
     */
    public static Map<String, String> getRightUserByrefresh_token(String refresh_token) {
        // 刷新access_token
        Map<String, String> tokenMap = refreshWebAccessToken(refresh_token);
        // 获取用户信息
        Map<String, String> userMap = getWebUser(tokenMap.get("access_token"), tokenMap.get("openid"));
        userMap.put("refresh_token", tokenMap.get("refresh_token"));
        return userMap;
    }

    /**
     * 刷新网页授权access_token
     */
    public static Map<String, String> refreshWebAccessToken(String refresh_token) {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + CacheUtil.getSysConfig("app_id") + "&grant_type=refresh_token&refresh_token=" + refresh_token + "";
        String respJson = httpGet(url);
        Gson gson = new Gson();
        return gson.fromJson(respJson, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * openId拉取用户信息
     */
    public static Map<String, String> getWebUser(String access_token, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId + "&lang=zh_CN";
        String respJson = httpGet(url);
        respJson = respJson.replace(",\"privilege\":[]", "");
        Gson gson = new Gson();
        return gson.fromJson(respJson, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 创建签名(自动排序)
     *
     * @param map
     * @param key
     * @return
     */
    public static String createSign(HashMap<String, String> map, String key) {
        SortedMap<String, String> packageParams = new TreeMap<String, String>(map);
        StringBuffer sb = new StringBuffer();
        Iterator it = packageParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        System.out.println("md5 sb:" + sb);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();

        return sign;
    }

}
