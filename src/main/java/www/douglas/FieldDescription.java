package www.douglas;

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
}
