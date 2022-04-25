package com.lcl.scs.sapmanagement.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;


public class CompositeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Field(order = 1)
	private String interfaceId;

	@Field(order = 2)
	private String fileName;

	public CompositeKey() {

	}

	public CompositeKey(String interfaceId, String fileName) {
		super();
		this.interfaceId = interfaceId;
		this.fileName = fileName;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (interfaceId == null) {
			if (other.interfaceId != null)
				return false;
		} else if (!interfaceId.equals(other.interfaceId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((interfaceId == null) ? 0 : interfaceId.hashCode());
		return result;
	}

}
