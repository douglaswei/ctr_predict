package www.douglas;

/**
 * Created by wgz on 14/11/11.
 */
public class FeatureCounter {
    private static FeatureCounter ourInstance = new FeatureCounter();

    public static FeatureCounter getInstance() {
        return ourInstance;
    }

    private FeatureCounter() {
    }

    public int nextFeatureId() {
        return featureCout++;
    }

    private int featureCout = 1;
}
