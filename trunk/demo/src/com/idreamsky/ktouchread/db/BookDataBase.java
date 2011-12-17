package com.idreamsky.ktouchread.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

import com.idreamsky.ktouchread.data.BookMark;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.data.ChapterContent;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.BookMarkFactory;
import com.idreamsky.ktouchread.data.net.Category;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.data.net.Top;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.AESCrypto;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.SDCardUtils;



public class BookDataBase {

	private static final String TABLE_BOOKMARK = "table_bookmark";	
	
	private static final String BOOKMARK_ID = "bookmark_id";
	private static final String MARK_BOOK_ID = "book_id";
	private static final String MARK_CHAPTER_ID = "chapter_id";
	private static final String MARK_TEXT = "mark_text";
	private static final String MARK_PERCENT = "mark_percent";
	private static final String BOOKMARK_ID_NET = "bookmark_id_net";
	private static final String BOOKMARK_POS = "bookmark_pos";
	private static final String BOOKMARK_DATE = "bookmark_date";
	private static final String BOOKMARK_SYNC = "bookmark_sync";	
	private static final String BOOKMARK_CPCODE = "bookmark_cpcode";	
	private static final String BOOKMARK_RPID = "bookmark_rpid";	
	private static final String BOOKMARK_FIELD1 = "bookmark_field1";	
	
	
	private static final String CREATE_TABLE_BOOKMARK = "CREATE TABLE " + TABLE_BOOKMARK 
	+ " (" + BOOKMARK_ID  + " integer primary key autoincrement,"
	+ MARK_BOOK_ID + " TEXT,"
	+ MARK_CHAPTER_ID + " TEXT,"
	+ MARK_TEXT + " TEXT,"
	+ MARK_PERCENT + " integer,"
	+ BOOKMARK_ID_NET + " TEXT,"
	+ BOOKMARK_POS + " TEXT,"
	+ BOOKMARK_DATE + " TEXT,"
	+ BOOKMARK_SYNC + " integer,"
	+ BOOKMARK_CPCODE + " TEXT,"
	+ BOOKMARK_RPID + " TEXT,"
	+ BOOKMARK_FIELD1 + " TEXT)";
	
	
	private static final String TABLE_CATEGORY = "table_cagegory";	
	private static final String CATEGORY_ID = "category_id";
	private static final String CATEGORY_CPCODE = "category_cpcode";
	private static final String CATEGORY_ID_NET = "category_id_net";
	private static final String CATEGORY_NAME = "category_name";
	private static final String CATEGORY_PARENT_ID = "category_parent_id";
	private static final String CATEGORY_LEVEL = "category_level";
	private static final String CATEGORY_BOOK_COUNT = "category_name_count";
	
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY 
	+ " (" + CATEGORY_ID  + " integer primary key autoincrement,"
	+ CATEGORY_CPCODE + " TEXT,"
	+ CATEGORY_ID_NET + " TEXT,"
	+ CATEGORY_NAME + " TEXT,"
	+ CATEGORY_PARENT_ID + " TEXT,"
	+ CATEGORY_LEVEL + " integer,"
	+ CATEGORY_BOOK_COUNT + " integer)";
	
	
	private static final String TABLE_TOP = "table_top";	
	private static final String TOP_ID = "top_id";
	private static final String TOP_CPCODE = "top_cpcode";
	private static final String TOP_ID_NET = "top_id_net";
	private static final String TOP_NAME = "top_name";
	private static final String TOP_DESC = "top_desc";
	
	private static final String CREATE_TABLE_TOP = "CREATE TABLE " + TABLE_TOP 
	+ " (" + TOP_ID  + " integer primary key autoincrement,"
	+ TOP_CPCODE + " TEXT,"
	+ TOP_ID_NET + " TEXT,"
	+ TOP_NAME + " TEXT,"
	+ TOP_DESC + " TEXT,"
	+ CATEGORY_LEVEL + " integer)";
	
	private static final String TABLE_ADVERT_BOOK = "table_advert_book";	
	private static final String ADVRT_BOOK_ID = "id";
	private static final String ADVRT_BOOK_SEQNO = "seqno";
	private static final String ADVRT_BOOK_IMAGEURL = "imageurl";
	private static final String ADVRT_BOOK_BOOKID = "bookid";
	private static final String ADVRT_BOOK_BOOKNAME = "bookname";
	private static final String ADVRT_BOOK_RPID = "rpid";
	private static final String ADVRT_BOOK_CPCODE = "cpcode";
	private static final String ADVRT_BOOK_TITTLE = "tittle";
	private static final String ADVRT_BOOK_UPDATES = "UPDATES";
	private static final String ADVRT_BOOK_POS = "POS";
	private static final String ADVRT_BOOK_STATUS = "status";
	private static final String ADVRT_BOOK_TYPE = "type";
	
	private static final String CREATE_TABLE_ADVERT_BOOK = "CREATE TABLE " + TABLE_ADVERT_BOOK 
	+ " (" + ADVRT_BOOK_ID  + " integer primary key autoincrement,"
	+ ADVRT_BOOK_SEQNO + " integer,"
	+ ADVRT_BOOK_IMAGEURL + " TEXT,"
	+ ADVRT_BOOK_BOOKID + " TEXT,"
	+ ADVRT_BOOK_BOOKNAME + " TEXT,"
	+ ADVRT_BOOK_RPID + " TEXT,"
	+ ADVRT_BOOK_CPCODE + " TEXT,"
	+ ADVRT_BOOK_TITTLE + " TEXT,"
	+ ADVRT_BOOK_UPDATES + " TEXT,"
	+ ADVRT_BOOK_POS + " integer,"
	+ ADVRT_BOOK_TYPE + " integer,"
	+ ADVRT_BOOK_STATUS + " integer)";
	
	
	private static final String TABLE_ADVERT_BOOKMARK = "table_advert_bookmark";
	private static final String ADVRT_BOOKMARK_ID = "id";
	private static final String ADVRT_BOOKMARK_SUBJECTID = "subject_id";
	private static final String ADVRT_BOOKMARK_NAME = "name";
	private static final String ADVRT_BOOKMARK_IMAGE = "image";
	
	private static final String CREATE_TABLE_ADVERT_BOOKMARK = "CREATE TABLE " + TABLE_ADVERT_BOOKMARK 
	+ " (" + ADVRT_BOOKMARK_ID  + " integer primary key autoincrement,"
	+ ADVRT_BOOKMARK_SUBJECTID + " TEXT,"
	+ ADVRT_BOOKMARK_NAME + " TEXT,"
	+ ADVRT_BOOKMARK_IMAGE + " TEXT)";
	
	private static final String TABLE_BOOKMARK_PIC = "table_bookmark_pic";
	private static final String BOOKMARK_PIC_ID = "id";
	private static final String BOOKMARK_PIC_ID_NET = "bookmark_id";
	private static final String BOOKMARK_PIC_DEC = "dec";
	private static final String BOOKMARK_PIC_TITTLE = "title";
	private static final String BOOKMARK_PIC_IMAGE = "image";
	private static final String BOOKMARK_PIC_FREE = "free";
	private static final String BOOKMARK_PIC_PRICE = "price";
	private static final String BOOKMARK_PIC_SUBJECTID = "subjectid";
	
