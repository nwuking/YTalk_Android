package com.nwuking.ytalk;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.DbUtils;
import com.nwuking.ytalk.activities.ChattingActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ChattingAdapter extends BaseAdapter {
    private static final String     LOG_TAG = "ChattingAdapterTag";

    private Context mContext;
    private List<MessageTextEntity> mChatMessages;
    private DbUtils db;
    private String                  drawablename;
    //private Date                  date;
    Bitmap bm;
    private int                     mnAccountID;
    private int                     position;
    private MsgDialog               mDialog;
    //private int					mResourceID;

    public ChattingAdapter(Context context, List<MessageTextEntity> messages) {
        super(context, messages);
        mContext = context;
        mChatMessages = messages;
        mDialog = new MsgDialog(context);
    }

    public void setChatMessages(List<MessageTextEntity> chatMessages) {
        mChatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (mChatMessages == null)
            return 0;

        return mChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageTextEntity msgItem = mChatMessages.get(position);
        int msgType = msgItem.getMsgType();
        int senderID = msgItem.getSenderID();
        int targetID = msgItem.getTargetID();
        int selfID = UserSession.getInstance().loginUser.get_userid();
        if (msgType == MessageTextEntity.CONTENT_TYPE_TEXT) {
            if (senderID == selfID) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.raw_send_message, null);
            }
            else {
//
//             convertView = LayoutInflater.from(mContext).inflate(R.layout.raw_received_message, null);
            }
        } else if (msgType == MessageTextEntity.CONTENT_TYPE_IMAGE_CONFIRM ||
                msgType == MessageTextEntity.CONTENT_TYPE_MOBILE_IMAGE) {
            ImageView img,headImg;
            if (senderID == selfID) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.picture_right, null);
            }
            else {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.picture_left, null);
            }

/*           img = (ImageView) convertView.findViewById(R.id.img);
           headImg = (ImageView) convertView.findViewById(R.id.img_head);
            Glide.with(convertView.getContext())
                    .load(new File(mChatMessages.get(position).getImgFile()))
                    .into(img);

            //好友发送的消息
            if (senderID != selfID) {
                Integer faceID;
                if (UserInfo.isGroup(senderID))
                    faceID = HeadUtil.get(targetID);
                else
                    faceID = HeadUtil.get(senderID);

                if (faceID == null) {
                    Log.e(LOG_TAG, "line 156, HeadUtil.get error, targetID: " + targetID + ", senderID: " + senderID);
                    faceID = 0;
                }

                Glide.with(convertView.getContext())
                        .load("file:///android_asset/head" + faceID + ".png")
                        .into(headImg);
            } else {
                Bitmap bmp = PictureUtil.getHeadPic(mContext.getAssets(), UserSession.getInstance().loginUser);
                if (bmp != null)
                    headImg.setImageBitmap(bmp);
            }
*/
            return convertView;
        } else {
            //TODO: 万一出现这种情况怎么办？
            LoggerFile.LogError("Illegal msg type:" + msgType);
        }

        final ViewHolder holder = new ViewHolder();
        long msgTimeLong = msgItem.getMsgTime() * 1000;
        Date msgTime = new Date(msgTimeLong);
        String msgTimeStr = TimeUtil.getFormattedTimeString(msgTime);
/*
        holder.time = (TextView) convertView.findViewById(R.id.timestamp);
        holder.time.setText(msgTimeStr);
        holder.img = (CircularImage) convertView.findViewById(R.id.iv_userhead);
        holder.msg_state = (ImageView) convertView.findViewById(R.id.msg_status);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.text = (TextView) convertView.findViewById(R.id.tv_chatcontent);
        holder.iv_sendPicture = (ImageView) convertView.findViewById(R.id.iv_sendPicture);
        holder.tvSenderName = (TextView) convertView.findViewById(R.id.tv_window_title);
*/
        //群消息显示发消息人的用户昵称
        if (UserInfo.isGroup(targetID)) {
            String senderName = UserSession.getInstance().getGroupMemberNickname(targetID, senderID);
            holder.tvSenderName.setText(senderName);
        }

        ChattingActivity chatActivity = (ChattingActivity) mContext;
        if (chatActivity != null) {
            if (UserInfo.isGroup(chatActivity.getTargetID())) {
                String userName = UserSession.getInstance().getNicknameById(senderID);
                if (holder.tv_name != null) {
                    holder.tv_name.setVisibility(View.VISIBLE);
                    holder.tv_name.setText(userName);
                }
            }
        }

