package com.gateway.filter;

import io.netty.handler.codec.http.HttpResponse;

/**
 * @author lw
 */
public interface ResponseFilter {

    /**
     * 对返回进行处理
     * @param response 相应
     */
    void filter(HttpResponse response);
}
