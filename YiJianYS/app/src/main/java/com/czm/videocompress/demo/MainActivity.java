package com.czm.videocompress.demo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.czm.videocompress.R;
import com.czm.videocompress.util.SGLog;
import com.czm.videocompress.util.Worker;
import com.czm.videocompress.video.VideoCompressListener;
import com.czm.videocompress.video.VideoCompressor;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TextView mResult;
    private String mStrResult;
    private ScrollView mScrollView;
    private Button btn_compress;
    private Button btn_ysh;
    private Button btn_ysq;
    private static final int REQUEST_CODE = 520;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mResult = (TextView) findViewById(R.id.tv_result);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        btn_compress = (Button) findViewById(R.id.btn_compress);
        btn_ysq = (Button) findViewById(R.id.btn_ysq);
        btn_ysh = (Button) findViewById(R.id.btn_ysh);
        btn_compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                compressVideo();
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_CODE);
            }

        });
    }

    private void compressVideo() {
        String sdcardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String mInputStr = sdcardDir+"/DCIM/Camera/21s.mp4";
        mStrResult = "Compress begin=========\n";
        mResult.setText(mStrResult);
        VideoCompressor.compress(this, mInputStr, new VideoCompressListener() {
            @Override
            public void onSuccess(final String outputFile, String filename, long duration) {
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"video compress success:"+outputFile,Toast.LENGTH_SHORT).show();
                        SGLog.e("video compress success:"+outputFile);
                        mStrResult +="Compress end=========onSuccess\n";
                        mResult.setText(mStrResult);
                        mScrollView.fullScroll(View.FOCUS_DOWN);

                    }
                });
            }

            @Override
            public void onFail(final String reason) {
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"video compress failed:"+reason,Toast.LENGTH_SHORT).show();
                        SGLog.e("video compress failed:"+reason);
                        mStrResult+="Compress end=========onFail\n";
                        mResult.setText(mStrResult);
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

            @Override
            public void onProgress(final int progress) {
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        SGLog.e("video compress progress:"+progress);
                        mStrResult += "Compress progress:"+progress +"%\n";
                        mResult.setText(mStrResult);
                        mScrollView.fullScroll(View.FOCUS_DOWN);

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String videoPath = cursor.getString(columnIndex);
            btn_compress.setText("压缩中....");
            btn_ysq.setText(videoPath);
            VideoCompressor.compress(this, videoPath, new VideoCompressListener() {
                @Override
                public void onSuccess(final String outputFile, String filename, long duration) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,"video compress success:"+outputFile,Toast.LENGTH_LONG).show();
                            SGLog.e("video compress success:"+outputFile);
                            mStrResult +="Compress end=========onSuccess\n";
                            mResult.setText(mStrResult);
                            btn_ysh.setText(outputFile);
                            btn_compress.setText("压缩完成");
                            mScrollView.fullScroll(View.FOCUS_DOWN);
//                7cedd6b04f411c1f9d9df2cdaee7d066
                        }
                    });
                }

                @Override
                public void onFail(final String reason) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,"video compress failed:"+reason,Toast.LENGTH_SHORT).show();
                            SGLog.e("video compress failed:"+reason);
                            mStrResult+="Compress end=========onFail\n";
                            mResult.setText(mStrResult);
                            mScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }

                @Override
                public void onProgress(final int progress) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            SGLog.e("video compress progress:"+progress);
                            mStrResult += "Compress progress:"+progress +"%\n";
                            mResult.setText(mStrResult);
                            mScrollView.fullScroll(View.FOCUS_DOWN);

                        }
                    });
                }
            });
            cursor.close();
        }
    }
}
