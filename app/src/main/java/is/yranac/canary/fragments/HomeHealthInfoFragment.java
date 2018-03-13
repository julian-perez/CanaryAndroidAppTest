package is.yranac.canary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import is.yranac.canary.R;

public class HomeHealthInfoFragment extends Fragment {
    private static final String LOG_TAG = "HomeHealthInfoFragment";

    public static final int TEMPERATURE = 0;
    public static final int HUMIDITY = 1;
    public static final int AIR_QUALITY = 2;

    public static HomeHealthInfoFragment newInstance(int infoType) {
        HomeHealthInfoFragment fragment = new HomeHealthInfoFragment();
        Bundle args = new Bundle();
        args.putInt("infoType", infoType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homehealth_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int infoType = getArguments().getInt("infoType");

        LinearLayout header = (LinearLayout) view.findViewById(R.id.header);
        header.findViewById(R.id.header_view_left_button).setVisibility(View.GONE);
        header.findViewById(R.id.header_view_right_button).setVisibility(View.GONE);

        switch (infoType) {
            case TEMPERATURE:
                ((TextView) header.findViewById(R.id.header_title_text_view)).setText(R.string.temperature);
                view.findViewById(R.id.temperature_info).setVisibility(View.VISIBLE);
                break;
            case HUMIDITY:
                ((TextView) header.findViewById(R.id.header_title_text_view)).setText(R.string.humidity);
                view.findViewById(R.id.humidity_info).setVisibility(View.VISIBLE);
                break;
            case AIR_QUALITY:
                ((TextView) header.findViewById(R.id.header_title_text_view)).setText(R.string.air_quality);
                view.findViewById(R.id.air_quality_info).setVisibility(View.VISIBLE);
                break;
        }


    }
}

