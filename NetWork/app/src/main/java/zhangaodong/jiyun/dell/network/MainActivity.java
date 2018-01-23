package zhangaodong.jiyun.dell.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import zhangaodong.jiyun.dell.network.entity.RegisterResult;
import zhangaodong.jiyun.dell.network.entity.User;

public class MainActivity extends AppCompatActivity {

    private EditText edt_username;
    private RelativeLayout layout_username;
    private EditText edt_password;
    private RelativeLayout layout_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        layout_username = (RelativeLayout) findViewById(R.id.layout_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        layout_password = (RelativeLayout) findViewById(R.id.layout_password);
        btn_login = (Button) findViewById(R.id.btn_login);

    }
    @OnClick(R.id.btn_login)
    public void onClick(){
        String username=edt_username.getText().toString();
        String password=edt_password.getText().toString();
        Toast.makeText(this, username+","+password, Toast.LENGTH_SHORT).show();
        register(username,password);
    }

    //注册
    private void register(String username, String password) {

        //日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //设置日志拦截器的级别
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义的拦截器
        BombInterceptor bombInterceptor = new BombInterceptor();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //添加 统一添加请求头 的拦截器
                .addInterceptor(bombInterceptor)
                //添加日志拦截器
                .addInterceptor(interceptor)
                .build();

        User user = new User(username, password);
        String toJson = new Gson().toJson(user);

        final RequestBody requestBody = RequestBody.create(null, toJson);

        Request request = new Request.Builder()
                .post(requestBody)
                .url("https://api.bmob.cn/1/users")
                .build();

        okHttpClient.newCall(request).enqueue(new UICallBack() {
            @Override
            public void onFailureInUI(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络连接超时！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseInUI(Call call, String body) {
                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                RegisterResult result = new Gson().fromJson(body, RegisterResult.class);
                Log.e("TAG", "onResponseInUI: "+result.toString() );
            }
        });
    }
}
