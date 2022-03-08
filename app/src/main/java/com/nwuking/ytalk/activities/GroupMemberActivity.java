package com.nwuking.ytalk.activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.UserInfo;
import com.nwuking.ytalk.UserSession;

import java.util.List;

public class GroupMemberActivity extends FragmentActivity {

    public static final String          GROUP_ID = "groupID";

    private List<UserInfo> mMembers;

    private RecyclerView mRecyclerView;
    private GroupMemberAdapter          mGroupMemberAdapter;

    private int                         groupID;
    private String                      groupName;


    private TextView mTxtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        mTxtName = (TextView) findViewById(R.id.tv_window_title);

        groupID = getIntent().getIntExtra(GROUP_ID, 0);
        groupName = getIntent().getStringExtra("groupName");

        mTxtName.setText(groupName +  " - " + "群成员");

        UserInfo currentGroup = UserSession.getInstance().getGroupById(groupID);
        if (currentGroup != null)
            mMembers = currentGroup.groupMembers;

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGroupMemberAdapter = new GroupMemberAdapter();
        mRecyclerView.setAdapter(mGroupMemberAdapter);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder> {

        @Override
        public GroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
            return new GroupMemberViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupMemberViewHolder holder, int position) {
            holder.bindDataToView(mMembers.get(position));
        }

        @Override
        public int getItemCount() {
            if (mMembers == null) {
                return 0;
            }
            return mMembers.size();
        }

        public class GroupMemberViewHolder extends RecyclerView.ViewHolder {

            private ImageView mMemberHead;
            private TextView  mMemberNickname;
            private TextView  mMemberUserName;

            public GroupMemberViewHolder(View itemView) {
                super(itemView);

                mMemberHead = (ImageView) itemView.findViewById(R.id.img_head);
                mMemberNickname = (TextView) itemView.findViewById(R.id.tv_window_title);
                mMemberUserName = (TextView) itemView.findViewById(R.id.txt_sign);
            }

            public void bindDataToView(UserInfo member) {
                Glide.with(itemView.getContext())
                        .load("file:///android_asset/head" + member.get_faceType() + ".png")
                        .into(mMemberHead);
                mMemberNickname.setText(member.get_nickname());
                mMemberUserName.setText(member.get_username());

                //HeadUtil.put(member.get_userid(), member.get_faceType());
            }
        }
    }
}
