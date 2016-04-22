package au.org.emii.gogoduck.worker;


public class GoGoDuckModule_cft extends GoGoDuckModule {
    @Override
    public SubsetParameters getSubsetParameters() {
        SubsetParameters subsetParametersNew = new SubsetParameters(subset);
        subsetParametersNew.remove("TIME");

        // Rename LATITUDE -> lat
        // Rename LONGITUDE -> lon
        subsetParametersNew.put("lat", subset.get("LATITUDE"));
        subsetParametersNew.put("lon", subset.get("LONGITUDE"));

        subsetParametersNew.remove("LATITUDE");
        subsetParametersNew.remove("LONGITUDE");
  
        return subsetParametersNew;
    }
}