package com.mymobkit;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mymobkit.model.Resolution;
import com.google.gson.*;

@RunWith(JUnit4.class)
public class GsonTest {

	
	@Test
	public void testGson(){
		Resolution res1 = new Resolution(800,600); 
		Resolution res2 = new Resolution(1270,680);
		List<Resolution> resList = new ArrayList<>();
		resList.add(res1);
		resList.add(res2);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(resList));
		
	}
}
