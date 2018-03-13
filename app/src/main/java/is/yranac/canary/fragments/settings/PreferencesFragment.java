package is.yranac.canary.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsPreferencesBinding;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.messages.UpdateCurrentCustomer;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACCOUNT_SETTINGS_PREFERENCES;


/**
 * Created by michaelschroeder on 5/8/17.
 */

public class PreferencesFragment extends SettingsFragment implements View.OnClickListener {


    private FragmentSettingsPreferencesBinding binding;

    private Customer customer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsPreferencesBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customer = CurrentCustomer.getCurrentCustomer();
        binding.temperatureLayout.setOnClickListener(this);
        binding.localeLayout.setOnClickListener(this);
        binding.soundLayout.setOnClickListener(this);

        binding.localeLayout.setText(LocaleHelper.getDisplayLanguage(getContext()));
        if (customer.celsius) {
            binding.fahrenheit.setSelected(false);
            binding.celsius.setSelected(true);
        } else {
            binding.fahrenheit.setSelected(true);
            binding.celsius.setSelected(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.preferences);
    }

    @Override
    public void onPause() {
        super.onPause();
        final Context context = getContext().getApplicationContext();
        LocaleHelper.setNewLocale(context, customer.languagePreference);
        CustomerAPIService.editCustomer(
                customer, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        CustomerDatabaseService.updateCustomer(customer);
                        TinyMessageBus.post(new UpdateCurrentCustomer());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
    }

    @Override
    protected String getAnalyticsTag() {
        return ACCOUNT_SETTINGS_PREFERENCES;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locale_layout:
                CountryCodeSelectFragment fragment = CountryCodeSelectFragment.newInstance(customer.languagePreference,
                        CountryCodeSelectFragment.LIST_TYPE.LOCALE, true, false);
                fragment.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        customer.languagePreference = countryCode.code;
                        Locale locale = new Locale(customer.languagePreference);
                        binding.localeLayout.setText(LocaleHelper.getDisplayLanguage(getContext(),locale));

                    }
                });
                addModalFragment(fragment);
                break;
            case R.id.temperature_layout:
                customer.celsius = !customer.celsius;
                if (customer.celsius) {
                    binding.fahrenheit.setSelected(false);
                    binding.celsius.setSelected(true);
                } else {
                    binding.fahrenheit.setSelected(true);
                    binding.celsius.setSelected(false);
                }
                break;
            case R.id.sound_layout:
                addFragmentToStack(new NotificationSoundFragment(), Utils.SLIDE_FROM_RIGHT);
                break;

        }
    }
}
