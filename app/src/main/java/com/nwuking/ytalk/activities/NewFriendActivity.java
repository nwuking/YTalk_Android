package com.nwuking.ytalk.activities;


import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nwuking.ytalk.MsgType;
import com.nwuking.ytalk.OperateFriendEnum;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.UserInfo;
import com.nwuking.ytalk.db.Contacts;
import com.nwuking.ytalk.db.MyDbUtil;
import com.nwuking.ytalk.net.NetWorker;

import java.util.ArrayList;
import java.util.List;

/*
 * 新的朋友
 */
public class NewFriendActivity extends BaseActivity {
    private TextView addsend;
    private MyHandler           handler = new MyHandler();
    private int                 uAccountID;

    private RecyclerView mRecyclerView;
    private AddedUserAdapter    mAdapter;

    private List<Contacts> mUsers = new ArrayList<>();

    @Override
    public void onClick(View v) {
/*        switch (v.getId()) {
            case R.id.btn_back:
                //con.getFriends(1);
                this.setResult(0);
                finish();
                back();
                break;

            //case R.id.addsend:
            //	startActivity(AddFriendActivity.class);
            //	back();
            //	break;

            default:
                break;
        }

 */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        addsend.postDelayed(new Runnable() {
            @Override
            public void run() {
                NetWorker.getFriendList();
            }
        }, 300);
    }

    @Override
    protected int getContentView() {
  ///      return R.layout.activity_newfriend;
        return 0;
    }

    @Override
    protected void initData() {
        addsend.setOnClickListener(this);
/*        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddedUserAdapter();
        mRecyclerView.setAdapter(mAdapter);

        ContactsDao contactsDao = MyDbUtil.getContactsDao();
        if (contactsDao != null) {
            mUsers = contactsDao.loadAll();
            mAdapter.notifyDataSetChanged();
        }

 */
    }

    @Override
    protected void setData() {
        if(application.getMemberEntity()!=null){
            uAccountID = application.getMemberEntity().getuAccountID();
        }
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);
        if (msg.what == MsgType.MSG_ORDER_ADD_FRIEND) {
            //收到加好友信息
            if (msg.arg1 == OperateFriendEnum.OPERATE_FRIEND_RECV_APPLY) {
                mUsers.add((Contacts) msg.obj);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);

            switch (msg.what) {
                case 0:
                    String text = (String) (msg.obj);
                    String[] ss = text.split("\\|");
                    String cmdtype = ss[0];
                    String TargetID = ss[1];
                    int uTargetId = Integer.parseInt(TargetID);
                    if (cmdtype.equals("Agree")) {
                        //con.addFriend(uTargetId, tms.User.TargetsAdd.cmd.Agree);
                    } else if (cmdtype.equals("Apply")){
                        //con.addFriend(uTargetId, tms.User.TargetsAdd.cmd.Apply);
                    }

                    break;

                default:
                    break;
            }
        }
    }


    public class AddedUserAdapter extends RecyclerView.Adapter<AddedUserAdapter.AddedUserViewHolder> {

        private LayoutInflater mLayoutInflater;

       @Override
        public AddedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mLayoutInflater == null) {
                mLayoutInflater = LayoutInflater.from(parent.getContext());
            }

  ///        View view = mLayoutInflater.inflate(R.layout.item_search_user, parent, false);
           View view = mLayoutInflater.inflate(R.layout.activity_main, parent, false);
            return new AddedUserViewHolder(view);
        }


        @Override
        public void onBindViewHolder(AddedUserViewHolder holder, int position) {
            holder.bindDataToView(mUsers.get(position));
        }

        @Override
        public int getItemCount() {
            if (mUsers == null) {
                return 0;
            }
            return mUsers.size();

        }

        public class AddedUserViewHolder extends RecyclerView.ViewHolder {

            private ImageView mImgHead;
            private TextView 	mTxtName;
            private TextView 	mTxtInfo;
            private Button mBtnAgree;

            public AddedUserViewHolder(View itemView) {
                super(itemView);
/*
                mImgHead = (ImageView) itemView.findViewById(R.id.img_head);
                mTxtName = (TextView) itemView.findViewById(R.id.tv_window_title);
                mTxtInfo = (TextView) itemView.findViewById(R.id.tv_nickname);
                mBtnAgree = (Button) itemView.findViewById(R.id.btn_add);
                mBtnAgree.setText("同意");

 */
            }


            public void bindDataToView(final Contacts result) {
                if (result.getType() == OperateFriendEnum.OPERATE_FRIEND_RECV_APPLY) {//B收到A的好友申请
                    mBtnAgree.setText("同意");
  ///                  mBtnAgree.setTextColor(itemView.getResources().getColor(R.color.green));
                    mBtnAgree.setEnabled(true);
                } else if (result.getType() == OperateFriendEnum.OPERATE_FRIEND_RESPONSE_APPLY) {//A收到B的同意信息
                    if (UserInfo.isGroup(result.getUserid()))
                        mBtnAgree.setText("已加入");
                    else
                        mBtnAgree.setText("已同意");

    ///                mBtnAgree.setTextColor(itemView.getResources().getColor(R.color.gray));
                    mBtnAgree.setEnabled(false);
                }

                mTxtName.setText(result.getUsername());

                mBtnAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetWorker.acceptNewFriend(result.getUserid(), result.getUsername());
                        if (UserInfo.isGroup(result.getUserid()))
                            mBtnAgree.setText("已加入");
                        else
                            mBtnAgree.setText("已同意");

     ///                   mBtnAgree.setTextColor(itemView.getResources().getColor(R.color.gray));
                        mBtnAgree.setEnabled(false);
                        //TODO: 最好是做个应答再刷新好友列表
                        NetWorker.getFriendList();
                    }
                });
            }
        }
    }

}