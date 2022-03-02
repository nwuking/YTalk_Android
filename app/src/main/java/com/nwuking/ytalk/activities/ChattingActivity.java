package com.nwuking.ytalk.activities;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nwuking.ytalk.ChatContent;
import com.nwuking.ytalk.ChatMessage;
import com.nwuking.ytalk.ChatSession;
import com.nwuking.ytalk.ChattingAdapter;
import com.nwuking.ytalk.ClientType;
import com.nwuking.ytalk.ContentText;
import com.nwuking.ytalk.FileInfo;
import com.nwuking.ytalk.FromMessageEntity;
import com.nwuking.ytalk.MainActivity;
import com.nwuking.ytalk.Md5Util;
import com.nwuking.ytalk.MessageTextEntity;
import com.nwuking.ytalk.MsgType;
import com.nwuking.ytalk.MyApplication;
import com.nwuking.ytalk.PictureUtil;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.StringUtil;
import com.nwuking.ytalk.TabbarEnum;
import com.nwuking.ytalk.ToastUtils;
import com.nwuking.ytalk.UserInfo;
import com.nwuking.ytalk.UserSession;
import com.nwuking.ytalk.XListView;
import com.nwuking.ytalk.net.ChatMsgMgr;
import com.nwuking.ytalk.net.ChatSessionMgr;
import com.nwuking.ytalk.net.NetWorker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/*
 * @聊天页面（单聊或群聊页面）
 */
public class ChattingActivity extends BaseActivity implements XListView.IXListViewListener {
    public static final String FILE_BASE_DIR = "/sdcard/ytalk/";
    private static final String LOG_TAG = "ChattingActivityTag";
    private static final String PHOTO_FILE_NAME = System.currentTimeMillis() + "jpg";

    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private ImageView iv_sendPicture;
    private Button btn_more;
    private Button btn_send;
    private Button send_button;
    private EditText mSendTextEditor;
    private XListView chatting_lv;
    private TextView tvWindowTitle;
    private RelativeLayout edittext_layout;
    private LinearLayout view_camera;
    private LinearLayout view_photo;

    private String type;
    private String editortalkmsg;
    private String talkmsg;
    private String text;
    private String msgtexts;
    private static String picname;

    private int mFriendID;
    private int mSenderID;
    private int mSelfID;
    private String mSelfName;
    private String mFriendNickName;
    private int uMsgID;
    private int faceid;
    private int uOffset;

    private ChattingAdapter chatingAdapter;
    private FromMessageEntity fromMessageEntity;
    private MessageTextEntity messageTextEntity;
    private ContentText contentText;


    //private ChatSession 		chatSession;
    private List<ChatSession> infos;

    private List<ChatMessage> mPendingChatMessages = new ArrayList<ChatMessage>();
    private List<ChatMessage> mPendingMsgList = new ArrayList<ChatMessage>();
    private List<MessageTextEntity> mCurrentMessage = new ArrayList<>();

    private Timer mUpdateRecvMsgStateTimer;    //更新收到的聊天消息的状态的定时器
    private static final int UPDATE_RECV_MSG_STATE_TIMER_ID = 0xFF;

    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    private byte[] strName;
    private byte[] contentIntleng;

    private List<FileInfo> m_downloadingFiles;

    public int getFriendID() {
        return mFriendID;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.getText();
/*
        switch (v.getId()) {
            case R.id.btn_back:
                navigateToMainActivity();
                break;

            case R.id.btn_send:
                //UIUtils.hideSoftInput(this, mSendTextEditor);
                sendChatMsg();
                mSendTextEditor.setText("");
                //让滚动条滚动到底部
                chatting_lv.setSelection(chatting_lv.getBottom());
                break;

            case R.id.text_editor:
                break;

            case R.id.view_photo:
                // 相册获取
                getImageFromGallery();
                break;

            case R.id.view_camera:
                // 照相
                getImageFromCamera();
                break;

            case R.id.iv_emoticons_normal:
                // 隐藏软键盘
                InputMethodManager imms = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imms.hideSoftInputFromWindow(mSendTextEditor.getWindowToken(), 0);
                break;

            default:
                break;
        }

 */
    }

    @Override
    protected int getContentView() {
//        return R.layout.activity_chatting;
        return 0;
    }

