package is.yranac.canary.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.databinding.CustomerListEntryBinding;

/**
 * Created by narendramanoharan on 8/9/16.
 */
public class CustomerListViewRowView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    private CustomerListEntryBinding customerListEntryBinding;

    public CustomerListViewRowView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        customerListEntryBinding = DataBindingUtil.inflate(inflater, R.layout.customer_list_entry, this, true);
    }

    @Override
    public void onCenterPosition(boolean b) {
    }

    @Override
    public void onNonCenterPosition(boolean b) {
    }

    public ImageView getCustomerAvatar() {
        return customerListEntryBinding.customerAvatar;
    }

    public TextView getCustomerName() {
        return customerListEntryBinding.customerName;
    }

    public ImageView getHomeFlag() {
        return customerListEntryBinding.homeFlagSmall;
    }

    public TextView getCustomerInitials() {
        return customerListEntryBinding.customerInitials;
    }
}
