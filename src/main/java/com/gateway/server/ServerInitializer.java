package com.gateway.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author lw
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));

        pipeline.addLast("my handler", new ServerHandler());

//        pipeline.addLast(new HttpServerCodec());
//        pipeline.addLast(new HttpServerExpectContinueHandler());
//        pipeline.addLast(new HttpResponseEncoder());
//        pipeline.addLast(new HttpRequestDecoder());
//        pipeline.addLast(new HttpObjectAggregator(1024*1024*64));
//        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
//        pipeline.addLast(new ServerHandler());
    }
}
