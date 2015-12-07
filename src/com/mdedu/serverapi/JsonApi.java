package com.mdedu.serverapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.mdedu.serverapi.message.RequestData;
import com.mdedu.utils.BeanUtils;

public class JsonApi<R> extends ServerApi {

	protected String host = "http://www.mdaedu.com:8080/mdbe/ws";
	private String baseurl=host;
	
	private Class<R> rClass;
	
	public JsonApi(Class<R> c) {
		this.rClass = c;
	}
	

	public String getBaseUrl() {
		return baseurl;
	}
	
	public void setBaseUrl(String baseurl){
		this.baseurl=baseurl;
	}
	
	
	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public R get(String paramName, String paramValue, RequestData datas) {
		R response = null;
		try {			
			Map<String, String> params = new HashMap<String, String>();
			params.put(paramName, paramValue);
			if(datas!=null){
				params.put("data", datas.toJsonString());
			}
			Log.d("JsonApi", "param:" + params);
			String param = addParams(getBaseUrl(), params, "UTF-8");
			String result = invoke(param);
			response = rClass.newInstance();
			JSONObject jsonObj = (JSONObject) new JSONTokener(result)
					.nextValue();
			BeanUtils.convertJsonToBean(jsonObj, response);
		} catch (Exception e) {
			Log.e("JsonApi", e.getMessage(), e);
		}
		return response;

	}
	
	public R post(RequestData datas){
		R response = null;
		try {		

			Log.d("JsonApi", "url:"+this.getBaseUrl()+",param:" + datas.toJsonString());
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("data", datas.toJsonString()));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters,"UTF-8");
			
			String result =invoke(this.getBaseUrl(),formEntity);
			response = rClass.newInstance();
			JSONObject jsonObj = (JSONObject) new JSONTokener(result)
					.nextValue();
			BeanUtils.convertJsonToBean(jsonObj, response);
		} catch (Exception e) {
			Log.e("JsonApi", e.getMessage(), e);
		}
		return response;
	}

	
}
