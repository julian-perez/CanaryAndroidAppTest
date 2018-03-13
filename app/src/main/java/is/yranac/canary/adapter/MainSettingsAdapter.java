package is.yranac.canary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.yranac.canary.fragments.AddALocationFragment;
import is.yranac.canary.fragments.LocationFragment;
import is.yranac.canary.fragments.LocationListFragment;
import is.yranac.canary.model.location.Location;

public class MainSettingsAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_LOCATIONS = 4;
    private static final String LOG_TAG = "MainSettingsAdapter";

    private Map<Integer, LocationFragment> mPageReferences = new HashMap<>();

    private final List<Location> locations = new ArrayList<>();

    public MainSettingsAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainSettingsAdapter(FragmentManager fm, List<Location> locations) {

        super(fm);
        this.locations.addAll(locations);
    }

    @Override
    public Fragment getItem(int position) {


        if (locations.size() > MAX_LOCATIONS) {
            if (position == 0) {
                List<Location> locations = new ArrayList<>(this.locations);
                locations.remove(0);
                return LocationListFragment.newInstance(locations);

            }
        }

        if (position == getCount() - 1) {
            return new AddALocationFragment();
        }
        Location location;
        if (locations.size() > MAX_LOCATIONS) {
            location = locations.get(0);
        } else {
            location = locations.get(position);
        }

        LocationFragment locationFragment;
        if (mPageReferences.containsKey(location.id)) {
            locationFragment = mPageReferences.get(location.id);
        } else {
            locationFragment = LocationFragment.newInstance(location.id);
            mPageReferences.put(location.id, locationFragment);
        }


        return locationFragment;
    }

    @Override
    public int getCount() {
        if (locations.size() > MAX_LOCATIONS) {
            return 3;
        }
        return locations.size() + 1;
    }


    public void setLocations(List<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        notifyDataSetChanged();
    }

    public Location getLocation(int poistion) {
        return locations.get(poistion);
    }

    public List<Location> getLocation() {
        return locations;
    }

}