    @Override
    protected void initData() {
/*       tvWindowTitle = (TextView) findViewById(R.id.tv_window_title);

        int selfID = UserSession.getInstance().loginUser.get_userid();

        mSenderID = getIntent().getIntExtra("selfid", 0);
        mFriendID = getIntent().getIntExtra("friendid", 0);
        mSelfID = UserSession.getInstance().loginUser.get_userid();
        mSelfName = UserSession.getInstance().loginUser.get_nickname();
        //主动发起群聊
        if (mSenderID == 0 && UserInfo.isGroup(mFriendID)) {
            //mSenderID = mSelfID;
        }

        //主动发起单聊
        if (mSenderID == 0 && !UserInfo.isGroup(mFriendID)) {
            //mSenderID = mSelfID;
        }

        //被动发起群聊
        if (mSenderID != selfID && UserInfo.isGroup(mFriendID)) {
            //后续 mSenderID mFriendID 用于发送消息
        }

        //被动发起单聊
        if (mSenderID != selfID && !UserInfo.isGroup(mFriendID)) {
            //后续 mSenderID mFriendID 用于发送消息
        }

        mFriendNickName = UserSession.getInstance().getNicknameById(mFriendID);

        if (UserInfo.isGroup(mFriendID))
            tvWindowTitle.setText("群聊 - " + mFriendNickName);
        else
            tvWindowTitle.setText("与" + mFriendNickName + "聊天中");
        // 点击事件
       mSendTextEditor = (EditText) findViewById(R.id.text_editor);
       mSendTextEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                   edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }
            }
        });

        mSendTextEditor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            }
        });

        // 检测内容事件
        mSendTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    //
                    // startActivity(LoginActivity.class);
                    String ss = s.toString();
                    if (ss.startsWith("([") && ss.endsWith("])")) {
                        String pics = ss.substring(2, ss.length() - 2);
                        String pic = pics.replace("\"", "");
                        String[] ssa = pic.split("\\,");
                        String name = ssa[0];
                        String uFilesize = ssa[2];
                        // makeTextShort(a);
                        mSendTextEditor.setText("");
                        String strPath = MyApplication.DEFAULT_APP_PATH + "/" + name;
                        Bitmap bitmap = PictureUtil.decodePicFromFullPath(strPath);
                        final File file = saveMyBitmap(bitmap, name);
                        String strChecksum = Md5Util.getMD5(file);
                        // String newFileName = uFilesize + strChecksum +
                        // ".jpg";
                        String newFileName = name;
                        //contwo.sendFileUpInfo(newFileName, strChecksum, 0,
                        //		Long.parseLong(uFilesize));
                        sendPicture(newFileName, strChecksum);
                    }
                    btn_more.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_more.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mUpdateRecvMsgStateTimer = new Timer();
        mUpdateRecvMsgStateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 定义一个消息传过去
                Message msg = new Message();
                msg.what = ChattingActivity.UPDATE_RECV_MSG_STATE_TIMER_ID;
                sendMessage(msg);
            }

        }, (long) 1000, (long) 5000);

        // 获取群数据
        int nGroupID = mFriendID;
        if (UserInfo.isGroup(nGroupID)) {
        }

        //加载历史消息
        if (mCurrentMessage.isEmpty()) {
            refreshList();
        }

 */
    }




    private void refreshList() {
        if (UserInfo.isGroup(mFriendID)) {
            mCurrentMessage = BaseActivity.getChatMsgDb().getChatMsgByTargetID(mFriendID);
        } else {
            //TODO: 数据条数多的时候，会很慢。影响界面响应速度，需要优化！
            mCurrentMessage = BaseActivity.getChatMsgDb().getChatMsgBySenderAndTargetID(mSelfID, mFriendID);
        }

        if (chatingAdapter != null) {
            chatingAdapter.setChatMessages(mCurrentMessage);
            chatingAdapter.notifyDataSetChanged();
        } else {
            chatingAdapter = new ChattingAdapter(this, mCurrentMessage);
            chatting_lv.setAdapter(chatingAdapter);
        }
    }

    @Override
    protected void setData() {
//        iv_sendPicture = (ImageView) findViewById(R.id.iv_sendPicture);
        chatting_lv.setXListViewListener(this);
        chatting_lv.setPullRefreshEnable(true);
        chatting_lv.setPullLoadEnable(false);
        chatting_lv.setAutoLoadEnable(false);

        RelativeLayout.LayoutParams statusTextViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        chatting_lv.setLayoutParams(statusTextViewParams);
        iv_emoticons_checked.setOnClickListener(this);
/*        view_photo = (LinearLayout) findViewById(R.id.view_photo);
        view_photo.setOnClickListener(this);
        view_camera = (LinearLayout) findViewById(R.id.view_camera);
        view_camera.setOnClickListener(this);

 */
        // 转发
        type = getIntent().getStringExtra("type");
        msgtexts = getIntent().getStringExtra("msgtexts");

    }

