package www.douglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wgz on 14/11/11.
 */
public class FieldDescription {
    private static FieldDescription ourInstance = new FieldDescription();

    public static FieldDescription getInstance() {
        return ourInstance;
    }

    private FieldDescription() {
    }

    public void init(String line) {
        List<String> fields = StringProc.getFields(line, ",");
        for (int index = 0; index < fields.size(); ++index) {
            setValue(fields.get(index), index);
        }
    }


    public int getValue(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return -1;
        }
    }

    private void setValue(String key, int value) {
        map.put(key, value);
    }

    private Map<String, Integer> map = new HashMap<String, Integer>();
}