//        holder.bar = (ProgressBar) convertView.findViewById(R.id.pb_sending);

        //正在发送和发送标识是否显隐藏
        int nFlag1 = View.GONE;
        int nFlag2 = View.GONE;
//		if (message.getmMsgState() == 0) {
//			nFlag1 = View.GONE;
//			nFlag2 = View.VISIBLE;
//		} else if (message.getmMsgState() == 1) {
//			nFlag1 = View.GONE;
//			nFlag2 = View.GONE;
//		} else {
//			nFlag1 = View.VISIBLE;
//			nFlag2 = View.GONE;
//		}

        if (holder.msg_state != null) {
            holder.msg_state.setVisibility(nFlag1);
        }
        if (holder.bar != null) {
            holder.bar.setVisibility(nFlag2);
        }

        //好友发送的消息
        if (senderID != selfID) {
            Integer faceID;
            if (UserInfo.isGroup(senderID))
                faceID = HeadUtil.get(targetID);
            else
                faceID = HeadUtil.get(senderID);

            if (faceID == null) {
                Log.e(LOG_TAG, "line 267, HeadUtil.get error, targetID: " + targetID + ", senderID: " + senderID);
                faceID = 0;
            }

            Glide.with(convertView.getContext())
                    .load("file:///android_asset/head" + faceID + ".png")
                    .into(holder.img);


            //if (storedUserList != null && storedUserList.size() > 0) {
//                Bitmap bmp = PictureUtil.getFriendHeadPic(mContext.getAssets(), friendInfo);
//                if (bmp != null) {
//                    holder.img.setImageBitmap(bmp);
//                }
            //}

            //自己发送的消息
        } else {
            Bitmap bmp = PictureUtil.getHeadPic(mContext.getAssets(), UserSession.getInstance().loginUser);
            if (bmp != null) {
                holder.img.setImageBitmap(bmp);
            }
        }

        //idd: [{"msgText":"dfg"}]
        //idd: |[1]|
        Face f;
        List<ContentText> c = msgItem.getContent();
        if (c != null) {
            for (int i = 0; i < c.size(); ++i) {
                ContentText t = c.get(i);
                if (t == null)
                    continue;

                if (t.getFaceID() != Face.DEFAULT_NULL_FACEID) {
                    //String html = "<img src='" + drawablename + "'/>";
                    String html = "<img src='" + "face" + t.getFaceID() + ".png'/>";
                    Html.ImageGetter imgGetter = new Html.ImageGetter() {

                        @Override
                        public Drawable getDrawable(String source) {
                            Bitmap b = getImageFromAssetsFile(source);
                            Drawable drawable = new BitmapDrawable(b);
                            drawable.setBounds(0, 0, 56, 56);
                            //drawable.setBackgroundColor(Color.TRANSPARENT);
                            return drawable;
                        }
                    };
                    CharSequence charSequence = Html.fromHtml(html, imgGetter, null);

//				getImageFromAssetsFile("face" + t.getFaceID() + ".png"
                    String faceIDStr = String.valueOf(t.getFaceID());
                    ImageSpan imgSpan = new ImageSpan(holder.text.getContext(), FaceConversionUtil.getInstace().getEmojeId(faceIDStr));
                    SpannableString spannableString = new SpannableString(" ");
                    spannableString.setSpan(imgSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (charSequence != null)
                        holder.text.append(spannableString);
                } else {
                    if (t.getMsgText() != null) {
                        holder.text.append(t.getMsgText());
                    }

                }
            }
        }

        return convertView;
    }

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    public void onInitView(View view, int position) {

    }


    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = mContext.getResources().getAssets();
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is, null, opts);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    static class ViewHolder {
        TextView      time;
        TextView      text;
        TextView      tv;
        TextView      tv_name;
        CircularImage img;                        //头像
        ImageView     iv_sendPicture;
        ImageView     msg_state;
        ProgressBar   bar;
        TextView      tvSenderName;               //发消息人昵称
    }
}
