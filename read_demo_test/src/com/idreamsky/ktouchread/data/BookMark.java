package com.idreamsky.ktouchread.data;

import java.io.Serializable;

import com.idreamsky.ktouchread.db.BookDataBase;


public class BookMark implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3066989127844318652L;
	public int BookMark_ID;
	public String Book_ID_Net;
	public String Chapter_ID_Net;
	public String Pos;
	public String Mark_Text;
	public int Percent;
	public String BookMarkIDNet;
	public String Date;
	public int Sync ;   //         0: 未同步到服务器 。1：已经同步到服务器
	public String cpcode;
	public String rpid;
	
	public BookMark()
	{
		BookMark_ID = -1;
		Book_ID_Net = "";
		Chapter_ID_Net = "";
		Mark_Text = "";
		Percent = -1;
		Sync = 1;
	}
	public BookMark(int BookMarkID,String BookID,String ChapterID,String text,int percent,
			String bookmarkidnet,String pos,String date,int sync,String Cpcode,String RPID)
	{
		BookMark_ID = BookMarkID;
		Book_ID_Net = BookID;
		Chapter_ID_Net = ChapterID;
		Mark_Text = text;
		Percent = percent;
		BookMarkIDNet = bookmarkidnet;
		Pos = pos;
		Date = date;
		Sync = sync;
		cpcode = Cpcode;
		rpid = RPID;
	}

	public boolean Update() {
		return BookDataBase.getInstance().UpdateBookMark(this);
	}
}
