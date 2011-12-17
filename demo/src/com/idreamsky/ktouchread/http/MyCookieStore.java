package com.idreamsky.ktouchread.http;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
public class MyCookieStore implements CookieStore{
	
	private List<Cookie> cookieList ; 
	
	public MyCookieStore()
	{
		cookieList = new ArrayList<Cookie>();
	}

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		cookieList.add(cookie);
		
	}

	@Override
	public List<Cookie> getCookies() {
		// TODO Auto-generated method stub
		return cookieList;
	}

	@Override
	public boolean clearExpired(Date date) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		cookieList.clear();
		
	}

}
