package com.mymobkit.server;

import static com.mymobkit.datastore.OfyService.ofy;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.mymobkit.model.LoginUser;
import com.mymobkit.model.Workspace;
import com.mymobkit.shared.EntityHelper;
import com.mymobkit.shared.RTCMode;


@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {
	
	protected static final Logger logger = Logger.getLogger(TestServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		LoginUser user = new LoginUser("mengwangk@gmail.com");
		user.loggedIn();
		ofy().save().entity(user).now();
		Workspace webcam  = new Workspace(user, EntityHelper.generateGuid(), "webcam 1", RTCMode.WEBCAM.getMode());
		ofy().save().entity(webcam).now();
		
		LoginUser user2 = new LoginUser("abc@gmail.com");
		user2.loggedIn();
		ofy().save().entity(user2).now();
		Workspace webcam2  = new Workspace(user2, EntityHelper.generateGuid(), "webcam 2", RTCMode.PHONE.getMode());
		ofy().save().entity(webcam2).now();
		Workspace webcam3  = new Workspace(user2, EntityHelper.generateGuid(), "webcam 3", RTCMode.WEBCAM.getMode());
		ofy().save().entity(webcam3).now();
		
		
		LoginUser searchUser = ofy().load().type(LoginUser.class).id("mengwangk@gmail.com").now();
		if (searchUser != null){
			resp.getWriter().print(searchUser.getEmail() + "<br/>");
		}
		
		List<Workspace> webCams = ofy().load().type(Workspace.class).ancestor(LoginUser.key("abc@gmail.com")).list();
		for (Workspace mycam: webCams){
			resp.getWriter().print(mycam.getName() + "<br/>");
		}
		
		Iterable<Workspace> searchCams = Iterables.filter(webCams, new Predicate<Workspace>(){  
		    public boolean apply(Workspace c) {  
		        return StringUtils.equals(c.getName(), "webcam 2");
		    }  
		});  
		
		for (Workspace mycam: searchCams){
			resp.getWriter().print(mycam.getName() + "<br/>");
		}
		
	}
}
