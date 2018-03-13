package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentEditCurrentUserBinding;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.messages.AvatarUpdated;
import is.yranac.canary.messages.PushToken;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.customer.CustomerCreate;
import is.yranac.canary.services.api.AvatarAPIServices;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.api.OathAuthenticationAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.ui.SetupFragmentStackActivity;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CREATE_ACCOUNT;


public class EditUserDetailsFragment extends SetUpBaseFragment implements StackFragment.StackFragmentCallback, View.OnClickListener {

    public static final int CAMERA_REQUEST = 1888;
    public static final int SELECT_PICTURE = 2888;


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 64;

    private CountryCode countryCode = null;

    private List<EditTextWithLabel> editTextList = new ArrayList<>();

    private Customer customer;

    private String password;
    private boolean popOnResume = false;

    private Uri mCameraTempUri;

    private FragmentEditCurrentUserBinding binding;
    private Bitmap newAvatarBitmap;

    private static final String customer_key = "customerJson";
    private static final String password_key = "password";

    public static EditUserDetailsFragment newInstance(String customerJson, String password) {
        EditUserDetailsFragment fragment = new EditUserDetailsFragment();

        Bundle args = new Bundle();
        args.putString(customer_key, customerJson);
        args.putString(password_key, password);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditCurrentUserBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customer = JSONUtil.getObject(getArguments().getString(customer_key), Customer.class);
        password = getArguments().getString(password_key);

        setCountryCode();

        binding.signInBtn.setText(StringUtils.spannableStringBuilder(getContext(),
                R.string.already_member, R.string.sign_in), TextView.BufferType.SPANNABLE);

        setupValidator();

        binding.nextBtn.setOnClickListener(this);
        binding.signInBtn.setOnClickListener(this);
        binding.avatarLayout.setOnClickListener(this);
        binding.countryCodeLayout.setOnClickListener(this);
        binding.languageLayout.setOnClickListener(this);

        binding.avatarImageLayout.grayCircle.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showHeader(true);

        setupHeader();

        if (popOnResume) {
            if (!LocaleHelper.getLocale(getResources()).getLanguage().equals(LocaleHelper.getLanguage(getContext()))) {
                LocaleHelper.setNewLocale(getContext(), customer.languagePreference);
                Activity activity = getActivity();
                if (activity != null) {
                    Intent i = new Intent(getActivity(), SetupFragmentStackActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    setLocationUri(null);
                    if (LocationUtil.doesNotHaveLocationPermission(getContext())) {
                        fragmentStack.addFragmentAndResetStack(new LocationPrimerFragment(), Utils.SLIDE_FROM_RIGHT);
                    } else {
                        fragmentStack.addFragmentAndResetStack(SetAddressFragment.newInstance(new Bundle(), true), Utils.SLIDE_FROM_RIGHT);
                    }
                }

            } else {
                popOnResume = true;
            }

        }
    }


    private Locale getStartingLocale() {
        Locale locale = getResources().getConfiguration().locale;

        String jsonString = Utils.loadJSONFromRawFile(R.raw.locale);

        if (jsonString == null)
            return Locale.ENGLISH;

        List<CountryCode> locales = JSONUtil.getList(CountryCode[].class, jsonString);

        for (CountryCode countryCode : locales) {
            if (countryCode.code.equalsIgnoreCase(locale.getLanguage()))
                return locale;
        }

        return Locale.ENGLISH;
    }

    private void setCountryCode() {
        String dialCode = null;

        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (tMgr != null)
            dialCode = tMgr.getSimCountryIso().toUpperCase();


        String jsonString = Utils.loadJSONFromRawFile(R.raw.countries);

        final List<CountryCode> countryCodes = JSONUtil.getList(CountryCode[].class, jsonString);

        if (dialCode != null) {
            for (CountryCode countryCode1 : countryCodes) {

                if (dialCode.equalsIgnoreCase(countryCode1.code)) {
                    this.countryCode = countryCode1;
                    setCelsiusCode();
                    return;
                }
            }
        }
        for (CountryCode countryCode1 : countryCodes) {

            if (countryCode1.code.equalsIgnoreCase("US")) {
                this.countryCode = countryCode1;
                return;
            }
        }
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        binding.nextBtn.setEnabled(allValid);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:

                hideSoftKeyboard();

                customer.firstName = binding.firstnameLayout.getText();
                customer.lastName = binding.lastNameLayout.getText();
                customer.phone = binding.mobilePhoneLayout.getText();

                if (countryCode != null) {
                    customer.dialCode = countryCode.code;
                    customer.phone = createPhoneNumber(countryCode.dialCode, customer.phone);
                }

                if (customer.phone == null || customer.phone.length() < 6) {
                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.invalid_phone_number));
                    return;
                }
                createAccount();

                break;

            case R.id.sign_in_btn:
                setShowIn(true);
                getActivity().onBackPressed();
                getActivity().onBackPressed();
                break;
            case R.id.language_layout:
                CountryCodeSelectFragment fragment = CountryCodeSelectFragment.newInstance(customer.languagePreference, CountryCodeSelectFragment.LIST_TYPE.LOCALE, true, true);
                fragment.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        customer.languagePreference = countryCode.code;
                        String customerJson = JSONUtil.getJSONString(customer);


