package com.idreamsky.ktouchread.xmlparase;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.KTouchToken;

public class KTouchTokenParase extends AbstractParser {

	public KTouchTokenParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse() throws IOException, JSONException {
		KTouchToken token = new KTouchToken();
		JSONObject jsonObject = null;
		jsonObject = new JSONObject(dataSource);
		JSONObject data = jsonObject.getJSONObject("Data"); 
		
		token.ktouchToken = data.getString("token");
		return token;
	}

}
