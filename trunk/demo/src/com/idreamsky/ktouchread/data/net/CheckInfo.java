package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class CheckInfo {

	public String UpdateModel;
	public String FileName;
	public String LatestVer;
	public String DownUrl;
	public int FileSize;
	public int IfUpdateble;
	public int IfForceUpdate;

	public static interface GetCheckinfoCallback extends RequestCallback {
		public void onSuccess(CheckInfo checkinfo);
	}

	public static void GetCheckInfo(final GetCheckinfoCallback cb) {
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_CHECK_INFO;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CHECK_UPDATE;
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
				 cb.onSuccess((CheckInfo ) object);
				 }
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				paramsHashMap.put("currver", UrlUtil.version);
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				paramsHashMap.put("productline", "bookstore");
				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return false;
			}

		}.makeRequest();
	}
}
