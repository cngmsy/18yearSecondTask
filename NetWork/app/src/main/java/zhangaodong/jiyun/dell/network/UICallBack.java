package zhangaodong.jiyun.dell.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *自定义UICallBack，更新UI
 * Created by llr on 2017/4/12.
 */

public abstract class UICallBack implements Callback{

    //获得主线程的handler
    private Handler handler=new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call,final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureInUI(call,e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, Response response) throws IOException {
        try {
            if(!response.isSuccessful()){
                throw new Exception("error code:"+response.code());
            }
            final String content = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onResponseInUI(call,content);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onFailureInUI(Call call,IOException e);
    public abstract void onResponseInUI(Call call,String body);
}
