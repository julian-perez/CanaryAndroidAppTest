package is.yranac.canary.ui.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import is.yranac.canary.R;
import is.yranac.canary.databinding.MenuIconBinding;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by Schroeder on 8/31/16.
 */
public class MenuIcon extends FrameLayout {
    private MenuIconBinding binding;
    private boolean inPreview = false;

    public MenuIcon(Context context) {
        super(context);
        init(context);
    }

    public MenuIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = MenuIconBinding.inflate(inflater, this, true);
        setInPreview(inPreview);
    }

    public void setInPreview(boolean inPreview) {
        this.inPreview = inPreview;
        if (binding == null)
            return;

        int margin;
        if (inPreview) {
            binding.circleView.setVisibility(VISIBLE);
            margin = DensityUtil.dip2px(getContext(), 3);
        } else {

            margin = DensityUtil.dip2px(getContext(), 4);
            binding.circleView.setVisibility(GONE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.topLine.getLayoutParams();
        params.rightMargin = margin;

        binding.topLine.setLayoutParams(params);
    }


}
