package com.gateway.client;

import com.gateway.filter.Filter;
import com.gateway.route.RouteTable;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static com.gateway.common.Constant.THIRD_CLIENT_ASYNC;

/**
 * 使用时请进行初始化操作
 * 客户端中心
 * 起一个中介中用，获取后台服务器结果，调用server outbound返回结果
 * @author lw
 */
public class ClientCenter {

    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private ClientCenter instance;

        EnumSingleton(){
            instance = new ClientCenter();
        }
        public ClientCenter getSingleton(){
            return instance;
        }
    }

    public static ClientCenter getInstance(){
        return EnumSingleton.INSTANCE.getSingleton();
    }

    private Client client;

    public void init(String clientType, EventLoopGroup clientGroup) {
        if (THIRD_CLIENT_ASYNC.equals(clientType)) {
            client = new ThirdClientAsync();
        } else {
            client = new CustomClientAsync(clientGroup);
        }
        System.out.println("Select client type: " + clientType);
    }

    public void execute(FullHttpRequest request, Channel serverOutbound) {
        // 路由转发处理,负载均衡
        String source = request.uri();
        String target = RouteTable.getTargetUrl(source);

        URI uri = null;
        try {
            uri = new URI(target);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        String address = uri.getHost();
        int port = uri.getPort();
        request.setUri(uri.getPath());

        // 请求过滤处理
        Filter.requestProcess(request);

        FullHttpResponse response = client.execute(request, address, port, serverOutbound);
        if (response == null) {
            System.out.println("backend server return null");
        }

        // 相应过滤处理
        Filter.responseProcess(response);

        // 返回Response数据给用户
        try {
            serverOutbound.writeAndFlush(response).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}