package com.lcl.scs.response;

import java.util.HashMap;

import com.lcl.scs.response.ListDataSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ListDataSerializer.class)
public class ListData {

	@JsonIgnore
//	@JsonProperty(value = "key")
	private String key;
	
	@JsonIgnore
//	@JsonProperty(value = "value")
	private String value;
	
	HashMap<String,Object> data;

	public ListData() {
		// Do Nothing. Default Constructor.
		if(data == null)
			data = new HashMap<String,Object>();
	}

	public ListData(String key, String value) {
		this();
		this.key = key;
		this.value = value;
	}
	
	public ListData(String key, String value,HashMap<String,Object> data) {
		this();
		this.key = key;
		this.value = value;
		this.data = data;
	}
	
	public ListData(Integer key, Integer value) {
		this();
		this.key = key.toString();
		this.value = value.toString();
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
		this.data.put("key",this.key);
	}

	public void setValue(String value) {
		this.value = value;
		this.data.put("value", this.value);
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	
	
}
