package com.idreamsky.ktouchread.xmlparase;

import java.io.IOException;

import org.json.JSONException;

public abstract class AbstractParser {

	protected static final String RESULT = "result";

	protected String dataSource;

	public AbstractParser(String data) {
		this.dataSource = data;
	}
	public abstract Object parse() throws IOException, JSONException;

}
