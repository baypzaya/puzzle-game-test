package com.gmail.txyjssr.reader.logic;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import com.gmail.txyjssr.reader.FileUtils;
import com.gmail.txyjssr.reader.dao.BookDao;
import com.gmail.txyjssr.reader.data.Book;

public class BookLogic {

	public Book addBook(File file) {
		Book book = new Book();
		book.name = file.getName();
		book.path = file.getPath();
		book.progress = 0;
		book.lastReadTime = System.currentTimeMillis();
		book.encodeType = FileUtils.getFileEncodeType(file);
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

		// r.seek(2);

		byte[] bs = new byte[(int) file.length()];
		int size = r.read(bs);
		while (size > 0) {
			sb.append(new String(bs, 0, size, code));
			size = r.read(bs);
		}
		r.close();

		return sb.toString();
	}

	public boolean deleteBook(Book book) {
		boolean result = revomeBookFromList(book);
		if (result) {
			File file = new File(book.path);
			if (file.exists()) {
				result = file.delete();
			}
		}
		return result;
	}

	public boolean revomeBookFromList(Book book) {
		BookDao dao = BookDao.getInstance();
		boolean isExist = dao.isExist(book);
		if (isExist) {
			int count = dao.deleteBook(book);
			return count > 0;
		} else {
			return true;
		}
	}

}
