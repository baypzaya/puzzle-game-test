package com.idreamsky.ktouchread.util;


import java.io.File;

import com.idreamsky.ktouchread.data.net.UrlUtil;

import android.os.Environment;

/**
 * @author 王文华
 * @since: 2011-03-31
 */
public class SDCardUtils {
	
	private static android.os.StatFs statfs = new android.os.StatFs(
			android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath());
	
	public static String GetPath()
	{
		return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static String GetSysPath()
	{
		return android.os.Environment.getRootDirectory().getAbsolutePath();
	}
	
	/**
	 * 判断SD卡是否可用
	 * 
	 * @author 王文华
	 * @Since:2011-03-31
	 * @return
	 */
	public static boolean isSDCardEnable() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SDCard总大小
	 * @author 王文华
	 * @Since:2011-03-31
	 * @return bit
	 */
	public static long getTotalSize() {
		// 获取SDCard上BLOCK总数
		long nTotalBlocks = statfs.getBlockCount();

		// 获取SDCard上每个block的SIZE
		long nBlocSize = statfs.getBlockSize();

		// 计算SDCard 总容量大小B
		long nSDTotalSize = nTotalBlocks * nBlocSize;

		return nSDTotalSize;
	}

	/**
	 * 获取可用空间大小
	 * @author 王文华
	 * @Since:2011-03-31
	 * @return bit
	 */
	public static long getFreeSize() {

		// 获取SDCard上每个block的SIZE
		long nBlocSize = statfs.getBlockSize();

		// 获取可供程序使用的Block的数量
		long nAvailaBlock = statfs.getAvailableBlocks();

		// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
		// long nFreeBlock = statfs.getFreeBlocks();

		// 计算 SDCard 剩余大小B
		long nSDFreeSize = nAvailaBlock * nBlocSize;
		return nSDFreeSize;
	}
	public static String GetDBPath()
	{
        StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
        pathBuffer.append(File.separator + "KTouchRead" + File.separator );
        return pathBuffer.toString();
	}
	public static String GetChaperPath(String BookID)
	{
	     StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
	        pathBuffer.append(File.separator + "KTouchRead" + File.separator );
	        pathBuffer.append("Book"+ File.separator);
	        pathBuffer.append(UrlUtil.USERID + File.separator);
	        pathBuffer.append(BookID + File.separator);
	        return pathBuffer.toString();
	}
	public static String GetBookPathEx(String BookID)
	{
	     StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
	        pathBuffer.append(File.separator + "KTouchRead" + File.separator );
	        pathBuffer.append("Book"+ File.separator);
	        pathBuffer.append(UrlUtil.USERID + File.separator);
	        pathBuffer.append(BookID );
	        return pathBuffer.toString();
	}
	public static String GetBookListPath()
	{
	     StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
	        pathBuffer.append(File.separator + "KTouchRead" + File.separator );
	        pathBuffer.append("Book"+ File.separator);
	        pathBuffer.append(UrlUtil.USERID + File.separator);
	        return pathBuffer.toString();
	}
	public static String GetBookPath()
	{
        StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
        pathBuffer.append(File.separator + "KTouchRead" + File.separator +"BookMark.png");
        File file = new File(pathBuffer.toString());
        if(file.exists())
        {
        	 return pathBuffer.toString();
        }
        StringBuffer pathBufferjpg = new StringBuffer(SDCardUtils.GetPath());
        pathBufferjpg.append(File.separator + "KTouchRead" + File.separator +"BookMark.jpg");
        File filejpg = new File(pathBufferjpg.toString());
        if(filejpg.exists())
        {
        	 return pathBufferjpg.toString();
        }
        return null;
	}
	
	public static String GetBookPath(String extention)
	{
        StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
        pathBuffer.append(File.separator + "KTouchRead" + File.separator +"BookMark." + extention);
        return pathBuffer.toString();
	}
	
	public static String GetpicPath()
	{
        StringBuffer pathBuffer = new StringBuffer(SDCardUtils.GetPath());
        pathBuffer.append(File.separator + "KTouchRead" + File.separator  +"PicCatch" + File.separator);
        return pathBuffer.toString();
	}
	
	public static String getFileExtension(String file) {
		int beginIndex = file.lastIndexOf("/");
		int endIndex = file.lastIndexOf(".");
		if (beginIndex >= 0) {
			if (beginIndex > endIndex) {
				return null;
			} else {
				return file.substring(endIndex + 1);
			}
		} else {
			if (endIndex < 0) {
				return null;
			} else {
				return file.substring(endIndex + 1);
			}
		}
	}
}

