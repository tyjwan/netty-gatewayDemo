package com.demo.gateway.common;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

/**
 * @author lw
 */
public class CreateRequest {

    public static FullHttpRequest create(FullHttpRequest fullHttpRequest) {
        return new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, fullHttpRequest.uri(), Unpooled.EMPTY_BUFFER);
//        HttpVersion.HTTP_1_1, HttpMethod.GET, "http://192.168.101.104:8080/", Unpooled.EMPTY_BUFFER);
    }
}