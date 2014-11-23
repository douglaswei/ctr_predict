package www.douglas;

import java.util.List;

/**
 * Created by wgz on 14/11/11.
 */
public abstract class FeatureNameExtractor {
    public FeatureNameExtractor() {
        featurePrefix = String.valueOf(FeatureCounter.getInstance().nextFeatureId()).concat("_");
    }

    public abstract String extractFeatureName(
            List<String> inputfields);

    public List<String> getInputFiledNames() {
        return inputFiledNames;
    }

    public void setInputFiledNames(List<String> inputFiledNames) {
        this.inputFiledNames = inputFiledNames;
    }

    protected List<String> inputFiledNames;
    protected String featurePrefix;

    public String getFeaturePrefix() {
        return featurePrefix;
    }

    public void setFeaturePrefix(String featurePrefix) {
        this.featurePrefix = featurePrefix;
    }
}

