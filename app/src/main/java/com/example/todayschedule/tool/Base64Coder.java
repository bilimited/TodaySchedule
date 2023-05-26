package com.example.todayschedule.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.todayschedule.R;
import com.example.todayschedule.bean.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * @ClassName Base64Coder
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/5/24 11:25
 * @Version 1.0
 */
public class Base64Coder {

    private static Bitmap resize(Bitmap bitmap,float scale){
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = scale;
        float scale_h = scale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }

    public static String getBase64FromFile(Context context, Uri fileUri) {
        String base64 = "";
        if(fileUri==null||fileUri.equals("")){
            return null;
        }
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(fileUri));
            if(bitmap==null){
                return null;
            }
            Log.d("压缩测试","原图大小:"+bitmap.getAllocationByteCount());

            for(int i = 0;i<10&&bitmap.getAllocationByteCount()>4000000;i++){
                bitmap = resize(bitmap,0.8f);
                Log.d("压缩测试","裁剪后大小:"+bitmap.getAllocationByteCount());
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);

            baos.flush();
            base64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);

            Log.d("压缩测试","base64 length:"+base64.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/jpeg;base64,"+base64;
    }

    private static boolean isBase64Img(String imgurl){
        if(!TextUtils.isEmpty(imgurl)&&(imgurl.startsWith("data:image/png;base64,")
                ||imgurl.startsWith("data:image/*;base64,")||imgurl.startsWith("data:image/jpg;base64,")
        ))
        {
            return true;
        }
        return false;
    }

    private static void base64ToImg(Activity context, String url, int error_resid, ImageView imageView){
        byte[] decode = null;
        if(isBase64Img(url))
        {
            url = url.split(",")[1];
            decode = Base64.decode(url, Base64.DEFAULT);
        }

        Glide.with(context).load(decode==null ?url:decode).into(imageView);

    }

    public static String imgToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    public static void LoadImage(Activity context,String imgID,ImageView imageView){

        BmobQuery<Image> bmobQuery = new BmobQuery<Image>();
        bmobQuery.getObject(imgID, new QueryListener<Image>() {
            @Override
            public void done(Image object, BmobException e) {
                if(e==null){
                    base64ToImg(context,object.getBase64(), R.drawable.ic_launcher_background,imageView);
                }else{
                    Log.d("test","找不到图片资源!");
                }
            }
        });

    }

}
