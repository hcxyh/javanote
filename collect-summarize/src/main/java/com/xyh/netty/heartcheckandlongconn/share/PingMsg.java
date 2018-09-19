package com.xyh.netty.heartcheckandlongconn.share;

public class PingMsg extends BaseMsg {
    public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
