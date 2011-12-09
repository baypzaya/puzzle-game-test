package com.idreamsky.ktouchread.xmlparase;


public class ParserFactory {

	public static final int TYPE_ERROR = 800;
	public static final int TYPE_ADVERT_LIST = 801;
	public static final int TYPE_BOOKINFOR = 802;
	public static final int TYPE_CATEGORY_LIST = 803;
	public static final int TYPE_CHAPTER = 804;
	public static final int TYPE_CHAPTER_CONTENT = 805;
	public static final int TYPE_ADD_CHASE = 806;
	public static final int TYPE_DELETE_CHASE = 807;
	public static final int TYPE_GET_NETBOOK_LIST = 810;
	public static final int TYPE_ADD_BOOKMARK = 911;
	public static final int TYPE_DELETE_BOOKMARK = 812;
	public static final int TYPE_GET_BOOKMARK_LIST  = 813;
	public static final int TYPE_BOOKSHELF_ADD = 814;
	public static final int TYPE_BOOKSHELF_DELETE = 815;
	public static final int TYPE_BOOKSHELF_LIST = 816;
	public static final int TYPE_CHAPTER_ORDER = 817;
	public static final int TYPE_TOP_LIST = 818;
	public static final int TYPE_SEARCH_KEY = 819;
	public static final int TYPE_CHECK_INFO = 820;
	public static final int TYPE_BOOKMARK_ADVERT = 821;
	public static final int TYPE_BOOKMARK_PIC_LIST = 822;

	public static AbstractParser createParser(int type, String data) {
		if (data == null) {
			throw new IllegalArgumentException(
					"ParserFactory : data can not be null");
		}
		switch (type) {
		case TYPE_ERROR:
			return new ServerErrorParser(data);
		case TYPE_ADVERT_LIST:
			return new AdvertParase(data);
		case TYPE_BOOKINFOR:
			return new BookInforParase(data);
		case TYPE_CATEGORY_LIST:
			return new CategoryListParase(data);
		case TYPE_CHAPTER:
			return new ChapterParase(data);
		case TYPE_CHAPTER_CONTENT:
			return new ChapterContentParase(data);
		case TYPE_ADD_BOOKMARK:
			return new AddBookMarkParase(data);
		case TYPE_ADD_CHASE:
		case TYPE_DELETE_CHASE:
		case TYPE_DELETE_BOOKMARK:
		case TYPE_BOOKSHELF_ADD:
		case TYPE_BOOKSHELF_DELETE:
			return new ReturnMsgParase(data);
		case TYPE_GET_NETBOOK_LIST:
		    return new NetBookListParase(data);
		case TYPE_GET_BOOKMARK_LIST:
			return new NetBookMarkListParase(data);
		case TYPE_BOOKSHELF_LIST:
			return new BookShelfListParase(data);
		case TYPE_CHAPTER_ORDER:
			return new ChapterOrderParase(data);
		case TYPE_TOP_LIST:
			return new TopListParase(data);
		case TYPE_SEARCH_KEY:
			return new SearchKeyParase(data);
		case TYPE_CHECK_INFO:
		    return new CheckInfoParase(data);
		case TYPE_BOOKMARK_ADVERT:
			return new BookMarkAdvetParase(data);
		case TYPE_BOOKMARK_PIC_LIST:
			return new BookMarkPicListParase(data);
		default:
			throw new IllegalArgumentException(
					"ParserFactory: unknown parser type " + type);
		}
	}
}