                        // persist the current customer settings to the arguments.  Cannot use
                        // onSaveInstanceState here as the Activity will not be pausing this fragment
                        getArguments().putString(customer_key, customerJson);


                        binding.nextBtn.setEnabled(textIsGood(editTextList));

                        Locale locale = new Locale(countryCode.code);

                        binding.languageLayout.setText(locale.getDisplayLanguage(Locale.getDefault()));

                    }
                });
                addModalFragment(fragment);
                break;
            case R.id.country_code_layout:
                hideSoftKeyboard();

                String customerJson = JSONUtil.getJSONString(customer);
                // persist the current customer settings to the arguments.  Cannot use
                // onSaveInstanceState here as the Activity will not be pausing this fragment
                getArguments().putString(customer_key, customerJson);
                CountryCodeSelectFragment fragment2 = CountryCodeSelectFragment.newInstance(countryCode.code, CountryCodeSelectFragment.LIST_TYPE.COUNTRY_CODE);
                fragment2.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        EditUserDetailsFragment.this.countryCode = countryCode;

                        binding.countryCodeLayout.setText(countryCode.name + " (" + countryCode.dialCode + ")");
                        setCelsiusCode();

                        binding.nextBtn.setEnabled(textIsGood(editTextList));

                    }
                });

                addModalFragment(fragment2);
                break;
            case R.id.avatar_layout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setSingleChoiceItems(
                        R.array.avatar_selection_array, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    takeAvatarPhoto();
                                } else {
                                    chooseAvatarPhoto();
                                }
                                dialog.dismiss();
                            }
                        }
                );
                AlertDialog alert = builder.create();

                alert.show();
                break;
        }
    }


    private void setupValidator() {

        editTextList.clear();
        editTextList.add(binding.firstnameLayout);
        editTextList.add(binding.lastNameLayout);
        editTextList.add(binding.languageLayout);
        editTextList.add(binding.mobilePhoneLayout);
        editTextList.add(binding.countryCodeLayout);

        setupViewWatcher(binding.firstnameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.lastNameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.mobilePhoneLayout, CanaryTextWatcher.VALID_PHONE, editTextList, this);
        setupViewWatcher(binding.languageLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.countryCodeLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);

        Locale locale = getStartingLocale();

        customer.languagePreference = locale.getLanguage();
        binding.languageLayout.setText(LocaleHelper.getDisplayLanguage(getContext(), locale));
        if (countryCode != null) {
            binding.countryCodeLayout.setText(countryCode.name + " (" + countryCode.dialCode + ")");
        }
    }

    @Override
    public void onRightButtonClick() {

    }

    private String createPhoneNumber(String dialCode, String phone) {
        phone = phone.replaceAll("\\D+", "");
        if (phone.length() < dialCode.length())
            return dialCode + phone;

        if (phone.substring(0, dialCode.length()).equalsIgnoreCase(dialCode))
            return dialCode + phone.substring(dialCode.length(), phone.length());


        String tempDialCode = dialCode.replace("+", "");

        if (phone.substring(0, tempDialCode.length()).equalsIgnoreCase(tempDialCode))
            return dialCode + phone.substring(tempDialCode.length(), phone.length());

        return dialCode + phone;
    }


    private void setupHeader() {
        fragmentStack.setHeaderTitle(R.string.account);

        fragmentStack.showRightButton(0);
        binding.nextBtn.setEnabled(textIsGood(editTextList));
    }

    private void createAccount() {

        showLoading(true, null);

        PreferencesUtils.setUserName(customer.email);


        CustomerAPIService.createCustomer(
                new CustomerCreate(customer, password), new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        String locationUri = null;

                        for (Header header : response.getHeaders()) {
                            if ("location".equalsIgnoreCase(header.getName())) {
                                locationUri = header.getValue().substring(Constants.BASE_URL.length());
                                break;
                            }
                        }

                        final String finalLocationUri = locationUri;
                        OathAuthenticationAPIService.oauthAuthentication(
                                customer.email, password, new Callback<OauthResponse>() {
                                    @Override
                                    public void success(OauthResponse oauthResponse, Response response) {

                                        showLoading(false, null);


                                        KeyStoreHelper.saveToken(oauthResponse.accessToken);
                                        KeyStoreHelper.saveRefreshToken(oauthResponse.refreshToken);

                                        TinyMessageBus.post(new PushToken());
                                        uploadImage(finalLocationUri);


                                        if (isVisible()) {
                                            if (!LocaleHelper.getLocale(getResources()).getLanguage().equals(LocaleHelper.getLanguage(getContext()))) {
                                                LocaleHelper.setNewLocale(getContext(), customer.languagePreference);
                                                Activity activity = getActivity();
                                                if (activity != null) {
                                                    Intent i = new Intent(getActivity(), SetupFragmentStackActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                } else {
                                                    setLocationUri(null);
                                                    if (LocationUtil.doesNotHaveLocationPermission(getContext())) {
                                                        fragmentStack.addFragmentAndResetStack(new LocationPrimerFragment(), Utils.SLIDE_FROM_RIGHT);
                                                    } else {
                                                        fragmentStack.addFragmentAndResetStack(SetAddressFragment.newInstance(new Bundle(), true), Utils.SLIDE_FROM_RIGHT);
                                                    }
                                                }

                                            } else {
                                                popOnResume = true;
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        showLoading(false, null);

                                        if (isVisible()) {
                                            try {
                                                AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                                            } catch (JSONException e) {
                                                e.printStackTrace();

                                            }
                                        }
                                    }
                                }

                        );

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        showLoading(false, getString(R.string.creating_account));

                        if (isVisible()) {
                            try {
                                if (error.getResponse() != null && error.getResponse().getStatus() == 400) {
                                    AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error),
                                            null, 0, getString(R.string.okay), getString(R.string.sign_in), 0, 0,
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (getActivity() != null)
                                                        getActivity().onBackPressed();
                                                }
                                            },
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    signIn();
                                                }
                                            });
                                    return;
                                }
                                AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }

        );
    }

    private void signIn() {

        showLoading(true, getString(R.string.signing_in));
        OathAuthenticationAPIService.oauthAuthentication(
                customer.email, password, new Callback<OauthResponse>() {
                    @Override
                    public void success(OauthResponse oauthResponse, Response response) {
                        showLoading(false, getString(R.string.signing_in));

                        KeyStoreHelper.saveToken(oauthResponse.accessToken);
                        KeyStoreHelper.saveRefreshToken(oauthResponse.refreshToken);

                        GlobalAPIntentServiceManager.resetAlarms(getContext());

                        Intent i = new Intent(getActivity(), LaunchActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        TinyMessageBus.post(new PushToken());
                        LocaleHelper.setLocale(getContext());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showLoading(false, getString(R.string.signing_in));

                        try {
                            if (getActivity() != null) {
                                String retrofitError = Utils.getErrorMessageFromRetrofit(getActivity(), error);
                                if (retrofitError.contains("invalid_grant")) {
                                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.invalid_login_credentials));
                                } else {
                                    AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    private void uploadImage(final String customerUri) {

        if (newAvatarBitmap != null) {
            AvatarAPIServices.uploadUserAvatarBitmap(
                    newAvatarBitmap, customerUri, new Callback<Avatar>() {
                        @Override
                        public void success(Avatar avatar, Response response) {

                            int customerId = Utils.getIntFromResourceUri(customerUri);

                            GoogleAnalyticsHelper.trackSettingsEvent(AnalyticsConstants.SETTINGS_AVATAR_UPDATE, AnalyticsConstants.SETTINGS_TYPE_CUSTOMER, customerId, null, 0,
                                    String.valueOf(false), String.valueOf(true));

                            if (avatar != null) {
                                AvatarDatabaseService.insertAvatar(avatar);
                                TinyMessageBus.post(new AvatarUpdated(customer));
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    }
            );
        }
    }

    private void setCelsiusCode() {
        if (countryCode == null)
            return;

        switch (countryCode.code) {
            case "US":
            case "VI":
            case "GU":
            case "PR":
            case "BZ":
            case "KY":
            case "BS":
            case "PW":
                customer.celsius = false;
                break;
            default:
                customer.celsius = true;
                break;

        }

    }

    public void chooseAvatarPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void takeAvatarPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }
        try {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            mCameraTempUri = getActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (mCameraTempUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        }
        startActivityForResult(intent, CAMERA_REQUEST);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap avatarBitmap = null;
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case SELECT_PICTURE:
                    Uri selectedImageUri = data.getData();

                    if (selectedImageUri != null) {
                        avatarBitmap = ImageUtils.getBitmap(selectedImageUri, getContext(), false);

                    }
                    break;
                case CAMERA_REQUEST:
                    avatarBitmap = ImageUtils.getBitmap(mCameraTempUri, getContext(), true);

                    break;
                default:
                    return;
            }

            if (avatarBitmap == null)
                return;

            int height;
            int width;

            if (avatarBitmap.getHeight() > avatarBitmap.getWidth()) {
                width = avatarBitmap.getWidth();
                height = avatarBitmap.getWidth();
            } else {
                width = avatarBitmap.getHeight();
                height = avatarBitmap.getHeight();
            }

            int centerX = avatarBitmap.getWidth() / 2;
            int centerY = avatarBitmap.getHeight() / 2;

            newAvatarBitmap = Bitmap.createBitmap(avatarBitmap, centerX - (width / 2), centerY - (width / 2), width
                    , height);

            binding.avatarImageLayout.memberImageView.setImageBitmap(newAvatarBitmap);
            binding.addPhotoLabel.setText(R.string.change_photo);

        }
    }

    @Override
    protected String getAnalyticsTag() {

        return SCREEN_CREATE_ACCOUNT;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());

                if (hasPermission) {
                    takeAvatarPhoto();
                }

            }
        }
    }
}

