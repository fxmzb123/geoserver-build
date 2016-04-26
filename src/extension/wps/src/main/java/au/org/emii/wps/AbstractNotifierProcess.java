package au.org.emii.wps;

import net.opengis.wps10.ExecuteType;

import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geoserver.wps.resource.WPSResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractNotifierProcess implements GeoServerProcess {
    private final WPSResourceManager resourceManager;
    private static final Logger logger = LoggerFactory.getLogger(AbstractNotifierProcess.class);

    protected AbstractNotifierProcess(WPSResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    protected WPSResourceManager getResourceManager() {
        return resourceManager;
    }

    protected String getWorkingDir() {
        try {
             // Use WPSResourceManager to create a temporary directory that will get cleaned up
             // when the process has finished executing (Hack! Should be a method on the resource manager)
             return resourceManager.getTemporaryResource("").dir().getAbsolutePath();
        } catch (Exception e) {
             logger.info("Exception accessing working directory: \n" + e);
             return System.getProperty("java.io.tmpdir");
        }
    }

}
