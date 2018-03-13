package is.yranac.canary.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import is.yranac.canary.R;

/**
 * Created by sergeymorozov on 6/4/15.
 */
public class CustomCheckedView extends CustomBaseView {

    TextView title;
    ImageView checkMark;
    boolean isChecked;

    public TextView getTitle() {
        if (title == null)
            title = (TextView) findViewById(R.id.row_title);
        return title;
    }

    public View getCheckMark() {
        if (checkMark == null)
            checkMark = (ImageView) findViewById(R.id.check_mark);
        return checkMark;
    }

    public CustomCheckedView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {

    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            getTitle().setTextColor(getResources().getColor(R.color.dark_moderate_cyan));
            getCheckMark().setVisibility(VISIBLE);
        } else {
            //            getTitle().setTextColor(getResources().getColor(R.color.black));
            getCheckMark().setVisibility(INVISIBLE);
        }
        isChecked = checked;
    }
}
