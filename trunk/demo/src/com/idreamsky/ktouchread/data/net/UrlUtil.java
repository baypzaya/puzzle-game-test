package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.bookshelf.KTouchReadApplication;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.pay.KPayAccount;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class UrlUtil {
//	public static final String URL_CHECK_UPDATE = "http://kpay.k-touch.cn:8008/VersionCheck";
	public static final String URL_CHECK_UPDATE = getUrlCheckUpdateHost();//"http://osatest01001.k-touch.cn:8006/VersionCheck";
	
	
//	private static final String HOST ="http://bookstore.k-touch.cn:80/";
	private static final String HOST ="http://10.32.187.91:8080/";//getHost(); //"http://osatest01001.k-touch.cn:8012/";
	
	private static String getUrlCheckUpdateHost(){
		return KTouchReadApplication.sContext.getString(R.string.URL_CHECK_UPDATE);
	}
	
	private static String getHost(){
		return KTouchReadApplication.sContext.getString(R.string.HOST);
	}
	
	public static final String URL_CATEGORY = HOST +"category/getlist";
	public static final String URL_CATEGORY_BOOKLIST = HOST +"category/getbooklist";
	public static final String URL_TOP = HOST +"toplist/getlist";
	public static final String URL_TOP_BOOKLIST = HOST +"toplist/getbooklist";
	public static final String URL_BOOK_SEARCH = HOST +"search/searchbook";
	public static final String URL_GET_BOOK_INFO = HOST +"book/getbasicinfo";
	
	public static final String URL_GET_CHAPTER_INFO = HOST +"book/getallchapterinfo";
	public static final String URL_GET_CHAPTER = HOST +"book/getChapterContent";
	public static final String URL_GET_UPDATE_CHAPTER = HOST +"book/getupdatechapterinfo";
	public static final String URL_ORDER = HOST +"kucc/Consume";
	
	
	public static final String URL_BOOK_SHELF_ADD = HOST +"shelf/add";
	public static final String URL_BOOK_SHELF_DELETE = HOST +"shelf/delete";
	public static final String URL_BOOK_SHELF_BOOKLIST = HOST +"shelf/getbooklist";
	
	public static final String URL_BOOKMARK_ADD = HOST +"bookmark/add";
	public static final String URL_BOOKMARK_DELETE = HOST +"bookmark/delete";
	public static final String URL_BOOKMARK_LIST = HOST +"bookmark/getlist";
	public static final String URL_BOOKMARK_USER_LIST = HOST +"bookmark/getusermarklist";
	
	public static final String URL_CHASE_ADD = HOST +"chase/add"; 
	public static final String URL_CHASE_DELETE = HOST +"chase/delete"; 
	public static final String URL_CHASE_BOOKLIST = HOST +"chase/getbooklist"; 
	public static final String URL_CHASE_LIST = HOST +"chase/getchaselist"; 
	
	public static final String URL_ADVERT_LIST = HOST +"bookadv/getlist";
	public static final String URL_SEARCH_KEY = HOST +"search/searchkeylist";
	public static final String URL_RECOMMEND = HOST +"recbooklist";
	

	public static final String URL_GET_TOKEN = HOST + "kucc/gtpl";
	public static final String URL_GET_BOOKMARK_ADVERT = HOST + "bookmark/getsubjectlist";
	public static final String URL_GET_BOOKMARK_LIST = HOST + "bookmark/getlist";
	public static final String URL_GET_BOOK_UPDATE_TIME = HOST + "book/glut";
	
	public static final String URL_CREATE_BOOK_ORDER = HOST + "kucc/CreateOrder";
	public static final String URL_CHECK_BOOK_ORDER = HOST + "kucc/CheckOrder";
	
	public static final int  Success = 200	;//正常的成功应答。
	public static final int  Order_chapter_relation_exist = 2002;//	章节已经购买过
	public static final int  Order_book_relation_exist = 	2001	;//书籍已经购买过
	public static final int  Bad_Request = 	4001	;//请求格式错误。
	public static final int  CurrVer_Bad_Format = 	4004	;//版本号格式错误。
	public static final int  CurrVer_Is_Required = 	4003	;//版本号不能为空。
	public static final int  Empty_Request = 	4002	;//空请求。
	public static final int  Failure	 = 400	;//业务内容错误。
	public static final int  IMEI_Is_Required = 	4005	;//Imei不能为空。
	public static final int  IMSI_Is_Required = 	4006	;//Imsi不能为空。
	public static final int  Consume_Good_name_is_required = 	4106	;//订购商品名称必填
	public static final int  Consume_price_is_bad_format = 	4105	;//订购价格格式错误
	public static final int  Consume_Price_is_required	 = 4104	;//订购价格不能为空
	public static final int  Consume_Productid_bad_format = 	4107	;//订购商品编号格式错误
	public static final int  Consume_Productid_bad_format_bookid = 	4110	;//订购商品格式之书籍编号格式错误。
	public static final int  Consume_Productid_bad_format_chapterid  = 	4111	;//订购商品格式之章节编号格式错误。
	public static final int  Consume_Productid_bad_format_cpcode = 	4108	;//订购商品格式之CP编号格式错误。
	public static final int  Consume_Productid_bad_format_rpid  = 	4109	;//订购商品格式之CP子CP编号格式错误。
	public static final int  Consume_Productid_is_required  = 	4103	;//产品标识不能为空
	public static final int  Consume_Productid_is_required_bookid  = 	4114	;//订购商品编号不能为空
	public static final int  Consume_Productid_is_required_cpcode	 = 4112	;//订购商品编号之CP不能为空
	public static final int  Consume_Productid_is_required_rpid = 	4113	;//订购商品编号之子CP不能为空
	public static final int  Consume_ProductLine_Not_Exist =	4102	;//业务线不存在
	public static final int  Uid_is_required	 = 4101	;//用户标识不能为空
	public static final int  User_token_invalid	 = 4201	;//用户身份令牌失效
	public static final int  User_token_is_null	 = 4202	;//用户身份令牌为空
	public static final int  User_token_is_outtime	 = 4203	;//用户身份令牌过期
	public static final int  Book_category_categoryid_is_required = 	4302	;//分类id不能为空
	public static final int  Book_category_cpcode_is_required = 	4303	;//cpcode不能为空
	public static final int  Book_category_level_is_required	 = 4304	;//分类层级不能为空
	public static final int  Book_categoryid_bad_format	 = 4306	;//分类id格式不正确
	public static final int  Book_cp_code_is_required	 = 4305	;//合作方编码不能为空。
	public static final int  Book_cpcode_bad_format	 = 4308	;//cpcode格式不正确
	public static final int  Book_toplist_id_is_required = 	4307;//	排行榜编号不能为空。
	public static final int  Book_toplistid_bad_format	 = 4309	;//排行榜id格式不正确
	public static final int  Book_search_key_bad_format	 = 4311	;//搜索关键字格式错误
	public static final int  Book_search_key_is_required = 	4310	;//搜索关键字不能为空
	public static final int  Book_basic_bookid_bad_format = 	4403;//	获取书籍信息bookid格式错误 
	public static final int  Book_basic_bookid_is_required = 	4404;//	获取书籍信息bookid不能为空
	public static final int  Book_basic_rpid_bad_format	 = 4402	;//获取书籍信息rpid格式错误 
	public static final int  Order_billingtype_incorrect = 	4406	;//购买类型不匹配。
	public static final int  Order_book_is_free	 = 4410	;//书籍免费，无需购买
	public static final int  Order_book_not_exist = 	4409	;//书籍不存在
	public static final int  Order_chapter_is_free = 	4405	;//章节免费，无需购买
	public static final int  Order_chapter_not_exist = 	4408	;//章节不存在
	public static final int  Order_relation_chapter_not_exist = 	4407	;//章节订购关系不存在，尚未购买。
	public static final int  Order_relation_book_not_exist = 4411; //书订购关系不存在，尚未购买。
	public static final int  Book_chapterid_bad_format = 	4501	;//获取章节信息chapterid格式错误 
	public static final int  Book_chapterid_is_required	 = 4502;//	获取章节信息chapterid不能为空
	public static final int  Book_shelf_bookstatus_is_required = 	4601;	//书籍状态不能为空
	public static final int  Book_mark_bookmarkid_bad_format	 = 4703	;//书签ID格式错误 
	public static final int  Book_mark_bookmarkid_is_required	 = 4702	;//书签ID不能为空 
	public static final int  Book_mark_pos_is_required	 = 4701;	//书签位置不能为空 
	public static final int  ServerFailure	 = 500	;//服务器处理异常。
	public static final int  ServerTimeOut	 = 501	;//超时。


	public static final String URL_GET_KTOUCH_TOKEN = KPayAccount.KPAY_HOST_TEMP +"/kpp/ylogon.do";


	

	
	
	public static String imei;
	public static String imsi;
	public static String deviveid;
	public static String version;
	public static String os;
	
	public static String TokenT = "";
	public static String TokenTPL = "";
	public static String USERID = "";
	public static String BookUpdateTime = null;
	public static String BookMarkUpdateTime = null;
//	public static boolean bUpdateBook = false;
//	public static boolean bUpdateBookmark = false;
	public static String resolution;
	
	
	
	public static void Init(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
		imei =  tm.getDeviceId();
		if(imei == null)
		{
			imei = "000000000000000";
		}
		imsi = tm.getSubscriberId();
		if(imsi == null)
		{
			imsi = "000000000000000";
		}
		deviveid = "TIANYU-KTOUCH/" + Build.MODEL;
		os = android.os.Build.ID;
		DisplayMetrics dm = new DisplayMetrics(); 
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		resolution = Integer.toString(dm.widthPixels) + "x" + Integer.toString(dm.heightPixels);
		String packageName = context.getPackageName();
		try {
			version = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, String> getAppInfoData(){
		
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("imei", UrlUtil.imei);
		paramsHashMap.put("imsi", UrlUtil.imsi);
		paramsHashMap.put("deviceIdentify", UrlUtil.deviveid);
		paramsHashMap.put("cv", UrlUtil.version);
		return paramsHashMap;
	}
	
}
