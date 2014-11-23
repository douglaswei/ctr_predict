package www.douglas;

import de.bwaldvogel.liblinear.FeatureNode;

import java.util.Comparator;

/**
 * Created by wgz on 14/11/11.
 */
class FeatureComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        return ((FeatureNode) arg0).getIndex() - ((FeatureNode) arg1).getIndex();
    }

}
