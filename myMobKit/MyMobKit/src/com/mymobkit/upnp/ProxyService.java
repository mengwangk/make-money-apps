package com.mymobkit.upnp;

import org.teleal.cling.binding.annotations.*;

@UpnpService (
		serviceId = @UpnpServiceId("myMobKitProxyService"),
		serviceType= @UpnpServiceType(value = "myMobKitProxyService", version = 1)
)
@UpnpStateVariables(
        {
                @UpnpStateVariable(
                        name = "WebService",
                        defaultValue = ""
                )
        }
)
public class ProxyService {
	
	private String webService = "";

	public void setWebService(String webService) {
		this.webService = webService;
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "ResultWebService"))
	public String getWebService() {
		return webService;
	}
}
