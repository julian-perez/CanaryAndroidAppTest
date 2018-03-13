package is.yranac.canary.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import is.yranac.canary.R;
import is.yranac.canary.text.style.CustomTypefaceSpan;
import is.yranac.canary.ui.views.CustomBaseView;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 6/4/15.
 */
public class SettingsSingleSelectionAdapter extends ArrayAdapter<String> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * instantiating views.
     * @param objects  The objects to represent in the ListView.
     */

    Context context;
    String[] predefinedDeviceNames;
    String[] comments;
    int resource;

    public SettingsSingleSelectionAdapter(Context context, String[] objects, int resource) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.predefinedDeviceNames = objects;
        this.comments = null;
    }


    public SettingsSingleSelectionAdapter(Context context, String[] objects, String[] comments, int resource) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.predefinedDeviceNames = objects;
        this.comments = comments;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }

        String name = predefinedDeviceNames[position];
        CustomBaseView v = (CustomBaseView) convertView;

        if (comments != null && position < comments.length && !StringUtils.isNullOrEmpty(comments[position])) {
            String comment = comments[position];

            String separator = "   ";
            Spannable commentSpan = new SpannableString(name + separator + comment);
            int commentStart = name.length();
            int commentEnd = commentStart + comment.length() + separator.length();

            commentSpan.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(context, R.color.dark_gray)),
                    commentStart,
                    commentEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Typeface font = Typeface.createFromAsset(context.getResources().getAssets(), "Gibson.otf");

            commentSpan.setSpan(
                    new CustomTypefaceSpan("", font, DensityUtil.dip2px(context, 13.0f)),
                    commentStart,
                    commentEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            v.getTitle().setText(commentSpan);
        } else
            v.getTitle().setText(name);


        return convertView;
    }
}
