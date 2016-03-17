package me.ele.jarch.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by bulu on 16/3/14.
 */
public class DemoClient {
    private static Logger logger = Logger.getLogger(DemoClient.class);
    private Channel channel;
    private String sql = "sql";

    private  String host;
    private int port;

    public void start(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        if(args != null && args.length == 2) {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                logger.info("Try to connect to remote host: " + host + " on port: " + port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ChannelFuture f = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new DemoClientInitializer());
            f = b.connect(host, port).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.channel().writeAndFlush(Unpooled.wrappedBuffer("sql".getBytes()));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert f != null;
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            group.shutdownGracefully();
        }
    }

    private class DemoClientInitializer extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast("decoder", new StringDecoder());
            ch.pipeline().addLast("encoder", new StringEncoder());
            ch.pipeline().addLast("handler", new DemoClientHandler(DemoClient.this));
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("/Users/bulu/log4j.properties");
        new DemoClient().start(args);
    }
}
