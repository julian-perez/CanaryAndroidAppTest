package is.yranac.canary.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.databinding.EditTextWithLabelBinding;

/**
 * Created by michaelschroeder on 12/20/16.
 */

public class EditTextWithLabel extends RelativeLayout implements View.OnTouchListener {

    private static final String LOG_TAG = "EditTextWithLabel";
    private EditTextWithLabelBinding binding;
    private int validation;
    private int labelColor;
    private boolean hasFocus;

    public EditTextWithLabel(Context context) {
        super(context);
        init(context, null);
    }


    public EditTextWithLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public EditTextWithLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = EditTextWithLabelBinding.inflate(LayoutInflater.from(context), this, true);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithLabel);
        String label = a.getString(R.styleable.EditTextWithLabel_label);
        String hint = a.getString(R.styleable.EditTextWithLabel_hint);
        String text = a.getString(R.styleable.EditTextWithLabel_android_text);
        boolean editable = a.getBoolean(R.styleable.EditTextWithLabel_editable, true);
        labelColor = a.getColor(R.styleable.EditTextWithLabel_labelColor, ContextCompat.getColor(context, R.color.gray));
        int textColor = a.getColor(R.styleable.EditTextWithLabel_textColor, ContextCompat.getColor(context, R.color.black));
        int inputType = a.getInt(R.styleable.EditTextWithLabel_android_inputType, InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        int maxLength = a.getInt(R.styleable.EditTextWithLabel_android_maxLength, 50);
        a.recycle();

        if (TextUtils.isEmpty(label)) {
            binding.label.setVisibility(GONE);
        } else {
            binding.label.setText(label);
        }

        binding.editText.setHint(hint);
        binding.editText.setText(text);

        Typeface typeface = binding.editText.getTypeface();

        binding.editText.setInputType(inputType);
        binding.label.setTextColor(labelColor);
        binding.editText.setTextColor(textColor);
        binding.editText.setTypeface(typeface);
        binding.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        binding.editText.setClickable(editable);
        binding.editText.setFocusable(editable);
        binding.editText.setLongClickable(editable);

        if (!editable) {
            binding.editText.setOnTouchListener(this);
            binding.editText.setFocusable(false);

        }

        binding.editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        EditTextWithLabel.this.hasFocus = hasFocus;
                        if (hasFocus) {
                            resetColors();
                        }
                        setTextViewColor(hasFocus);
                    }
                }
        );
    }


    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.editTextString = binding.editText.getEditableText().toString();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        binding.editText.setText(ss.editTextString);
    }


    private void setTextViewColor(boolean hasFocus) {
        int color;
        if (hasFocus) {
            color = ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan);
        } else {
            color = labelColor;
        }
        binding.label.setTextColor(color);
    }

    public TextView getLabel() {
        return binding.label;
    }

    public View getLine() {
        return binding.line;
    }

    public EditText getEditText() {
        return binding.editText;
    }

    public void setText(String text) {
        binding.editText.setText(text);
    }

    public void setText(@StringRes int text) {
        binding.editText.setText(text);

    }

    public String getText() {
        return binding.editText.getText().toString();
    }

    public void setCursorVisible(boolean b) {
        binding.editText.setCursorVisible(b);
    }

    public void setValidation(int validation) {
        this.validation = validation;
    }

    public int getValidation() {
        return validation;
    }

    public void showErrorState() {
        int red = ContextCompat.getColor(getContext(), R.color.red);
        binding.label.setTextColor(red);
        binding.line.setBackgroundColor(red);
    }

    public void resetColors() {
        if (!hasFocus) {
            int gray = ContextCompat.getColor(getContext(), R.color.dark_gray);
            binding.label.setTextColor(gray);
            binding.line.setBackgroundColor(gray);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        onTouchEvent(event);
        return false;
    }

    static class SavedState extends BaseSavedState {
        String editTextString;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.editTextString = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.editTextString);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        binding.editText.addTextChangedListener(textWatcher);
    }

}
