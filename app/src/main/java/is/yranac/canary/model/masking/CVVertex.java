package is.yranac.canary.model.masking;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class CVVertex {

    @SerializedName("x")
    public float x;

    @SerializedName("y")
    public float y;

    public CVVertex() {
    }

    public CVVertex(float x, float y) {
        this.x = formatFloat(x);
        this.y = formatFloat(y);
    }


    public void setX(float x) {
        this.x = formatFloat(x);
    }

    public void setY(float y) {
        this.y = formatFloat(y);
    }

    private float formatFloat(float i) {
        BigDecimal b = new BigDecimal(i);
        b = b.round(new MathContext(4));
        return b.floatValue();
    }

    public CVVertex clone() {
        CVVertex newVertex = new CVVertex();
        newVertex.x = this.x;
        newVertex.y = this.y;

        return newVertex;
    }
}
