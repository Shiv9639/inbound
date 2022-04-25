package com.lcl.scs.sapmanagement.model;

public class StoreMasterId {
	private String externalLocationNumber;

	private String internalLocationNumber;

	public String getExternalLocationNumber() {
		return externalLocationNumber;
	}

	public void setExternalLocationNumber(String externalLocationNumber) {
		this.externalLocationNumber = externalLocationNumber;
	}

	public String getInternalLocationNumber() {
		return internalLocationNumber;
	}

	public void setInternalLocationNumber(String internalLocationNumber) {
		this.internalLocationNumber = internalLocationNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalLocationNumber == null) ? 0 : externalLocationNumber.hashCode());
		result = prime * result + ((internalLocationNumber == null) ? 0 : internalLocationNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreMasterId other = (StoreMasterId) obj;
		if (externalLocationNumber == null) {
			if (other.externalLocationNumber != null)
				return false;
		} else if (!externalLocationNumber.equals(other.externalLocationNumber))
			return false;
		if (internalLocationNumber == null) {
			if (other.internalLocationNumber != null)
				return false;
		} else if (!internalLocationNumber.equals(other.internalLocationNumber))
			return false;
		return true;
	}
	
}
