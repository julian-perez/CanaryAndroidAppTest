package is.yranac.canary.adapter;

import is.yranac.canary.fragments.ImageSlidePageFragment;
import is.yranac.canary.model.SetupPagerContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<SetupPagerContent> setupPagerContents;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<SetupPagerContent> setupPagerContents) {
        super(fm);
        this.setupPagerContents = setupPagerContents;

    }

    @Override
    public Fragment getItem(int position) {
        ImageSlidePageFragment fragment = new ImageSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ImageSlidePageFragment.IMAGE_ID, setupPagerContents.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return setupPagerContents.size();
    }
}