package com.idreamsky.ktouchread.xmlparase;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.BookOrder;

public class CheckBookOrderParase extends AbstractParser {

	public CheckBookOrderParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse() throws IOException, JSONException {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject(dataSource);
		
		BookOrder bookOrder = new BookOrder();
		bookOrder.orderId = jsonObject.getString("OrderId");
		bookOrder.orderStatus = jsonObject.getInt("OrderStatus");
		bookOrder.payInRedPacket =jsonObject.getString("PayInRedPacket");
		bookOrder.payInCash = jsonObject.getString("PayInCash");
		return bookOrder;
	}

}
