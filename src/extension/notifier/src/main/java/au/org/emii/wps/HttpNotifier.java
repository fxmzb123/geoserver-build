package au.org.emii.wps;

import java.io.IOException;
import java.net.URL;

public interface HttpNotifier {

    void notify(URL notificationUrl, URL wpsServerUrl, String uuid, String notificationParams) throws IOException;
}