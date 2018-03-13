package is.yranac.canary.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.adapter.CountryCodeAdapter;
import is.yranac.canary.databinding.FragmentsettingsCountryCodeSelectBinding;
import is.yranac.canary.fragments.settings.SettingsFragment;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_LANGUAGE_UPDATE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_CUSTOMER;

public class CountryCodeSelectFragment extends SettingsFragment {


    private static final String LOG_TAG = "CountryCodeSelectFragment";
    private List<CountryCode> countryCodes;
    private boolean isCreatingNewAccount;

    public enum LIST_TYPE {
        COUNTRY,
        STATE,
        COUNTRY_CODE,
        LOCALE

    }

    public static CountryCodeSelectFragment newInstance(String code, LIST_TYPE list_type) {
        return newInstance(code, list_type, true, false);
    }

    public static CountryCodeSelectFragment newInstance(String code, LIST_TYPE list_type, boolean header, boolean isCreatingNewAccount) {

        Bundle args = new Bundle();
        args.putString("code", code);
        args.putSerializable("list_type", list_type);
        args.putBoolean("header", header);
        args.putBoolean("isCreatingNewAccount", isCreatingNewAccount);
        CountryCodeSelectFragment fragment = new CountryCodeSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface CountryCodeComplete {
        void countryCodeSelected(CountryCode countryCode);
    }

    private CountryCodeComplete countryCodeComplete = null;

    private boolean showHeader;
    private CountryCodeAdapter adapter;

    private FragmentsettingsCountryCodeSelectBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmentsettings_country_code_select, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments();

        showHeader = args.getBoolean("header");
        isCreatingNewAccount = args.getBoolean("isCreatingNewAccount");

        if (!showHeader) {
            binding.header.getRoot().setVisibility(View.GONE);
        }
        final LIST_TYPE listType = (LIST_TYPE) args.getSerializable("list_type");
        if (listType == null)
            return;

        String jsonString;
        if (listType == LIST_TYPE.COUNTRY || listType == LIST_TYPE.COUNTRY_CODE) {
            jsonString = Utils.loadJSONFromRawFile(R.raw.countries);
        } else if (listType == LIST_TYPE.STATE) {
            jsonString = Utils.loadJSONFromRawFile(R.raw.states);
        } else if (listType == LIST_TYPE.LOCALE) {
            jsonString = Utils.loadJSONFromRawFile(R.raw.locale);
        } else {
            return;
        }
        if (jsonString == null)
            return;


        Type type = new TypeToken<ArrayList<CountryCode>>() {
        }.getType();
        countryCodes = new Gson().fromJson(jsonString, type);

        if (countryCodes == null)
            return;
        Collections.sort(countryCodes, new Comparator<CountryCode>() {
            @Override
            public int compare(CountryCode lhs, CountryCode rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);

            }
        });

        String code = args.getString("code");
        if (code == null) {
            code = Locale.getDefault().getLanguage();
        }


        switch (listType) {
            case COUNTRY:
                binding.header.headerTitleTextView.setText(R.string.country);
                adapter = new CountryCodeAdapter(getActivity(), countryCodes, code, false);
                break;
            case COUNTRY_CODE:
                binding.header.headerTitleTextView.setText(R.string.country_code);
                adapter = new CountryCodeAdapter(getActivity(), countryCodes, code, true);
                break;
            case STATE:
                binding.header.headerTitleTextView.setText(R.string.state);
                adapter = new CountryCodeAdapter(getActivity(), countryCodes, code, false);
                break;
            case LOCALE:
                binding.header.headerTitleTextView.setText(R.string.change_language);
                adapter = new CountryCodeAdapter(getActivity(), countryCodes, code, false);
                break;
            default:
                return;

        }

        binding.header.headerViewLeftButton.setVisibility(View.GONE);
        binding.header.headerViewRightButton.setVisibility(View.GONE);

        binding.countryCodeListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        binding.countryCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode countryCode = countryCodes.get(position);
                if (adapter.getCode().equalsIgnoreCase(countryCode.code)) {
                    return;
                }

                if (listType == LIST_TYPE.LOCALE && showHeader && !isCreatingNewAccount) {
                    changeLocale(countryCode);
                    return;
                }
                binding.countryCodeListView.setItemChecked(position, true);

                adapter.setCodeAndName(countryCode);
                if (countryCodeComplete != null) {
                    countryCodeComplete.countryCodeSelected(countryCode);

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        binding.countryCodeListView.setAdapter(adapter);
        int i = 0;

        for (CountryCode countryCode : countryCodes) {
            if (countryCode.code.equalsIgnoreCase(code)
                    || countryCode.name.equalsIgnoreCase(code)) {

                binding.countryCodeListView.setItemChecked(i, true);
                break;
            }

            i++;
        }


    }

    public void setCountryCodeComplete(CountryCodeComplete countryCodeComplete) {
        this.countryCodeComplete = countryCodeComplete;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!showHeader) {
            LIST_TYPE listType = (LIST_TYPE) getArguments().getSerializable("list_type");
            if (listType == LIST_TYPE.LOCALE) {
                fragmentStack.setHeaderTitle(R.string.change_language);
                fragmentStack.showRightButton(R.string.save);
                fragmentStack.enableRightButton(this, true);
            }
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

        changeLocale(null);
    }

    private void changeLocale(CountryCode countryCode) {
        final BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection()) {
            return;
        }
        final Locale locale;
        if (countryCode != null) {
            locale = new Locale(countryCode.code);
        } else {
            locale = new Locale(adapter.getCode());
        }

        final CountryCode finalCountryCode = countryCode;
        int redColor = ContextCompat.getColor(getContext(), R.color.light_red);
        AlertUtils.showGenericAlert(baseActivity, getString(R.string.update_language), getString(R.string.this_change_will_update, LocaleHelper.getDisplayLanguage(getContext(), locale)), 0, getString(R.string.update),
                getString(R.string.cancel), redColor, 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Customer customer = CurrentCustomer.getCurrentCustomer();
                        if (customer != null) {
                            showLoading(true, getActivity().getString(R.string.updating));
                            final String oldLanguage = customer.languagePreference;

                            final String newLanguage = locale.getLanguage();

                            customer.languagePreference = locale.getLanguage();


                            CustomerAPIService.editCustomer(customer, new Callback<Void>() {
                                @Override
                                public void success(Void aVoid, Response response) {
                                    showLoading(false, getActivity().getString(R.string.updating));


                                    GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_LANGUAGE_UPDATE, SETTINGS_TYPE_CUSTOMER, customer.id,
                                            null, 0, oldLanguage, newLanguage);
                                    if (countryCodeComplete != null) {
                                        countryCodeComplete.countryCodeSelected(finalCountryCode);
                                    }
                                    CustomerDatabaseService.updateCustomer(customer);

                                    LocaleHelper.setNewLocale(getContext(), locale.getLanguage());
                                    if (!baseActivity.isPaused() && !baseActivity.isFinishing())
                                        baseActivity.onBackPressed();


                                    baseActivity.recreate();

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    showLoading(false, getActivity().getString(R.string.updating));

                                }
                            });
                        } else {
                            LocaleHelper.setNewLocale(getContext(), locale.getLanguage());
                            baseActivity.recreate();

                        }
                    }
                }, null);
    }
}
