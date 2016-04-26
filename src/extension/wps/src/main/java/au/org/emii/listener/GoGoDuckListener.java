package au.org.emii.listener;

import java.net.URL;

import org.geoserver.config.GeoServer;
import org.geoserver.wps.resource.WPSResourceManager;
import org.geoserver.wps.ProcessEvent;
import org.geoserver.wps.WPSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.emii.notifier.HttpNotifier;

public class GoGoDuckListener extends AbstractListener {
	
	private static final Logger logger = LoggerFactory.getLogger(GoGoDuckListener.class);
	
	public GoGoDuckListener(HttpNotifier httpNotifier, GeoServer geoserver, WPSResourceManager resourceManager) {
	    super(httpNotifier, geoserver, resourceManager);
	}
    
	private URL getCallbackUrl(ProcessEvent event) {
		return (URL)event.getInputs().get("callbackUrl");
	}
	
    private String getCallbackParams(ProcessEvent event) {
		return (String)event.getInputs().get("callbackParams");
	}
	
	@Override
    public void succeeded(ProcessEvent event) throws WPSException {
		
		URL callbackUrl = this.getCallbackUrl(event);
		String callbackParams = this.getCallbackParams(event);
		
		notifySuccess(callbackUrl, callbackParams);
    }
	
	@Override
	public void failed(ProcessEvent event) {
		URL callbackUrl = this.getCallbackUrl(event);
		String callbackParams = this.getCallbackParams(event);
		
        notifyFailure(callbackUrl, callbackParams);
	}
}