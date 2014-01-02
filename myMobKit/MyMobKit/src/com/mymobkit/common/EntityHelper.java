package com.mymobkit.common;

import java.util.UUID;

public final class EntityHelper {

	public static String generateGuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
