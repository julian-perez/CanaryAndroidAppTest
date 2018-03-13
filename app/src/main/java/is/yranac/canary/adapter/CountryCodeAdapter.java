package is.yranac.canary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.ui.views.CustomBaseView;
import is.yranac.canary.util.StringUtils;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class CountryCodeAdapter extends ArrayAdapter<CountryCode> {

    private Context context;
    private String code;
    private boolean showCodes;
    private String name;

    public CountryCodeAdapter(Context context, List<CountryCode> objects, String code, boolean showCodes) {
        super(context, 0, objects);
        this.context = context;
        this.code = code;
        this.showCodes = showCodes;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setting_row_radio, parent, false);
        }

        CustomBaseView v = (CustomBaseView) convertView;

        CountryCode countryCode = getItem(position);
        String title = countryCode.name;
        if (showCodes){
            title += " " + countryCode.dialCode;
        }
        v.getTitle().setText(title);
        return v;
    }

    public void setCodeAndName(CountryCode code) {
        this.code = code.code;
        this.name = code.name;
        notifyDataSetChanged();
    }


    public String getCode() {
        return code;
    }

    public Object getName() {
        return name;
    }
}
