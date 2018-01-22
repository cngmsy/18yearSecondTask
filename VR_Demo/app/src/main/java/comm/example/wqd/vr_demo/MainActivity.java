package comm.example.wqd.vr_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private VrPanoramaView mVr;
    private ImageAsyncTask mImageAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //A.对VR控件初始化
        mVr = (VrPanoramaView) findViewById(R.id.vr);
        //隐藏掉VR效果左下角的信息按钮显示
        mVr.setInfoButtonEnabled(false);
        //隐藏掉VR效果右下角全屏显示按钮
        mVr.setFullscreenButtonEnabled(false);
        //切换VR的模式   参数: VrWidgetView.DisplayMode.FULLSCREEN_STEREO设备模式(手机横着放试试)
        //   VrWidgetView.DisplayMode.FULLSCREEN_MONO手机模式
        mVr.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);
        //D.设置对VR运行状态的监听,如果VR运行出现错误,可以及时处理.
        mVr.setEventListener(new MyVREventListener());
        //B.使用自定义的AsyncTask,播放VR效果
        mImageAsyncTask = new ImageAsyncTask();
        mImageAsyncTask.execute();
    }

    /**
     * B.自定义一个类继承AsyncTask,只使用我们需要的方法.
     * 由于VR资源数据量大,获取需要时间,故把加载图片放到子线程中进行,主线程来显示图片,故可以使用一个异步线程AsyncTask或EventBus来处理.
     */
    class ImageAsyncTask extends AsyncTask<Void,Void,Bitmap> {
        //B.该方法在子线程运行,从本地文件中把资源加载到内存中.
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                //从资产目录拿到资源,返回结果是字节流
                InputStream open = getAssets().open("andes.jpg");
                //把字节流转换成Bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(open);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        //该方法在主线程运行
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //创建bVrPanoramaView.Options,去决定显示VR是普通效果,还是立体效果
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            //TYPE_STEREO_OVER_UNDER立体效果:图片的上半部分放在左眼显示,下半部分放在右眼显示 TYPE_MONO:普通效果
            options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            //使用VR控件对象,显示效果  参数:1.Bitmap对象      2.VrPanoramaView.Options对象,决定显示的效果
            mVr.loadImageFromBitmap(bitmap,options);
            super.onPostExecute(bitmap);
        }
    }
    //C.因为VR很占用内存,所以当界面进入onPause状态,暂停VR视图显示,进入onResume状态,继续VR视图显示,进入onDestroy,杀死VR,关闭异步任务

    //当失去焦点时,回调
    @Override
    protected void onPause() {
        //暂停渲染和显示
        mVr.pauseRendering();
        super.onPause();
    }

    //当重新获取到焦点时,回调
    @Override
    protected void onResume() {
        super.onResume();
        //继续渲染和显示
        mVr.resumeRendering();
    }

    //当Activity销毁时,回调
    @Override
    protected void onDestroy() {
        //关闭渲染视图
        mVr.shutdown();
        if(mVr != null){
            //在退出activity时,如果异步任务没有取消,就取消
            if(!mImageAsyncTask.isCancelled()){
                mImageAsyncTask.cancel(true);
            }
        }
        super.onDestroy();
    }

    //VR运行状态监听类,自定义一个类继承VrPanoramaEventListener,复写里面的两个方法
    private class MyVREventListener extends VrPanoramaEventListener {
        //当VR视图加载成功的时候回调
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Toast.makeText(MainActivity.this, "加载成功,么么哒", Toast.LENGTH_SHORT).show();

        }

        //当VR视图加载失败的时候回调
        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            Toast.makeText(MainActivity.this, "加载失败,因为易大师影响", Toast.LENGTH_SHORT).show();
        }
    }
}