	private static final String CREATE_TABLE_BOOKMARK_PIC = "CREATE TABLE " + TABLE_BOOKMARK_PIC 
	+ " (" + BOOKMARK_PIC_ID  + " integer primary key autoincrement,"
	+ BOOKMARK_PIC_SUBJECTID + " TEXT,"
	+ BOOKMARK_PIC_ID_NET + " TEXT,"
	+ BOOKMARK_PIC_DEC + " TEXT,"
	+ BOOKMARK_PIC_TITTLE + " TEXT,"
	+ BOOKMARK_PIC_IMAGE + " TEXT,"
	+ BOOKMARK_PIC_FREE + " integer,"
	+ BOOKMARK_PIC_PRICE + " TEXT)";
	
	
	
	
	private static final String TABLE_SEARCH_KEY = "table_search_key";	
	private static final String SEARCH_KEY = "search_key";	
	
	private static final String CREATE_SEARCH_KEY = "CREATE TABLE " + TABLE_SEARCH_KEY 
	+ " (" + SEARCH_KEY  + " TEXT)";
	

	
	
	private SQLiteDatabase mSQLiteDatabase = null;
	private DatabaseOpenHelper mDatabaseOpenHelper = null;
	public static Context mContext = null;
	public BookDataBase() {
		
	}
	public static void  Initial(Context context)
	{
		mContext = context;
	}
	private static BookDataBase sInstance;
	private static byte[] SYNC = new byte[0];
	private static byte[] SYNCCHAPTER = new byte[0];
	
	public static BookDataBase getInstance() {
		if (null == sInstance) {
			synchronized (SYNC) {
				if (null == sInstance) {
					sInstance = new BookDataBase();
					sInstance.open();
				}
			}
		}
		return sInstance;
	}

