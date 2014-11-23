package www.douglas;

import java.util.List;

/**
 * Created by wgz on 14/11/11.
 */
public class SimpleFeatureNameExtractor extends FeatureNameExtractor {

    public SimpleFeatureNameExtractor() {
        super();
    }

    @Override
    public String extractFeatureName(List<String> inputfields) {
        if (inputfields == null || inputfields.isEmpty()) {
            return null;
        }
        String key = inputFiledNames.get(0);
        int valueIdx = FieldDescription.getInstance().getValue(key);
        if (valueIdx < 0 || inputfields.size() < valueIdx) {
            return null;
        }
        String value = key.concat("_" + inputfields.get(valueIdx));
        return value;
    }
}
