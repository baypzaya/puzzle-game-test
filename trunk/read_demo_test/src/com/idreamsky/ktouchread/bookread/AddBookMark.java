package com.idreamsky.ktouchread.bookread;

import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.BookMark;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.util.Util;

public class AddBookMark {
	public static final int mark_text = 0;
	public static final int chapterName = 1;
	public static final int currIndex = 2;
	/**
	 * 
	 * @param book
	 * @param chapter
	 * @param pageNumber 页码
	 * @param mark_Text 截取的字
	 * @param currIndex 章节的index
	 * @return
	 */
	public boolean add(Book book,Chapter chapter,int pageNumber,String mark_Text,int currIndex)
	{
		BookMark bookMark= new BookMark();
		bookMark.Pos=String.valueOf(pageNumber+1);
		bookMark.Mark_Text = mark_Text+","+chapter.ChapterName+","+currIndex;
		bookMark.Book_ID_Net = book.bookidNet;
		bookMark.Chapter_ID_Net = chapter.ChapterIDNet;
		bookMark.Date = Util.getBookMarkDate();
		book.AddBookMark(bookMark, chapter.ChapterIDNet);
		return true;
	}
	
	public boolean deleteBookMark(Book book,BookMark bookMark)
	{
		return book.DeleteBookMark(bookMark);
	}
}
