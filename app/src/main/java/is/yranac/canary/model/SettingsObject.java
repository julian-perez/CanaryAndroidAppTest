package is.yranac.canary.model;

import android.graphics.Bitmap;

import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.ui.views.CanaryTextWatcher;

public class SettingsObject {
    public int settingType;
    public String title;
    public String editableHint;
    public String editableText;
    public int editableInputType = 0;
    public int validationMethod = CanaryTextWatcher.ANY_TEXT;
    public boolean toggleState;
    public Avatar avatar;
    public Bitmap newAvatarBitmap;
    public String initiials;
    public int imageResourceId;
    public boolean selectable;
    public boolean otaing;
    public boolean otaFailed = false;
    public boolean fieldInvalid = false;

    /**
     * Constructor for editable setting
     *
     * @param title             title prompt for this setting
     * @param editableHint      hint text
     * @param editableText      the setting text
     * @param editableInputType valid input type See <a href="http://developer.android.com/reference/android/text/InputType.html">Input Type</a>
     * @param validationMethod  Input method reference from CanaryTextWatcher
     * @see is.yranac.canary.ui.views.CanaryTextWatcher#validateString(CharSequence, int)
     */
    public static SettingsObject editableText(String title, String editableHint, String editableText, int editableInputType, int validationMethod) {
        return new SettingsObject(SettingsAdapter.TYPE_EDITABLE, title, editableHint, editableText, editableInputType, validationMethod);
    }

    /**
     * Constructor for prompt with title
     *
     * @param title           title prompt for this setting
     * @param nonEditableText the setting text
     */
    public static SettingsObject promptWithTitle(String title, String nonEditableText) {
        return new SettingsObject(SettingsAdapter.TYPE_PROMPT_WITH_TITLE, title, null, nonEditableText, 0, 0);
    }

    /**
     * Constructor for checkbox setting
     *
     * @param title       title prompt for this setting
     * @param toggleState initial state for the checkbox
     */
    public static SettingsObject checkbox(String title, boolean toggleState, boolean selectable) {
        return new SettingsObject(SettingsAdapter.TYPE_CHECKBOX, title, toggleState, selectable);
    }

    /**
     * Constructor for plain prompt (chevron will appear at right of layout)
     *
     * @param prompt prompt text
     */
    public static SettingsObject prompt(String prompt) {
        return new SettingsObject(SettingsAdapter.TYPE_PROMPT, prompt);
    }


    /**
     * Prompt with image and chevron.  Image could be green circle with plus in it for example
     *
     * @param prompt          the prompt
     * @param imageResourceId the image resource id
     * @return the settings object
     */
    public static SettingsObject promptWithImage(String prompt, int imageResourceId, boolean otaing, boolean otaFailed) {
        return new SettingsObject(SettingsAdapter.TYPE_IMAGE, prompt, imageResourceId, true, otaing, otaFailed);
    }

    /**
     * Photo with title.  Used to display avatar image and person name
     *
     * @param avatar the avatar
     * @param title  the title
     * @return the settings object
     */
    public static SettingsObject photoWithTitle(Avatar avatar, String title, String initials) {
        return new SettingsObject(SettingsAdapter.TYPE_PHOTO, avatar, title, initials);
    }

    /**
     * Constructor for editable setting
     *
     * @param settingType       SettingsAdapter.TYPE_EDITABLE
     * @param title             title prompt for this setting
     * @param editableHint      hint text
     * @param editableText      the setting text
     * @param editableInputType valid input type See <a href="http://developer.android.com/reference/android/text/InputType.html">Input Type</a>
     * @param validationMethod  Input method reference from CanaryTextWatcher
     * @see is.yranac.canary.ui.views.CanaryTextWatcher#validateString(CharSequence, int)
     */
    private SettingsObject(int settingType, String title, String editableHint, String editableText, int editableInputType, int validationMethod) {
        this.settingType = settingType;
        this.title = title;
        this.editableHint = editableHint;
        this.editableText = editableText;
        this.editableInputType = editableInputType;
        this.validationMethod = validationMethod;
    }

    /**
     * Constructor for checkbox setting
     *
     * @param settingType SettingsAdapter.TYPE_CHECKBOX
     * @param title       title prompt for this setting
     * @param toggleState initial state for the checkbox
     */
    private SettingsObject(int settingType, String title, boolean toggleState, boolean selectable) {
        this.settingType = settingType;
        this.title = title;
        this.toggleState = toggleState;
        this.selectable = selectable;
    }

    /**
     * Constructor for plain prompt (chevron will appear at right of layout)
     * and also for info (no chevron will appear)
     *
     * @param settingType
     * @param title
     */
    private SettingsObject(int settingType, String title) {
        this.settingType = settingType;
        this.title = title;
    }

    /**
     * Constructor for image prompt (chevron will appear at right of layout)
     * and also for info (no chevron will appear)
     *
     * @param settingType
     * @param title
     */
    private SettingsObject(int settingType, String title, int imageResourceId, boolean selectable, boolean otaing, boolean otaFailed) {
        this.settingType = settingType;
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.selectable = selectable;
        this.otaing = otaing;
        this.otaFailed = otaFailed;
    }

    /**
     * Constructor for avatar info
     *
     * @param settingType
     * @param avatar
     * @param title
     */
    private SettingsObject(int settingType, Avatar avatar, String title, String initiials) {
        this.settingType = settingType;
        this.avatar = avatar;
        this.title = title;
        this.initiials = initiials;
    }


}
