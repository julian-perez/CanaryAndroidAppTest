package is.yranac.canary.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;

/**
 * Created by Schroeder on 9/2/16.
 */
public class MembershipSlideShowAdapter extends PagerAdapter {


    private final Context context;
    private final boolean isUnitedStates;

    public MembershipSlideShowAdapter(Context context, boolean isUnitedStates) {
        this.context = context;
        this.isUnitedStates = isUnitedStates;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (isUnitedStates)
            return 6;
        else
            return 4;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
