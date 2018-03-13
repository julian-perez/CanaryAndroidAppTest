package is.yranac.canary.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.EntryDetailTagAdapter;
import is.yranac.canary.databinding.FragmentEntrydetailTagBinding;
import is.yranac.canary.messages.CustomTagEditBackPressed;
import is.yranac.canary.messages.CustomTagEntered;
import is.yranac.canary.messages.DeleteTag;
import is.yranac.canary.messages.EntryLabelStringUpdated;
import is.yranac.canary.messages.GetTags;
import is.yranac.canary.messages.InsertTag;
import is.yranac.canary.messages.Tags;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.LabelDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EntryDetailTagFragment extends Fragment {
    private static final String LOG_TAG = "EntryDetailTagFragment";

    private EntryDetailTagAdapter entryDetailTagAdapter;

    private AlertDialog mProgressDialog;

    private long entryId;
    private List<Tag> tagList = new ArrayList<>();
    private List<Tag> startingTagList = new ArrayList<>();

    private List<String> entryLabelStringList = new ArrayList<>();

    private ArrayList<Tag> defaultTags = new ArrayList<>();
    private SwipeToDismissTouchListener<ListViewAdapter> touchListener;
    private AbsListView.OnScrollListener scrollListener;
    private String customTag;

    private FragmentEntrydetailTagBinding binding;

    public static EntryDetailTagFragment newInstance(long entryId) {
        EntryDetailTagFragment f = new EntryDetailTagFragment();

        Bundle args = new Bundle();
        args.putLong("entryId", entryId);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entrydetail_tag, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        entryId = getArguments().getLong("entryId");

        // the current tags for the event
        entryLabelStringList = LabelDatabaseService.getLabelsForEntryIdAsStringList(entryId);

        // all possible tags for this entry
        getTags();

        entryDetailTagAdapter = new EntryDetailTagAdapter(getActivity(), tagList);
        binding.entrydetailTagList.setAdapter(entryDetailTagAdapter);
        setDoneButtonState();

        touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(binding.entrydetailTagList),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return position != entryDetailTagAdapter.getCount() - 1 &&
                                        position >= defaultTags.size();
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                Tag tag = entryDetailTagAdapter.getItem(position);
                                TinyMessageBus.post(new DeleteTag(tag.title));
                                entryDetailTagAdapter.remove(position);
                            }
                        });

        binding.entrydetailTagList.setOnTouchListener(touchListener);
        scrollListener = (AbsListView.OnScrollListener) touchListener.makeScrollListener();
        binding.entrydetailTagList.setOnScrollListener(tagListScrollListener);
        binding.entrydetailTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    if (position < tagList.size()) {
                        hideSoftKeyboard();
                        tagList.get(position).selected = !tagList.get(position).selected;
                        entryDetailTagAdapter.notifyDataSetChanged();
                    }
                    setDoneButtonState();
                }
            }
        });

        final View footer = getLayoutInflater(savedInstanceState).inflate(R.layout.listrow_editable_cv_tag, binding.entrydetailTagList, false);
        footer.setAlpha(0.7f);

        final EditText editText = (EditText) footer.findViewById(R.id.cv_tag_text_view);
        final ImageView checkImageView = (ImageView) footer.findViewById(R.id.cv_tag_checkbox);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                customTag = s.toString();
                if (!StringUtils.isNullOrEmpty(customTag)) {
                    footer.setAlpha(1.0f);
                    checkImageView.setVisibility(View.VISIBLE);
                } else {
                    footer.setAlpha(0.7f);
                    checkImageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        editText.addTextChangedListener(textWatcher);
        editText.setOnKeyListener(new EditText.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK ||
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideSoftKeyboard();
                    binding.entrydetailTagList.clearFocus();
                    return true;
                }
                return false;
            }
        });

        binding.entrydetailTagList.addFooterView(footer);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        if (touchListener != null) {
            touchListener.processPendingDismisses();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus
                .register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus
                .unregister(this);
        super.onStop();
    }

    private AlertDialog getProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = AlertUtils.initLoadingDialog(getActivity(), getActivity().getString(R.string.loading_dialog));
        return mProgressDialog;
    }

    private void getTags() {
        TinyMessageBus.post(new GetTags());
    }


    private void setDoneButtonState() {
        boolean isEnabled = false;

        for (int i = 0; i < startingTagList.size(); i++) {
            if (tagList.size() <= i)
                continue;

            if (startingTagList.get(i).selected != tagList.get(i).selected) {
                isEnabled = true;
                break;
            }
        }

        if (customTag != null) {
            if (!StringUtils.isNullOrEmpty(customTag
                    .trim())) {
                isEnabled = true;
            }
        }

        if (isEnabled) {
            binding.doneButton.setOnClickListener(doneButtonClickListener);
            binding.doneButton.setAlpha(1.0f);
        } else {
            binding.doneButton.setAlpha(0.5f);
            binding.doneButton.setOnClickListener(null);
        }
    }

    View.OnClickListener doneButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            if (!baseActivity.hasInternetConnection()) return;

            getProgressDialog().show();

            EntryAPIService.patchEntryLabels(
                    getLabelListForApi(), String.valueOf(entryId), new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            LabelDatabaseService.setLabelsForEntryFromStringList(entryId,
                                    getEntryLabelStrings());
                            TinyMessageBus
                                    .post(new EntryLabelStringUpdated(getNewLabelString()));

                            getProgressDialog().dismiss();

                            if (getActivity() != null && !((BaseActivity) getActivity()).isPaused())
                                getActivity().onBackPressed();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            getProgressDialog().dismiss();
                            try {
                                AlertUtils.showGenericAlert(getContext(), Utils.getErrorMessageFromRetrofit(getContext(), error));
                            } catch (JSONException ignored) {

                            }
                        }
                    });
        }
    };

    AbsListView.OnScrollListener tagListScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            hideSoftKeyboard();
            binding.entrydetailTagList.clearFocus();
            setDoneButtonState();
            scrollListener.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view,
                             int firstVisibleItem,
                             int visibleItemCount,
                             int totalItemCount) {
            scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    };

    private void hideSoftKeyboard() {
        try {
            InputMethodManager imm =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus()
                            .getWindowToken(), 0);
        } catch (Exception ignore) {

        }
    }

    public class Tag {
        public String title;
        public boolean selected;

        public Tag(String title) {
            this.title = title;
            selected = entryLabelStringList.contains(title.toLowerCase(Locale.getDefault()));
        }
    }

    public String getLabelListForApi() {
        StringBuilder sb = new StringBuilder();

        List<String> entryLabels = getEntryLabelStrings();

        if (entryLabels.size() == 0) return "";

        String lastLabel = entryLabels.get(entryLabels.size() - 1);
        for (String entryLabel : entryLabels) {
            sb.append("\"").append(entryLabel).append("\"");
            if (!lastLabel.equals(entryLabel)) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public String getNewLabelString() {
        StringBuilder sb = new StringBuilder();

        List<String> entryLabels = getEntryLabelStrings();

        if (entryLabels.size() == 0) return "";

        String lastLabel = entryLabels.get(entryLabels.size() - 1);
        for (String entryLabel : entryLabels) {
            if (entryLabels.size() == 2) {
                if (lastLabel.equalsIgnoreCase(entryLabel)) {
                    sb.append(" and ");
                }
                sb.append(entryLabel);
            } else {
                if (lastLabel.equalsIgnoreCase(entryLabel) && sb.length() != 0) {
                    sb.append(", and ");
                } else if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(entryLabel);
            }
        }

        return sb.toString();
    }

    private List<String> getEntryLabelStrings() {
        Set<String> entryLabels = new HashSet<>();
        for (Tag tag : tagList) {
            if (tag.selected) {
                entryLabels.add(tag.title);
            }
        }


        if (!StringUtils.isNullOrEmpty(customTag)) {
            customTag = customTag.trim();

            if (customTag.length() > 0) {
                if (entryLabels.add(customTag))
                    updateCustomTags(customTag);

            }
        }

        return new ArrayList<>(entryLabels);
    }

    private void updateCustomTags(String customTag) {
        TinyMessageBus.post(new InsertTag(customTag, true));
    }

    @Subscribe
    public void onCustomTagEntered(CustomTagEntered message) {
        hideSoftKeyboard();
        binding.entrydetailTagList.clearFocus();
        setDoneButtonState();
        entryDetailTagAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onCustomTagEditBackPressed(CustomTagEditBackPressed message) {
        setDoneButtonState();
    }

    @Subscribe
    public void gotTags(Tags tags) {
        startingTagList = new ArrayList<>();
        for (String tag : tags.defaultTags) {
            tagList.add(new Tag(tag));
            startingTagList.add(new Tag(tag));
        }
        defaultTags = new ArrayList<>(tagList);

        for (String tag : tags.userTags) {
            tagList.add(new Tag(tag));
            startingTagList.add(new Tag(tag));
        }
        entryDetailTagAdapter.notifyDataSetChanged();
    }
}
