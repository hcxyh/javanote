package com.xyh.netty.heartcheckandlongconn.share;

import java.io.Serializable;

/**
 * 
 * @author xyh
 *
 */
public class AskParams implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private String auth;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}