package www.douglas;

import java.util.List;

/**
 * Created by wgz on 14/11/21.
 */
public class SpecifiedFieldExtractor extends FeatureNameExtractor {
    SpecifiedFieldExtractor() {
        super();
    }
    @Override
    public String extractFeatureName(List<String> inputfields) {
        if (inputfields == null | inputfields.size() == 0) {
            return null;
        }

        String key = inputFiledNames.get(0);
        int keyIndex = FieldDescription.getInstance().getValue(key);
        String valueField = inputfields.get(keyIndex);
        String value = valueField.substring(valueFromIdx, valueFromIdx + valueLen);
        return featurePrefix.concat(value);
    }

    public int getValueFromIdx() {
        return valueFromIdx;
    }

    public void setValueFromIdx(int valueFromIdx) {
        this.valueFromIdx = valueFromIdx;
    }

    public int getValueLen() {
        return valueLen;
    }

    public void setValueLen(int valueLen) {
        this.valueLen = valueLen;
    }

    int valueFromIdx = 0;
    int valueLen = 1;
}
