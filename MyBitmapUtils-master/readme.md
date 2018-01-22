#高清图加载
效果图gif
![](sanjihuancun.gif)



#实现的核心代码是
###步骤一:
、、、
       /**
        * 内存缓存
        */
       public class MemoryCacheUtils {

           /**
            * LinkedHashMap<>(10,0.75f,true);
            * <p/>
            * 10是最大致   0.75f是加载因子   true是访问排序   false插入排序
            *
            *
            */
           //private LinkedHashMap<String,Bitmap> mMemoryCache = new LinkedHashMap<>(5,0.75f,true);

           private LruCache<String, Bitmap> mLruCache;


           public MemoryCacheUtils() {
               long maxMemory = Runtime.getRuntime().maxMemory();//最大内存  默认是16兆  运行时候的
               mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
                   @Override
                   protected int sizeOf(String key, Bitmap value) {
                       //int byteCount = value.getByteCount();
                       //得到图片字节数
                       // @return number of bytes between rows of the native bitmap pixels.
                       int byteCount = value.getRowBytes() * value.getWidth();
                       return byteCount;
                   }
               };
           }

           /**
            * 从内存中读取
            *
            * @param url
            */
           public Bitmap getFromMemroy(String url) {

               Log.d("MyBitmapUtils", "从内存中加载图片");
               return mLruCache.get(url);
           }

           /**
            * 写入到内存中
            *
            * @param url
            * @param bitmap
            */
           public void setToMemory(String url, Bitmap bitmap) {
               mLruCache.put(url, bitmap);
           }
       }

、、、
###步骤二:
、、、
/**
 * 自定义的bitmap工具类
 */
public class MyBitmapUtils {


    /**
     * 网络缓存
     */
    public NetCacheUtils mNetCacheUtils;

    /**
     * 本地缓存
     */
    public SDcardCacheUtils mSdCacheUtils;

    /**
     * 内存缓存
     */
    public MemoryCacheUtils mMemoryCacheUtils;


    public MyBitmapUtils() {
        mSdCacheUtils = new SDcardCacheUtils();
        mMemoryCacheUtils = new MemoryCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mSdCacheUtils, mMemoryCacheUtils);
    }

    /**
     * 展示图片的方法
     *
     * @param image
     * @param url
     */
    public void display(ImageView image, String url) {


        //从内存中读取
        Bitmap fromMemroy = mMemoryCacheUtils.getFromMemroy(url);
        //如果内存中有的h话就直接返回，从内存中读取
        if (fromMemroy != null) {
            image.setImageBitmap(fromMemroy);

            return;
        }


        //从本地SD卡读取
        Bitmap fromSd = mSdCacheUtils.getFromSd(url);
        if (fromSd != null) {
            image.setImageBitmap(fromSd);

            mMemoryCacheUtils.setToMemory(url, fromSd);

            return;
        }
        //从网络中读取
        mNetCacheUtils.getDataFromNet(image, url);

    }
}
、、、
###步骤三:
、、、
/**
     * 从网络中下载图片
     *
     * @param image
     * @param url
     */
    public void getDataFromNet(ImageView image, String url) {
        new MyAsyncTask().execute(image, url);  //启动Asynctask，传入的参数到对应doInBackground（）
    }


    /**
     * 异步下载
     * <p/>
     * 第一个泛型 ： 参数类型  对应doInBackground（）
     * 第二个泛型 ： 更新进度   对应onProgressUpdate（）
     * 第三个泛型 ： 返回结果result   对应onPostExecute
     */
    class MyAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        /**
         * 后台下载  子线程
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {

            //拿到传入的image
            mImageView = (ImageView) params[0];

            //得到图片的地址
            mUrl = (String) params[1];
            //将imageview和url绑定，防止错乱
            mImageView.setTag(mUrl);

            Bitmap bitmap = downLoadBitmap(mUrl);

            return bitmap;
        }


        /**
         * 进度更新   UI线程
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 回调结果，耗时方法结束后，主线程
         *
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                //得到图片的tag值
                String url = (String) mImageView.getTag();
                //确保图片设置给了正确的image
                if (url.equals(mUrl)) {
                    mImageView.setImageBitmap(bitmap);

                    /**
                     * 当从网络上下载好之后保存到sdcard中
                     */
                    mDcardCacheUtils.savaSd(mUrl, bitmap);

                    /**
                     *  写入到内存中
                     */
                    mMemoryCacheUtils.setToMemory(mUrl, bitmap);
                    Log.d("MyBitmapUtils", "我是从网络缓存中读取的图片啊");
                }
            }
        }
    }

    /**
     * 下载图片
     *
     * @param url 下载图片地址
     * @return
     */
    private Bitmap downLoadBitmap(String url) {

        //连接
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url)
                    .openConnection();

            //设置读取超时
            conn.setReadTimeout(5000);
            //设置请求方法
            conn.setRequestMethod("GET");
            //设置连接超时连接
            conn.setConnectTimeout(5000);
            //连接
            conn.connect();

            //响应码
            int code = conn.getResponseCode();

            if (code == 200) {  //请求正确的响应码是200
                //得到响应流
                InputStream inputStream = conn.getInputStream();
                //得到bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }
  、、、

###步骤四:
、、、
  /**
       * 从本地读取
       * @param url
       */
      public Bitmap getFromSd(String url){
          String fileName = null;
          try {
              //得到图片的url的md5的文件名
              fileName = MD5Encoder.encode(url);
          } catch (Exception e) {
              e.printStackTrace();
          }
          File file = new File(CACHE_PATH,fileName);

          //如果存在，就通过bitmap工厂，返回的bitmap，然后返回bitmap
          if (file.exists()){
              try {
                  Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                  Log.d("MyBitmapUtils", "从本地读取图片啊");
                  return bitmap;
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
          }
          return null;
      }

      /**
       * 向本地缓存
       *
       * @param url   图片地址
       * @param bitmap   图片
       */
      public void savaSd(String url,Bitmap bitmap){
          String fileName = null;
          try {
              //我们对图片的地址进行MD5加密，作为文件名
              fileName = MD5Encoder.encode(url);
          } catch (Exception e) {
              e.printStackTrace();
          }

          /**
           * 以CACHE_PATH为文件夹  fileName为文件名
           */
          File file = new File(CACHE_PATH,fileName);

          //我们首先得到他的符文剑
          File parentFile = file.getParentFile();
          //查看是否存在，如果不存在就创建
          if (!parentFile.exists()){
              parentFile.mkdirs(); //创建文件夹
          }

          try {
              //将图片保存到本地
              /**
               * @param format   The format of the compressed image   图片的保存格式
               * @param quality  Hint to the compressor, 0-100. 0 meaning compress for
               *                 small size, 100 meaning compress for max quality. Some
               *                 formats, like PNG which is lossless, will ignore the
               *                 quality setting
               *                 图片的保存的质量    100最好
               * @param stream   The outputstream to write the compressed data.
               */
              bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }

          、、、