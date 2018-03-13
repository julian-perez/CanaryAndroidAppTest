package is.yranac.canary.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import is.yranac.canary.R;
import is.yranac.canary.util.Log;

/**
 * Created by Schroeder on 9/24/15.
 */
public class RadioButtonPlus extends RadioButton {
    private static final String TAG = "RadioButtonPlus";


    public RadioButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public RadioButtonPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private void setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
        }

        setTypeface(tf);
    }
}
