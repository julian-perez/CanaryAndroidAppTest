package is.yranac.canary.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.CanaryWearApplication;
import is.yranac.canary.utils.Constants;

/**
 * Created by narendramanoharan on 7/25/16.
 */
public class AnalyticService extends IntentService {


    public static void sendViewIntent(Context context, String category, String event) {
        Intent analyticsIntent = new Intent(CanaryWearApplication.getContext(), AnalyticService.class);
        analyticsIntent.putExtra("category", category);
        analyticsIntent.putExtra("event", event);
        context.startService(analyticsIntent);
    }

    public AnalyticService() {
        super("AnalyticService");
    }

    private List<String> getNodes(GoogleApiClient mGoogleApiClient) {
        ArrayList<String> results = new ArrayList<>();
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(AnalyticService.this).addApi(Wearable.API).build();
        mGoogleApiClient.blockingConnect();
        List<String> nodes = getNodes(mGoogleApiClient);
        DataMap dataMap = new DataMap();
        dataMap.putString("category", intent.getStringExtra("category"));
        dataMap.putString("event", intent.getStringExtra("event"));
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodes.get(0), Constants.PATH_ANALYTICS, dataMap.toByteArray());
    }
}
