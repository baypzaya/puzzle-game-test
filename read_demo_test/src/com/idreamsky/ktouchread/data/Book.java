package com.idreamsky.ktouchread.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;
import android.util.Xml;
import com.idreamsky.ktouchread.data.net.NetBookMark;
import com.idreamsky.ktouchread.data.net.NetBookShelf;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.SDCardUtils;


public class Book implements Serializable{

	private static final long serialVersionUID = -1211671783691699603L;
	private static final String SyncBook = "syncbook";
	private static final String SyncChase = "syncchase";
	private static final String SyncSync = "syncsync";
	public int Book_ID;
	public String Book_Name;
	public String Author;
	public String pic_Path;
	public String Pic_Url;
	public String Recent_Chapter_ID;
	public String Recent_Chapter;
	public String Recent_Chapter_Name;
	public int bUpdate;                 //0 不追更     ，1追更  2,完结
	public int bFree;                   //1：整本2：章节3：免费
	public List<Chapter> chapters;
	
	public String cpcode;        //CP编号
	public String rpid;          //CP的CP编号
	public String bookidNet;        //书籍编号
	
	public long RecentReadTime;
	
	public String Price;
	public int Sync   ;//         0: 未同步到服务器 。1：已经同步到服务器 2 :删除
	public int BookType;          //0:网络书城的书， 1：本地搜索的书
	public int unreadChapterNumber = -1;
	public String BookToken;

		
//	private List<BookMark> mBookMarkList = null;
	private List<Chapter> mChapterList = null;
	private static List<Book> mBookList = null;
	private static List<Book> mSyncBookList = null;
	private static List<Book> mChaseBookList = null;
	
	
	private HashMap<String,Chapter> mChapterHashMap;
	public Book()
	{
		
		Book_ID = -1;
		Book_Name = "";
		Author = "";
		pic_Path = "";
		Pic_Url = "";
		Recent_Chapter_ID = "";
		Recent_Chapter = "";
		Recent_Chapter_Name = "";
		bUpdate = 0;
		bFree = 0;
		cpcode = "";
		rpid = "";
		bookidNet = "";
		RecentReadTime = 0;
		Price = "";
		Sync = 1;
		BookType = 0;
		BookToken = "";
	}
	
	public boolean OpenBook()
	{
 		if(mChapterList == null)
		{
			mChapterList = BookDataBase.getInstance().QueryChapterByBookID(bookidNet);
		}
// 		if(mBookMarkList!=null)
// 			mBookMarkList.clear();
//		mBookMarkList = null;
//		mBookMarkList = BookDataBase.getInstance().QueryBookMarkByBookID(bookidNet);
		
		return mChapterList.size() > 0;
	}
	public void CloseBook()
	{
		if(mChapterList != null)
		{
			mChapterList.clear();
			mChapterList = null;
		}
		if(mChapterHashMap != null)
		{
			mChapterHashMap.clear();
			mChapterHashMap = null;
		}
		if(chapters!=null)
		{
//			Chapter.SaveChapters(bookidNet, chapters);
			chapters.clear();
			chapters = null;
		}
	}
	public void setUpdateChapter(List<Chapter> chapters)
	{
		this.chapters = chapters;
	}

	public List<Chapter> getUpdateChapter()
	{
		return this.chapters;
	}
	public HashMap<String,Chapter> GetChapterMap()
	{
		if(mChapterHashMap != null)
		{
			mChapterHashMap = BookDataBase.getInstance().QueryChapterMapByBookID(bookidNet);
		}
		
		return mChapterHashMap;
	}
	public boolean UpdateReadTime()
	{
		RecentReadTime = System.currentTimeMillis();
		return true;
	}
	public boolean UpdateRecent()
	{
		return true;
	}
	public boolean UpdateStatus(int nStatus)
	{
		synchronized(SyncChase)
		{
			if(nStatus == 2)
			{
				if(bUpdate == 1)
				{
					DeleteChaseBook(this.bookidNet);
				}
			}else if(nStatus == 1)
			{
				mChaseBookList.add(this);
			}else
			{
				mChaseBookList.remove(this);
			}
		}
		bUpdate = nStatus;

		return true;
	}
	
	private static void  DeleteChaseBook(String BookidNet)
	{
		for(int i = 0 ; i < mChaseBookList.size(); i++)
		{
			Book book = mChaseBookList.get(i);
			if(book.bookidNet.compareTo(BookidNet) == 0)
			{
				mChaseBookList.remove(i);
				break;
			}
		}
	}
	
