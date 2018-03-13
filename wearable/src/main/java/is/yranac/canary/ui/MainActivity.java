package is.yranac.canary.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.GridViewPagerAdapter;
import is.yranac.canary.databinding.ActivityMainBinding;
import is.yranac.canary.model.CurrentCustomer;
import is.yranac.canary.model.Customer;
import is.yranac.canary.model.LocationAndEntry;
import is.yranac.canary.model.WearData;
import is.yranac.canary.utils.Constants;
import is.yranac.canary.utils.JSONUtil;

/**
 * Created by narendramanoharan on 6/20/16.
 */
public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private static final String LOG_TAG = "MainActivity";
    private ActivityMainBinding binding;
    private GoogleApiClient mGoogleApiClient;
    private List<LocationAndEntry> locationAndEntries;
    private static final String LOCATION_DATA = "location_data";
    private static final String THUMBNAIL = "thumbnail";
    private static final String CUSTOMER = "customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupGoogleAPI();
    }

    private void setupGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "onConnected");
        Wearable.DataApi.addListener(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.i(LOG_TAG, status.toString());
                retrieveDeviceNodeAndSendMessage();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "onConnectionFailed");
    }

    public void retrieveDeviceNodeAndSendMessage() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult getConnectedNodesResult) {

                List<Node> nodes = getConnectedNodesResult.getNodes();
                String nodeId;
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                    Log.i(LOG_TAG, "Node found");
                } else {
                    Log.i(LOG_TAG, "Node not found");
                    return;
                }

                Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, Constants.PATH_LOCATION_DATA, null);
            }


        });

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(Constants.PATH_LOCATION_DATA)) {
                DataMapItem splashDataMap = DataMapItem.fromDataItem(event.getDataItem());
                String locData = splashDataMap.getDataMap().getString(LOCATION_DATA);
                WearData wearData = JSONUtil.getObject(locData, WearData.class);
                locationAndEntries = wearData.locationAndEntries;
                CurrentCustomer.setCustomer(wearData.currentCustomer);

                for (LocationAndEntry locationAndEntry : locationAndEntries) {
                    if (locationAndEntry.entry != null) {
                        locationAndEntry.asset = splashDataMap.getDataMap().getAsset(THUMBNAIL + "_" + locationAndEntry.entry.id);
                    }
                    for (Customer customer : locationAndEntry.location.customers) {
                        customer.asset = splashDataMap.getDataMap().getAsset(CUSTOMER + "_" + customer.id);
                    }
                }
                updateUI();
                binding.progressBar.setVisibility(View.GONE);

                if (locationAndEntries.isEmpty()) {
                    binding.noDataTextView.setVisibility(View.VISIBLE);

                } else {

                    binding.pager.setVisibility(View.VISIBLE);

                }
            }

        }
    }

    private void updateUI() {
        GridViewPagerAdapter adapter = new GridViewPagerAdapter(getFragmentManager(), locationAndEntries);
        binding.pager.setAdapter(adapter);
    }


}



