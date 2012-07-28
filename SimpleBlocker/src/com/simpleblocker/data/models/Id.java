package com.simpleblocker.data.models;

import java.io.Serializable;

import com.simpleblocker.utils.SHA1Utils;

/**
 * 
 */
public class Id implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Id() {

		// get the actual system time in nanoseconds
		String time = String.valueOf(System.nanoTime());

		this.id = SHA1Utils.generateSHA1toString(time);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Id other = (Id) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id [id=" + id + "]";
	}

	/**
	 * Indicate if this is a NULL object - see NULL object pattern
	 * @return
	 */
	public boolean isEmpty() {
		return false;
	}

}
