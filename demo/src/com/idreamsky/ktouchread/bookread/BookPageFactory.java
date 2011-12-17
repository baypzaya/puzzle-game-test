/**
 *  Author :  hmg25
 *  Description :
 */
package com.idreamsky.ktouchread.bookread;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.data.ChapterContent;
import com.idreamsky.ktouchread.util.BitmapUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class BookPageFactory {
	public String defaultFont = "默认默认默认默认默认默认默认默认默认默认默认默认默认默认默认默认默认默认默认"; // 默认计算出行显示多少个字
	private Bitmap m_book_bg = null;
	public int mWidth;
	private int mHeight;
	private int gap = 10; // 行间距
	public static int defaultSize = 24;
	public static int m_fontSize = 24;
	//public static int m_textColor = Color.BLACK;
	public static int m_textColor = 0xffffff;
	//public static int m_textColor = R.id.yeshu;
	//private int m_backColor = 0xffff9e85; // 背景颜色
	public static int m_backColorDay = 0xffe5e5e5;
	public static  int m_backColorNight = 0xff434343;
	public static int marginWidth = 15; // 左右与边缘的距离
	public int marginHeight = 20; // 上下与边缘的距离

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage, m_islastPage;
	public static int lastPageNumber;

	private int cutCount = 3; // 需要减去时间字符位置
	private Paint paint;
	public int currentPage = 0;
	public static int totalSize = 0;
	public String pos;
	private Paint mPaint;

	private boolean mode = false;
	private Context mContext = null;

	public Paint getTitlePaint() {
		return paint;
	}

	public Paint getPaint() {
		return mPaint;
	}

	private List<Vector<String>> pageList = new ArrayList<Vector<String>>();

	public List<Vector<String>> getPageList() {
		return pageList;
	}

	public BookPageFactory(Context context, int w, int h, int size,
			int textColor) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mWidth = w;
		mHeight = h;
		m_fontSize = size;
		if(mWidth==320 && mHeight==480)
		{
			defaultSize = 16;
		}
		if(size>defaultSize)
		{
			defaultSize = 24;
		}
		m_textColor = textColor;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2 - m_fontSize;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + gap)); // 可显示的行数
		// mLineCount = mLineCount-2;
		m_isfirstPage = true;
		currentPage = 0;
		totalSize = 0;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(defaultSize);
		paint.setColor(m_textColor);
	}

	public String content; // 内容
	public String chapter_Name;// 章节标题

	public void openbook(Book book, Chapter chapter,
			ChapterContent chapterContent) {
		chapter_Name = chapter.ChapterName;
		content = chapterContent.ChapterContent;
	}

	public void initContent() {
		if (pageList.size() == 0) {
			initContent(content);
			totalSize = pageList.size();
		}
	}

	public void printContent(Canvas c) {
		if (c != null) {
			try {
				if (pageList.size() > 0) {

					if(Configuration.ReadBackGroudUseColor)
					{
						if (mode) {
							c.drawColor(m_backColorDay);
						} else {
							c.drawColor(m_backColorNight);
						}
						
					}else {
						if (m_book_bg == null || m_book_bg.isRecycled()) {
							if (mode) {
								m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
										R.drawable.page);
							} else {
								m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
										R.drawable.page_a);

							}
							c.drawBitmap(m_book_bg, 0, 0, null);
						}

						else
							c.drawBitmap(m_book_bg, 0, 0, null);
					}

					int y = marginHeight + 15; // 离顶部的间距
					if (currentPage >= pageList.size()) {
						currentPage = pageList.size() - 1;
					}
					for (String strLine : pageList.get(currentPage)) {
						y += m_fontSize + gap;
						c.drawText(strLine, marginWidth, y, mPaint);

					}
				}
			} catch (Exception e) {
			} finally {
				drawTitleAndTile(c); // 打印时间 章节标题,页码
			}

		}
	}

	public void onDraw(Canvas c) {
		initContent();
		printContent(c);

	}

	public List<Vector<String>> initContent(String content) {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		int i = 0;
		int index = 0;
		while (content.length() > 0) {

			if (content.indexOf("\r\n") != -1) {
				index = content.indexOf("\r\n");
				strParagraph = content.substring(0, index);
				strParagraph = strParagraph.replaceAll("\r\n", "");
				if (strParagraph.length() == 0) {
					lines.add(strParagraph);
				}
				while (strParagraph.length() > 0) {
					int nSize = mPaint.breakText(strParagraph, true,
							mVisibleWidth, null);
					lines.add(strParagraph.substring(0, nSize));
					if (lines.size() >= mLineCount) { // 满一页

						pageList.add(lines);
						strParagraph = strParagraph.substring(nSize);
						lines = new Vector<String>();
						continue;
					} else {
						strParagraph = strParagraph.substring(nSize);
					}
				}
				// content = content.replaceFirst("\r\n", "");
				content = content.substring(index + 2);
				if (content.length() <= 0) {
					if (lines.size() != 0) {
						pageList.add(lines);
						lines = new Vector<String>();
					}
				}
			} else if (content.indexOf("\n") != -1) {
				index = content.indexOf("\n");
				strParagraph = content.substring(0, index + 1);
				strParagraph = strParagraph.replaceAll("\n", "");
				if (strParagraph.length() == 0) {
					lines.add(strParagraph);
				}
				while (strParagraph.length() > 0) {
					int nSize = mPaint.breakText(strParagraph, true,
							mVisibleWidth, null);
					lines.add(strParagraph.substring(0, nSize));
					if (lines.size() >= mLineCount) { // 满一页

						pageList.add(lines);
						strParagraph = strParagraph.substring(nSize);
						lines = new Vector<String>();
						continue;
					} else {
						strParagraph = strParagraph.substring(nSize);
					}
				}
				content = content.substring(index + 1);
				if (content.length() <= 0) {
					if (lines.size() != 0) {
						pageList.add(lines);
						lines = new Vector<String>();
					}
				}
			} else {
				while (content.length() > 0) {
					int nSize = mPaint.breakText(content, true, mVisibleWidth,
							null);
					lines.add(content.substring(0, nSize));
					if (lines.size() >= mLineCount) { // 满一页

						pageList.add(lines);
						lines = new Vector<String>();
						continue;
					} else {
						content = content.substring(nSize);
					}
				}
				if (content.length() == 0) {
					pageList.add(lines);
					lines = new Vector<String>();
				}
			}
			if (lines.size() >= mLineCount) { // 满一页

				pageList.add(lines);
				lines = new Vector<String>();
				continue;
			}
		}
		return pageList;
	}

	protected void prePage() throws IOException {
		if (currentPage <= 0) {
			currentPage = 0;
			m_isfirstPage = true;
			return;
		} else
			m_isfirstPage = false;
		currentPage--;
		if (currentPage < 0) {
			currentPage = 0;
		}
		pos = pageList.get(currentPage).get(0);// 记录当前页的第一行文字
		if (pos.length() >= 6)
			pos = pos.substring(0, 6);
		LogEx.Log_I("currentPage", currentPage + "");
		LogEx.Log_I("posString", pos);
	}

	public void nextPage() throws IOException {

		m_isfirstPage = false;
		if ((currentPage + 1) == totalSize) {
			m_islastPage = true;
			return;
		} else
			m_islastPage = false;
		if ((currentPage + 1) != totalSize)
			currentPage++;
		pos = pageList.get(currentPage).get(0);// 记录当前页的第一行文字
		if (pos.length() >= 6)
			pos = pos.substring(0, 6);
		LogEx.Log_I("currentPage", currentPage + "");
		LogEx.Log_I("posString", pos);

	}

	public void setBgBitmap(Bitmap BG, boolean mode) {
		this.mode = mode;
		if (m_book_bg != null && !m_book_bg.isRecycled()) {
			m_book_bg.recycle();
		}
		m_book_bg = BG;
	}

	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	public boolean islastPage() {
		return m_islastPage;
	}

	public void drawTitleAndTile(Canvas c) {
		if (c != null) {
			String strPercent = "第" + (currentPage + 1) + "页/共" + totalSize
					+ "页"; // 显示页码
			
			int nPercentWidth = (int) paint.measureText(strPercent) + 1;
			c.drawText(strPercent, (mWidth / 2) - (nPercentWidth / 2),
					mHeight - 10, paint); // 计算中间位置
		    
			if (chapter_Name.length() != 0) {
				if (pageList.size() != 0) {
					int size = paint.breakText(defaultFont, true,
							mVisibleWidth, null);
					if (chapter_Name.length() < 10) {
						c.drawText(chapter_Name, marginWidth,
								defaultSize + gap, paint);
					} else {
						if (chapter_Name.length() >= size - cutCount) { // 判断是否够需要截取字符
							String cutTitle = chapter_Name.substring(0, size
									- cutCount); // 得到第一行的
							c.drawText(cutTitle + "...", marginWidth,
									defaultSize + gap, paint);

						} else {
							c.drawText(chapter_Name, marginWidth, defaultSize
									+ gap, paint);
						}
					}

					String currentTime = getCurrentTime();
					int nCurTime = (int) paint.measureText(currentTime) + 1;
					c.drawText(currentTime,
							(mWidth - marginWidth) - (nCurTime), defaultSize
									+ gap, paint); // 计算时间要放的位置
				}

			}
		}
	}

	public void changeTextSize(int size, PageWidget mPageWidget,
			Canvas mCurPageCanvas, Bitmap mCurPageBitmap) {
		pageList.clear();
		m_fontSize = size; // 改变size
		LogEx.Log_I("size", m_fontSize + "");
		mPaint.setTextSize(m_fontSize);// 改变size
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2 - m_fontSize;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + gap)); // 可显示的行数
		// mLineCount = mLineCount-2;
		onDrawPlusSize(mCurPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.invalidate();
	}

	public void onDrawPlusSize(Canvas c) {
		if (pageList.size() == 0) {
			initContent(content);
			totalSize = pageList.size();
		}
		if (pageList.size() > 0) {

			if(Configuration.ReadBackGroudUseColor)
			{
				if (mode) {
					c.drawColor(m_backColorDay);
				} else {
					c.drawColor(m_backColorNight);
				}
				
			}else {
				if (m_book_bg == null || m_book_bg.isRecycled()) {
					if (mode) {
						m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
								R.drawable.page);
					} else {
						m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
								R.drawable.page_a);

					}
					c.drawBitmap(m_book_bg, 0, 0, null);
				}

				else
					c.drawBitmap(m_book_bg, 0, 0, null);
			}

			int y = marginHeight + 15; // 离顶部的间距
			if ((currentPage + 1) > totalSize) // 如果当前页大于总页面
			{
				currentPage = totalSize - 1;
			}
			if (currentPage <= 0) {
				currentPage = 0;
			}
			LogEx.Log_I("currentPage", "currentPage is " + currentPage
					+ " totalSize is " + totalSize);
			StringBuffer sBuffer = new StringBuffer();
			while (changeFlag) {
				if ((currentPage + 1) > totalSize) // 如果当前页大于总页面
				{
					currentPage = totalSize - 1;
				}

				for (String str : pageList.get(currentPage)) {
					sBuffer.append(str);
				}
				if (pos == null) // 第一页
				{
					break;
				}
				if (sBuffer.indexOf(pos) == -1) // 不存在
				{
					currentPage = currentPage + 1;// 继续去下一页找
					if (currentPage == totalSize) // 找不到重置到第一页
					{
						currentPage = 0;
						break;
					}
					sBuffer.setLength(0);
					continue;
				} else {
					sBuffer.setLength(0);
					break;
				}

			}

			for (String strLine : pageList.get(currentPage)) {
				y += m_fontSize + gap;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}
		drawTitleAndTile(c); // 打印时间 章节标题,页码
	}

	public void changeTextSizeReduce(int size, PageWidget mPageWidget,
			Canvas mCurPageCanvas, Bitmap mCurPageBitmap) {
		pageList.clear();
		m_fontSize = size; // 改变size
		mPaint.setTextSize(m_fontSize);// 改变size
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2 - m_fontSize;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + gap)); // 可显示的行数
		// mLineCount = mLineCount-2;
		onDrawReduceSize(mCurPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.invalidate();
	}

	private boolean changeFlag = true;

	public void onDrawReduceSize(Canvas c) {
		if (pageList.size() == 0) {
			initContent(content);
			totalSize = pageList.size();
		}
		if (pageList.size() > 0) {

			if(Configuration.ReadBackGroudUseColor)
			{
				if (mode) {
					c.drawColor(m_backColorDay);
				} else {
					c.drawColor(m_backColorNight);
				}
				
			}else {
				if (m_book_bg == null || m_book_bg.isRecycled()) {
					if (mode) {
						m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
								R.drawable.page);
					} else {
						m_book_bg = BitmapUtil.getBitmapFromRes(mContext,
								R.drawable.page_a);

					}
					c.drawBitmap(m_book_bg, 0, 0, null);
				}

				else
					c.drawBitmap(m_book_bg, 0, 0, null);
			}


			int y = marginHeight + 15; // 离顶部的间距
			if ((currentPage + 1) > totalSize) // 如果当前页大于总页面
			{
				currentPage = totalSize - 1;
			}
			if (currentPage <= 0) {
				currentPage = 0;
			}
			LogEx.Log_I("currentPage", "currentPage is " + currentPage
					+ " totalSize is " + totalSize);
			StringBuffer sBuffer = new StringBuffer();
			while (changeFlag) {

				if (currentPage <= 0) {
					currentPage = 0;
				}
				for (String str : pageList.get(currentPage)) {
					sBuffer.append(str);
				}
				if (sBuffer.indexOf(pos) == -1) // 不存在
				{
					currentPage = currentPage - 1;// 继续去上一页找
					if (currentPage <= 0) // 找不到重置到第一页
					{
						currentPage = 0;
						break;
					}
					sBuffer.setLength(0);
					continue;
				} else {
					sBuffer.setLength(0);
					break;
				}
			}

			for (String strLine : pageList.get(currentPage)) {
				y += m_fontSize + gap;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}
		drawTitleAndTile(c); // 打印时间 章节标题,页码
	}

	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date());
	}
}