    @Override
    protected void onResume() {
        super.onResume();

        NetWorker.clearSenderCount();
        NetWorker.clearNotificationCount();

        refreshMessageList();
        updateUnreadChatMsgCount();
    }

    public void sendChatMsg() {

        String rawChatMsg1 = mSendTextEditor.getText().toString().trim();
        if (rawChatMsg1.isEmpty()) {
            ToastUtils.showLongToast(this, "不能发送空消息");
            return;
        }

        MessageTextEntity entity = MessageTextEntity.rawStringToEntity(rawChatMsg1);
        entity.setClientType(ClientType.CLIENT_TYPE_ANDROID);

        entity.setSenderID(mSelfID);
        entity.setTargetID(mFriendID);

        String msgID = MessageTextEntity.generateMsgID();
        entity.setMsgID(msgID);
        entity.setMsgType(MessageTextEntity.CONTENT_TYPE_TEXT);
        long msgTime = System.currentTimeMillis() / 1000;
        entity.setMsgTime(msgTime);
        mCurrentMessage.add(entity);

        ChatContent chatContent = new ChatContent();
        chatContent.setClientType(ClientType.CLIENT_TYPE_ANDROID);

        chatContent.setSenderId(mSelfID);
        chatContent.setTargetId(mFriendID);

        chatContent.setMsgType(MessageTextEntity.CONTENT_TYPE_TEXT);
        chatContent.setTime((System.currentTimeMillis() / 1000));

        //"|[32]|"

        List<ChatContent.ContentBean> list = new ArrayList<>();
        while (rawChatMsg1.contains("|[") || rawChatMsg1.contains("]|")) {
            int start = rawChatMsg1.indexOf("[");
            int end = rawChatMsg1.indexOf("]");

            String num = rawChatMsg1.substring(start + 1, end);

            int id = -1;
            try {
                id = Integer.parseInt(num);
            } catch (NumberFormatException e) {

            }

            if (id == -1)
                return;

            ChatContent.ContentBean contentBean = new ChatContent.ContentBean();
            contentBean.setFaceID(id);
            list.add(contentBean);

            rawChatMsg1 = rawChatMsg1.replaceFirst("\\|\\[", "");

            rawChatMsg1 = rawChatMsg1.replaceFirst(num, "");
            rawChatMsg1 = rawChatMsg1.replaceFirst("\\]\\|", "");
        }

        ChatContent.ContentBean bean = new ChatContent.ContentBean();
        bean.setMsgText(rawChatMsg1);
        list.add(bean);

        chatContent.setContent(list);

        //chatMsg格式：
        //{"clientType":2,"content":[{"faceID":7},{"faceID":7},{"faceID":-1,"msgText":"gg"}],
        // "msgID":"9c07a6a5-93fa-4b83-bcf3-6059a1d354b2","msgTime":1522746339,"msgType":1,"senderID":1,"targetID":2}
        //String chatMsg = JSON.toJSONString(entity);
        String chatMsg = chatContent.toJson();

        ChatMsgMgr.getInstance().addMessageTextEntity(mFriendID, entity);

        //chatContentJson格式：
        //[{"faceID":7},{"faceID":7},{"faceID":-1,"msgText":"gg"}]
        String chatContentJson = entity.contentToJson();
        //数据库中存储的是原始的消息文字
        BaseActivity.getChatMsgDb().insertChatMsg(msgID, msgTime, mSelfID, mFriendID,
                MessageTextEntity.CONTENT_TYPE_TEXT, chatContentJson,
                ClientType.CLIENT_TYPE_ANDROID, 0,
                "", "");


        refreshList();

        //在这里将聊天消息发出去
        NetWorker.sendChatMsg(mFriendID, chatMsg);

        ChatSessionMgr.getInstance().updateSession(mFriendID, mSelfName, chatContentJson, "", new Date());

    }

