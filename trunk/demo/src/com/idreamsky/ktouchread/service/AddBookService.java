package com.idreamsky.ktouchread.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.bookshelf.AddFile;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.data.ChapterContent;
import com.idreamsky.ktouchread.data.SearchData;
import com.idreamsky.ktouchread.util.LogEx;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class AddBookService extends Service  {
	public static boolean isSuccessAddChapter = false;
	public static final String CHAPTER = "CHAPTER";
	public static boolean isAddChapter = false; 

	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		final SearchData searchData =  (SearchData) intent.getSerializableExtra("searchData");
		final String bookId = (String) intent.getSerializableExtra("bookId");
		isAddChapter = false;
		new Thread(){

			@Override
			public void run() {
				StringBuffer sb = null;
				Chapter chapter = null;
				boolean flag = true;
				String line="";
				List<Chapter> ChapterList = null;
				int chapterName = 1;
				int  count = 0;
				boolean f = false;
				int c = 0;
				FileInputStream fileStream = null;
				BufferedReader reader = null;
				try {
					isAddChapter = true;
					fileStream = new FileInputStream(searchData.getFile().getAbsolutePath());
					BufferedInputStream in = new BufferedInputStream(fileStream);
					String chatSet =  get_charset(searchData.getFile());
					reader = new BufferedReader(new InputStreamReader(in, chatSet));

					sb = new StringBuffer();
					long startTime = System.currentTimeMillis();
					sb = sb.append(reader.readLine()).append("\r\n");   
//					int chapterCount = 0;
					ChapterList = new ArrayList<Chapter>();
					chapter  = new Chapter();
					chapter.BookIDNet = bookId;
					chapter.bFree = 0;
					chapter.bRead = 0;
					chapter.bDownLoad = 1;
					while (flag) {
						if(!Book.IsExit(bookId))
						{
							break;
						}
						line = reader.readLine();   
						
						sb.append(line).append("\r\n");
						 if (line == null)
			              {
							  if(ChapterList!=null)
							  {
								  chapter.ChapterIDNet = String.valueOf(chapterName);
								  chapter.ChapterName = "第"+String.valueOf(chapterName)+"章";
								  Book.AddChapterToDB(chapter,bookId);
								  ChapterContent chapterContent = new ChapterContent();
								  chapterContent.BookIDNet = bookId;
								  chapterContent.ChapterIDNet = chapter.ChapterIDNet;
								  chapterContent.ChapterContent = sb.toString();
								  Book.AddChapterContentToDB(chapterContent,1);
								  isSuccessAddChapter = true;
								  f = true;
									if (f) {
										Intent intent = new Intent(AddFile.action);
										sendBroadcast(intent);
										f = false;
									}
							  }
			                  LogEx.Log_I("time", sb.toString()+"");
			                  sb.setLength(0);
			                  count = 0;
			                  break;
			               }
						if(count>=20){
							chapter.ChapterIDNet = String.valueOf(chapterName);
							chapter.ChapterName = "第"+String.valueOf(chapterName)+"章";
							Book.AddChapterToDB(chapter,bookId);
							ChapterContent chapterContent = new ChapterContent();
							chapterContent.BookIDNet =bookId;
							chapterContent.ChapterIDNet = chapter.ChapterIDNet;
							chapterContent.ChapterContent = sb.toString();
							Book.AddChapterContentToDB(chapterContent,1);
							sb.setLength(0);
							count=0;
							line = reader.readLine();   
							sb.append(line).append("\r\n");
							chapterName++;
//							continue;
							Intent inte = new Intent(BookReadActivity.FILERECEIVER);
							inte.putExtra(AddBookService.CHAPTER, chapter);
							sendBroadcast(inte);
							isSuccessAddChapter = true;
							f = true;
							if(f && c==0)
							{
								Intent intent = new Intent(AddFile.action);
								sendBroadcast(intent);
								f = false;
								c++;
							}
						 }
						count++;
						
						Thread.sleep(50);
					}
					isAddChapter = false;
					Intent intent = new Intent(getApplicationContext(),AddBookService.class);
					stopService(intent);
					LogEx.Log_I("time", (System.currentTimeMillis() - startTime)+"");
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						fileStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
			
		}.start();
	}
	/** 
	* 上传文件编码判断 
	* */ 
	public String get_charset(File file ) {   
	        String charset = "GBK";   
	        byte[] first3Bytes = new byte[3];   
	        try {   
	            boolean checked=false;;   
	            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );   
	            bis.mark( 0 );   
	            int read = bis.read( first3Bytes, 0, 3 );   
	            if ( read == -1 ) return charset;   
	            if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) {   
	                charset = "UTF-16LE";   
	                checked = true;   
	            }   
	            else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) {   
	                charset = "UTF-16BE";   
	                checked = true;   
	            }   
	            else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) {   
	                charset = "UTF-8";   
	                checked = true;   
	            }   
//	            bis.reset();   
	            if ( !checked ) {   
	            //    int len = 0;   
	                int loc = 0;   
	  
	                while ( (read = bis.read()) != -1 ) {   
	                    loc++;   
	                    if ( read >= 0xF0 ) break;   
	                    if ( 0x80 <= read && read <= 0xBF ) // 单独出现BF以下的，也算是GBK   
	                    break;   
	                    if ( 0xC0 <= read && read <= 0xDF ) {   
	                        read = bis.read();   
	                        if ( 0x80 <= read && read <= 0xBF ) // 双字节 (0xC0 - 0xDF) (0x80   
	                                                                        // - 0xBF),也可能在GB编码内   
	                        continue;   
	                        else break;   
	                    }   
	                    else if ( 0xE0 <= read && read <= 0xEF ) {// 也有可能出错，但是几率较小   
	                        read = bis.read();   
	                        if ( 0x80 <= read && read <= 0xBF ) {   
	                            read = bis.read();   
	                            if ( 0x80 <= read && read <= 0xBF ) {   
	                                charset = "UTF-8";   
	                                break;   
	                            }   
	                            else break;   
	                        }   
	                        else break;   
	                    }   
	                }   
	                //System.out.println( loc + " " + Integer.toHexString( read ) );   
	            }   
	  
	            bis.close();   
	        } catch ( Exception e ) {   
	            e.printStackTrace();   
	        }   
	  
	        return charset;   
	    }   

	
}
