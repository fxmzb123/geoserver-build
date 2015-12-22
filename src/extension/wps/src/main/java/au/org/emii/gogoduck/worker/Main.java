package au.org.emii.gogoduck.worker;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, String> urlMangling = new HashMap<String, String>();
    static {
        urlMangling.put("^CARS2009_Australia_weekly.nc$", "/mnt/imos-t3/climatology/CARS/2009/eMII-product/CARS2009_Australia_weekly.nc");
        urlMangling.put("^CARS2009_World_monthly.nc$", "/mnt/imos-t3/climatology/CARS/2009/eMII-product/CARS2009_World_monthly.nc");
        urlMangling.put("^/mnt/imos-t3/", "https://data.aodn.org.au/");
        urlMangling.put("^/mnt/opendap/2/SRS/", "https://thredds.aodn.org.au/thredds/fileServer/IMOS/SRS/");
        urlMangling.put("^IMOS/", "http://imos-data.aodn.org.au/IMOS/");
    }

    public static Map<String, String> getUrlMangling() {
        return urlMangling;
    }

    public static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("gogoduck-worker", options);

        System.exit(3);
    }

    public static void main(String[] args) {
        URLMangler.setUrlManglingMap(getUrlMangling());

        Options options = new Options();

        options.addOption("g", "geoserver", true, "Geoserver to get list of URLs from. Default is http://geoserver-123.aodn.org.au/geoserver");
        options.addOption("l", "limit", true, "Maximum amount of file to allow processing of.");
        options.addOption("n", "threads", true, "Set thread count for operation (default is 1).");
        options.addOption("o", "output", true, "Output file to use.");
        options.addOption("p", "profile", true, "Profile to apply");
        options.addOption("s", "subset", true, "Subset to apply, semi-colon separated.");
        options.addOption("S", "score", false, "Only output job score, then quit.");
        options.addOption("u", "user-log", true, "Output file for user logging.");
        options.addOption("t", "tmp-dir", true, "Set temporary directory for operation.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e) {
            usage(options);
        }

        String geoserver = cmd.getOptionValue("g", "http://geoserver-123.aodn.org.au/geoserver");
        String limit = cmd.getOptionValue("l", "100");
        String threadCount = cmd.getOptionValue("n", "1");
        String outputFile = cmd.getOptionValue("o");
        String profile = cmd.getOptionValue("p");
        String subset = cmd.getOptionValue("s", "");
        String userLog = cmd.getOptionValue("u");
        String tmpDir = cmd.getOptionValue("t", System.getProperty("java.io.tmpdir"));

        if (null == outputFile) { usage(options); }
        if (null == profile) { usage(options); }

        GoGoDuck ggd = new GoGoDuck(geoserver, profile, subset, outputFile, Integer.parseInt(limit));

        if(cmd.hasOption("n")) {
            ggd.setThreadCount(Integer.parseInt(threadCount));
        }

        if(cmd.hasOption("t")) {
            ggd.setTmpDir(tmpDir);
        }

        if(cmd.hasOption("u")) {
            ggd.setUserLog(userLog);
        }

        if (cmd.hasOption("S")) {
            ggd.score();
        }
        else {
            ggd.run();
        }
    }
}
