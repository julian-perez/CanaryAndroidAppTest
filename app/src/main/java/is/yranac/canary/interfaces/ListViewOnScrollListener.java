package is.yranac.canary.interfaces;

import android.view.View;
import android.widget.AbsListView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Schroeder on 8/31/16.
 */
public abstract class ListViewOnScrollListener implements AbsListView.OnScrollListener {


    public abstract void onScrollPosition(int lastY);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onScrollPosition(getScroll(view));
    }

    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();

    private int getScroll(AbsListView view) {
        View c = view.getChildAt(0); //this is the first visible row
        if (c == null)
            return 0;
        int scrollY = -c.getTop();
        listViewItemHeights.put(view.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < view.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }

        float density = view.getResources().getDisplayMetrics().density;
        return (int) (scrollY / density);

    }

}