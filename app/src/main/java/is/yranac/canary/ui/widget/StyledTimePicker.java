package is.yranac.canary.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

import is.yranac.canary.R;

/**
 * Created by Schroeder on 4/15/16.
 */
public class StyledTimePicker extends TimePicker {

    public StyledTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        Class<?> internalRID = null;
        try {
            internalRID = Class.forName("com.android.internal.R$id");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field month = null;
        try {
            month = internalRID.getField("hour");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        NumberPicker npMonth = null;
        try {
            npMonth = (NumberPicker) findViewById(month.getInt(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Field day = null;
        try {
            day = internalRID.getField("minute");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        NumberPicker npDay = null;
        try {
            npDay = (NumberPicker) findViewById(day.getInt(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Field year = null;
        try {
            year = internalRID.getField("amPm");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        NumberPicker npYear = null;
        try {
            npYear = (NumberPicker) findViewById(year.getInt(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Class<?> numberPickerClass = null;
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field selectionDivider = null;
        try {
            selectionDivider = numberPickerClass.getDeclaredField("mSelectionDivider");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            selectionDivider.setAccessible(true);
            selectionDivider.set(npMonth, getResources().getDrawable(
                    R.color.dark_moderate_cyan));
            selectionDivider.set(npDay, getResources().getDrawable(
                    R.color.dark_moderate_cyan));
            selectionDivider.set(npYear, getResources().getDrawable(
                    R.color.dark_moderate_cyan));
        } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setNumberPickerTextColor(npMonth, getResources().getColor(
                R.color.black));
        setNumberPickerTextColor(npDay, getResources().getColor(
                R.color.black));
        setNumberPickerTextColor(npYear, getResources().getColor(
                R.color.black));

    }

    private boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    EditText editText = (EditText) child;
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    editText.setTextColor(color);
                    setCustomFont(editText, "Gibson-Light.otf");
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ignored) {
                }
            }
        }
        return false;
    }

    public void setCustomFont(EditText text, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(getContext().getAssets(), asset);
        } catch (Exception ignored) {
        }

        text.setTypeface(tf);
    }

}