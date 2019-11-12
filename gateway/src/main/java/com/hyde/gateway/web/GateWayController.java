package com.hyde.gateway.web;

import com.hyde.gateway.util.WeChatResplyUtil;
import com.hyde.gateway.util.WeixinReqSignatures;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(value="/gateway")
public class GateWayController {

	private final Logger logger = LoggerFactory.getLogger(GateWayController.class);

	@RequestMapping(value = "/wx", method = RequestMethod.GET)
	public void wxs(String signature, String nonce, String timestamp, String echostr, HttpServletResponse response, HttpServletRequest request) throws Exception {
		String ret = "";
		try {
			logger.info("signature:" + signature + ",nonce:" + nonce + ",timestamp:" + timestamp + ",echostr:" + echostr);
			if (WeixinReqSignatures.verify("123456", timestamp, nonce, signature)) {
				logger.debug("微信请求参数="+echostr);
				ret = echostr;
			}
		} catch (Exception ex) {
			logger.error("Error:verify signature on configurate dev mode", ex);
		}finally{
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out =  response.getWriter();
			out.print(ret);
			out.flush();
			out.close();
		}
	}

	@RequestMapping(value = "/wx", method = RequestMethod.POST)
	public void wx(HttpServletRequest request, HttpServletResponse response){
		String retXml="";
		try {
			//获取请求参数
			Map<String, String> map = WeChatResplyUtil.parseXmlToMap(request.getInputStream());
			logger.info("params: " + map.toString());
			String msgType = map.get("MsgType");
			String userName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			if(WeChatResplyUtil.MESSAGE_TEXT.equals(msgType)){
				//文字处理
				String content = map.get("Content");
				retXml = retMsg(content,userName,fromUserName,map);
				if(StringUtils.isBlank(retXml)){
					retXml = retMsg(userName + "默认回复消息", userName, fromUserName,map);
				}
				if("success".equals(retXml)){
					retXml="";
				}
				return;
			}
			if(WeChatResplyUtil.MESSAGE_IMAGE.equals(msgType)||WeChatResplyUtil.MESSAGE_VOICE.equals(msgType)||WeChatResplyUtil.MESSAGE_VIDEO.equals(msgType)||WeChatResplyUtil.MESSAGE_SHORTVIDEO.equals(msgType)||WeChatResplyUtil.MESSAGE_LOCATION.equals(msgType)||WeChatResplyUtil.MESSAGE_LINK.equals(msgType)){
				//图片处理,语音处理,视频或者小视频处理,地理位置处理,链接处理
				String content = "";
				retXml = retMsg(content,userName,fromUserName,map);
				if(StringUtils.isBlank(retXml)){
					retXml = retMsg(userName + "默认回复消息", userName, fromUserName,map);
				}
				return;
			}
			if(WeChatResplyUtil.MESSAGE_EVENT.equals(msgType)){

				return;
			}
			retXml = retMsg(userName + "默认回复消息", userName, fromUserName,map);
			logger.info("返回用户信息xml串为【" + retXml + "】，用户为【" + fromUserName + "】");
		} catch (Exception e) {
			logger.error("Error:return message error:", e);
		}finally{
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(retXml);
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error("Error:IO error:", e);
			}
		}
	}


	/**
	 * @description 获取后台响应数据
	 * @param content
	 * @param userName
	 * @param fromUserName
	 * @return
	 * @throws Exception
	 */
	private String retMsg(String content, String userName, String fromUserName,Map<String, String> map) throws Exception{
		String retXml ="success";
		String openid = map.get("FromUserName");
		String Ticket = map.get("Ticket");
		String Event = map.get("Event");
		return retXml;
	}

}
