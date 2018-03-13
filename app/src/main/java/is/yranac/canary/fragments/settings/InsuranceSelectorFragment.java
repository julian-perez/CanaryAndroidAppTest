package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.fragments.GenericWebviewFragment;

/**
 * Created by Schroeder on 4/19/16.
 */
public class InsuranceSelectorFragment extends SettingsFragment {

    public enum InsuranceType {
        STATE_FARM,
        LIBERTY_MUTUAL
    }


    public static InsuranceSelectorFragment newInstance(InsuranceType insuranceType) {

        InsuranceSelectorFragment fragment = new InsuranceSelectorFragment();
        Bundle args = new Bundle();
        args.putSerializable("insuranceType", insuranceType);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insurance_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        InsuranceType insuranceType = (InsuranceType) getArguments().getSerializable("insuranceType");

        TextView headerTextView = (TextView) view.findViewById(R.id.header_title_text_view);
        headerTextView.setText(R.string.discount_eligibility);
        view.findViewById(R.id.header_view_left_button).setVisibility(View.GONE);
        view.findViewById(R.id.header_view_right_button).setVisibility(View.GONE);

        ImageView logo = (ImageView) view.findViewById(R.id.insurance_logo_image_view);
        TextView insuranceText = (TextView) view.findViewById(R.id.insurance_optin_dsc_large);

        switch (insuranceType) {
            case LIBERTY_MUTUAL:
                logo.setImageResource(R.drawable.libertymutual_large);
                insuranceText.setText(R.string.insurance_liberty_dsc);
                break;

            case STATE_FARM:
                logo.setImageResource(R.drawable.statefarm_large);
                insuranceText.setText(R.string.insurance_state_farm_dsc);
                break;

            default:
                return;
        }


        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InsuranceSharingFragment fragment = new InsuranceSharingFragment();
                fragment.setArguments(getArguments());
                addSecondModalFragment(fragment);


            }
        });

        View moreAboutInsurance = view.findViewById(R.id.more_about_insurance_text_view);
        moreAboutInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenericWebviewFragment fragment = GenericWebviewFragment.newInstance(Constants.CANARY_INSURANCE(), R.string.canary_insurance_url_header);
                addModalFragment(fragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

        getActivity().onBackPressed();
    }
}
