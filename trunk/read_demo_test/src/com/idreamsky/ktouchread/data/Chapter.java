package com.idreamsky.ktouchread.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.db.BookDataBase;



public class Chapter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1634150911214745634L;
	public int ChapterID;
	public String BookIDNet;
	public String ChapterName;
	public String ChapterIDNet;
	public int WordCount;
    public String Price;
	public int bFree = 0;   //1 免费     ，0付费
	public int bDownLoad = 0 ; //0 未下载,1已经下载
	public int bRead = 0;  //0 未阅读，1已经阅读
	
    public String updatets;       //最后更新时间
    public String VolumeName;
	

	
	public Chapter()
	{
		ChapterID = -1;
		BookIDNet = "";
		ChapterName = "";
		ChapterIDNet = null;
		WordCount = 0;
		Price = "";
		bFree = 0;  
		bDownLoad = 0;
		bRead = 0;
		updatets = "";
		VolumeName = "";
	}
	
	public Chapter(int chapterid,String bookidnet,String chaptername,String chapteridnet,
			int worldcount,String price ,int free,int download,int nread,String updatetime,String Volumename)
	{
		ChapterID = chapterid;
		BookIDNet = bookidnet;
		ChapterName = chaptername;
		ChapterIDNet = chapteridnet;
		WordCount = worldcount;
		Price = price;
		bFree = free; 
		bRead = nread;
		bDownLoad = download;
		updatets = updatetime;
		VolumeName = Volumename;
	}
	public boolean UpdateChapterRead(String BookIDNet)
	{
		bRead = 1;
		return true;
	}
	public List<Chapter> GetUnReadChapter(String BookIDNet)
	{
		return BookDataBase.getInstance().QueryUnReadChapterByBookID(BookIDNet);
	}
	public int GetUnreadChapterCount(String BookIDNet)
	{
		return BookDataBase.getInstance().GetUnreadChapterCount(BookIDNet);
	}
	public static void SaveChapters(String BookIDNet,List<Chapter> chapters)
	{
		BookDataBase.getInstance().SaveChapters(chapters, BookIDNet);
	}
	public static int AddChapter(final NetChapter netChapter ,final  boolean bClearData)
	{
		new Thread(){
			public void run() {
				BookDataBase.getInstance().InsertChapter(netChapter,netChapter.bookid, bClearData);
			};
		}.start();
		return 0;

	}
	
	public static List<Chapter> AddChapterEx(final NetChapter netChapter ,final boolean bClearData)
	{
		final List<Chapter> chapterList = new ArrayList<Chapter>();
		for(int i = 0 ; i < netChapter.mChapterInfoList.size();i++)
		{
			NetChapter.ChapterInfo chapterInfo = netChapter.mChapterInfoList.get(i);
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
			
			chapterList.add(chapter);

		}
		new Thread(){
			public void run() {
				BookDataBase.getInstance().InsertChapter(chapterList,netChapter.bookid,bClearData);
			};
		}.start();
		
		return chapterList;
	}
	
}
