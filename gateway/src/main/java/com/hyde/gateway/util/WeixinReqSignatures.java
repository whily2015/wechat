package com.hyde.gateway.util;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;



/**
 * 
 * @author Diablo Wu 
 * 
 */
public class WeixinReqSignatures {
    public static final String REQ_KEY_TIMESTAMP = "timestamp";
    public static final String REQ_KEY_NONCE = "nonce";
    public static final String REQ_KEY_SIGN = "signature";
    
    
    /**
     *证
     * @param token
     * @param req
     * @return
     */
    public static Boolean verify(String token, final HttpServletRequest req){
        String ts = req.getParameter(REQ_KEY_TIMESTAMP);
        String nonce = req.getParameter(REQ_KEY_NONCE);
        String sign = req.getParameter(REQ_KEY_SIGN);
        if(StringUtils.isNotEmpty(ts) &&
                StringUtils.isNotEmpty(nonce)&&
                StringUtils.isNotEmpty(sign)){
            return verify( token, ts, nonce, sign);
        }else{
            return false;
        }
    }
    
   
    /**
     * 
     * @param token
     * @param ts
     * @param nonce
     * @param sign
     * @return
     */
    public static Boolean verify(String token,String ts,String nonce,String sign){
        if(StringUtils.isEmpty(sign) ||
                StringUtils.isEmpty(ts) ||
                StringUtils.isEmpty(nonce) ||
                StringUtils.isEmpty(token)){
            return false;
        }
        String[] sa = new String[]{token.trim(),ts.trim(),nonce.trim()};
        Arrays.sort(sa);
        
        String ss = StringUtils.join(sa,"");
        
        String si = StringUtil.getStrSHA1(ss);
        return si.equals(sign);
    }

}

