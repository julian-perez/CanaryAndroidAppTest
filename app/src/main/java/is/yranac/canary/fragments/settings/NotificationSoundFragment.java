package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.io.IOException;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsSingleSelectionAdapter;
import is.yranac.canary.databinding.FragmentSettingsSimpleListBinding;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.CustomerAPIService;

import static is.yranac.canary.receiver.PushIntentReceiver.CUSTOM_SOUND;
import static is.yranac.canary.receiver.PushIntentReceiver.DEFAULT_SOUND;


/**
 * Created by michaelschroeder on 10/26/16.
 */

public class NotificationSoundFragment extends SettingsFragment implements AdapterView.OnItemClickListener {

    private FragmentSettingsSimpleListBinding binding;
    private String[] predefinedDeviceNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_simple_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listView.setAdapter(new SettingsSingleSelectionAdapter(getActivity(), getPredefinedNames(), R.layout.setting_row_radio));
        binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        binding.listView.setOnItemClickListener(this);
        binding.listView.setItemChecked(getCurrentSelection(), true);

    }

    private String[] getPredefinedNames() {
        if (predefinedDeviceNames == null) {
            predefinedDeviceNames = getResources().getStringArray(R.array.sound_names);
        }
        return predefinedDeviceNames;
    }

    @Override
    public void onRightButtonClick() {


    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.sounds);
    }

    @Override
    public void onPause() {
        super.onPause();

        final String sound = getSoundFromSeletion();
        CustomerAPIService.editCustomerSound(sound);
    }

    private int getCurrentSelection() {
        Customer customer = CurrentCustomer.getCurrentCustomer();
        String sound = customer.notificationsSound;

        if (sound == null)
            return 0;

        switch (sound) {
            case CUSTOM_SOUND:
                return 1;
        }

        return 0;
    }

    private String getSoundFromSeletion() {
        int selection = binding.listView.getCheckedItemPosition();

        switch (selection) {
            case 1:
                return CUSTOM_SOUND;
            case 0:
                return DEFAULT_SOUND;
        }

        return DEFAULT_SOUND;
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentStack.enableRightButton(this, position != getCurrentSelection());

        playSound(position);
    }

    private void playSound(int position) {
        Uri notification;

        switch (position) {
            case 0:
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                break;
            case 1:
                notification = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.notification_sound);
                break;
            default:
                return;
        }
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(getContext(), notification);
            mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mp.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
        }
    }
}
