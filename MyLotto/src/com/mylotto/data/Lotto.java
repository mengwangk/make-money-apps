package com.mylotto.data;

/**
 * Represent a lotto instance
 * 
 * @author MEKOH
 *
 */
public final class Lotto {

	public long id;
	public long countryId;
	public String name;
	public String imageName;
	
	
	/**
	 * Default constructor 
	 */
	public Lotto(){
	
	}
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param imageName
	 */
	public Lotto(final String name, final String imageName){
		this.name = name;
		this.imageName = imageName;
	}


		
	
	
}
