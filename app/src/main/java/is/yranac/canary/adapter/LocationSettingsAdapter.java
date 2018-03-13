package is.yranac.canary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import is.yranac.canary.fragments.settings.LocationSettingsFragment;
import is.yranac.canary.fragments.settings.MembershipFragment;

/**
 * Created by michaelschroeder on 6/7/17.
 */

public class LocationSettingsAdapter extends FragmentPagerAdapter {

    private final SparseArray<Fragment> fragments = new SparseArray<>();

    public LocationSettingsAdapter(FragmentManager fm, int locationId) {
        super(fm);
        MembershipFragment membershipFragment = new MembershipFragment();
        LocationSettingsFragment fragment = LocationSettingsFragment.newInstance(locationId);
        fragments.put(0, membershipFragment);
        fragments.put(1, fragment);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    public MembershipFragment getMembershipFragment() {
        return (MembershipFragment) fragments.get(0);
    }

    public LocationSettingsFragment getLocationSettingsFragment() {
        return (LocationSettingsFragment) fragments.get(1);
    }
}
