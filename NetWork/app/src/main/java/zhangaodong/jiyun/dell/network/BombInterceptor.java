package zhangaodong.jiyun.dell.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自定义拦截器，统一添加头信息
 * Created by llr on 2017/4/11.
 */

public class BombInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        
        //拦截的请求消息
        Request request = chain.request();
        //请求消息的构造器
        Request.Builder builder = request.newBuilder();

        //请求头
        //表明你正在访问的是哪个App程序
        builder.addHeader("X-Bmob-Application-Id","623aaef127882aed89b9faa348451da3");
        //用来授权
        builder.addHeader("X-Bmob-REST-API-Key","c00104962a9b67916e8cbcb9157255de");
        //表明内容是Json
        builder.addHeader("Content-Type","application/json");

        //构建添加完请求头的请求消息
        request=builder.build();

        //执行请求拿到响应消息
        Response response = chain.proceed(request);

        return response;
    }
}