	public boolean AddBookMark(final BookMark bookMark,String ChapterIDNet)
	{
		if(BookType == 1)
		{
			bookMark.Sync = 0;
//			mBookMarkList.add(bookMark);
			return BookDataBase.getInstance().InsertBookMark(bookMark);
		}
		bookMark.Sync = 1;
		boolean bRes = BookDataBase.getInstance().InsertBookMark(bookMark);
		if(bRes)
		{
//			if(mBookMarkList == null)
//			{
//				mBookMarkList = BookDataBase.getInstance().QueryBookMarkByBookID(bookidNet);
//			}
//			mBookMarkList.add(bookMark);
			if(BookType == 0)
			{
				NetBookMark.Add(cpcode, rpid, bookidNet, ChapterIDNet, bookMark.Pos, bookMark.Mark_Text, "0", new NetBookMark.AddNetBookMarkCallback() {
					
					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						LogEx.Log_V("API","AddBookMarkNet:"+ msg);
					}
					
					@Override
					public void onSuccess(String strMsg) {
						// TODO Auto-generated method stub
						
						String bookmarkidNet = strMsg;
						bookMark.BookMarkIDNet = bookmarkidNet;
						BookDataBase.getInstance().UpdateBookMarkIDNet(bookMark.BookMark_ID, bookmarkidNet);
						BookDataBase.getInstance().UpdateBookMarkSync(bookMark.BookMark_ID,0);
						LogEx.Log_V("API", "AddBookMarkNetID :" + bookmarkidNet);
						
					}
				});
			}	
		}
		return bRes;
	}

	public boolean DeleteBookMark(final BookMark bookMark)
	{
		if(BookType == 1)
		{
//			mBookMarkList.remove(bookMark);
			return BookDataBase.getInstance().DeleteBookMarkById(bookMark.BookMark_ID);
		}
		boolean bRes = BookDataBase.getInstance().UpdateBookMarkSync(bookMark.BookMark_ID,2);
		if(bRes)
		{
//			mBookMarkList.remove(bookMark);
			
			NetBookMark.Delete(bookMark.BookMarkIDNet,  new NetBookMark.DeleteNetBookMarkCallback() {
				
				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					LogEx.Log_V("API", "DeleteBookMarkNet:"+msg);
					
				}
				
				@Override
				public void onSuccess(String strMsg) {
					// TODO Auto-generated method stub
					LogEx.Log_V("API", "DeleteBookMarkNet:"+"Success");
					if(BookDataBase.getInstance().DeleteBookMarkById(bookMark.BookMark_ID))
					{
						LogEx.Log_V("API", "DeleteBookMarkNetFromDB:"+"Success");
					}
					else {
						LogEx.Log_V("API", "DeleteBookMarkNetFromDB:"+"Fail");
					}
					
				}
			});
		}
		else
		{
			LogEx.Log_V("API", "UpdateBookMarkSync:"+"Fail");
		}
		return bRes;
	}

	public List<BookMark> GetBookMarkList()
	{
		return BookDataBase.getInstance().QueryBookMarkByBookID(bookidNet);
	}
	
	public List<BookMark> GetBookMarkList(String ChapterIDNet)
	{
		if(GetBookSyncStatus(bookidNet) == 2)
		{
			return new ArrayList<BookMark>();
		}
		return BookDataBase.getInstance().QueryChapterBookMarkByBookID(bookidNet,ChapterIDNet);
	}
	
	public List<Chapter> GetChapterList()
	{
		if(BookType==1)
		{
			if(mChapterList!=null)
			{
				mChapterList.clear();
				mChapterList = null;
			}
			mChapterList = BookDataBase.getInstance().QueryChapterByBookID(bookidNet);
		}else if(mChapterList == null)
		{
			mChapterList = BookDataBase.getInstance().QueryChapterByBookID(bookidNet);
		}
		
		return mChapterList;
	}
	public void SetChapterList(List<Chapter> chapterList)
	{
		mChapterList = chapterList;
		mChapterHashMap = new HashMap<String, Chapter>();
		for(int i = 0 ; i < mChapterList.size(); i++)
		{
			Chapter chapter = mChapterList.get(i);
			mChapterHashMap.put(chapter.ChapterIDNet, chapter);
		}
	}
	public List<Chapter> GetUnReadChapter()
	{
		return BookDataBase.getInstance().QueryUnReadChapterByBookID(bookidNet);
	}
	
	public int GetUnreadChapterCount()
	{
		return BookDataBase.getInstance().GetUnreadChapterCount(bookidNet);
	}
	public int GetChapterCount()
	{
		return BookDataBase.getInstance().GetChapterCount(bookidNet);
	}
	@SuppressWarnings("unchecked")
	public static List<Book> GetBookList()
	{
		if(mBookList == null)
			return new ArrayList<Book>();
		Collections.sort(mBookList, new BookReadTimeComparator());
		return mBookList;
	}
	public static List<Book> GetBookListEx()
	{
		return mBookList;
	}
	private static class BookReadTimeComparator implements Comparator
	{

		@Override
		public int compare(Object object1, Object object2) {
			// TODO Auto-generated method stub
			Book book1 = (Book) object1;
			Book book2 = (Book) object2;
			if(book1.RecentReadTime < book2.RecentReadTime)
				return 1;
			else if(book1.RecentReadTime > book2.RecentReadTime)
				return -1;
			return 0;
		}
		
	}
    public static List<Book> GetSyncBooks()
    {
    	if(mSyncBookList == null)
    		mSyncBookList = new ArrayList<Book>();
    	return mSyncBookList;
    }
    public static List<Book> GetChaseBooks()
    {
    	if(mChaseBookList == null)
    		mChaseBookList = new ArrayList<Book>();
    	return mChaseBookList;
    }
	public static boolean AddBook(final Book book)
	{
		boolean bRes = false;
		book.RecentReadTime = System.currentTimeMillis();
		if(book.BookType == 1)
		{
			book.Sync = 0;
		    mBookList.add(0, book);
			return true;
		}
		if(!IsExit(book.bookidNet))
		{
			book.Sync = 1;
			mBookList.add(0, book);
			
			NetBookShelf.add(book.cpcode, book.rpid, book.bookidNet,  new NetBookShelf.AddBookCallback() {
				
				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					LogEx.Log_V("API", "AddBookShelfNet :" + msg);
					book.Sync = 0;
					mSyncBookList.add(book);
					
				}
				
				@Override
				public void onSuccess(String strMsg) {
					// TODO Auto-generated method stub
					LogEx.Log_V("API", "AddBookShelfNet:Success");
					
				}
			});
			if(book.GetChapterList()!= null && book.GetChapterList().size() > 0 )
			{
				Book.AddChapterToDB(book.GetChapterList(),book.bookidNet,false);
			}
			else
			{
				NetChapter.GetBookChapter(book.rpid, book.cpcode, book.bookidNet, new NetChapter.GetBookChapterCallback() {
					
					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						LogEx.Log_V("API","GetNetChapterList:"+msg);
						
					}
					
					@Override
					public void onSuccess(NetChapter NetChapterInfo) {
						// TODO Auto-generated method stub
						LogEx.Log_V("API", "GetNetChapterList:Success");
						
						int nCount = Chapter.AddChapter(NetChapterInfo,true);
						LogEx.Log_V("API", "ChapterListToDB Count:" + Integer.toString(nCount));
						
					}
				});
			}

		}
		else if(GetBookSyncStatus(book.bookidNet) == 2)
		{
			
			UpdateBookSyncStatusTime(book.bookidNet,0);
			if(book.GetChapterList()!= null && book.GetChapterList().size() > 0 )
			{
				Book.AddChapterToDB(book.GetChapterList(),book.bookidNet,false);
			}
			else
			{
				NetChapter.GetBookChapter(book.rpid, book.cpcode, book.bookidNet, new NetChapter.GetBookChapterCallback() {
					
					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						LogEx.Log_V("API","GetNetChapterList:"+msg);
						
					}
					
					@Override
					public void onSuccess(NetChapter NetChapterInfo) {
						// TODO Auto-generated method stub
						LogEx.Log_V("API", "GetNetChapterList:Success");
						int nCount = Chapter.AddChapter(NetChapterInfo,true);
						LogEx.Log_V("API", "ChapterListToDB Count:" + Integer.toString(nCount));
						
					}
				});
			}
		}
		
		return bRes;
	}
	public static boolean DeleteBook(final Book book)
	{
		
		boolean bRes = false;
		if(book.BookType == 0)
		{
		    UpdateBookSyncStatus(book.bookidNet,2);
		    bRes = true;
		}
		else {

			synchronized (SyncBook) {
				bRes = mBookList.remove(book);
				BookDataBase.getInstance().DeleteBookById(book.bookidNet);
			}
		}

		return bRes;
	}
	public static boolean DeleteBook(final String bookidnet)
	{
		
		boolean bRes = false;
		synchronized(SyncBook)
		{
			for(int i = 0 ; i < mBookList.size();i++)
			{
				Book book = mBookList.get(i);
				if(book.bookidNet.compareTo(bookidnet) == 0)
				{
					mBookList.remove(i);
					BookDataBase.getInstance().DeleteBookById(bookidnet);
					break;
				}
			}
		}


		return bRes;
	}
	public static boolean IsExit(String BookIdNet)
	{
		if(BookIdNet == null || mBookList == null || BookIdNet.length() < 1)
			return false;
		boolean bExist = false;
		for(Book book:mBookList)
		{
			if(book.bookidNet.compareTo(BookIdNet) == 0)
			{
				bExist = true;
				break;
			}
		}
		return bExist;
	}
	public static int GetBookSyncStatus(String BookIdNet)
	{
		int syncstatuc = 0;
		synchronized (SyncSync)
		{
			if(mSyncBookList == null)
			{
				syncstatuc = 2;
			}
			else {
				for(Book book:mSyncBookList)
				{
					if(book.bookidNet != null && book.bookidNet.compareTo(BookIdNet) == 0)
					{
						syncstatuc = book.Sync;
						break;
					}
				}
			}

		}
		return syncstatuc;
	}
	
	public static boolean UpdateBookSyncStatus(String BookIdNet ,int sync)
	{
		boolean  bSuccess = false;
		synchronized (SyncBook){
			for(int i = 0 ; i < mBookList.size(); i++ )
			{
				Book book = mBookList.get(i);
				if(book.bookidNet.compareTo(BookIdNet) == 0)
				{
					if(sync == 2)
					{
						book.Sync = sync;
						mSyncBookList.add(book);
						mBookList.remove(i);
					}
					else
					{
						book.Sync = sync;
						bSuccess = true;
					}
					break;
				}
			}
		}

		return bSuccess;
	}
	
	public static boolean UpdateBookSyncStatusTime(String BookIdNet ,int sync)
	{
		boolean bSuccess = false;
		synchronized (SyncSync) {
			for (int i = 0; i < mSyncBookList.size(); i++) {
				Book book = mSyncBookList.get(i);
				if (book.bookidNet.compareTo(BookIdNet) == 0) {
					book.Sync = sync;
					book.bUpdate = 1;
					book.RecentReadTime = System.currentTimeMillis();
					bSuccess = true;
					mSyncBookList.remove(i);
					mBookList.add(0, book);
					break;
				}
			}
		}
		return bSuccess;
	}
	
	public static int AddChapterToDB(final List<Chapter> ChapterList,final String BookIDNet,final boolean bClearData)
	{
		new Thread(){
			public void run() {
				BookDataBase.getInstance().InsertChapter(ChapterList,BookIDNet,bClearData);
			};
		}.start();
		return 0;
	}
	public static boolean AddChapterToDB(Chapter chapter,String BookIDNet)
	{
    	return BookDataBase.getInstance().InsertChapter(chapter,BookIDNet);
	}
	public static boolean AddChapterContentToDB(ChapterContent chapterConent,int BookType)
	{
		return BookDataBase.getInstance().InsertChapterContent(chapterConent,BookType);
	}
	public static boolean IsExistByName(String BookName)
	{
		boolean bExist = false;
		synchronized (SyncSync){
			for(Book book:mBookList)
			{
				if(book.Book_Name.compareTo(BookName) == 0)
				{
					bExist = true;
					break;
				}
			}
		}

		return bExist;
	}
	
	public static void SaveBookList(List<Book> books, String FileName)
	{
		String path = SDCardUtils.GetBookListPath();
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+FileName;
		File fileout = new File(name);
		try {
			FileOutputStream outputStream = new FileOutputStream(fileout);
			OutputStreamWriter writer  =new OutputStreamWriter(outputStream, "UTF-8");
			XmlSerializer serializer = Xml.newSerializer();
			  try {
			        serializer.setOutput(writer);
			        serializer.startDocument("UTF-8", true);
			        serializer.startTag("", "books");
			        for(Book c : books)
			        {
			        	serializer.startTag("", "book");
			        	
			        	serializer.attribute("", "Book_Name", c.Book_Name);
			        	serializer.attribute("", "Author", c.Author);
			        	serializer.attribute("", "pic_Path", c.pic_Path);
			        	serializer.attribute("", "Pic_Url", c.Pic_Url);
			        	serializer.attribute("", "Recent_Chapter_ID", c.Recent_Chapter_ID);
			        	serializer.attribute("", "Recent_Chapter", c.Recent_Chapter);
			        	serializer.attribute("", "Recent_Chapter_Name", c.Recent_Chapter_Name);
			        	serializer.attribute("", "bUpdate", Integer.toString(c.bUpdate));
			        	serializer.attribute("", "bFree", Integer.toString(c.bFree));
			        	serializer.attribute("", "cpcode", c.cpcode);
			        	serializer.attribute("", "rpid", c.rpid);
			        	serializer.attribute("", "bookidNet", c.bookidNet);
			        	serializer.attribute("", "RecentReadTime", Long.toString(c.RecentReadTime));
			        	serializer.attribute("", "Price", c.Price);	
			        	serializer.attribute("", "Sync", Integer.toString(c.Sync));
			        	serializer.attribute("", "BookType", Integer.toString(c.BookType ));
			        	
			        	serializer.endTag("", "book");
			        }
			        serializer.endTag("", "books");
			        serializer.endDocument();
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
	private static List<Book> GetBooks(String FileName)
	{
		String path = SDCardUtils.GetBookListPath();
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
		String name = path+FileName;
		File fileout = new File(name);
		if(!fileout.exists())
			return new ArrayList<Book>();
		
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();//创建解析器
			
			SAXBookXmlMap han = new SAXBookXmlMap();
			FileInputStream inputStream = new FileInputStream(fileout);
			saxParser.parse(inputStream, han);
			return han.getChapters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Book>();
	}
	

	
	private static class SAXBookXmlMap extends DefaultHandler {
		private List<Book> Books;
		public  List<Book> getChapters()
		{
			return Books;
		}
		@Override
		public void startDocument() throws SAXException {
			Books = new ArrayList<Book>();
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName.equals("book"))
			{
				Book book = new Book();
				
				book.Book_Name = attributes.getValue("Book_Name");
				book.Author = attributes.getValue("Author");
				book.pic_Path = attributes.getValue("pic_Path");
				book.Pic_Url = attributes.getValue("Pic_Url");
				book.Recent_Chapter_ID = attributes.getValue("Recent_Chapter_ID");
				book.Recent_Chapter = attributes.getValue("Recent_Chapter");
				book.Recent_Chapter_Name = attributes.getValue("Recent_Chapter_Name");
				book.bUpdate = Integer.parseInt(attributes.getValue("bUpdate"));
				book.bFree = Integer.parseInt(attributes.getValue("bFree"));
				book.cpcode = attributes.getValue("cpcode");
				book.rpid = attributes.getValue("rpid");
				book.bookidNet = attributes.getValue("bookidNet");
				book.RecentReadTime = Long.parseLong(attributes.getValue("RecentReadTime"));
				book.Price = attributes.getValue("Price");
				book.Sync  = Integer.parseInt(attributes.getValue("Sync"));
				book.BookType = Integer.parseInt(attributes.getValue("BookType"));
				
				
				Books.add(book);
			}
			super.startElement(uri, localName, qName, attributes);
		}	
	}
	public static HashMap<String,String> GetBookIDMap()
	{
		HashMap<String,String> map = new HashMap<String,String>();
		for (Book book:mBookList)
		{
			map.put(book.bookidNet, book.bookidNet);
		}
		return map;
	}
	public static void Init()
	{
		mBookList = GetBooks("booklist.xml");
		mSyncBookList = GetBooks("syncbooklist.xml");
		mChaseBookList = new ArrayList<Book>();
		InitChase();

	}
	
	public static void InitChase()
	{
		mChaseBookList.clear();
		for(Book book: mBookList)
		{
			if(book.bUpdate == 1)
			{
				mChaseBookList.add(book);
			}
		}
	}
	public static void Save()
	{
		SaveBookList(mBookList,"booklist.xml");
		SaveBookList(mSyncBookList,"syncbooklist.xml");
	}
	public static Book GetBook(String BookIDnet)
	{
		synchronized (SyncBook) {
			
			for(Book book:mBookList)
			{
				if(book.bookidNet.compareTo(BookIDnet) == 0)
					return book;
			}
		}
		return null;
	}
}
