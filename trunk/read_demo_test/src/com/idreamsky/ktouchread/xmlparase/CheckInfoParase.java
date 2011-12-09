package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.CheckInfo;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class CheckInfoParase extends AbstractParser{

	public CheckInfoParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{
		CheckInfo checkInfo = new CheckInfo();
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			JSONObject updatemole = jsonObject.getJSONObject("UpdateModel");
			checkInfo.FileName = updatemole.getString("FileName");
			checkInfo.LatestVer = updatemole.getString("LatestVer");
			checkInfo.DownUrl = updatemole.getString("DownUrl");
			checkInfo.FileSize = Integer.parseInt(updatemole.getString("FileSize"));
			String bUpdateable = updatemole.getString("IfUpdateble");
			if(bUpdateable.compareTo("true") == 0)
				checkInfo.IfUpdateble = 1;
			else
				checkInfo.IfUpdateble = 0;
			
			String bForceUpdate = updatemole.getString("IfForceUpdate");
			if(bForceUpdate.compareTo("true") == 0)
				checkInfo.IfForceUpdate = 1;
			else
				checkInfo.IfForceUpdate = 0;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		
		return checkInfo;
	}
}