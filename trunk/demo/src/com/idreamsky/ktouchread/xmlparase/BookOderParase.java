package com.idreamsky.ktouchread.xmlparase;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.BookOrder;

public class BookOderParase extends AbstractParser {

	public BookOderParase(String data) {
		super(data);
	}

	@Override
	public Object parse() throws IOException, JSONException {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject(dataSource);
		

		BookOrder bookOrder = new BookOrder();
		bookOrder.orderId = jsonObject.getString("OrderId");
		bookOrder.paySignStr = jsonObject.getString("PaySignStr");
		bookOrder.orderStatus = 2;
		
		return bookOrder;
	}

}
