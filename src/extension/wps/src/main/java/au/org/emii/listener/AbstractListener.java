package au.org.emii.listener;

import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;

import net.opengis.wps10.ExecuteType;

import org.geoserver.wps.ProcessListenerAdapter;
import org.geoserver.ows.Dispatcher;
import org.geoserver.config.GeoServer;
import org.geoserver.wps.resource.WPSResourceManager;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.emii.notifier.HttpNotifier;

public class AbstractListener extends ProcessListenerAdapter {

	private final WPSResourceManager resourceManager;
	private final HttpNotifier httpNotifier;
	private final GeoServer geoserver;
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractListener.class);
	
	protected AbstractListener(HttpNotifier httpNotifier, GeoServer geoserver, WPSResourceManager resourceManager) {
		this.httpNotifier = httpNotifier;
	    this.geoserver = geoserver;
	    this.resourceManager = resourceManager;
	}
	
    protected URL getWpsUrl() throws MalformedURLException {
        return new URL(ResponseUtils.appendPath(getBaseUrl(), "ows"));
    }

    protected String getBaseUrl() {
        String url = ((geoserver != null) ? geoserver.getSettings().getProxyBaseUrl() : null);

        if (url == null) {
            Operation op = Dispatcher.REQUEST.get().getOperation();
            ExecuteType execute = (ExecuteType) op.getParameters()[0];
            url = execute.getBaseUrl();
        }

        return url;
    }

    protected String getId() {
        return resourceManager.getExecutionId(true);
    }
	
    protected void notifySuccess(URL callbackUrl, String callbackParams) {
        boolean successful = true;
        notify(successful, callbackUrl, callbackParams);
    }

    protected void notifyFailure(URL callbackUrl, String callbackParams) {
        boolean successful = false;
        notify(successful, callbackUrl, callbackParams);
    }

    protected void notify(boolean successful, URL callbackUrl, String callbackParams) {
        if (callbackUrl == null) return;

        try {
            httpNotifier.notify(callbackUrl, getWpsUrl(), getId(), successful, callbackParams);
        } catch (IOException ioe) {
            logger.error("Could not call callback", ioe);
        }
    }
}