package www.douglas;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by wgz on 14/11/4.
 */
public class StringProc {
    public static List<String> getFields(String line, String seprator) {
        List<String> fields = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(line, seprator);
        String toekn;
        while (st.hasMoreTokens()) {
            toekn = st.nextToken();
            fields.add(toekn);
        }
        return fields;
    }

    public static int BKDHash(String data) {
        int seed = 131; // 31 131 1313 13131 131313 etc..
        int hash = 0;
        for (int i = 0; i < data.length(); i++)
        {
            hash = (hash * seed) + data.charAt(i);
        }
        return Math.abs(hash);
    }
}
