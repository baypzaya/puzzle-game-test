package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.data.net.BookInfo.GetBookInforCallback;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class BookOrder {

	public String orderId;
	public String paySignStr;
	public int orderStatus;
	public String payInRedPacket;
	public String payInCash;

	public interface BookOrderCallback {
		public void onSuccess(Object o);

		public void onFaile(String msg);
	};

	// http://hostname/kucc/CreateOrder?
	// productid=yzsc_10_1080_557&productname=斗破苍穹_第一章&price=15&pricerp=10
	public static void createOrder(
			final String productId, final String productName,
			final String price, final String priceRP, final BookOrderCallback cb) {
		new JsonRequest() {

			@Override
			public void onSuccess(Object obj) {
				cb.onSuccess(obj);
			}

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_BOOKORDER;
			}

			@Override
			public void onFail(String msg) {
				cb.onFaile(msg);

			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CREATE_BOOK_ORDER;
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil
						.getAppInfoData();
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				paramsHashMap.put("productid", productId);
				paramsHashMap.put("productname", productName);
				paramsHashMap.put("price", price);
				paramsHashMap.put("pricerp", priceRP);

				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}

		}.makeRequest();
	}

	// http://hostname/kucc/CheckOrder? orderid=F5FB1C39CD8DF34B948E70BE102F2254
	public static int checkOrder(final String orderId,
			final BookOrderCallback cb) {
		new JsonRequest() {

			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				cb.onFaile(msg);
			}

			@Override
			public void onSuccess(Object obj) {
				// TODO Auto-generated method stub
				cb.onSuccess(obj);
			}

			@Override
			public int getParserType() {

				return ParserFactory.TYPE_CHECKBOOKORDER;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CHECK_BOOK_ORDER;
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil
						.getAppInfoData();
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				paramsHashMap.put("orderid", orderId);
				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				return true;
			}
		}.makeRequest();
		return 0;
	}

	// http://hostname/book/getbasicinfo? cpcode=yzsc&bookid=1719982&rpid=10
	/*
	 * 名称 类型 是否必填 描述 cpcode string 是 cp rpid string 是 cp的cp bookid string 是 书id
	 */
	public static void GetBookInfo(final String cpcode, final String rpid,
			final String bookid, final GetBookInforCallback cb) {
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_BOOKINFOR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_BOOK_INFO;
			}

			@Override
			public void onFail(String msg) {
				if (null != cb) {
					cb.onFail(msg);
				}
			}

			@Override
			public void onSuccess(Object object) {
				if (null != cb) {
					cb.onSuccess((BookInfo) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil
						.getAppInfoData();
				paramsHashMap.put("cpcode", cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid", bookid);
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}
		}.makeRequest();
	}

}
