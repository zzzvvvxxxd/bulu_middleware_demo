package me.ele.jarch.demo.session;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;

/**
 * Created by bulu on 16/3/14.
 */
public class ServerSession {
    private static Logger logger = Logger.getLogger(ServerSession.class);
    private volatile Channel serverChannel;

    private final int nettyThreadCount = 2;
    private ServerSessionHandler handler = new ServerSessionHandler(ServerSession.this);

    public void start(String host, int port) {
        connectToServer(host, port);
    }

    public void connectToServer(String host, int port) {
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup(nettyThreadCount)).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(handler);
                    }
                });
        b.connect(host, port).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()) {
                    doQuit();
                }
            }
        });
    }

    public ChannelFuture writeAndFlush(ByteBuf buf) {
        return this.getServerChannel().writeAndFlush(buf).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("write sql to db!");
            }
        });
    }

    private void doQuit() {
        if(this.serverChannel != null) {
            try {
                this.serverChannel.close().sync();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getServerChannel() {
        return this.serverChannel;
    }

    public static void main(String[] args) {

    }
}
