package is.yranac.canary.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.StringUtils;

public class SettingsAdapter extends ArrayAdapter<SettingsObject> {
    public static final int TYPE_EDITABLE = R.layout.setting_editable;
    public static final int TYPE_PROMPT = R.layout.setting_prompt;
    public static final int TYPE_PROMPT_WITH_TITLE = R.layout.setting_prompt_with_title;
    public static final int TYPE_IMAGE = R.layout.setting_prompt_image;
    public static final int TYPE_CHECKBOX = R.layout.setting_prompt_checkbox;
    public static final int TYPE_PHOTO = R.layout.setting_photo;


    public static final int CAMERA_REQUEST = 1888;
    public static final int SELECT_PICTURE = 2888;

    private LayoutInflater inflater;
    private Context context;

    private SettingsAdapterCallback settingsAdapterCallback;
    private SettingsAdapterPhotoCallback settingsAdapterPhotoCallback;

    public interface SettingsAdapterCallback {
        void onOptionSelected(int position);

    }

    public static class SimpleSettingsAdapterCallback implements SettingsAdapterCallback {
        @Override
        public void onOptionSelected(int position) {
        }

    }

    public static abstract class SettingsAdapterPhotoCallback {
        public abstract void onPhotoActionSelected(int action);
    }

    public SettingsAdapter(Context context, List<SettingsObject> objects, SettingsAdapterCallback settingsAdapterCallback) {
        this(context, objects, settingsAdapterCallback, null);
    }

