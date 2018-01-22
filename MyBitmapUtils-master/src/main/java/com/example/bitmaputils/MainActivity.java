package com.example.bitmaputils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bitmaputils.bitmap.MyBitmapUtils;
import com.lidroid.xutils.BitmapUtils;

public class MainActivity extends AppCompatActivity {

    private String[] mImageViews;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageViews = ImageDataUtils.ImagesUtils;
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(new PhotoAdapter());
    }

    /**
     * ListView的适配器
     */
    class PhotoAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        private MyBitmapUtils utils;
        public PhotoAdapter() {
            //mBitmapUtils = new BitmapUtils(MainActivity.this);
           // mBitmapUtils.configDefaultLoadingImage(R.mipmap.defaut);
            utils = new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public Object getItem(int position) {
            return mImageViews[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(),R.layout.photo_item_list,null);
                holder.tvImage = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //PhotoData.PhotoInfo photoInfo = getItem(position);
           // holder.tvTitle.setText(photoInfo.title);
            //mBitmapUtils.display(holder.tvImage,mImageViews[position]);
            utils.display(holder.tvImage,mImageViews[position]);
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView tvImage;
    }
}
