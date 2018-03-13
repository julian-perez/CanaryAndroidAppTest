package is.yranac.canary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.CanaryWearApplication;
import is.yranac.canary.R;
import is.yranac.canary.databinding.EntryFragmentBinding;
import is.yranac.canary.messages.EntryAsset;
import is.yranac.canary.messages.EntryBitmap;
import is.yranac.canary.messages.OpenActivity;
import is.yranac.canary.model.Entry;
import is.yranac.canary.services.AnalyticService;
import is.yranac.canary.utils.Constants;
import is.yranac.canary.utils.DateUtil;
import is.yranac.canary.utils.JSONUtil;
import is.yranac.canary.utils.TinyMessageBus;

/**
 * Created by narendramanoharan on 6/24/16.
 */
public class EntryFragment extends Fragment {

    private static final String LOG_TAG = "EntryFragment";
    public Entry entry;
    private EntryFragmentBinding binding;
    private String nodeId;
    private Asset asset;

    private static final String ENTRY = "entry";
    private static final String ASSET = "asset";

    public static EntryFragment newInstance(Entry entry, Asset asset) {
        Bundle args = new Bundle();

        args.putString(ENTRY, JSONUtil.getJSONString(entry));
        args.putParcelable(ASSET, asset);

        EntryFragment entryFragment = new EntryFragment();
        entryFragment.setArguments(args);
        return entryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.entry_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String entryString = args.getString(ENTRY);
        entry = JSONUtil.getObject(entryString, Entry.class);
        asset = args.getParcelable(ASSET);
        updateUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        if (asset != null) {
            TinyMessageBus.post(new EntryAsset(asset));
        }
        sendViewIntent("EntryFragment", "Started");

    }

    @Override
    public void onPause() {
        super.onPause();

        TinyMessageBus.unregister(this);
    }


    @Subscribe(mode = Subscribe.Mode.Background)
    public void toBitmap(EntryAsset profileAsset) {
        if (getActivity() == null)
            return;
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .build();
        if (mGoogleApiClient.blockingConnect().isSuccess()) {
            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, profileAsset.asset).await().getInputStream();
            Bitmap avatar = BitmapFactory.decodeStream(assetInputStream);
            Log.i(LOG_TAG, String.valueOf(avatar == null));
            TinyMessageBus.post(new EntryBitmap(avatar));
            mGoogleApiClient.disconnect();
        }
    }

    @Subscribe(mode = Subscribe.Mode.Main)
    public void catchBitmap(EntryBitmap profileBitmap) {
        binding.entryThumbnail.setImageBitmap(profileBitmap.getBitmap());
    }

    public void updateUI() {
        binding.entryLabel.setText(entry.description);
        binding.entryTimeLabel.setText(DateUtil.utcDateToDisplayString(entry.startTime));
        binding.entryThumbnail.setImageResource(R.drawable.img_not_available);
        binding.entryThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyMessageBus.post(new OpenActivity());
            }
        });
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void sendEntryDetailActivityOpen(OpenActivity openActivity) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .build();
        if (mGoogleApiClient.blockingConnect().isSuccess()) {
            NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            List<Node> nodes = result.getNodes();
            if (nodes.size() > 0) {
                nodeId = nodes.get(0).getId();
            }

            byte[] data = String.valueOf(entry.id).getBytes();
            Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, Constants.PATH_OPEN_ENTRY_ACTIVITY, data);
            mGoogleApiClient.disconnect();
        }
    }

    private void sendViewIntent(String category, String event) {
        Intent analyticsIntent = new Intent(CanaryWearApplication.getContext(), AnalyticService.class);
        analyticsIntent.putExtra("category", category);
        analyticsIntent.putExtra("event", event);
        getActivity().startService(analyticsIntent);
    }
}