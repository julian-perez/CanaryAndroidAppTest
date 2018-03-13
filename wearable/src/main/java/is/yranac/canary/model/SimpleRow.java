package is.yranac.canary.model;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelschroeder on 10/5/16.
 */

public class SimpleRow {

    final List<Fragment> mPagesRow = new ArrayList<Fragment>();

    public void addPages(Fragment page) {
        mPagesRow.add(page);
    }

    public Fragment getPages(int index) {
        return mPagesRow.get(index);
    }

    public int size() {
        return mPagesRow.size();
    }
}
