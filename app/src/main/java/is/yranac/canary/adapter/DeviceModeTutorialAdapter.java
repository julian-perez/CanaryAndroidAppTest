package is.yranac.canary.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import is.yranac.canary.R;
import is.yranac.canary.fragments.ModeTutorialFragment.TutorialType;


/**
 * Created by Schroeder on 10/16/14.
 */
public class DeviceModeTutorialAdapter extends ViewPagerAdapter {

    private Context context;
    private TutorialType tutorialType;


    private int count = 2;

    public DeviceModeTutorialAdapter(Context context, TutorialType tutorialType) {
        super();
        this.context = context;
        this.tutorialType = tutorialType;
    }

    @Override
    public View getView(int position, ViewPager pager) {

        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        return view;
    }

    @Override
    public int getCount() {
        switch (tutorialType) {
            case MODE_TUTORIAL:
                return 8;
            case OTA:
                return count;
        }
        return 0;
    }

    public void setPageCount(int count) {

        if (count > this.count) {
            this.count = count;
            notifyDataSetChanged();
        }
    }

    public void setPageCountOverride(int count) {
        this.count = count;
        notifyDataSetChanged();
    }


    public void setTutorialType(TutorialType tutorialType) {
        this.tutorialType = tutorialType;
        notifyDataSetChanged();
    }
}
