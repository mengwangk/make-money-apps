package com.mymobkit.upnp;

import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.Icon;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;

public final class ProxyDevice {
	
	public static LocalDevice createDevice(String webService) throws ValidationException {
		DeviceIdentity identity = new DeviceIdentity(
			UDN.uniqueSystemIdentifier("com.mymobkit")
		);

		DeviceType type = new UDADeviceType("myMobKitProxyService", 1);

		DeviceDetails details =
			new DeviceDetails(
					"myMobKit proxy device",
					new ManufacturerDetails("mymobkit.com"),
					new ModelDetails(
							"myMobKit proxy v1",
							"A mobile proxy device.",
							"v1"
					)
			);

		Icon icon =	null;

		LocalService<ProxyService> smsSenderService = new AnnotationLocalServiceBinder().read(ProxyService.class);

		smsSenderService.setManager(new DefaultServiceManager<ProxyService>(smsSenderService, ProxyService.class));
		smsSenderService.getManager().getImplementation().setWebService(webService);

		return new LocalDevice(identity, type, details, icon, smsSenderService);
	}
}
