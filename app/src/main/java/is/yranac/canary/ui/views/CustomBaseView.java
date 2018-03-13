package is.yranac.canary.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sergeymorozov on 5/20/16.
 */
public abstract class CustomBaseView extends RelativeLayout implements Checkable {

    public abstract TextView getTitle();

    public abstract View getCheckMark();

    public CustomBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
