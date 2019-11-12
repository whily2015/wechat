package com.hyde.gateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatResplyUtil {

	private final static Logger logger = LoggerFactory.getLogger(WeChatResplyUtil.class);

	// 文本类型信息
	public static final String MESSAGE_TEXT = "text";
	// 图片类型信息
	public static final String MESSAGE_IMAGE = "image";
	// 语音信息
	public static final String MESSAGE_VOICE = "voice";
	//视频信息
	public static final String MESSAGE_VIDEO = "video";
	//小视频信息
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";
	// 链接类型信息
	public static final String MESSAGE_LINK = "link";
	// 事件类型信息
	public static final String MESSAGE_EVENT = "event";
	//语音类型开始信息
	public static final String MESSAGE_BEGIN = "begin";
	//语音类型结束信息
	public static final String MESSAGE_END = "end";
	// 位置类型信息
	public static final String MESSAGE_LOCATION = "location";
	// 事件类型信息(click)
	public static final String MESSAGE_CLICK = "CLICK";
	// 事件类型信息(subscribe关注)
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	// 事件类型信息(unsubscribe取消关注)
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	// 扫描二维码非关注事件
	public static final String MESSAGE_SCAN = "SCAN";

	// 事件为模板消息发送结束
	public static final String MESSAGE_TEMPLATESEND = "TEMPLATESENDJOBFINISH";

	// 微信文本消息xml
	public static final String textTpl = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Content><![CDATA[%5$s]]></Content></xml>";
	// 微信文本消息xml
	public static final String textXMLTpl = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Content>SUCCESS</Content></xml>";
	// 微信图文消息xml
	public static final String newsTpl = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><ArticleCount>%5$s</ArticleCount><Articles>%6$s</Articles></xml>";

	// 微信图文消息xml
	public static final String itemTpl = "<item><Title><![CDATA[%1$s]]></Title><Description><![CDATA[%2$s]]></Description><PicUrl><![CDATA[%3$s]]></PicUrl><Url><![CDATA[%4$s]]></Url></item>";

	// 微信消息转发到多客服xml
	public static final String transfer_customer_service = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType></xml>";

	/**
	 * @Title: parseXmlToMap
	 * @Description:将流转化为map
	 * @param is
	 *            请求输入流
	 * @return Map
	 * @author LiuXuhui
	 * @throws
	 */
	public static Map<String, String> parseXmlToMap(InputStream is) {
		Map<String, String> xmlMap = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(is);
			Element rootElement = document.getRootElement();
			List<Element> elementList = rootElement.elements();
			for (Element el : elementList) {
				xmlMap.put(el.getName(), el.getText());
			}
		} catch (DocumentException e) {
			logger.error("==================解析流转换为map对象时出错！", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("==================request输入流关闭出错！", e);
				}
			}
		}
		return xmlMap;
	}

	public static String parseXml(String fromUserName, String toUserName, String reply) {
		return String.format(WeChatResplyUtil.textTpl, new Object[] { fromUserName, toUserName, System.currentTimeMillis(), "text", reply });
	}

}