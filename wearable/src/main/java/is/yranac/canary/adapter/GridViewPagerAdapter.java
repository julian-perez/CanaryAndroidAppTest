package is.yranac.canary.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.fragments.CustomerDetailsFragment;
import is.yranac.canary.fragments.EntryFragment;
import is.yranac.canary.fragments.HomeScreenFragment;
import is.yranac.canary.model.LocationAndEntry;
import is.yranac.canary.model.SimpleRow;

public class GridViewPagerAdapter extends FragmentGridPagerAdapter {

    private List<SimpleRow> mPages;

    public GridViewPagerAdapter(FragmentManager fm, List<LocationAndEntry> locationAndEntries) {
        super(fm);
        initializePages(locationAndEntries);
    }

    private void initializePages(List<LocationAndEntry> locationAndEntries) {

        mPages = new ArrayList<>();
        for (LocationAndEntry locationAndEntry : locationAndEntries) {
            SimpleRow row = new SimpleRow();
            row.addPages(HomeScreenFragment.newInstance(locationAndEntry));
            row.addPages(CustomerDetailsFragment.newInstance(locationAndEntry.location.customers, locationAndEntry.location.resourceUri));
            if (locationAndEntry.entry != null) {
                row.addPages(EntryFragment.newInstance(locationAndEntry.entry, locationAndEntry.asset));
            }
            mPages.add(row);
        }

    }

    @Override
    public Fragment getFragment(int i, int i1) {
        return mPages.get(i).getPages(i1);
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int i) {
        return mPages.get(i).size();
    }
}