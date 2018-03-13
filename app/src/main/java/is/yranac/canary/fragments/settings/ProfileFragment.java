package is.yranac.canary.fragments.settings;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.databinding.FragmentSettingsProfileBinding;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.messages.UpdateCurrentCustomer;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.AvatarAPIServices;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_EDIT_ACCOUNT;


/**
 * Created by michaelschroeder on 5/8/17.
 */

public class ProfileFragment extends SettingsFragment implements StackFragment.StackFragmentCallback, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 64;
    private FragmentSettingsProfileBinding binding;
    private Customer customer;
    protected List<EditTextWithLabel> editTextList = new ArrayList<>();
    private Bitmap newAvatarBitmap;
    private Uri mCameraTempUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customer = CurrentCustomer.getCurrentCustomer();
        Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);

        if (avatar != null) {
            ImageUtils.loadAvatar(binding.avatarImageLayout.memberImageView, avatar.thumbnailUrl());
        }


        binding.firstNameLayout.setText(customer.firstName);
        binding.lastNameLayout.setText(customer.lastName);
        binding.emailAddress.setText(customer.email);
        binding.mobilePhoneLayout.setText(customer.phone);
        binding.countryCodeLayout.setOnClickListener(this);
        binding.avatarLayout.setOnClickListener(this);
        setCountryCode();
        setupValidator();
    }

    private void setupValidator() {
        editTextList.clear();
        editTextList.add(binding.firstNameLayout);
        editTextList.add(binding.lastNameLayout);
        editTextList.add(binding.mobilePhoneLayout);

        setupViewWatcher(binding.firstNameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.lastNameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.mobilePhoneLayout, CanaryTextWatcher.VALID_PHONE, editTextList, this);
    }


    private void setCountryCode() {

        String dialCode = customer.dialCode;

        String jsonString = Utils.loadJSONFromRawFile(R.raw.countries);
        Type listType = new TypeToken<ArrayList<CountryCode>>() {
        }.getType();
        final List<CountryCode> countryCodes = new Gson().fromJson(jsonString, listType);

        for (CountryCode countryCode : countryCodes) {

            if (countryCode.code.equalsIgnoreCase(dialCode)) {
                String countryCodeText = countryCode.name + " (" + countryCode.dialCode + ")";
                binding.countryCodeLayout.setText(countryCodeText);
            }
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_EDIT_ACCOUNT;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.profile);

    }

    @Override
    public void onStop() {

        customer.firstName = binding.firstNameLayout.getText();
        customer.lastName = binding.lastNameLayout.getText();
        customer.phone = binding.mobilePhoneLayout.getText();

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
        uploadImage(customer.resourceUri);

        super.onStop();
    }

    private void uploadImage(final String customerUri) {
        if (newAvatarBitmap == null) {
            return;
        }
        AvatarAPIServices.uploadUserAvatarBitmap(
                newAvatarBitmap, customerUri, new Callback<Avatar>() {
                    @Override
                    public void success(Avatar avatar, Response response) {

                        boolean oldValue = newAvatarBitmap == null;
                        int customerId = Utils.getIntFromResourceUri(customerUri);

                        GoogleAnalyticsHelper.trackSettingsEvent(AnalyticsConstants.SETTINGS_AVATAR_UPDATE, AnalyticsConstants.SETTINGS_TYPE_CUSTOMER, customerId, null, 0,
                                String.valueOf(oldValue), String.valueOf(true));

                        if (avatar != null) {
                            AvatarDatabaseService.insertAvatar(avatar);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                }
        );
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        if (allValid) {
            fragmentStack.enableBackButton();
            removeErrorStates(editTextList);
        } else {
            fragmentStack.disableBackButton();
            showErrorStates(editTextList);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

            case R.id.country_code_layout:

                CountryCodeSelectFragment fragment2 = CountryCodeSelectFragment.newInstance(customer.dialCode, CountryCodeSelectFragment.LIST_TYPE.COUNTRY_CODE);
                fragment2.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        customer.dialCode = countryCode.code;
                        setCountryCode();
                    }
                });
                addModalFragment(fragment2);

                break;
        }
    }

    public void chooseAvatarPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SettingsAdapter.SELECT_PICTURE);
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
        startActivityForResult(intent, SettingsAdapter.CAMERA_REQUEST);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap avatarBitmap = null;
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case SettingsAdapter.SELECT_PICTURE:
                    Uri selectedImageUri = data.getData();

                    if (selectedImageUri != null) {
                        avatarBitmap = ImageUtils.getBitmap(selectedImageUri, getContext(), false);


                    }
                    break;
                case SettingsAdapter.CAMERA_REQUEST:
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
        }
    }
}
