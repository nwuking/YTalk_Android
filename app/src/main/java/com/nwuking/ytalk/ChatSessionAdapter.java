package com.nwuking.ytalk;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nwuking.ytalk.net.ChatMsgMgr;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class ChatSessionAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatSession> mChatSessions;
    private ChatMsgMgr mChatMsgMgr;

    public ChatSessionAdapter(Context context, List<ChatSession> sessions) {
        super();
        mContext = context;
        mChatSessions = sessions;
        mChatMsgMgr = ChatMsgMgr.getInstance();
        Collections.sort(mChatSessions, new Comparator<ChatSession>() {
            @Override
            public int compare(ChatSession chatSession, ChatSession t1) {
                if (chatSession.getTime().after(t1.getTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    public void setSessionList(List<ChatSession> sessions) {
        mChatSessions = sessions;
        Collections.sort(mChatSessions, new Comparator<ChatSession>() {
            @Override
            public int compare(ChatSession chatSession, ChatSession t1) {
                if (chatSession.getTime().after(t1.getTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    @Override
    public int getCount() {
        return mChatSessions.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("log", String.valueOf(position));

        ViewHolder holder = new ViewHolder();
        ChatSession sessionItem = mChatSessions.get(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sessionlist_adapter,null);

        holder.name = (TextView) convertView.findViewById(R.id.tv_window_title);
        holder.head = (ImageView) convertView.findViewById(R.id.img_head);
        holder.rl_newmsg = (RelativeLayout) convertView.findViewById(R.id.rl_newmsg);
        holder.tv_message_num = (TextView) convertView.findViewById(R.id.tv_message_num);
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

        //String headPath = PictureUtil.GetHeadPath(sessionItem.getFriendID());
        //		if (headPath != null && headPath.length() > 0) {
        //			Bitmap bitmap = PictureUtil.decodeHeadFromFile(headPath);
        //			// BitmapFactory.decodeFile("/sdcard/org.org.hootina/" + headPath);
        //			holder.head.setImageBitmap(bitmap);
        //		}


        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date msgTime = sessionItem.getTime();
        String msgTimeString = "";
        if (msgTime != null) {
            msgTimeString = TimeUtil.getFormattedTimeString(msgTime);
            holder.tv_time.setText(msgTimeString);
        }

        int friendId = sessionItem.getFriendID();

//        if (sessionItem.getSelfID() > GROUP_ID_BOUNDARY) {
//            friendId = sessionItem.getSelfID();
//        }

        Integer faceIDInteger = HeadUtil.get(friendId);
        if (UserInfo.isGroup(sessionItem.getFriendID())) {
            Glide.with(convertView.getContext())
                    .load("file:///android_asset/head" + 100 + ".png")
                    .into(holder.head);
        } else if (faceIDInteger != null) {
            Glide.with(convertView.getContext())
                    .load("file:///android_asset/head" + faceIDInteger + ".png")
                    .into(holder.head);
        }


        String username = UserSession.getInstance().getUsernameById(friendId);
        String nickName = UserSession.getInstance().getNicknameById(friendId);

        holder.name.setText(nickName + "(" + username + ")");

        if (holder.tv_message_num != null) {
            // ????????????????????????
            int nUnreadCount = mChatMsgMgr.getUnreadChatMsgCountBySenderID(sessionItem.getFriendID());
            if (nUnreadCount == 0) {
                holder.rl_newmsg.setVisibility(View.INVISIBLE);
            } else {
                holder.rl_newmsg.setVisibility(View.VISIBLE);
                String strText = "";
                if (nUnreadCount <= 100 && nUnreadCount > 0)
                    strText = String.valueOf(nUnreadCount);
                else if (nUnreadCount > 100)
                    strText = "100+";

                holder.tv_message_num.setText(strText);
            }
        }

        if (mContext != null) {
//			try
//			{
//				List<UserInfo> list = BaseActivity.getDb().findAll(
//						Selector.from(FriendInfo.class)
//								.where("uAccountID", "=", sessionItem.getmSelfID())
//								.and(WhereBuilder
//										.b("uTargetID", "=", sessionItem.getmFriendID())));
//
//				if(list != null && list.size() > 0)
//				{
//					Bitmap bmp = PictureUtil.getFriendHeadPic(mContext.getAssets(), list.get(0));
//					holder.head.setImageBitmap(bmp);
//				}
//			}
//			catch (DbException e) {
//				e.printStackTrace();
//			}
        }

        holder.text = (TextView) convertView.findViewById(R.id.text);
        // holder.text.set
        //holder.name.setText(sessionItem.getFriendNickName());
        String lastMsg = sessionItem.getLastMsg();
        int faceID;
        String faceFileName = "";

        //lastMsg????????????
        //[{"msgText":"hello"},{"faceID":13},{"msgText":"world"},{"faceID":14}]
        try {
            JsonReader reader = new JsonReader(new StringReader(lastMsg));

            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String innerName = reader.nextName();
                    //???????????????????????????==???????????????equals
                    if (innerName.equals("faceID")) {
                        faceID = reader.nextInt();
                        faceFileName = "face" + faceID + ".png";
                        String html = "<img src='" + faceFileName + "'/>";
                        Html.ImageGetter imgGetter = new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                Bitmap b = getImageFromAssetsFile(source);
                                Drawable d = new BitmapDrawable(b);
                                d.setBounds(0, 0, 40, 40);
                                return d;
                            }
                        };

                        CharSequence charSequence = Html.fromHtml(html, imgGetter, null);
                        holder.text.append(charSequence);
                    } else if (innerName.equals("msgText")) {
                        holder.text.append(reader.nextString());
                    } else if (innerName.equals("pic")) {
                        //TODO??? ????????????????????????
                    } else {
                        reader.skipValue();
                    }
                }// end inner-while-loop

                reader.endObject();

            }// end outer-while-loop
            reader.endArray();
            reader.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            LoggerFile.LogError("parse session last msg error, data=", lastMsg);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LoggerFile.LogError("parse session last msg error, data=", lastMsg);
        } catch (IOException e) {
            e.printStackTrace();
            LoggerFile.LogError("parse session last msg error, data=", lastMsg);
        }

//		if (idd == null)
//			return convertView;
//		String[] ss = idd.split("\\|");
//		String element = "";
//
//		for (int i = 0; i < ss.length; i++) {
//			element = ss[i];
//			if (element.isEmpty())
//				continue;
//
//			if (element.startsWith("[") && element.endsWith("]")) {
//				faceID = element.substring(1, element.length() - 1);
//				faceFileName = "face" + faceID + ".png";
//				String html = "<img src='" + faceFileName + "'/>";
//				ImageGetter imgGetter = new ImageGetter() {
//
//					@Override
//					public Drawable getDrawable(String source) {
//						Bitmap b = getImageFromAssetsFile(source);
//						Drawable d = new BitmapDrawable(b);
//						d.setBounds(0, 0, 40, 40);
//						return d;
//					}
//				};
//				CharSequence charSequence = Html
//						.fromHtml(html, imgGetter, null);
//
//				holder.text.append(charSequence);
//			} else if ((element.startsWith("([") && element.endsWith("])"))
//					|| (element.startsWith("{") && element.endsWith("}"))) {
//				holder.text.append("[??????]");
//			} else {
//				holder.text.append(element);
//			}
//		}

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
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    static class ViewHolder {
        TextView        name;
        TextView        text;
        ImageView       head;
        TextView        tv_message_num;
        RelativeLayout  rl_newmsg;
        TextView        tv_time;
    }
}