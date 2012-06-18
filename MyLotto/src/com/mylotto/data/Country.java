package com.mylotto.data;


/**
 * Country class
 * 
 * @author MEKOH
 *
 */
public class Country {

	public long id;
	public String name;

	/**
	 * Default constructor
	 */
	public Country() {
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	public Country(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}
}
