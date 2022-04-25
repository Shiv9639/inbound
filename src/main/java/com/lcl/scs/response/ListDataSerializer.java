package com.lcl.scs.response;

import java.io.IOException;

import com.lcl.scs.response.ListData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ListDataSerializer extends StdSerializer<ListData> {
	private static final long serialVersionUID = 1L;

	public ListDataSerializer() {
        this(null);
    }
   
    public ListDataSerializer(Class<ListData> t) {
        super(t);
    }
 
    @Override
    public void serialize(
    		ListData value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	
        jgen.writeStartObject();
        jgen.writeStringField("key", value.getKey());
        jgen.writeStringField("value", value.getValue());
        if(value.getData()!=null) {
	        for (String key : value.getData().keySet()) {
	        	if(value.getData().get(key) instanceof String)
	        		jgen.writeStringField(key, value.getData().get(key).toString());
	        	if(value.getData().get(key) instanceof Object)
	        		jgen.writeObjectField(key, value.getData().get(key));
	        	   //System.out.println("key: " + key + " value: " + loans.get(key));
	        }
        }
        jgen.writeEndObject();
    }
}
