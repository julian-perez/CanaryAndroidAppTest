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
import com.google.android.gms.wearable.Wearable;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.CanaryWearApplication;
import is.yranac.canary.R;
import is.yranac.canary.adapter.CustomerListAdapter;
import is.yranac.canary.databinding.CustomerDetailsFragmentBinding;
import is.yranac.canary.messages.CustomerAsset;
import is.yranac.canary.messages.CustomerBitmap;
import is.yranac.canary.model.Customer;
import is.yranac.canary.services.AnalyticService;
import is.yranac.canary.utils.JSONUtil;
import is.yranac.canary.utils.TinyMessageBus;

/**
 * Created by narendramanoharan on 6/24/16.
 */
public class CustomerDetailsFragment extends Fragment {

    private static final String LOG_TAG = "CustomerDetailsFragment";
    private CustomerDetailsFragmentBinding binding;
    private String locationUri;
    private List<Customer> customers;


    private static final String LOCATION = "location";
    private static final String CUSTOMERS = "customers";
    private CustomerListAdapter customerListAdapter;

    public static CustomerDetailsFragment newInstance(List<Customer> customers, String locationUri) {
        Bundle args = new Bundle();
        args.putString(LOCATION, locationUri);
        args.putString(CUSTOMERS, JSONUtil.getJSONString(customers));
        CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
        customerDetailsFragment.setArguments(args);
        return customerDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.customer_details_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationUri = getArguments().getString(LOCATION);
        String customersString = getArguments().getString(CUSTOMERS);
        customers = JSONUtil.getObject(customersString, new TypeToken<List<Customer>>() {
        }.getType());

        binding.customerList.setGreedyTouchMode(true);
        updateUI();
    }

    public void updateUI() {
        customerListAdapter = new CustomerListAdapter(getActivity(), customers, locationUri);
        binding.customerList.setAdapter(customerListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        sendViewIntent("CustomerFragment", "Started");
    }

    @Override
    public void onPause() {
        super.onPause();
        TinyMessageBus.unregister(this);
        sendViewIntent("CustomerFragment", "Paused");
    }


    private void sendViewIntent(String category, String event) {
        Intent analyticsIntent = new Intent(CanaryWearApplication.getContext(), AnalyticService.class);
        analyticsIntent.putExtra("category", category);
        analyticsIntent.putExtra("event", event);
        getActivity().startService(analyticsIntent);
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void toBitmap(CustomerAsset customerAsset) {
        if (getActivity() == null)
            return;
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .build();
        if (mGoogleApiClient.blockingConnect().isSuccess()) {
            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, customerAsset.asset).await().getInputStream();
            Bitmap avatar = BitmapFactory.decodeStream(assetInputStream);
            if (avatar != null) {
                TinyMessageBus.post(new CustomerBitmap(avatar, customerAsset.imageView));
            }
            mGoogleApiClient.disconnect();
        }
    }

    @Subscribe
    public void loadBitmap(CustomerBitmap customerBitmap) {

        customerBitmap.imageView.setVisibility(View.VISIBLE);
        customerBitmap.imageView.setImageBitmap(customerBitmap.bitmap);

    }
}
