package is.yranac.canary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

public class VerticalFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<Fragment> fragments = new SparseArray<>();

    public VerticalFragmentPagerAdapter(FragmentManager fm, SparseArray<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }



    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}