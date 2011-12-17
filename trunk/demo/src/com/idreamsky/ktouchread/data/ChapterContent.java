package com.idreamsky.ktouchread.data;

import com.idreamsky.ktouchread.data.net.NetChapterContent;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;

public class ChapterContent {
	
//	public int ChapterContentID;
    public String ChapterIDNet;
    public String ChapterContent;
//	public String PreChapterIdNet;
//	public String NextChapterIdNet;
//	public int PreChapterIsFree;
//	public int NextChapterIsFree;
//	public long RecentReadTime;
	public String BookIDNet;
	
	public ChapterContent(int chaptercontentid,String chapteridnet,String content,String prechapteridnet,
			String nextchapteridnet,int prefree,int nextfree,long recentreadtime,String bookIdNet)
	{
//		ChapterContentID = chaptercontentid;
//	    ChapterIDNet = chapteridnet;
//	    ChapterContent = content;
//		PreChapterIdNet = prechapteridnet;
//		NextChapterIdNet = nextchapteridnet;
//		PreChapterIsFree = prefree;
//		NextChapterIsFree = nextfree;
//		RecentReadTime = recentreadtime;
//		BookIDNet = bookIdNet;
	}
	public ChapterContent()
	{
		
	}
//	public static int CountAddChapterContent(List<ChapterContent> ChapterContentList)
//	{
//		return BookDataBase.getInstance().InsertChapterContent(ChapterContentList);
//	}
	public static interface GetChapterContentCallback extends RequestCallback {
		public void onSuccess(ChapterContent chaptercontent);
		public void onFail(ErrorResult result);
	}
	public static boolean IsExitInLoacal(String ChapterNetContentID,String BookIDNet)
	{
		return BookDataBase.getInstance().IsExistsChapterContent(ChapterNetContentID,BookIDNet);
	}
	public static ChapterContent GetChapterContentFromDB(String ChapterNetContentID,String BookIDNet,int BookType)
	{
		return BookDataBase.getInstance().GetChapterContenByChapterID(ChapterNetContentID,BookIDNet,BookType);
	}
	public static void GetChapterContent(final String cpcode,final String rpid,final String bookid,final String chapterid,final GetChapterContentCallback cb)
	{

			NetChapterContent.GetChapterContent(cpcode, rpid, bookid, chapterid, new NetChapterContent.GetChapterContentCallback() {
				
				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					cb.onFail(msg);
				}
				
				@Override
				public void onSuccess(NetChapterContent chaptercontentNet) {
					// TODO Auto-generated method stub
					//ChapterContent;
					ChapterContent chapterConent = new ChapterContent();
					
					chapterConent.ChapterIDNet = chaptercontentNet.ChapterId;
					chapterConent.ChapterContent = chaptercontentNet.ChapterContent;
//					chapterConent.PreChapterIdNet = chaptercontentNet.PreChapterId;
//					chapterConent.NextChapterIdNet = chaptercontentNet.NextChapterId;
//					chapterConent.PreChapterIsFree = chaptercontentNet.PreChapterIdIsFree;
//					chapterConent.NextChapterIsFree = chaptercontentNet.NextChapterIdIsFree;
//					chapterConent.RecentReadTime = System.currentTimeMillis();
					chapterConent.BookIDNet = bookid;
					if(chapterConent.ChapterContent != null && chapterConent.ChapterContent.length() > 0)
					{
						BookDataBase.getInstance().InsertChapterContent(chapterConent,0);
						cb.onSuccess(chapterConent);
					}
					else {
						chapterConent = null;
						cb.onSuccess(null);
					}

				}

				@Override
				public void onFail(ErrorResult result) {
					// TODO Auto-generated method stub
					cb.onFail(result);
					
				}
			});
	}
	
	
}
