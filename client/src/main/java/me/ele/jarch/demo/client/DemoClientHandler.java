package me.ele.jarch.demo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;


/**
 * Created by bulu on 16/3/14.
 */
public class DemoClientHandler extends SimpleChannelInboundHandler<String> {
    private static Logger logger = Logger.getLogger(DemoClientHandler.class);
    DemoClient client;

    public DemoClientHandler(DemoClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //客户端read
        logger.info(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channel Active to " + ctx.channel().remoteAddress());
        //ctx.writeAndFlush(ctx.channel().localAddress() + " is saying hello");
        this.client.setChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel inActive to " + ctx.channel().remoteAddress());
    }
}
