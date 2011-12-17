package com.idreamsky.ktouchread.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import com.idreamsky.ktouchread.bookshelf.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImgUtil {
	 // 缩放图片
	public static  ConcurrentHashMap<String, WeakReference<Bitmap>> sBitmapPool = new ConcurrentHashMap<String, WeakReference<Bitmap>>();
//	public static Bitmap zoomImg(String img, int newWidth ,int newHeight,boolean model,Activity mActivity){
//	// 图片源
//	    Bitmap bm = BitmapFactory.decodeFile(img);
//	    if(null!=bm){
//	    	return zoomImg(bm,newWidth,newHeight,model,mActivity);
//	    }
//	    return null;
//	}
//	
//	public static Bitmap zoomImg(Context context,String img, int newWidth ,int newHeight,boolean model,Activity mActivity){
//		// 图片源
//		try {
//			Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
//					.open(img));
//			if (null != bm) {
//				return zoomImg(bm, newWidth, newHeight,model,mActivity);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		}
//	 // 缩放图片
//	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight,boolean model,Activity mActivity){
//	    if(bm==null || bm.isRecycled()){
//	    	if(model)//白天模式
//	    	{
//	    		bm = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page);
//	    	}else
//	    	{
//	    		bm = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page_a);
//	    	}
//	    }
//		// 获得图片的宽高
//	    int width = bm.getWidth();
//	    int height = bm.getHeight();
//	    if(width==newWidth && newHeight==height){
//	    	return bm;
//	    }
//	    // 计算缩放比例
//	    float scaleWidth = ((float) newWidth) / width;
//	    float scaleHeight = ((float) newHeight) / height;
//	    // 取得想要缩放的matrix参数
//	    Matrix matrix = new Matrix();
//	    matrix.postScale(scaleWidth, scaleHeight);
//	    // 得到新的图片
//	    Bitmap bmnew = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
////	    bm.recycle();
////	    bm = null;
//    	return bmnew;
//	}
	 // 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
//	    if(bm==null){
//	    	if(model)//白天模式
//	    	{
//	    		bm = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page);
//	    	}else
//	    	{
//	    		bm = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page_a);
//	    	}
//	    }
		// 获得图片的宽高
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    // 计算缩放比例
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // 取得想要缩放的matrix参数
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 得到新的图片
	    Bitmap bmnew = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//	    bm.recycle();
//	    bm = null;
    	return bmnew;
	}
	/**
	 * 为书封面 换上框架(衣裳) 呈现立体感
	 * @param oldCover
	 * @return
	 */
	public static Bitmap getRemodelBookCover(Bitmap oldCover,Context mContext){
		//书封面即将换上的框架(衣裳)
		Bitmap coverFrameworkBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book_frame_bg);
		//换上框架(衣裳)后的高度，宽度
		final int newCoverHeight = coverFrameworkBitmap.getHeight();
		final int newCoverWidth = coverFrameworkBitmap.getWidth();
		
		Bitmap newCover = Bitmap.createBitmap(newCoverWidth, newCoverHeight, Config.ARGB_4444);
		Canvas mCanvas = new Canvas(newCover);
		
		mCanvas.drawBitmap(coverFrameworkBitmap, 0, 0, null);
		mCanvas.drawBitmap(oldCover, 0, 0, null);
		
		//内存释放
		oldCover.recycle();
		
		return newCover;
	}
	
	public static Bitmap getImageByPath(String BookIdNet){
		Bitmap resultBitmap=null;
		String imagePathpng = SDCardUtils.GetpicPath() +BookIdNet + ".png";
		String imagePathjpg = SDCardUtils.GetpicPath() +BookIdNet + ".jpg";
		
		if(imagePathjpg!=null&&!"".equals(imagePathjpg.trim())){
			File coverFilepng = new File(imagePathjpg);

			if(coverFilepng.exists()){
				if(coverFilepng.isFile()){
					resultBitmap=BitmapFactory.decodeFile(coverFilepng.getAbsolutePath());
					Log.v("Bitmap", "isFile");
				}
			}else{
				File coverFilejpg = new File(imagePathpng);

				if(coverFilejpg.exists()){
					if(coverFilejpg.isFile()){
						resultBitmap=BitmapFactory.decodeFile(coverFilejpg.getAbsolutePath());
						Log.v("Bitmap", "isFile");
					}
				}
			}	
		}
		return resultBitmap;
	}
	/**
	 * 改变图片大小
	 * @param newWidth
	 * @param newHight
	 * @param oldBitmap
	 * @return
	 */
	public static Bitmap changeBitmapSize(float newWidth,float newHight,Bitmap oldBitmap){
		if(oldBitmap==null){
			return null;
		}
		float bmpWidth = oldBitmap.getWidth();   
        float bmpHeight = oldBitmap.getHeight();   
        
        if(bmpWidth==newWidth&&newHight==bmpHeight){
        	return oldBitmap;
        }
        
        if(bmpWidth==newWidth && bmpHeight==newHight){
        	return oldBitmap;
        }
        
        /* 计算出缩小后的长宽 比例*/   
        float scaleWidth = newWidth/bmpWidth;   
        float scaleHeight = newHight/bmpHeight;   
        
        /* 产生Resize后的Bitmap对象 */   
        Matrix matrix = new Matrix();   
        matrix.postScale(scaleWidth, scaleHeight);   
        Bitmap bm=Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(),   
                matrix, true);   
        oldBitmap.recycle();
        return  bm;
	}
	
	public static String SaveBitmap(final Bitmap bm,final  String extention,final String BookIDNet){
		FileOutputStream fos = null;
		String filePath= null;
		String Extention = extention;
		long bitmapSize=bm.getRowBytes()*bm.getHeight();
		if(!SDCardUtils.isSDCardEnable()){
			return null;
		}
		else if(SDCardUtils.getFreeSize()<=bitmapSize){
			return null;
		}
		String rootPath= SDCardUtils.GetpicPath();
		
		CompressFormat format;
		if(Extention.compareTo("jpg") == 0)
		{
			format = CompressFormat.JPEG ;
		}else if (Extention.compareTo("png") == 0) {
			format = CompressFormat.PNG ;
		}else{
			format = CompressFormat.JPEG ;
			Extention = "jpg";
		}
		
		try{
			File file=new File(rootPath);
			if(!file.exists()){
				file.mkdirs();
			}


			filePath = rootPath +BookIDNet+"." + Extention ;
			
			file=new File(filePath);
			if(file.exists()){
				return null;
			}
			
			
			
			file.createNewFile();
			fos=new FileOutputStream(file);
			bm.compress(format, 75, fos);
			
			fos.flush();
			fos.close();
			return filePath;
			  
		}catch (Exception e) {
		    Log.e("BookReader", e.toString());
		}
		return null;
	}
	
	
	public static Drawable drawableToBitmap(Bitmap bm) {  
        return new BitmapDrawable(bm);  
    }  
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}
	
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	public static Bitmap drawableToBitmap(Drawable d)
	{
		BitmapDrawable bd = (BitmapDrawable) d;
		return bd.getBitmap();
	}


		 
}
