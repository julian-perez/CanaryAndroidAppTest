package is.yranac.canary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import is.yranac.canary.adapter.SelectLocationAdapter;
import is.yranac.canary.databinding.FragmentLocationListBinding;
import is.yranac.canary.messages.CheckLocation;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;

/**
 * Created by michaelschroeder on 5/10/17.
 */

public class LocationListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private FragmentLocationListBinding binding;
    private SelectLocationAdapter adapter;

    private static final String locations = "locations";

    public static LocationListFragment newInstance(List<Location> location) {
        LocationListFragment locationFragment = new LocationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(locations, JSONUtil.getJSONString(location));
        locationFragment.setArguments(bundle);
        return locationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String locationsString = getArguments().getString(locations);
        List<Location> locations = JSONUtil.getList(Location[].class, locationsString);
        adapter = new SelectLocationAdapter(getContext(), locations);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Location location = adapter.getItem(position);

        TinyMessageBus.post(new CheckLocation(location.id, true));

    }


}
