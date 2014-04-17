package george;

// Utilities class
//
import java.util.Collection;

public class U {
    public static float fmax(Float...a) {
        float cur = Float.NEGATIVE_INFINITY;
        for(float x : a) {
            if(x > cur) {
                cur = x;
            }
        }

        return cur;
    }

    public static float fmax(Collection<Float> a) {
        return fmax(a.toArray(new Float[0]));
    }

}
