package is.yranac.canary.ui.views;

import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.util.Log;

public class CanaryTextWatcher implements TextWatcher {
    public static final String LOG = "CanaryTextWatcher";

    public static final int ANY_TEXT = 0;
    public static final int VALID_EMAIL = 1;
    public static final int VALID_PASSWORD = 2;
    public static final int VALID_PHONE = 3;

    public static final int TEXT_COLOR_OK = R.color.dark_moderate_cyan;
    public static final int TEXT_COLOR_INVALID = R.color.gray;

    private int validation;
    private boolean isValidText = false;
    private boolean mStopFormatting;
    private boolean mSelfChange;

    public CanaryTextWatcher(int validation) {
        this.validation = validation;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        isValidText = validateString(s, validation);

        if (mSelfChange) {
            // Ignore the change caused by s.replace().
            return;
        }

        if (validation == CanaryTextWatcher.VALID_PHONE && isValidText) {
            formatPhoneNumber(s);
        }

    }

    private void formatPhoneNumber(Editable s) {
        String formatted;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            formatted = PhoneNumberUtils.formatNumber(s.toString(), Locale.US.getCountry());
        } else {
            formatted = PhoneNumberUtils.formatNumber(s.toString());
        }

        if (formatted != null) {
            mSelfChange = true;
            s.replace(0, s.length(), formatted, 0, formatted.length());
            mSelfChange = false;
        }
    }

    // method has parameters so that it can be called statically if needed
    public static boolean validateString(CharSequence s, int validation) {
        if (s.toString()
                .length() == 0) {
            return false;
        } else {
            switch (validation) {
                case VALID_EMAIL:
                    if (!emailValid(s.toString())) {
                        return false;
                    }
                    break;
                case VALID_PHONE:
                    if (!phoneValid(s.toString())) {
                        return false;
                    }
                    break;
            }
        }

        // case ANY_TEXT:
        return true;
    }

    private static boolean phoneValid(String number) {
        String trimedNumber = number.replaceAll("\\D+", "");

        return trimedNumber.length() >= 6;
    }


    private static boolean emailValid(String email) {

        if (TextUtils.isEmpty(email))
            return false;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;

        if (email.length() < 5)
            return false;

        String[] stringSplit = email.split("@");

        if (email.contains(" "))
            return false;

        if (stringSplit.length != 2)
            return false;

        String secondHalf = stringSplit[1];

        Log.i(LOG, secondHalf);
        String[] secondHalfSplit = secondHalf.split("\\.");

        return secondHalfSplit.length >= 2;


    }

    public boolean getIsTextValid() {
        return isValidText;
    }

    public static boolean validateString(EditTextWithLabel editText) {
        return validateString(editText.getText(), editText.getValidation());
    }
}
