package www.douglas;

import java.util.*;

/**
 * Created by wgz on 14/11/5.
 */

public class FeatureNameFactory {
    public FeatureNameFactory() {
    }

    public List<String> extractFeatureNames(List<String> inputFields) {
        List<String> featureNames = new ArrayList<String>();
        for (FeatureNameExtractor featureNameExtractor : featureNameExtractors) {
            String featureName = featureNameExtractor.extractFeatureName(inputFields);
            if (featureName != null) {
                featureNames.add(featureName);
            }
        }
        return featureNames;
    }

    public List<FeatureNameExtractor> getFeatureNameExtractors() {
        return featureNameExtractors;
    }

    public void setFeatureNameExtractors(List<FeatureNameExtractor> featureNameExtractors) {
        this.featureNameExtractors = featureNameExtractors;
    }

    List<FeatureNameExtractor> featureNameExtractors = null;
}