    public SettingsAdapter(
            Context context, List<SettingsObject> objects, SettingsAdapterCallback settingsAdapterCallback, SettingsAdapterPhotoCallback settingsAdapterPhotoCallback) {
        super(context, 0, objects);

        inflater = LayoutInflater.from(context);

        this.settingsAdapterCallback = settingsAdapterCallback;
        this.settingsAdapterPhotoCallback = settingsAdapterPhotoCallback;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SettingsObject settingsObject = getItem(position);

        final View view = inflater.inflate(settingsObject.settingType, parent, false);

        switch (settingsObject.settingType) {
            case TYPE_EDITABLE:
                final TextView settingLabel = (TextView) view.findViewById(R.id.setting_label);
                if (settingsObject.title != null) {
                    settingLabel.setText(settingsObject.title);
                } else {
                    settingLabel.setVisibility(View.GONE);
                }

                final EditText editableText = (EditText) view.findViewById(R.id.setting_edit_text);
                editableText.setHint(settingsObject.editableHint);
                Typeface typeface = editableText.getTypeface();

                if (settingsObject.editableInputType != 0) {
                    editableText.setInputType(settingsObject.editableInputType);
                }

                if (settingsObject.editableInputType == InputType.TYPE_CLASS_PHONE) {
                    editableText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                    editableText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.length() == 0 || s.subSequence(0, 1).equals("+")) {

                            }
                        }
                    });
                } else if (settingsObject.editableInputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    editableText.setSelection(editableText.getText().length());
                } else if (settingsObject.editableInputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    editableText.setSelection(editableText.getText().length());
                }

                editableText.setTypeface(typeface);

                // set the initial color of the text view
                setTextViewColor(settingLabel, false);
                editableText.setText(settingsObject.editableText);

                final View barView = view.findViewById(R.id.gray_bar_with_margin);


                editableText.addTextChangedListener(
                        new CanaryTextWatcher(settingsObject.validationMethod) {
                            @Override
                            public void afterTextChanged(Editable s) {
                                super.afterTextChanged(s);

                                getItem(position).editableText = s.toString();
                                callbackOptionSelected(position);

                                if (settingsObject.fieldInvalid && !s.toString().isEmpty()) {
                                    settingsObject.fieldInvalid = false;
                                    setTextViewColor(settingLabel, true);
                                    int gray = getContext().getResources().getColor(R.color.gray);
                                    barView.setBackgroundColor(gray);
                                } else if (!settingsObject.fieldInvalid && s.toString().isEmpty()) {
                                    settingsObject.fieldInvalid = true;

                                    int red = getContext().getResources().getColor(R.color.red);
                                    settingLabel.setTextColor(red);
                                    barView.setBackgroundColor(red);
                                }
                            }
                        }
                );

                editableText.setOnFocusChangeListener(
                        new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!settingsObject.fieldInvalid)
                                    setTextViewColor(settingLabel, hasFocus);

                            }
                        });

                if (settingsObject.fieldInvalid) {
                    int red = getContext().getResources().getColor(R.color.red);
                    settingLabel.setTextColor(red);
                    barView.setBackgroundColor(red);
                } else {
                    int gray = getContext().getResources().getColor(R.color.dark_moderate_cyan);
                    settingLabel.setTextColor(gray);
                    barView.setBackgroundColor(gray);

                }
                break;
            case TYPE_IMAGE:
                ((AppCompatImageView) view.findViewById(R.id.setting_image_view)).setImageResource(settingsObject.imageResourceId);
                if (!settingsObject.selectable) {
                    view.findViewById(R.id.setting_container).setAlpha(.5f);
                    view.findViewById(R.id.setting_container).setBackgroundResource(R.color.transparent);
                }

                TextView otaTextView = (TextView) view.findViewById(R.id.setting_dsc);
                if (settingsObject.otaing) {
                    otaTextView.setVisibility(View.VISIBLE);
                    otaTextView.setText(R.string.update_in_progress);
                    otaTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                }
                if (settingsObject.otaFailed) {
                    otaTextView.setVisibility(View.VISIBLE);
                    otaTextView.setText(R.string.setup_failed);
                    otaTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }
            case TYPE_PROMPT:
                TextView settingTitle = (TextView) view.findViewById(R.id.setting_title);
                settingTitle.setText(settingsObject.title);
                break;
            case TYPE_PROMPT_WITH_TITLE:
                ((TextView) view.findViewById(R.id.setting_title)).setText(settingsObject.title);
                ((TextView) view.findViewById(R.id.setting_edit_text)).setText(settingsObject.editableText);
                break;
            case TYPE_CHECKBOX:
                ((TextView) view.findViewById(R.id.setting_title)).setText(settingsObject.title);

                final SwitchCompat checkbox = (SwitchCompat) view.findViewById(R.id.setting_checkbox);
                checkbox.setChecked(settingsObject.toggleState);

                if (settingsObject.selectable) {
                    view.findViewById(R.id.setting_checkbox_view)
                            .setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            settingsObject.toggleState = !settingsObject.toggleState;
                                            checkbox.setChecked(settingsObject.toggleState);
                                            callbackOptionSelected(position);
                                        }
                                    }
                            );
                }
                break;
            case TYPE_PHOTO:
                if (settingsObject.title != null) {
                    ((TextView) view.findViewById(R.id.add_photo_label)).setText((settingsObject.title));
                    view.findViewById(R.id.photo_container).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    String text;
                    boolean noAvatar = settingsObject.avatar == null || settingsObject.avatar.thumbnailUrl() == null;
                    if (noAvatar) {
                        text = context.getString(R.string.add_photo);
                    } else {
                        text = context.getString(R.string.change_photo);
                    }
                    ((TextView) view.findViewById(R.id.add_photo_label)).setText(text);
                    view.findViewById(R.id.photo_container).setOnClickListener(addPhotoListener);
                }

                ((TextView) view.findViewById(R.id.customer_initials)).setText(settingsObject.initiials);
                ImageView avatarImageView = (ImageView) view.findViewById(R.id.member_image_view);
                if (settingsObject.newAvatarBitmap != null) {
                    avatarImageView.setImageBitmap(settingsObject.newAvatarBitmap);
                } else if (settingsObject.avatar != null && settingsObject.avatar.thumbnailUrl() != null) {
                    ImageUtils.loadAvatar(avatarImageView, settingsObject.avatar.thumbnailUrl());
                } else if (!StringUtils.isNullOrEmpty(settingsObject.initiials)) {
                    avatarImageView.setVisibility(View.GONE);
                }
                break;
        }


        return view;
    }

    private void callbackOptionSelected(int position) {
        if (settingsAdapterCallback != null) {
            settingsAdapterCallback.onOptionSelected(position);
        }
    }

    private View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setSingleChoiceItems(
                    R.array.avatar_selection_array, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                settingsAdapterPhotoCallback.onPhotoActionSelected(CAMERA_REQUEST);
                            } else {
                                settingsAdapterPhotoCallback.onPhotoActionSelected(SELECT_PICTURE);
                            }
                            dialog.dismiss();
                        }
                    }
            );
            AlertDialog alert = builder.create();

            alert.show();
        }
    };

    private void setTextViewColor(TextView textView, boolean isValidText) {
        if (isValidText) {
            textView.setTextColor(
                    CanaryApplication.getContext()
                            .getResources()
                            .getColor(CanaryTextWatcher.TEXT_COLOR_OK));
        } else {
            textView.setTextColor(
                    CanaryApplication.getContext()
                            .getResources()
                            .getColor(CanaryTextWatcher.TEXT_COLOR_INVALID));
        }

    }

}
