package is.yranac.canary.nativelibs.nativeclasses;


/**
 * Created by Schroeder on 8/26/14.
 */
public class BlackBox {


    static {
        System.loadLibrary("blackbox");
    }

    public native void encode(String destination, String message);
    public native String decode(short[] message, float factor, int direction);

    private static BlackBox blackBox;

    public static BlackBox instance() {
        if (blackBox == null) {
            blackBox = new BlackBox();
        }
        return blackBox;
    }
}
