package is.yranac.canary.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.util.Log;

/**
 * Created by Schroeder on 7/18/16.
 */
public class CustomRadioLocationView extends CustomBaseView {

    private static final String LOG_TAG = "CustomRadioLocationView";
    private TextView address;
    private TextView name;
    private AppCompatRadioButton checkMark;

    public CustomRadioLocationView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public TextView getTitle() {
        if (address == null)
            address = (TextView) findViewById(R.id.row_address);
        return address;
    }

    public AppCompatRadioButton getCheckMark() {
        if (checkMark == null)
            checkMark = (AppCompatRadioButton) findViewById(R.id.check_mark);
        return checkMark;
    }


    public TextView getName() {
        if (name == null)
            name = (TextView) findViewById(R.id.row_name);
        return name;
    }


    @Override
    public boolean isChecked() {
        return getCheckMark().isChecked();
    }

    @Override
    public void toggle() {
        AppCompatRadioButton radio = getCheckMark();
        radio.setChecked(!isChecked());
    }

    @Override
    public void setChecked(boolean checked) {
        AppCompatRadioButton radio = getCheckMark();
        radio.setChecked(checked);
    }
}
