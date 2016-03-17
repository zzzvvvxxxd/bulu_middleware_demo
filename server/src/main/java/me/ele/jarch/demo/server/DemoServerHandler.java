package me.ele.jarch.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

/**
 * Created by bulu on 16/3/14.
 */
@ChannelHandler.Sharable
public class DemoServerHandler extends SimpleChannelInboundHandler<String> {

    Logger logger = Logger.getLogger(DemoServerHandler.class);
    private DemoServer server;

    public DemoServerHandler(DemoServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {
            super.channelActive(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //服务器读取信息
        logger.info("receive message: [" + msg + "] from remote address " + ctx.channel().remoteAddress());
        ctx.writeAndFlush("Received");
        SqlContext sqlContext = new SqlContext(this.server.getScheduler(), msg);
        this.server.enqueue(sqlContext);
        for(;;) {
            if(sqlContext.getResult() != null) {
                // end time
                sqlContext.setEnd(System.currentTimeMillis());
                // 获取到了返回值,写回到客户端
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(sqlContext.getResult().getBytes()));
                return;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //读取完毕
        logger.info("Read Complete");
    }
}
