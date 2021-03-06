package com.xyh.netty.heartcheckandlongconn.server;

import com.xyh.netty.heartcheckandlongconn.share.AskMsg;
import com.xyh.netty.heartcheckandlongconn.share.BaseMsg;
import com.xyh.netty.heartcheckandlongconn.share.LoginMsg;
import com.xyh.netty.heartcheckandlongconn.share.MsgType;
import com.xyh.netty.heartcheckandlongconn.share.PingMsg;
import com.xyh.netty.heartcheckandlongconn.share.ReplyClientBody;
import com.xyh.netty.heartcheckandlongconn.share.ReplyMsg;
import com.xyh.netty.heartcheckandlongconn.share.ReplyServerBody;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelMap.remove((SocketChannel)ctx.channel());
    }

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
		
		 if(MsgType.LOGIN.equals(baseMsg.getType())){
	            LoginMsg loginMsg=(LoginMsg)baseMsg;
	            if("xyh".equals(loginMsg.getUserName())&&"nana".equals(loginMsg.getPassword())){
	                //登录成功,把channel存到服务端的map中
	                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)channelHandlerContext.channel());
	                System.out.println("client"+loginMsg.getClientId()+" 登录成功");
	            }
	        }else{
	        	//通过clientId来获取用户连接
	            if(NettyChannelMap.get(baseMsg.getClientId())==null){
	                    //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
	                    LoginMsg loginMsg=new LoginMsg();
	                    channelHandlerContext.channel().writeAndFlush(loginMsg);
	            }
	        }
	        switch (baseMsg.getType()){
	            case PING:{
	                PingMsg pingMsg=(PingMsg)baseMsg;
	                PingMsg replyPing=new PingMsg();
	                NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
	            }break;
	            case ASK:{
	                //收到客户端的请求
	                AskMsg askMsg=(AskMsg)baseMsg;
	                if("authToken".equals(askMsg.getParams().getAuth())){
	                    ReplyServerBody replyBody=new ReplyServerBody("server info $$$$ !!!");
	                    ReplyMsg replyMsg=new ReplyMsg();
	                    replyMsg.setBody(replyBody);
	                    NettyChannelMap.get(askMsg.getClientId()).writeAndFlush(replyMsg);
	                }
	            }break;
	            case REPLY:{
	                //收到客户端回复
	                ReplyMsg replyMsg=(ReplyMsg)baseMsg;
	                ReplyClientBody clientBody=(ReplyClientBody)replyMsg.getBody();
	                System.out.println("receive client msg: "+clientBody.getClientInfo());
	            }break;
	            default:break;
	        }
	        ReferenceCountUtil.release(baseMsg);
	}
}