    public void loadfile(ChatMessage msg) {
        String strText = msg.getmMsgText();
        String strTemp = strText.substring(2, strText.length() - 2);
        String[] strArray = strTemp.split("\\,");
        String strLocalName = strArray[0];
        strLocalName = strLocalName.replaceAll("\"", "");
        String strPics = strArray[1];
        String strPic = strPics.replaceAll("\"", "");
        String path = FILE_BASE_DIR + strPics;
        File file = new File(path);
        if (!file.exists()) {
            PictureUtil.loadfile(strLocalName, strPic);
        }
    }

    public int getTargetID() {
        return mFriendID;
    }

    private int updateUnreadRecvMsgToReadState() {

        return 0;
    }

    public void setMessageFailed() {

    }

    private void refreshMessageList() {
        if (mCurrentMessage == null || mCurrentMessage.isEmpty())
            return;

        chatingAdapter = new ChattingAdapter(this, mCurrentMessage);
        chatting_lv.setAdapter(chatingAdapter);
        chatting_lv.setSelection(chatingAdapter.getCount() - 1);

    }

    private void updateUnreadChatMsgCount() {
        ChatMsgMgr.getInstance().clearUnreadChatMsgCountBySenderID(mFriendID);
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);

        if (msg.what == ChattingActivity.UPDATE_RECV_MSG_STATE_TIMER_ID) {
            int updateMsgCount = updateUnreadRecvMsgToReadState();
            //if (updateMsgCount > 0)
            //refreshMessageList();

        } else if (msg.what == MsgType.msg_type_chat) {
            //更新session
            //MessageTextEntity messageTextEntity = JSONObject.parseObject((String)msg.obj, MessageTextEntity.class);
            int senderID = msg.arg1;
            int targetID = msg.arg2;

            //TODO： 消息越多越慢，改从内存中取消息
            refreshList();
        }
    }

    /*
     * 通过相机获取图片
     */
    public void getImageFromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //判断是否有SD卡
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            PHOTO_FILE_NAME)));
        }

        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /*
     * 从相册获取
     */
    public void getImageFromGallery() {
        // 判断存储卡是否可以用，可用进行存储
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                parseGalleryImage(data);
            }
        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            parseCameraImage();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void parseGalleryImage(Intent data) {
        // 得到图片的全路径
        Uri uri = data.getData();
        String filePath = getRealFilePath(getApplicationContext(), uri);
        Log.d(LOG_TAG, filePath);
        String md5 = Md5Util.getMD5(new File(filePath));
        if (StringUtil.isEmpty(md5)) {
//            Toast.makeText(this, R.string.parse_img_error, Toast.LENGTH_SHORT).show();
            return;
        }

        MessageTextEntity entity = new MessageTextEntity();
        entity.setClientType(ClientType.CLIENT_TYPE_ANDROID);

        entity.setSenderID(mSelfID);
        entity.setTargetID(mFriendID);

        String msgID = MessageTextEntity.generateMsgID();
        entity.setMsgID(msgID);
        entity.setMsgType(MessageTextEntity.CONTENT_TYPE_TEXT);
        long msgTime = System.currentTimeMillis() / 1000;
        entity.setMsgTime(msgTime);
        entity.setImgFile(filePath);
        entity.setMsgType(MessageTextEntity.CONTENT_TYPE_MOBILE_IMAGE);
        mCurrentMessage.add(entity);

        chatingAdapter.notifyDataSetChanged();

        BaseActivity.getChatMsgDb().insertChatMsg(msgID,
                msgTime,
                mSelfID,
                mFriendID,
                MessageTextEntity.CONTENT_TYPE_MOBILE_IMAGE,
                "[图片]",
                ClientType.CLIENT_TYPE_ANDROID,
                0,
                "",
                filePath);

        sendPicture(filePath, md5);
        uri.toString();

        String strPath = null;
        if (data.getScheme().equals("content")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor == null)
                return;

            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                strPath = cursor.getString(column_index);
            }

            cursor.close();
        } else {
            strPath = uri.getPath();
        }

        long uFilesize = 0;
        File ff = new File(strPath);
        if (!ff.exists()) {
            return;
        }

        uFilesize = ff.length();

        Bitmap bitmap = PictureUtil.decodePicFromFullPath(strPath);
        if (bitmap == null) {
            Log.i(LOG_TAG, "open image failed: " + strPath);
            return;
        } else {
            String name = mSenderID + System.currentTimeMillis() + ".jpg";
            final File file = saveMyBitmap(bitmap, name);

            // 压缩没有效果
            if (ff.length() <= file.length()) {
                file.delete();
                try {
                    FileInputStream fosfrom = new FileInputStream(ff);
                    FileOutputStream fosto = new FileOutputStream(file);
                    try {
                        byte[] bt = new byte[1024 * 100];
                        int c;
                        while ((c = fosfrom.read(bt)) > 0) {
                            fosto.write(bt, 0, c);
                        }

                        fosfrom.close();
                        fosto.close();
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "readfile: " + ex.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String strChecksum = Md5Util.getMD5(file);
            uFilesize = file.length();

            String newFileName = uFilesize + strChecksum + ".jpg";
            File newFile = new File(FILE_BASE_DIR + newFileName);
            if (newFile.exists() && !newFile.getName().equals(file.getName())) {
                newFile.delete();
            }
            file.renameTo(newFile);

            //contwo.sendFileUpInfo(newFileName, strChecksum, 0, uFilesize);
            //sendPicture(newFileName, strChecksum);

            Date sessionItemTime = new Date();
            //[{"msgText":"hello"},{"faceID":13},{"msgText":"world"},{"faceID":14}]
            String chatContentJson = "[{\"msgText\":\"[图片]\"}]";
            ChatSessionMgr.getInstance().updateSession(mFriendID, mSelfName, chatContentJson, "", sessionItemTime);
        }
    }

    private void parseCameraImage() {
        if (!hasSdcard()) {
            Toast.makeText(ChattingActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
            return;
        }

        File tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
        String strPath = tempFile.getPath();

        PictureUtil.deleteFromMemCache(strPath);
        Bitmap bitmap = PictureUtil.decodePicFromFullPath(strPath);
        if (bitmap == null) {
            return;
        }
        String name = mSenderID + System.currentTimeMillis() + ".jpg";
        final File file = saveMyBitmap(bitmap, name);

        String strChecksum = Md5Util.getMD5(file);
        if (StringUtil.isEmpty(strChecksum)) {
///            Toast.makeText(this, R.string.parse_img_error, Toast.LENGTH_SHORT).show();
            return;
        }

        long uFilesize = file.length();

        String newFileName = uFilesize + strChecksum + ".jpg";
        File newFile = new File(FILE_BASE_DIR + newFileName);
        if (newFile.exists()) {
            newFile.delete();
        }
        file.renameTo(newFile);

        MessageTextEntity entity = new MessageTextEntity();
        entity.setClientType(ClientType.CLIENT_TYPE_ANDROID);

        entity.setSenderID(mSelfID);
        entity.setTargetID(mFriendID);

        String msgID = MessageTextEntity.generateMsgID();
        entity.setMsgID(msgID);
        entity.setMsgType(MessageTextEntity.CONTENT_TYPE_TEXT);
        long msgTime = System.currentTimeMillis() / 1000;
        entity.setMsgTime(msgTime);
        entity.setImgFile(FILE_BASE_DIR + newFileName);
        entity.setMsgType(MessageTextEntity.CONTENT_TYPE_MOBILE_IMAGE);
        mCurrentMessage.add(entity);

        chatingAdapter.notifyDataSetChanged();

        Date sessionItemTime = new Date();
        String chatContentJson = "[{\"msgText\":\"[图片]\"}]}";

        BaseActivity.getChatMsgDb().insertChatMsg(msgID,
                msgTime,
                mSelfID,
                mFriendID,
                MessageTextEntity.CONTENT_TYPE_MOBILE_IMAGE,
                "[图片]",
                ClientType.CLIENT_TYPE_ANDROID,
                0,
                "",
                FILE_BASE_DIR + newFileName);

        sendPicture(strPath, strChecksum);

        ChatSessionMgr.getInstance().updateSession(mFriendID, mSelfName, chatContentJson, "", sessionItemTime);
    }

    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public File saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(FILE_BASE_DIR + bitName);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        int options = 100;
        while (out.toByteArray().length / 1024 > 200 && options >= 50) {
            options -= 10;
            out.reset();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
        }

        try {
            fOut.write(out.toByteArray());
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    // 相册或相机发送图片
    public void sendPicture(String newFileName, String strChecksum) {
        NetWorker.uploadFile(newFileName, strChecksum, mFriendID);
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        navigateToMainActivity();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        application.setTabIndex(TabbarEnum.MESSAGE);
        startActivity(intent);
        finish();
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
