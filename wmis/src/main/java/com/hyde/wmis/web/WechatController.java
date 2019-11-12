/**
 *
 */
package com.hyde.wmis.web;

import com.hyde.wmis.util.WxUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/wechat")
public class WechatController {

	private static final String TAGS_GET_URL = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=";
	private static final String BATCHTAGGING_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";

	@RequestMapping(value = "/batchtagging", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String batchtagging(HttpServletRequest request, HttpServletResponse response) {
		String respJson = "";
		try {
			String accessToken = request.getParameter("access_token");
			String tagid = request.getParameter("tagid");
			String openidList = request.getParameter("openid_list");
			String[] openids = openidList.split("\\n");
			if (StringUtils.isBlank(accessToken)) {
			}
			int max = 50;//每次传入的openid列表个数不能超过50个
			int num = 0;
			StringBuffer strb = new StringBuffer();
			String res = null;
			for (String openid : openids) {
				strb.append("\"").append(openid).append("\"").append(",");
				num++;
				if (num%max == 0 || openids.length==num) {
					String reqJson = "{\"openid_list\" : [" +
							strb.substring(0, strb.length()-1)
							+ "], " +
							"\"tagid\":" + tagid +
							" } ";
					res = WxUtil.post(BATCHTAGGING_URL + accessToken, reqJson , "html/text", "UTF-8");
					strb = new StringBuffer();
					respJson += "{\"reqJson\":" + reqJson + ", \"respJson\":" + res + "},";
				}
			}
			respJson = "[" + respJson.substring(0, respJson.length()-1) + "]";
			System.out.println(respJson);
			return respJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		respJson = "{\"errcode\":-1, \"errmsg\":\"系统繁忙\"} ";
		return respJson;

	}
	@RequestMapping(value = "/tags/get", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String tagsGet(HttpServletRequest request, HttpServletResponse response) {
		String respJson = "{\"errcode\":-1, \"errmsg\":\"系统繁忙\"} ";
		try {
			String accessToken = request.getParameter("access_token");
			if (StringUtils.isBlank(accessToken)) {
			}
			respJson = WxUtil.httpGet(TAGS_GET_URL + accessToken);
			return respJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respJson;
	}
}
