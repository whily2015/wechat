package com.hyde.wmis.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping(value="/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/wx", method = RequestMethod.GET)
	public void wxs(HttpServletResponse response, HttpServletRequest request) {
		String ret = "";
		try {
		} catch (Exception e) {
			logger.error("Error:verify signature on configurate dev mode", e);
		}finally{
		}
	}

}
