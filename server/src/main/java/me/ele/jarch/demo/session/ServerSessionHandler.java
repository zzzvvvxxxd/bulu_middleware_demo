package me.ele.jarch.demo.session;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by bulu on 16/3/15.
 */
public class ServerSessionHandler extends SimpleChannelInboundHandler<String>{

    private ServerSession serverSession;

    public ServerSessionHandler(ServerSession serverSession) {
        this.serverSession = serverSession;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.serverSession.setServerChannel(ctx.channel());

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    }
}
