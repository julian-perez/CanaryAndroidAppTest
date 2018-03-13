package is.yranac.canary.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.utils.Constants;

import static android.os.Build.FINGERPRINT;

/**
 * Created by narendramanoharan on 7/25/16.
 */
public class ErrorService extends IntentService {

    public ErrorService() {
        super("ErrorService");
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
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(ErrorService.this).addApi(Wearable.API).build();
        mGoogleApiClient.blockingConnect();
        List<String> nodes = getNodes(mGoogleApiClient);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(intent.getSerializableExtra("exception"));
            byte[] exceptionData = bos.toByteArray();
            DataMap dataMap = new DataMap();
            dataMap.putString("fingerprint", FINGERPRINT);
            dataMap.putByteArray("exception", exceptionData);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, nodes.get(0), Constants.PATH_ERROR, dataMap.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException exx) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException exx) {
                // ignore close exception
            }
        }
    }
}
