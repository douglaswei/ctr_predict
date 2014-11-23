package www.douglas;

import de.bwaldvogel.liblinear.FeatureNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wgz on 14/11/5.
 */

abstract class FeatureProcessor {
    abstract public double proc(
            List<String> fields,
            String descriton,
            Map<String, Integer> columnMap) throws ParseException;
}


public class FeatureNameFactory {
    public FeatureNameFactory() {
    }

}
