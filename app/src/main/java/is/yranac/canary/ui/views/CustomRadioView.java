package is.yranac.canary.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import is.yranac.canary.R;

/**
 * Created by sergeymorozov on 6/4/15.
 */
public class CustomRadioView extends CustomBaseView {

    TextView title;
    AppCompatRadioButton checkMark;
    boolean isChecked;

    public TextView getTitle() {
        if (title == null)
            title = (TextView) findViewById(R.id.row_title);
        return title;
    }

    public View getCheckMark() {
        if (checkMark == null)
            checkMark = (AppCompatRadioButton) findViewById(R.id.check_mark);
        return checkMark;
    }

    public CustomRadioView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        AppCompatRadioButton radio = (AppCompatRadioButton) getCheckMark();
        radio.setChecked(!isChecked());
    }

    @Override
    public void setChecked(boolean checked) {
        AppCompatRadioButton radio = (AppCompatRadioButton) getCheckMark();
        radio.setChecked(checked);
        isChecked = checked;
    }
}