package is.yranac.canary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.CustomerAsset;
import is.yranac.canary.messages.CustomerBitmap;
import is.yranac.canary.messages.EntryAsset;
import is.yranac.canary.messages.EntryBitmap;
import is.yranac.canary.model.Customer;
import is.yranac.canary.utils.TinyMessageBus;

/**
 * Created by narendramanoharan on 6/24/16.
 */
public class CustomerListAdapter extends WearableListView.Adapter {

    private static final String LOG_TAG = "CustomerListAdapter";
    private Context mContext;
    private List<Customer> viewItemList;
    private String locationUri;

    public CustomerListAdapter(Context context, List<Customer> customerListViewItems, String locationUri) {
        this.mContext = context;
        this.viewItemList = customerListViewItems;
        this.locationUri = locationUri;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new CustomerListViewRowView(mContext));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        CustomerListViewRowView listViewRowView = (CustomerListViewRowView) holder.itemView;
        final Customer customer = viewItemList.get(position);

        listViewRowView.getCustomerName().setText(customer.getFullName());
        listViewRowView.getCustomerInitials().setText(customer.getInitials());

        if (locationUri.equalsIgnoreCase(customer.currentLocation)) {
            listViewRowView.getHomeFlag().setVisibility(View.VISIBLE);
        } else {
            listViewRowView.getHomeFlag().setVisibility(View.GONE);
        }

        listViewRowView.getCustomerAvatar().setVisibility(View.GONE);

        if (customer.asset != null) {
            TinyMessageBus.post(new CustomerAsset(customer.asset, listViewRowView.getCustomerAvatar()));
        }

    }



    @Override
    public int getItemCount() {
        return viewItemList.size();
    }

}
