package me.ele.jarch.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.PropertyConfigurator;

import java.net.InetSocketAddress;

/**
 * Created by bulu on 16/3/14.
 */
public class DemoServer {
    private Scheduler scheduler;

    private DemoServerHandler handler = new DemoServerHandler(DemoServer.this);

    public DemoServer() {
        scheduler = new Scheduler(2);
    }

    public void enqueue(SqlContext ctx) {
        this.scheduler.enqueue(ctx);
    }

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new DemoServerInitializer());
            ChannelFuture f = b.bind().sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully().sync();
            //workerGroup.shutdownGracefully().sync();
        }
    }

    public void start(String[] args) throws InterruptedException {
        int port = 9981;
        try {
            if(args != null && args.length > 0)
                port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bind(port);

        scheduler.start();
    }

    private class DemoServerInitializer extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel arg0) {
            arg0.pipeline().addLast("decoder", new StringDecoder());
            arg0.pipeline().addLast("decode", new StringEncoder());
            arg0.pipeline().addLast("handler", handler);
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public static void main(String[] args) throws InterruptedException {
        PropertyConfigurator.configure("/Users/bulu/log4j.properties");
        new DemoServer().start(args);
    }
}
