package com.xyh.netty.action.test.longlive;

import java.io.Serializable;

public class RequestInfoVO implements Serializable {

    /** * */ private static final long serialVersionUID = 2145740808678744013L;
    /** * 消息头 */ private Integer header;
    /** * 类型 */ private Byte type;
    /** * 请求标识 */ private Integer sequence;
    /** * 消息体 */ private String body;


    @Override
    public String toString() {
        return "RequestInfoVO{" +
                "header=" + header +
                ", type=" + type +
                ", sequence=" + sequence +
                ", body='" + body + '\'' +
                '}';
    }

    public Integer getHeader() {
        return header;
    }

    public void setHeader(Integer header) {
        this.header = header;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}