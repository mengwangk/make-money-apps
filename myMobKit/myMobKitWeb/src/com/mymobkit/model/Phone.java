package com.mymobkit.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public final class Phone {
	@Id Long id;
	
	public Long getId() {
		return id;
	}
}
