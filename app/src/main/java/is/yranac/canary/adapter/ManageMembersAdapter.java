package is.yranac.canary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import is.yranac.canary.R;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.model.membership.Member;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.util.ImageUtils;

public class ManageMembersAdapter extends ArrayAdapter<Member> {


    private static final String LOG_TAG = "ManageMembersAdapter";

    private LayoutInflater layoutInflater;

    public ManageMembersAdapter(Context context) {
        super(context, R.layout.listrow_manage_member, new ArrayList<Member>());
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ManageMemberHolder {
        public View mainLayout;
        public ImageView imageView;
        public TextView textView;
        public TextView detialTextView;
        public TextView customerInitials;
        public View customerInitialsGrayCircle;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ManageMemberHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listrow_manage_member, parent, false);
            holder = new ManageMemberHolder();
            holder.mainLayout = view.findViewById(R.id.main_layout);
            holder.imageView = (ImageView) view.findViewById(R.id.member_image_view);
            holder.textView = (TextView) view.findViewById(R.id.member_title);
            holder.customerInitialsGrayCircle = view.findViewById(R.id.gray_circle);
            holder.customerInitials = (TextView) view.findViewById(R.id.customer_initials);
            holder.detialTextView = (TextView) view.findViewById(R.id.invite_sent_text_view);
            view.setTag(holder);
        } else {
            holder = (ManageMemberHolder) view.getTag();
        }

        final Member member = getItem(position);
        if (member == null)
            return view;

        switch (member.memberType) {
            case Member.MEMBER:
                Customer customer = member.customer;
                holder.textView.setText(customer.getFullName());
                holder.customerInitials.setVisibility(View.VISIBLE);
                holder.customerInitialsGrayCircle.setVisibility(View.VISIBLE);
                holder.customerInitials.setText(customer.getInitials());
                holder.imageView.setVisibility(View.GONE);
                holder.detialTextView.setVisibility(View.GONE);
                updateAvatarView(holder.imageView, customer);
                break;
            case Member.INVITATION:
                Invitation invitation = member.invitation;
                holder.textView.setText(invitation.getFullName());
                holder.customerInitialsGrayCircle.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.customerInitials.setVisibility(View.VISIBLE);
                holder.customerInitials.setText(invitation.getInitials());
                holder.detialTextView.setVisibility(View.VISIBLE);
                break;
            case Member.ADD_NEW_MEMBER:
                holder.textView.setText(R.string.invite_a_person);
                holder.imageView.setImageResource(R.drawable.ic_add_icon);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.customerInitialsGrayCircle.setVisibility(View.GONE);
                holder.customerInitials.setVisibility(View.GONE);
                holder.detialTextView.setVisibility(View.GONE);
                break;
        }

        return view;
    }

    private void updateAvatarView(ImageView imageView, Customer customer) {
        Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);

        if (avatar != null) {
            ImageUtils.loadAvatar(imageView, avatar.thumbnailUrl());
        }

    }
}
