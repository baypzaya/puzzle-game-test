package com.gmail.txyjssr.reader;

import java.util.ArrayList;
import java.util.List;

public class BookDao {
	private static BookDao sBookDao;

	// test code
	private static List<Book> testBooks = new ArrayList<Book>();

	public static BookDao getInstance() {
		if (sBookDao == null) {
			sBookDao = new BookDao();
		}
		return sBookDao;
	}

	public List<Book> getBooks() {
		return testBooks;
	}

	public void addBook(Book book) {
		testBooks.add(book);
	}
}
