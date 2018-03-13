package is.yranac.canary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.model.SetupPagerContent;

/**
 * Created by michaelschroeder on 2/8/17.
 */
public class AssembleImageSlidePageFragment extends Fragment {
    public static final String IMAGE_ID = "image_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_assemble_image_layout, container, false);
        Bundle args = getArguments();
        SetupPagerContent setupPagerContent = (SetupPagerContent) args.getSerializable(IMAGE_ID);

        TextView textView = (TextView) view.findViewById(R.id.image_text);
        textView.setText(setupPagerContent.string2);

        return view;
    }
}
