package com.gmail.txyjssr.reader.logic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;

import com.gmail.txyjssr.reader.Book;
import com.gmail.txyjssr.reader.BookDao;

public class BookLogic {

	public Book addBook(File file) {
		Book book = new Book();
		book.name = file.getName();
		book.path = file.getPath();
		book.progress = 0;
		book.total = file.length();
		book.lastReadTime = System.currentTimeMillis();
		
		try {
			book.encodeType = getFileEncodeType(file);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		BookDao.getInstance().addBook(book);
		return book;
	}

	public List<Book> getBooks() {
		return BookDao.getInstance().getBooks();
	}

	public String getBookContent(Book book) throws Exception {
		String code = book.encodeType;
		
		File file = new File(book.path);
		RandomAccessFile r = new RandomAccessFile(file, "r");
		StringBuffer sb = new StringBuffer();
		
//		r.seek(2);
		
		byte[] bs = new byte[(int) book.total];
		int size = r.read(bs);
		while(size>0){
			sb.append(new String(bs,0,size,code));
			size = r.read(bs);
		}
		r.close();

		
		return sb.toString();
	}

	private String getFileEncodeType(File file) throws Exception {
		String encode = "GBK";

		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream in = new BufferedInputStream(fis);
		in.mark(4);
		byte[] first3bytes = new byte[3];
		in.read(first3bytes);
		in.reset();
		in.close();
		if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {
			encode = "utf-8";
		} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
			encode = "unicode";
		} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
			encode = "utf-16be";
		} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
			encode = "utf-16le";
		}
		
		return encode;
	}

}