	public void open() {
		try {
			mDatabaseOpenHelper = new DatabaseOpenHelper(mContext);
			
			mSQLiteDatabase = mDatabaseOpenHelper.getWritableDatabase();
			String pathString = mSQLiteDatabase.getPath();
			if (mSQLiteDatabase != null) {
	
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_BOOKMARK)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_BOOKMARK);
				}

				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_CATEGORY)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
				}
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_TOP)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_TOP);
				}
				
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_SEARCH_KEY)) {
					mSQLiteDatabase.execSQL(CREATE_SEARCH_KEY);
				}
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_ADVERT_BOOK)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_ADVERT_BOOK);
				}
				
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_ADVERT_BOOKMARK)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_ADVERT_BOOKMARK);
				}
				if (!mDatabaseOpenHelper.isExistsTable(mSQLiteDatabase,
						TABLE_BOOKMARK_PIC)) {
					mSQLiteDatabase.execSQL(CREATE_TABLE_BOOKMARK_PIC);
				}
				
			}
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
	}

	public  String GetDBPath()
	{
		return mSQLiteDatabase.getPath();
	}
	public void close() {
		try {
			if(mSQLiteDatabase != null && mSQLiteDatabase.isOpen()){
				mSQLiteDatabase.close();
				mSQLiteDatabase = null;
			}
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		sInstance = null;
	}


	
	private BookMark CreateBookMarkByCursor(Cursor cursor){
		BookMark bookmark = null;
		bookmark = new BookMark(
				cursor.getInt	(cursor.getColumnIndex(BOOKMARK_ID)),	 
				cursor.getString(cursor.getColumnIndex(MARK_BOOK_ID)), 
				cursor.getString(cursor.getColumnIndex(MARK_CHAPTER_ID)), 
				cursor.getString(cursor.getColumnIndex(MARK_TEXT)), 
				cursor.getInt(cursor.getColumnIndex(MARK_PERCENT)),
				cursor.getString(cursor.getColumnIndex(BOOKMARK_ID_NET)),
				cursor.getString(cursor.getColumnIndex(BOOKMARK_POS)),
				cursor.getString(cursor.getColumnIndex(BOOKMARK_DATE)),
				cursor.getInt(cursor.getColumnIndex(BOOKMARK_SYNC)),
				cursor.getString(cursor.getColumnIndex(BOOKMARK_CPCODE)),
				cursor.getString(cursor.getColumnIndex(BOOKMARK_RPID))
				);		
		return bookmark;
		
	}
	

	private Category CreateCategoryByCursor(Cursor cursor){
		Category category = null;
		try {
			category = new Category(
					cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)),
					cursor.getString(cursor.getColumnIndex(CATEGORY_CPCODE)),
					cursor.getString(cursor.getColumnIndex(CATEGORY_ID_NET)),
					cursor.getString	(cursor.getColumnIndex(CATEGORY_NAME)),
					cursor.getString	(cursor.getColumnIndex(CATEGORY_PARENT_ID)),
					cursor.getInt(cursor.getColumnIndex(CATEGORY_LEVEL)),
					cursor.getInt(cursor.getColumnIndex(CATEGORY_BOOK_COUNT))
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				e.printStackTrace();
			}
			
		}		
		return category;
	}
	
	private Top CreateTopByCursor(Cursor cursor){
		Top top = null;
		try {
			top = new Top(
					cursor.getInt(cursor.getColumnIndex(TOP_ID)),
					cursor.getString(cursor.getColumnIndex(TOP_CPCODE)),
					cursor.getString(cursor.getColumnIndex(TOP_ID_NET)),
					cursor.getString	(cursor.getColumnIndex(TOP_NAME)),
					cursor.getString	(cursor.getColumnIndex(TOP_DESC))
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				e.printStackTrace();
			}
			
		}		
		return top;
	}
	




	
	
	

	

	

	
	
	

	
	public boolean IsExistsBookMark(String BookMarkIDNet){
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		int count = 0;
		Cursor cursor=null;
		try {
			String sql = "select 1 from "
					+ TABLE_BOOKMARK + " where "+BOOKMARK_ID_NET+"="+BookMarkIDNet;
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor != null ) {
				count =  cursor.getCount();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return count>0;
	}
	
	
	public boolean IsExistsChapterContent(String ChapterContentIDNet,String BookIDNet){
		
		String path = SDCardUtils.GetChaperPath(BookIDNet);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+ChapterContentIDNet + ".toc";
		File filein = new File(name);
		return filein.exists();
	}
	
	
	public boolean IsExistCategory(String CategoryIDNet){
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		int count = 0;
		Cursor cursor=null;
		try {
			String sql = "select 1 from "
					+ TABLE_CATEGORY + " where "+CATEGORY_ID_NET+"="+CategoryIDNet;
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor != null ) {
				count =  cursor.getCount();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return count>0;
	}
	
	public boolean IsExistTop(String TopIDNet){
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		int count = 0;
		Cursor cursor=null;
		try {
			String sql = "select 1 from "
					+ TABLE_TOP + " where "+TOP_ID_NET+"="+TopIDNet;
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor != null ) {
				count =  cursor.getCount();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return count>0;
	}
	
	/**
	 * 获取最新插入的ID
	 * 
	 * @author Max
	 * @Since:2010-11-30
	 * @return
	 */
	public int QueryNewestId(String TableName ,String  Field) {
		int ID = 0;
		Cursor cursor=null;
		try {
			String sql = "select max(" + Field + ") as max_id from "
					+ TableName;
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					ID = cursor.getInt(0);
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return ID;
	}

	public String GetNewestChapterUpdateTime(String BookID)
	{
		String update= "";
		List<Chapter> chapters = GetChapters(BookID);
		if(chapters != null && chapters.size() >0)
		{
			update = chapters.get(chapters.size() -1).updatets;
			chapters.clear();
			chapters = null;
			
		}
		return update;
	}
	

	

	


	

	
	public boolean DeleteBookById(String id) {
		long result = 0;
		if(!DeleteBookMarkByBookId(id))
		{
			LogEx.Log_D("DeleteBookMark", "Delete BookMark Error. BookID:" + id);
		}
//		if(!DeleteChapterByBookId(id))
//		{
//			LogEx.Log_D("DeleteChapter", "Delete Chapter Error.BookID:" + id);
//		}
//		if(!DeleteChapterContentByBookId(id))
//		{
//			LogEx.Log_D("DeleteChapterContent", "Delete ChapterContent Error.BookID:" + id);
//		}
	
		String path = SDCardUtils.GetBookPathEx(id);
		File file = new File(path);
		if(file.exists())
		{
			File[] filesDelete = file.listFiles();
			for(int i = 0 ; i < filesDelete.length ; i++)
			{
				File f = filesDelete[i];
				f.delete();
			}
			file.delete();
		}
		
		return (result > 0);
	}
	
	public boolean DeleteBookMarkById(int id) {
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		long result = 0;
		try {
			result = mSQLiteDatabase.delete(TABLE_BOOKMARK, BOOKMARK_ID + " = '"
					+ id + "'", null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		return (result > 0);
	}
	public boolean DeleteBookMarkByBookId(String id) {
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		long result = 0;
		try {
			result = mSQLiteDatabase.delete(TABLE_BOOKMARK, MARK_BOOK_ID + " = '"
					+ id + "'", null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
			
		}
		return (result > 0);
	}
	
	public boolean DeleteChapterByBookId(String id) {
		
		String path = SDCardUtils.GetChaperPath(id);
		File file = new File(path);
		if(file.exists())
		{
			File[] filesDelete = file.listFiles();
			for(int i = 0 ; i < filesDelete.length ; i++)
			{
				File f = filesDelete[i];
				f.deleteOnExit();
			}
			file.deleteOnExit();
		}
		return true;
	}
	
	public boolean DeleteChapterContentByBookId(String id) {
		String path = SDCardUtils.GetChaperPath(id);
		File file = new File(path);
		if(file.exists())
		{
			File[] filesDelete = file.listFiles();
			for(int i = 0 ; i < filesDelete.length ; i++)
			{
				File f = filesDelete[i];
				f.deleteOnExit();
			}
			file.deleteOnExit();
		}
		return true;
	}
	
	
	public boolean DeleteBookMarkAll() {
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		long result = 0;
		try {
			result = mSQLiteDatabase.delete(TABLE_BOOKMARK, null, null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		return (result > 0);
	}

	
	public boolean InsertBookMark(BookMark bookmark) {
		
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		//事务开始
		mSQLiteDatabase.beginTransaction();
		
		try {
			ContentValues initialValues = new ContentValues();
			
			initialValues.put(MARK_BOOK_ID, bookmark.Book_ID_Net);
			initialValues.put(MARK_CHAPTER_ID, bookmark.Chapter_ID_Net);
			initialValues.put(MARK_TEXT, bookmark.Mark_Text);			
			initialValues.put(MARK_PERCENT, bookmark.Percent);
			initialValues.put(BOOKMARK_ID_NET, bookmark.BookMarkIDNet);
			initialValues.put(BOOKMARK_POS, bookmark.Pos);
			initialValues.put(BOOKMARK_DATE, bookmark.Date);
			initialValues.put(BOOKMARK_SYNC, bookmark.Sync);
			initialValues.put(BOOKMARK_CPCODE, bookmark.cpcode);
			initialValues.put(BOOKMARK_RPID, bookmark.rpid);
	
			long count = mSQLiteDatabase.insert(TABLE_BOOKMARK, BOOKMARK_ID,
					initialValues);
			initialValues.clear();
			initialValues = null;
			
			//插入目录失败，则回滚事务
			int Id = QueryNewestId(TABLE_BOOKMARK,BOOKMARK_ID);
			bookmark.BookMark_ID = Id;			
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
			return count > 0;
		} catch (Exception e) {
			mSQLiteDatabase.endTransaction();
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
			return false;
		}
	}
	
	
	public boolean UpdateBookMark(BookMark bookmark)
	{		
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
		    return false;
		int reti = 0;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(MARK_BOOK_ID, bookmark.Book_ID_Net);
			initialValues.put(MARK_CHAPTER_ID, bookmark.Chapter_ID_Net);
			initialValues.put(MARK_TEXT, bookmark.Mark_Text);			
			initialValues.put(MARK_PERCENT, bookmark.Percent);
			initialValues.put(BOOKMARK_ID_NET, bookmark.BookMark_ID);
			initialValues.put(BOOKMARK_POS, bookmark.Pos);
			initialValues.put(BOOKMARK_DATE, bookmark.Date);
			
			reti = mSQLiteDatabase.update(
					TABLE_BOOKMARK,
					initialValues,
					BOOKMARK_ID + "=? ",
					new String[] { "" + bookmark.BookMark_ID});
			
		} catch (Exception e) {
			e.printStackTrace();
			reti = 0;
		}
		return (reti > 0);
	}
	
	public boolean UpdateBookMarkSync(int BookMarkID,int status)
	{
		
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		int reti = 0;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(BOOKMARK_SYNC, status);
			
			reti = mSQLiteDatabase.update(
					TABLE_BOOKMARK,
					initialValues,
					BOOKMARK_ID + "=? ",
					new String[] { "" + BookMarkID});
			
		} catch (Exception e) {
			e.printStackTrace();
			reti = 0;
		}
		return (reti > 0);
	}
	
	public boolean UpdateBookMarkIDNet(int BookMarkID,String BookIDNet)
	{
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return false;
		int reti = 0;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(BOOKMARK_ID_NET, BookIDNet);
			
			reti = mSQLiteDatabase.update(
					TABLE_BOOKMARK,
					initialValues,
					BOOKMARK_ID + "=? ",
					new String[] { "" + BookMarkID});
			
		} catch (Exception e) {
			e.printStackTrace();
			reti = 0;
		}
		return (reti > 0);
	}
	
	
//	public boolean InsertChapter(Chapter chapter,String BookIDNet) {
//		try {
//			Thread.sleep(30);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		//事务开始
//		if(IsExistsChapter(chapter.ChapterIDNet,BookIDNet))
//			return false;
//			
//		mSQLiteDatabase.beginTransaction();
//		
//		try {
//			ContentValues initialValues = new ContentValues();
//			
//			initialValues.put(CH_BOOK_ID, chapter.BookIDNet);
//			initialValues.put(CHAPTER_NAME, chapter.ChapterName);
//			initialValues.put(CHAPTER_ID_NET, chapter.ChapterIDNet);			
//			initialValues.put(CHAPTER_WORD_COUNT, chapter.WordCount);
//			initialValues.put(CHAPTER_PRICE, chapter.Price);
//			initialValues.put(CHAPTER_FREE, chapter.bFree);
//			initialValues.put(CHAPTER_DOWNLOAD, chapter.bDownLoad);		
//			initialValues.put(CHAPTER_READ, chapter.bRead);	
//			initialValues.put(CHAPTER_UPDATE_TIME, chapter.updatets);	
//			initialValues.put(CHAPTER_VOLUME_NAME, chapter.VolumeName);	
//			long count = mSQLiteDatabase.insert(TABLE_CHAPTER, CHAPTER_ID,
//					initialValues);
//			initialValues.clear();
//			initialValues = null;
//			
//			//插入目录失败，则回滚事务
//			int Id = QueryNewestId(TABLE_CHAPTER,CHAPTER_ID);
//			chapter.ChapterID = Id;			
//			mSQLiteDatabase.setTransactionSuccessful();
//			mSQLiteDatabase.endTransaction();
//			return count > 0;
//		} catch (Exception e) {
//			mSQLiteDatabase.endTransaction();
//			if (Configuration.DEBUG_VERSION) 
//			{
//				e.printStackTrace();
//			}
//			return false;
//		}
//	}
	
	public int InsertChapterEx(final NetChapter netChapter ,final String BookIDNet)
	{
		int nCount = 0;
		synchronized (SYNCCHAPTER) {
			
			List<Chapter> chaptersold = GetChapters(BookIDNet);
			HashMap<String, Chapter> map = GetChapterMap(BookIDNet);
			for(int i = 0 ; i < netChapter.mChapterInfoList.size();i++)
			{
				NetChapter.ChapterInfo chapterInfo = netChapter.mChapterInfoList.get(i);
				
				if(!map.containsKey(chapterInfo.chapterid))
				{
					Chapter chapter = new Chapter();
					chapter.BookIDNet = netChapter.bookid;
					chapter.ChapterName = chapterInfo.chaptername;
					chapter.ChapterIDNet = chapterInfo.chapterid;
					chapter.WordCount = chapterInfo.wordcount;
					chapter.Price = chapterInfo.price;
					chapter.bFree = chapterInfo.isfree;
					chapter.bDownLoad = 0;
					chapter.bRead = 0 ;
					chapter.VolumeName = chapterInfo.VolumeName;
					chapter.updatets = chapterInfo.updatets;
					chaptersold.add(chapter);
					nCount++;
				}

				
		    }
			if(nCount >0)
			  SaveChapters(chaptersold,BookIDNet);
			
			chaptersold.clear();
			chaptersold = null;
			map.clear();
			map = null;
			
			
		}

		
		
		return nCount;
	}
	
	public int InsertChapter(final NetChapter netChapter ,final String BookIDNet,final boolean bClearData)
	{
		int nCount = 0;
		synchronized (SYNCCHAPTER)
		{
			
			List<Chapter> chaptersold = GetChapters(BookIDNet);
			HashMap<String, Chapter> map = GetChapterMap(BookIDNet);
			for(int i = 0 ; i < netChapter.mChapterInfoList.size();i++)
			{
				NetChapter.ChapterInfo chapterInfo = netChapter.mChapterInfoList.get(i);
				
				if(!map.containsKey(chapterInfo.chapterid))
				{
					
					Chapter chapter = new Chapter();
					chapter.BookIDNet = netChapter.bookid;
					chapter.ChapterName = chapterInfo.chaptername;
					chapter.ChapterIDNet = chapterInfo.chapterid;
					chapter.WordCount = chapterInfo.wordcount;
					chapter.Price = chapterInfo.price;
					chapter.bFree = chapterInfo.isfree;
					chapter.bDownLoad = 0;
					chapter.bRead = 0 ;
					chapter.VolumeName = chapterInfo.VolumeName;
					chapter.updatets = chapterInfo.updatets;
					chaptersold.add(chapter);
					nCount++;
				}

				
		    }
			if(nCount > 0)
			  SaveChapters(chaptersold,BookIDNet);
			
			if(bClearData)
			{
				netChapter.mChapterInfoList.clear();
				netChapter.mChapterInfoList = null;
			}
			chaptersold.clear();
			chaptersold = null;
			map.clear();
			map = null;
		
		}		
		return nCount;
	}
	
	public boolean InsertChapter(final Chapter chapter,final String BookIDNet)
	{
		synchronized (SYNCCHAPTER) {
			LogEx.Log_I("chapter", chapter.ChapterName);
			List<Chapter> chaptersold = GetChapters(BookIDNet);
			chaptersold.add(chapter);
			SaveChapters(chaptersold,BookIDNet);
			chaptersold.clear();
			chaptersold = null;
		}
		return true;
	}
	
	public int InsertChapter(final List<Chapter> chapterList ,final String BookIDNet,final boolean bClearData) {
		
		int nCount = 0;
		synchronized (SYNCCHAPTER){
			
			
			List<Chapter> chaptersold = GetChapters(BookIDNet);
			HashMap<String, Chapter> map = GetChapterMap(BookIDNet);
			for(int i = 0 ; i < chapterList.size();i++)
			{
				Chapter chapter = chapterList.get(i);
				
				if(!map.containsKey(chapter.ChapterIDNet))
				{
					nCount++;
					chaptersold.add(chapter);
				}

				
		    }
			if(nCount >0)
			   SaveChapters(chaptersold,BookIDNet);
			if(bClearData)
				chapterList.clear();
			
			chaptersold.clear();
			chaptersold = null;
			map.clear();
			map = null;
		
		}
		return nCount;
	}
	
	
	public List<Chapter> QueryChapterByBookID(String BookID)
	{

		synchronized (SYNCCHAPTER) {

			return GetChapters(BookID);
		}
	}
	
	public HashMap<String,Chapter> QueryChapterMapByBookID(String BookID,int ChapterID)
	{
		synchronized (SYNCCHAPTER) 
		{
			return  GetChapterMap(BookID);
		}

	}
	
	public HashMap<String,Chapter> QueryChapterMapByBookID(String BookID)
	{
		synchronized (SYNCCHAPTER)
		{
			return  GetChapterMap(BookID);

		}
	}
	
	public List<Chapter> QueryUnReadChapterByBookID(String BookID)
	{
		synchronized (SYNCCHAPTER){
		return GetUnReadChapter(BookID);
		}
	}

	public int GetChapterCount(String BookIDNet)
	{
		return GetChapterCountxml(BookIDNet);
	}
	
	public int GetUnreadChapterCount(String BookIDNet)
	{
		return GetUnReadChapterCount(BookIDNet);
	}
	public boolean UpdateChapterRead(String BookIDNet,String ChapterIDNet)
	{
		List<Chapter> chapters =  GetChapters(BookIDNet);
		for(int i = 0 ; i < chapters.size(); i++ )
		{
		     Chapter chapter = chapters.get(i);
		     if(chapter.ChapterIDNet.compareTo(ChapterIDNet)==0 )
		     {
		    	 chapter.bRead = 1;
		    	 break;
		     }
		}
		SaveChapters(chapters, BookIDNet);
		return true;
	}
	
	
	public List<BookMark> QueryBookMarkByBookID(String BookIDNet)
	{
		List<BookMark> BookMarkList = new ArrayList<BookMark>();
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return BookMarkList;
		
		String[] columns = { BOOKMARK_ID, MARK_BOOK_ID, MARK_CHAPTER_ID,
				MARK_TEXT, MARK_PERCENT,BOOKMARK_ID_NET,BOOKMARK_POS,BOOKMARK_DATE ,BOOKMARK_SYNC
				,BOOKMARK_CPCODE,BOOKMARK_RPID};
		String[] params = {BookIDNet,"2"};
		String where = MARK_BOOK_ID + " = ? and " + BOOKMARK_SYNC + " != ?";
		Cursor cursor = mSQLiteDatabase.query(TABLE_BOOKMARK, columns,
				where, params, null, null, BOOKMARK_DATE+" DESC");

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				 BookMark bookMark = CreateBookMarkByCursor(cursor);
				 BookMarkList.add(bookMark);
			} while (cursor.moveToNext());

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}	
		return BookMarkList;
	}
	
	public List<BookMark> QueryChapterBookMarkByBookID(String BookIDNet,String ChapterIDNet)
	{
		List<BookMark> BookMarkList = new ArrayList<BookMark>();
		String[] columns = { BOOKMARK_ID, MARK_BOOK_ID, MARK_CHAPTER_ID,
				MARK_TEXT, MARK_PERCENT,BOOKMARK_ID_NET,BOOKMARK_POS,BOOKMARK_DATE ,BOOKMARK_SYNC,
				BOOKMARK_CPCODE,BOOKMARK_RPID};
		String[] params = {BookIDNet,"2",ChapterIDNet};
		String where = MARK_BOOK_ID + " = ? and " + BOOKMARK_SYNC + " != ? and " + MARK_CHAPTER_ID + " = ?";
		Cursor cursor = mSQLiteDatabase.query(TABLE_BOOKMARK, columns,
				where, params, null, null, BOOKMARK_DATE+" DESC");

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				 BookMark bookMark = CreateBookMarkByCursor(cursor);
				 BookMarkList.add(bookMark);
			} while (cursor.moveToNext());

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}	
		return BookMarkList;
	}
	
	
	public List<BookMark> QueryBookMarkUnSync()
	{
		List<BookMark> BookMarkList = new ArrayList<BookMark>();
		if(mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
			return BookMarkList;
		String[] columns = { BOOKMARK_ID, MARK_BOOK_ID, MARK_CHAPTER_ID,
				MARK_TEXT, MARK_PERCENT,BOOKMARK_ID_NET,BOOKMARK_POS,BOOKMARK_DATE,BOOKMARK_SYNC,BOOKMARK_CPCODE,BOOKMARK_RPID };
		String[] params = {"local","0"};
		String where = BOOKMARK_CPCODE + " != ? and " +  BOOKMARK_SYNC + " != ?";
		Cursor cursor = mSQLiteDatabase.query(TABLE_BOOKMARK, columns,
				where, params, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				 BookMark bookMark = CreateBookMarkByCursor(cursor);
				 BookMarkList.add(bookMark);
			} while (cursor.moveToNext());

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}	
		return BookMarkList;
	}
	
	
	public boolean InsertChapterContent(ChapterContent chaptercontent,int BookType) {
		
		SaveChapcontent(chaptercontent, BookType);
		return true;
	}

	public ChapterContent GetChapterContenByChapterID(String chapterid,String BookIDNet,int BookType)
	{
		return GetChapterContent(BookIDNet,chapterid,BookType);
	}
	
	public boolean InsertCategory(Category category) {
		
		//事务开始
		mSQLiteDatabase.beginTransaction();
		
		try {
			ContentValues initialValues = new ContentValues();
			
			initialValues.put(CATEGORY_CPCODE, category.cpcode);
			initialValues.put(CATEGORY_ID_NET, category.categoryid);
			initialValues.put(CATEGORY_NAME, category.categoryname);
			
			initialValues.put(CATEGORY_PARENT_ID, category.parentid);
			initialValues.put(CATEGORY_LEVEL, category.level);
			initialValues.put(CATEGORY_BOOK_COUNT, category.bookcount);
				
			long count = mSQLiteDatabase.insert(TABLE_CATEGORY, CATEGORY_ID,
					initialValues);
			initialValues.clear();
			initialValues = null;
			
			//插入目录失败，则回滚事务
			int Id = QueryNewestId(TABLE_CATEGORY,CATEGORY_ID);
			category.CategoryIdDB = Id;			
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
			return count > 0;
		} catch (Exception e) {
			mSQLiteDatabase.endTransaction();
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public int InsertCategory(List<Category> categorys) {
		
		//事务开始
		int nCount = 0;
		mSQLiteDatabase.beginTransaction();
		for(int i = 0 ; i < categorys.size() ; i ++)
		{
			Category category = categorys.get(i);
			try {
				ContentValues initialValues = new ContentValues();
				
				initialValues.put(CATEGORY_CPCODE, category.cpcode);
				initialValues.put(CATEGORY_ID_NET, category.categoryid);
				initialValues.put(CATEGORY_NAME, category.categoryname);
				
				initialValues.put(CATEGORY_PARENT_ID, category.parentid);
				initialValues.put(CATEGORY_LEVEL, category.level);
				initialValues.put(CATEGORY_BOOK_COUNT, category.bookcount);
					
				long count = mSQLiteDatabase.insert(TABLE_CATEGORY, CATEGORY_ID,
						initialValues);
				initialValues.clear();
				initialValues = null;
				
				//插入目录失败，则回滚事务
				int Id = QueryNewestId(TABLE_CATEGORY,CATEGORY_ID);
				category.CategoryIdDB = Id;		
				nCount++;

			} catch (Exception e) {
				mSQLiteDatabase.endTransaction();
				if (Configuration.DEBUG_VERSION) 
				{
					e.printStackTrace();
				}
				break;
			}
			
		}
		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;
	}
	
	
	public List<Category> QueryAllCategory() {
		List<Category> CategoryList = new ArrayList<Category>();
		Cursor cursor=null;
		try {
			String[] columns = {CATEGORY_ID,CATEGORY_CPCODE,CATEGORY_ID_NET,CATEGORY_NAME,
					CATEGORY_PARENT_ID,CATEGORY_LEVEL,CATEGORY_BOOK_COUNT};
			String where = "";
			cursor = mSQLiteDatabase.query(TABLE_CATEGORY, columns, where,
					null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					Category category = null;
					category=CreateCategoryByCursor(cursor);
					if(category!=null){
						CategoryList.add(category);
					}
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return CategoryList;
	}
	
	public boolean UpdateCategory(Category category)
	{
	
		ContentValues initialValues = new ContentValues();
		initialValues.put(CATEGORY_CPCODE, category.cpcode);
		initialValues.put(CATEGORY_NAME, category.categoryname);		
		initialValues.put(CATEGORY_PARENT_ID, category.parentid);
		initialValues.put(CATEGORY_LEVEL, category.level);
		initialValues.put(CATEGORY_BOOK_COUNT, category.bookcount);
		int nCount = mSQLiteDatabase.update(TABLE_CATEGORY, initialValues,
				CATEGORY_ID_NET + "=" + category.categoryid, null);
		return nCount > 0;
	}
	
	
	public boolean InsertTop(Top top) {
		
		//事务开始
		mSQLiteDatabase.beginTransaction();
		
		try {
			ContentValues initialValues = new ContentValues();
			
			initialValues.put(TOP_CPCODE, top.cpcode);
			initialValues.put(TOP_ID_NET, top.toplistid);
			initialValues.put(TOP_NAME, top.toplistname);
			initialValues.put(TOP_DESC, top.toplistdesc);	
			long count = mSQLiteDatabase.insert(TABLE_TOP, TOP_ID,
					initialValues);
			initialValues.clear();
			initialValues = null;
			
			//插入目录失败，则回滚事务
			int Id = QueryNewestId(TABLE_TOP,TOP_ID);
			top.TopIdDB = Id;			
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
			return count > 0;
		} catch (Exception e) {
			mSQLiteDatabase.endTransaction();
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public int InsertTop(List<Top> tops) {
		
		//事务开始
		int nCount = 0;
		mSQLiteDatabase.beginTransaction();
		for(int i = 0 ; i< tops.size();i++)
		{
			Top top = tops.get(i);
			try {
				ContentValues initialValues = new ContentValues();
				
				initialValues.put(TOP_CPCODE, top.cpcode);
				initialValues.put(TOP_ID_NET, top.toplistid);
				initialValues.put(TOP_NAME, top.toplistname);
				initialValues.put(TOP_DESC, top.toplistdesc);	
				long count = mSQLiteDatabase.insert(TABLE_TOP, TOP_ID,
						initialValues);
				initialValues.clear();
				initialValues = null;
				
				//插入目录失败，则回滚事务
				int Id = QueryNewestId(TABLE_TOP,TOP_ID);
				top.TopIdDB = Id;	
				nCount++;

			} catch (Exception e) {
				mSQLiteDatabase.endTransaction();
				if (Configuration.DEBUG_VERSION) 
				{
					e.printStackTrace();
				}
				break;
			}
		}
		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;

	}
	
	public List<Top> QueryAllTop() {
		List<Top> TopList = new ArrayList<Top>();
		Cursor cursor=null;
		try {
			String[] columns = {TOP_ID,TOP_CPCODE,TOP_ID_NET,TOP_NAME,TOP_DESC};
			String where = "";
			cursor = mSQLiteDatabase.query(TABLE_TOP, columns, where,
					null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					Top top = null;
					top=CreateTopByCursor(cursor);
					if(top!=null){
						TopList.add(top);
					}
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return TopList;
	}
	
	public boolean UpdateTop(Top top)
	{
	
		ContentValues initialValues = new ContentValues();
		initialValues.put(TOP_CPCODE, top.cpcode);
		initialValues.put(TOP_NAME, top.toplistname);
		initialValues.put(TOP_DESC, top.toplistdesc);	
		int nCount = mSQLiteDatabase.update(TABLE_TOP, initialValues,
				TOP_ID_NET + "=" + top.toplistid, null);
		return nCount > 0;
	}
	
	public boolean InsertSearchKey(String key) {
		
		//事务开始
		mSQLiteDatabase.beginTransaction();
		
		try {
			ContentValues initialValues = new ContentValues();
			
			initialValues.put(SEARCH_KEY, key);

			long count = mSQLiteDatabase.insert(TABLE_SEARCH_KEY, null,
					initialValues);
			initialValues.clear();
			initialValues = null;
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
			return count > 0;
		} catch (Exception e) {
			mSQLiteDatabase.endTransaction();
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public int InsertSearchKey(List<String> keys) {

		// 事务开始
		mSQLiteDatabase.beginTransaction();
		int nCount = 0;
		for(int i = 0 ; i<keys.size();i++)
		{
			String key = keys.get(i);
			try {
				ContentValues initialValues = new ContentValues();

				initialValues.put(SEARCH_KEY, key);

				mSQLiteDatabase.insert(TABLE_SEARCH_KEY, null,
						initialValues);
				initialValues.clear();
				initialValues = null;
				nCount++;

			} catch (Exception e) {
				mSQLiteDatabase.endTransaction();
				if (Configuration.DEBUG_VERSION) {
					e.printStackTrace();
				}


			}
		}


		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;
	}
	
	public List<String> QuerySearchKeys() {
		List<String> KeyList = new ArrayList<String>();
		Cursor cursor=null;
		try {
			String[] columns = {SEARCH_KEY};
			String where = "";
			cursor = mSQLiteDatabase.query(TABLE_SEARCH_KEY, columns, where,
					null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String key = cursor.getString(cursor.getColumnIndex(SEARCH_KEY));
					if(key!=null){
						KeyList.add(key);
					}
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return KeyList;
	}
	
	public boolean DeleteAllSearchKey()
	{
		long result = 0;
		try {
			result = mSQLiteDatabase.delete(TABLE_SEARCH_KEY, null, null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		return (result > 0);
	}
	
	public int InsertAdverts(List<Advert> AdvertList ,int type) {
		
		//事务开始
		int nCount = 0;
		try {
			nCount = mSQLiteDatabase.delete(TABLE_ADVERT_BOOK, ADVRT_BOOK_TYPE + " = '"
					+ type + "'", null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		
		mSQLiteDatabase.beginTransaction();
		nCount = 0;
		for(int i = 0 ; i < AdvertList.size() ; i++)
		{
			Advert advert = AdvertList.get(i);

				try {
					ContentValues initialValues = new ContentValues();
					
					initialValues.put(ADVRT_BOOK_SEQNO, advert.SeqNo);
					initialValues.put(ADVRT_BOOK_IMAGEURL, advert.ImgUrl);
					initialValues.put(ADVRT_BOOK_BOOKID, advert.bookid);			
					initialValues.put(ADVRT_BOOK_BOOKNAME, advert.bookname);
					initialValues.put(ADVRT_BOOK_RPID, advert.rpid);
					initialValues.put(ADVRT_BOOK_CPCODE, advert.cpcode);
					initialValues.put(ADVRT_BOOK_TITTLE, advert.title);		
					initialValues.put(ADVRT_BOOK_UPDATES, advert.updatets);	
					initialValues.put(ADVRT_BOOK_POS, advert.Pos);	
					initialValues.put(ADVRT_BOOK_STATUS, advert.Status);	
					initialValues.put(ADVRT_BOOK_TYPE, type);	
					
					long count = mSQLiteDatabase.insert(TABLE_ADVERT_BOOK, ADVRT_BOOK_ID,
							initialValues);
					initialValues.clear();
					initialValues = null;
					

					
					nCount++;
				} catch (Exception e) {
					mSQLiteDatabase.endTransaction();
					if (Configuration.DEBUG_VERSION) 
					{
						e.printStackTrace();
					}
					break;
				}
		}

		

		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;
	}
	
	public List<Advert> QueryAdvert( int type) {
		List<Advert> AdvertList = new ArrayList<Advert>();
		Cursor cursor=null;
		try {
			String[] columns = {ADVRT_BOOK_SEQNO,ADVRT_BOOK_IMAGEURL,ADVRT_BOOK_BOOKID,ADVRT_BOOK_BOOKNAME,
					ADVRT_BOOK_RPID,ADVRT_BOOK_CPCODE,ADVRT_BOOK_TITTLE,ADVRT_BOOK_UPDATES,ADVRT_BOOK_POS,
					ADVRT_BOOK_STATUS};
			String[] params = {Integer.toString(type)};
			String where = ADVRT_BOOK_TYPE + " = ?";
			cursor = mSQLiteDatabase.query(TABLE_ADVERT_BOOK, columns, where,
					params, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					Advert advert = new Advert();
					advert.SeqNo = cursor.getInt(cursor.getColumnIndex(ADVRT_BOOK_SEQNO));
					advert.ImgUrl = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_IMAGEURL));
					advert.bookid = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_BOOKID));
					advert.bookname = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_BOOKNAME));
					advert.rpid = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_RPID));
					advert.cpcode = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_CPCODE));
					advert.title = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_TITTLE));
					advert.updatets = cursor.getString(cursor.getColumnIndex(ADVRT_BOOK_UPDATES));
					advert.Pos = cursor.getInt(cursor.getColumnIndex(ADVRT_BOOK_POS));
					advert.Status = cursor.getInt(cursor.getColumnIndex(ADVRT_BOOK_STATUS));
					AdvertList.add(advert);
					
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return AdvertList;
	}
	
	public int InsertBookMarkAdverts(List<BookMarkFactory.BookMarkAdvert> AdvertList) {
		
		//事务开始
		int nCount = 0;
		try {
			nCount = mSQLiteDatabase.delete(TABLE_ADVERT_BOOKMARK, null, null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		
		mSQLiteDatabase.beginTransaction();
		nCount = 0;
		for(int i = 0 ; i < AdvertList.size() ; i++)
		{
			BookMarkFactory.BookMarkAdvert advert = AdvertList.get(i);

				try {
					ContentValues initialValues = new ContentValues();
					
					initialValues.put(ADVRT_BOOKMARK_SUBJECTID, advert.subjectid);
					initialValues.put(ADVRT_BOOKMARK_NAME, advert.subjectname);
					initialValues.put(ADVRT_BOOKMARK_IMAGE, advert.subjectImgUrl);			

					
					long count = mSQLiteDatabase.insert(TABLE_ADVERT_BOOKMARK, ADVRT_BOOKMARK_ID,
							initialValues);
					initialValues.clear();
					initialValues = null;
					nCount++;
				} catch (Exception e) {
					mSQLiteDatabase.endTransaction();
					if (Configuration.DEBUG_VERSION) 
					{
						e.printStackTrace();
					}
					break;
				}
		}
		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;
	}
	
	
	public List<BookMarkFactory.BookMarkAdvert> QueryAdvertBookmark() {
		List<BookMarkFactory.BookMarkAdvert> AdvertList = new ArrayList<BookMarkFactory.BookMarkAdvert>();
		Cursor cursor=null;
		try {
			String[] columns = {ADVRT_BOOKMARK_SUBJECTID,ADVRT_BOOKMARK_NAME,ADVRT_BOOKMARK_IMAGE};
			cursor = mSQLiteDatabase.query(TABLE_ADVERT_BOOKMARK, columns, null,
					null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					BookMarkFactory.BookMarkAdvert advert = new BookMarkFactory.BookMarkAdvert();
					advert.subjectid = cursor.getString(cursor.getColumnIndex(ADVRT_BOOKMARK_SUBJECTID));
					advert.subjectname = cursor.getString(cursor.getColumnIndex(ADVRT_BOOKMARK_NAME));
					advert.subjectImgUrl = cursor.getString(cursor.getColumnIndex(ADVRT_BOOKMARK_IMAGE));
					AdvertList.add(advert);
					
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return AdvertList;
	}
	
	public int InsertBookMarkPIC(List<BookMarkFactory.BookMarkPIC> AdvertList,String SubjectID) {
		
		//事务开始
		int nCount = 0;
		try {
			nCount=  mSQLiteDatabase.delete(TABLE_BOOKMARK_PIC, BOOKMARK_PIC_SUBJECTID + " = "
						+ SubjectID , null);
		} catch (Exception e) {
			if (Configuration.DEBUG_VERSION) 
			{
				e.printStackTrace();
			}
		}
		
		mSQLiteDatabase.beginTransaction();
		nCount = 0;
		for(int i = 0 ; i < AdvertList.size() ; i++)
		{
			BookMarkFactory.BookMarkPIC pic = AdvertList.get(i);

				try {
					ContentValues initialValues = new ContentValues();
					initialValues.put(BOOKMARK_PIC_SUBJECTID,SubjectID);
					initialValues.put(BOOKMARK_PIC_ID_NET, pic.bookmarkid);
					initialValues.put(BOOKMARK_PIC_DEC, pic.textdesc);
					initialValues.put(BOOKMARK_PIC_TITTLE, pic.title);	
					initialValues.put(BOOKMARK_PIC_IMAGE, pic.ImgUrl);
					initialValues.put(BOOKMARK_PIC_FREE, pic.isFree);
					initialValues.put(BOOKMARK_PIC_PRICE, pic.price);	

					
					long count = mSQLiteDatabase.insert(TABLE_BOOKMARK_PIC, BOOKMARK_PIC_ID,
							initialValues);
					initialValues.clear();
					initialValues = null;
					nCount++;
				} catch (Exception e) {
					mSQLiteDatabase.endTransaction();
					if (Configuration.DEBUG_VERSION) 
					{
						e.printStackTrace();
					}
					break;
				}
		}
		mSQLiteDatabase.setTransactionSuccessful();
		mSQLiteDatabase.endTransaction();
		return nCount;
	}
	
	
	public List<BookMarkFactory.BookMarkPIC> QueryBookMarkPic( String  subjectid) {
		List<BookMarkFactory.BookMarkPIC> PicList = new ArrayList<BookMarkFactory.BookMarkPIC>();
		Cursor cursor=null;
		try {
			String[] columns = {BOOKMARK_PIC_ID_NET,BOOKMARK_PIC_DEC,BOOKMARK_PIC_TITTLE
					,BOOKMARK_PIC_IMAGE,BOOKMARK_PIC_FREE,BOOKMARK_PIC_PRICE};
			String[] params = {subjectid};
			String where = BOOKMARK_PIC_SUBJECTID + " = ?";
			cursor = mSQLiteDatabase.query(TABLE_BOOKMARK_PIC, columns, where,
					params, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					BookMarkFactory.BookMarkPIC pic = new BookMarkFactory.BookMarkPIC();
					pic.bookmarkid = cursor.getString(cursor.getColumnIndex(BOOKMARK_PIC_ID_NET));
					pic.textdesc = cursor.getString(cursor.getColumnIndex(BOOKMARK_PIC_DEC));
					pic.title = cursor.getString(cursor.getColumnIndex(BOOKMARK_PIC_TITTLE));
					pic.ImgUrl = cursor.getString(cursor.getColumnIndex(BOOKMARK_PIC_IMAGE));
					pic.isFree = cursor.getInt(cursor.getColumnIndex(BOOKMARK_PIC_FREE));
					pic.price = cursor.getString(cursor.getColumnIndex(BOOKMARK_PIC_PRICE));

					PicList.add(pic);
					
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return PicList;
	}
	
	private List<Chapter> GetChapters(String BookId)
	{
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File filein = new File(name);
		if(!filein.exists())
			return new ArrayList<Chapter>();
		
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXChapterXmlList han = new SAXChapterXmlList();
			File fileout = new File(name);
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Chapter>();
	}
	private List<Chapter> GetUnReadChapter(String BookId) 
	{
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File filein = new File(name);
		if(!filein.exists())
			return new ArrayList<Chapter>();
		
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXUnReadChapterXmlList han = new SAXUnReadChapterXmlList();
			File fileout = new File(name);
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Chapter>();
	}
	private int GetUnReadChapterCount(String BookId) 
	{
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File filein = new File(name);
		if(!filein.exists())
			return 0;
		
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXUnReadChapterXmlCount han = new SAXUnReadChapterXmlCount();
			File fileout = new File(name);
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private int GetChapterCountxml(String BookId) 
	{
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File filein = new File(name);
		if(!filein.exists())
			return 0;
		
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXReadChapterXmlCount han = new SAXReadChapterXmlCount();
			File fileout = new File(name);
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	private HashMap<String, Chapter>GetChapterMap(String BookId)
	{
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File filein = new File(name);
		if(!filein.exists())
			return new HashMap<String, Chapter>();
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXChapterXmlMap han = new SAXChapterXmlMap();
			File fileout = new File(name);
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Chapter>();
	}
	
	private ChapterContent GetChapterContent(String BookId,String ChapterID,int type)
	{
		ChapterContent content = new ChapterContent();
		
		String path = SDCardUtils.GetChaperPath(BookId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+ChapterID + ".toc";
		File filein = new File(name);
		if(!filein.exists())
			return null;
		
		try {
			FileInputStream inputStream = new FileInputStream(filein);
			
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];
			int nRead =-1;

			while((nRead = inputStream.read(buf)) > 0)
			{
				byteArray.write(buf, 0, nRead);
			}

			


			if(type == 0)
			{
				content.ChapterContent = AESCrypto.decrypt(UrlUtil.USERID, byteArray.toString());
			}
			else
			{
				content.ChapterContent = byteArray.toString();
			}
			byteArray.close();
			byteArray = null;
			inputStream.close();
			inputStream = null;

			content.ChapterIDNet = ChapterID;
			content.BookIDNet = BookId;
		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return content;
		
	}
	public void SaveChapters(List<Chapter> chapters,String BookID)
	{
		String path = SDCardUtils.GetChaperPath(BookID);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+"chapter.xml";
		File fileout = new File(name);
		try {
			FileOutputStream outputStream = new FileOutputStream(fileout);
			OutputStreamWriter writer  =new OutputStreamWriter(outputStream, "UTF-8");
			XmlSerializer serializer = Xml.newSerializer();
			  try {
			        serializer.setOutput(writer);
			        serializer.startDocument("UTF-8", true);
			        serializer.startTag("", "chapters");
			        for(Chapter c : chapters)
			        {
			        	serializer.startTag("", "chapter");
			        	
			        	serializer.attribute("", "BookIDNet", c.BookIDNet);
			        	serializer.attribute("", "ChapterName", c.ChapterName);
			        	serializer.attribute("", "ChapterIDNet", c.ChapterIDNet);
			        	serializer.attribute("", "bFree", Integer.toString(c.bFree));
			        	serializer.attribute("", "WordCount", Integer.toString(c.WordCount));
			        	serializer.attribute("", "bDownLoad", Integer.toString(c.bDownLoad));
			        	serializer.attribute("", "bRead", Integer.toString(c.bRead));
			        	serializer.attribute("", "updatets", c.updatets);
			        	serializer.attribute("", "VolumeName", c.VolumeName);			        	
			        	serializer.endTag("", "chapter");
			        }
			        serializer.endTag("", "chapters");
			        serializer.endDocument();
//			        return writer.toString();
			    }catch (Exception e) {
			    	e.printStackTrace();
				}
			    writer.flush();
			    writer.close();
			    outputStream.flush();
			    outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	private void SaveChapcontent(ChapterContent content,int type)
	{
		String path = SDCardUtils.GetChaperPath(content.BookIDNet);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+content.ChapterIDNet + ".toc";
		File fileout = new File(name);
		try {
			FileOutputStream outputStream = new FileOutputStream(fileout);

			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			if(type == 0)
			{
				String string = AESCrypto.encrypt(UrlUtil.USERID, content.ChapterContent);
				writer.write(string, 0,string.length());
				string = null;
			}
			else
			{
				writer.write(content.ChapterContent, 0,content.ChapterContent.length());
			}
			
			
			writer.flush();
			writer.close();
			outputStream.flush();
			outputStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	private class SAXChapterXmlList extends DefaultHandler {
		private List<Chapter> chapters;
		public  List<Chapter> getChapters()
		{
			return chapters;
		}
		@Override
		public void startDocument() throws SAXException {
			chapters = new ArrayList<Chapter>();
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("chapter"))
			{
				Chapter chapter = new Chapter();
				
	        	chapter.BookIDNet = attributes.getValue("BookIDNet");
	        	chapter.ChapterName = attributes.getValue("ChapterName");
	        	chapter.ChapterIDNet = attributes.getValue("ChapterIDNet");
	        	chapter.WordCount = Integer.parseInt(attributes.getValue("WordCount"));
	        	chapter.bFree = Integer.parseInt(attributes.getValue("bFree"));
	        	chapter.bDownLoad = Integer.parseInt(attributes.getValue("bDownLoad"));
	        	chapter.bRead = Integer.parseInt(attributes.getValue("bRead"));
	        	chapter.updatets = attributes.getValue("updatets");
	        	chapter.VolumeName = attributes.getValue("VolumeName");

				chapters.add(chapter);
			}
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	private class SAXUnReadChapterXmlList extends DefaultHandler {
		private List<Chapter> chapters;
		public  List<Chapter> getChapters()
		{
			return chapters;
		}
		@Override
		public void startDocument() throws SAXException {
			chapters = new ArrayList<Chapter>();
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("chapter"))
			{
				Chapter chapter = new Chapter();
				
	        	chapter.BookIDNet = attributes.getValue("BookIDNet");
	        	chapter.ChapterName = attributes.getValue("ChapterName");
	        	chapter.ChapterIDNet = attributes.getValue("ChapterIDNet");
	        	chapter.WordCount = Integer.parseInt(attributes.getValue("WordCount"));
	        	chapter.bFree = Integer.parseInt(attributes.getValue("bFree"));
	        	chapter.bDownLoad = Integer.parseInt(attributes.getValue("bDownLoad"));
	        	chapter.bRead = Integer.parseInt(attributes.getValue("bRead"));
	        	chapter.updatets = attributes.getValue("updatets");
	        	chapter.VolumeName = attributes.getValue("VolumeName");

	        	if(chapter.bRead == 0)
				   chapters.add(chapter);
			}
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	private class SAXUnReadChapterXmlCount extends DefaultHandler {
		private int count = 0;
		public  int  getChapters()
		{
			return count;
		}
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("chapter"))
			{
				if(Integer.parseInt(attributes.getValue("bRead")) == 0)
				{
					count ++;
				}

			}
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	private class SAXReadChapterXmlCount extends DefaultHandler {
		private int count = 0;
		public  int  getChapters()
		{
			return count;
		}
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("chapter"))
			{
				count ++;

			}
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	private class SAXChapterXmlMap extends DefaultHandler {
		private HashMap<String, Chapter> chapters;
		public  HashMap<String, Chapter>  getChapters()
		{
			return chapters;
		}
		@Override
		public void startDocument() throws SAXException {
			chapters = new HashMap<String, Chapter>();
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("chapter"))
			{
				Chapter chapter = new Chapter();
				
	        	chapter.BookIDNet = attributes.getValue("BookIDNet");
	        	chapter.ChapterName = attributes.getValue("ChapterName");
	        	chapter.ChapterIDNet = attributes.getValue("ChapterIDNet");
	        	chapter.WordCount = Integer.parseInt(attributes.getValue("WordCount"));
	        	chapter.bFree = Integer.parseInt(attributes.getValue("bFree"));
	        	chapter.bDownLoad = Integer.parseInt(attributes.getValue("bDownLoad"));
	        	chapter.bRead = Integer.parseInt(attributes.getValue("bRead"));
	        	chapter.updatets = attributes.getValue("updatets");
	        	chapter.VolumeName = attributes.getValue("VolumeName");

				chapters.put(chapter.ChapterIDNet, chapter);
			}
			super.startElement(uri, localName, qName, attributes);
		}
		
	}
}
