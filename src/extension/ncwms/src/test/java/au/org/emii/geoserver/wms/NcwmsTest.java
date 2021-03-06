package au.org.emii.geoserver.wms;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.*;

import static org.apache.wicket.util.tester.WicketTesterHelper.assertEquals;

public class NcwmsTest {
    private Document getCapabilitiesDocument() throws Exception {
        SAXReader reader = new SAXReader();
        return reader.read(new FileInputStream("./src/test/resources/get_capabilities.xml"));
    }

    @Test
    public void testGetSupportedStyles() throws Exception {
        List<String> expectedStyles = new ArrayList<String>() {{
            add("barb");
            add("fancyvec");
            add("trivec");
            add("stumpvec");
            add("linevec");
            add("vector");
            add("boxfill");
        }};
        Collections.sort(expectedStyles);

        List<String> returnedStyles = Ncwms.getSupportedStyles(getCapabilitiesDocument(), "sea_water_velocity");
        Collections.sort(returnedStyles);

        assertEquals(expectedStyles, returnedStyles);
    }

    @Test
    public void testGetPalettes() throws Exception {
        List<String> expectedPalettes = new ArrayList<String>() {{
            add("alg");
            add("redblue");
            add("alg2");
            add("ncview");
            add("greyscale");
            add("occam");
            add("rainbow");
            add("sst_36");
            add("occam_pastel-30");
            add("ferret");
        }};
        Collections.sort(expectedPalettes);

        List<String> returnedPalettes = Ncwms.getPalettes(getCapabilitiesDocument(), "sea_water_velocity");
        Collections.sort(returnedPalettes);

        assertEquals(expectedPalettes, returnedPalettes);
    }
}
