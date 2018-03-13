package is.yranac.canary.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;

import is.yranac.canary.text.style.CustomTypefaceSpan;

/**
 * Created by sergeymorozov on 6/5/15.
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String capitalize(final String str, @Nullable final char... delimiters) {
        final int delimLen = delimiters == null ? -1 : delimiters.length;
        if (StringUtils.isNullOrEmpty(str) || delimLen == 0) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    private static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (final char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }

    public static SpannableStringBuilder spannableStringBuilder(Context context, int string1Id, int string2Id) {
        return spannableStringBuilder(context, string1Id, string2Id, "Gibson-Light.otf", "Gibson.otf");
    }

    public static SpannableStringBuilder spannableStringBuilder(Context context, int string1Id, int string2Id,
                                                                String fontLeft, String fontRight) {
        String string1 = context.getResources().getString(string1Id);
        String string2 = context.getResources().getString(string2Id);

        return spannableStringBuilder(context, string1, string2, fontLeft, fontRight);
    }

    public static SpannableStringBuilder spannableStringBuilder(Context context, String string1, String string2,
                                                                String fontLeft, String fontRight) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        Typeface font = Typeface.createFromAsset(context.getResources().getAssets(), fontLeft);
        Typeface font2 = Typeface.createFromAsset(context.getResources().getAssets(), fontRight);

        SpannableString notUsingSecuredNetworkSpannable = new SpannableString(string1);

        notUsingSecuredNetworkSpannable.setSpan(new CustomTypefaceSpan("", font, DensityUtil.dip2px(context, 14.0f)), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        SpannableString needWifiTipsSpanable = new SpannableString(string2);

        needWifiTipsSpanable.setSpan(new CustomTypefaceSpan("", font2, DensityUtil.dip2px(context, 14.0f)), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        builder.append(notUsingSecuredNetworkSpannable);
        builder.append(" ");
        builder.append(needWifiTipsSpanable);

        return builder;
    }

    public static SpannableStringBuilder spannableStringBuilder(Context context, String string1, String string2,
                                                                String fontLeft, String fontRight, float fontSize1, float fontSize2) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        Typeface font = Typeface.createFromAsset(context.getResources().getAssets(), fontLeft);
        Typeface font2 = Typeface.createFromAsset(context.getResources().getAssets(), fontRight);

        SpannableString notUsingSecuredNetworkSpannable = new SpannableString(string1);

        notUsingSecuredNetworkSpannable.setSpan(new CustomTypefaceSpan("", font, DensityUtil.dip2px(context, fontSize1)), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        SpannableString needWifiTipsSpanable = new SpannableString(string2);

        needWifiTipsSpanable.setSpan(new CustomTypefaceSpan("", font2, DensityUtil.dip2px(context, fontSize2)), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        builder.append(notUsingSecuredNetworkSpannable);
        builder.append(" ");
        builder.append(needWifiTipsSpanable);

        return builder;
    }


    public static SpannableStringBuilder spannableStringBuilder(Context context, int string1Id, int string2Id,
                                                                String fontLeft, String fontRight, float fontSize1, float fontSize2) {
        String string1 = context.getResources().getString(string1Id);
        String string2 = context.getResources().getString(string2Id);
        return spannableStringBuilder(context, string1, string2, fontLeft, fontRight, fontSize1, fontSize2);
    }

    public static SpannableStringBuilder spannableStringBuilderColors(Context context, int string1Id, int string2Id,
                                                                      int colorLeft, int colorRight) {
        String string1 = context.getResources().getString(string1Id);
        String string2 = context.getResources().getString(string2Id);
        SpannableStringBuilder builder = new SpannableStringBuilder();


        Typeface font = Typeface.createFromAsset(context.getResources().getAssets(), "Gibson.otf");
        Typeface font2 = Typeface.createFromAsset(context.getResources().getAssets(), "Gibson-Light.otf");

        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());

        SpannableString notUsingSecuredNetworkSpannable = new SpannableString(string1);

        notUsingSecuredNetworkSpannable.setSpan(new ForegroundColorSpan(colorLeft), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        notUsingSecuredNetworkSpannable.setSpan(new CustomTypefaceSpan("", font, size), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        SpannableString needWifiTipsSpanable = new SpannableString(string2);

        needWifiTipsSpanable.setSpan(new ForegroundColorSpan(colorRight), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        needWifiTipsSpanable.setSpan(new CustomTypefaceSpan("", font2, size), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        builder.append(notUsingSecuredNetworkSpannable);
        builder.append(" ");
        builder.append(needWifiTipsSpanable);

        return builder;
    }
}
