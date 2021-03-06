package com.nwuking.ytalk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nwuking.ytalk.ClientType;
import com.nwuking.ytalk.Face;
import com.nwuking.ytalk.FriendList;
import com.nwuking.ytalk.MainActivity;
import com.nwuking.ytalk.MemberEntity;
import com.nwuking.ytalk.MsgType;
import com.nwuking.ytalk.MyApplication;
import com.nwuking.ytalk.NetUtils;
import com.nwuking.ytalk.OnlineType;
import com.nwuking.ytalk.ParserByPULL;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.SharedPreferencesUtils;
import com.nwuking.ytalk.UserInfo;
import com.nwuking.ytalk.UserSession;
import com.nwuking.ytalk.db.MyDbUtil;
import com.nwuking.ytalk.net.NetWorker;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/*
 * @desc 登录界面
 *
 */
public class LoginActivity extends BaseActivity {
    private static String SHARED_PREFERENCE_KEY = "sharedPreferenceKey";
    private static String SHARED_PREFERENCE_USER = "sharedPreferenceUser";
    private static String SHARED_PREFERENCE_PASSWORD = "sharedPreferencePassword";
    private TextView mTvServerSet;
    private EditText mEdtUsername;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private ProgressBar mProgressBar;
    private TextView mTvProgressText;

    private String mUsername;
    private String mPassword;
    private TextView tv_server;
    private LinearLayout ll_moble, ll_iutalk;
    private RelativeLayout rl_server, rl_progressbar;
    private MemberEntity memberEntity;
    private List<UserInfo> userlist;
    private ImageView iv_line_mobile;
    private File file;
    private String picname;
    private String sign;
    private int uBirthday;
    private boolean flag = true;
    private Handler _handler;
    private int _currentGroupNum = 0;
    private boolean mLogining = false;

    private boolean dbInitialized = false;

    @Override
    protected void onCreate(Bundle arg0) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        super.onCreate(arg0);

        getLoginUserInfoFromSharedPreferences();
        mEdtUsername.setText(mUsername);
        mEdtPassword.setText(mPassword);

        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("faceconfig.xml");
            List<Face> list = ParserByPULL.getStudents(input);
            for (Face stu : list) {
                Face face = new Face();
                face.setFile(stu.getFile());
                face.setFaceid(stu.getFaceid());
                face.setTip(stu.getTip());
                //BaseActivity.getDb().save(face);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        //getLoginUserInfoFromSharedPreferences();
        //mEdtUsername.setText(mUsername);
        //mEdtPassword.setText(mPassword);

        super.onRestart();
    }

    @Override
    protected int getContentView() {
       return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        //mTvServerSet = (TextView) findViewById(R.id.tv_server);
        mEdtUsername = (EditText) findViewById(R.id._etUsername);
        mEdtPassword = (EditText) findViewById(R.id._etPassword);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTvProgressText = (TextView) findViewById(R.id.tvProgressText);
        mProgressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void setData() {
        //ll_iutalk.setOnClickListener(this);
        //ll_moble.setOnClickListener(this);
        //rl_server.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mUsername = mEdtUsername.getText().toString().trim();
        mPassword = mEdtPassword.getText().toString().trim();

        switch (v.getId()) {
            /*case R.id.btn_back:
                finish();
                break;

             */

            case R.id.btn_login:
                if (!NetUtils.isConnected(this)) {
                    Toast.makeText(this, R.string.net_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                // 登录
                if (mUsername.length() <= 0) {
                    Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mPassword.length() <= 0) {
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                enableUI(false);

                saveLoginUserInfoToSharedPreferences();

                mProgressBar.setVisibility(View.VISIBLE);
                mLogining = true;
                NetWorker.login(mUsername, mPassword, ClientType.CLIENT_TYPE_ANDROID, OnlineType.online_type_android_cellular);

                if (!dbInitialized) {
                    MyDbUtil.initDatabase(this, mUsername);
                    dbInitialized = true;
                }
                //MyDbUtil.initDatabase(this, mUsername);
                break;

            // 注册
            case R.id.btn_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, BaseActivity.REGISTER_RESULT);
                mProgressBar.setVisibility(View.GONE);
                break;

            case R.id.rl_server:
                startActivity(ChangeServerActivity.class);
                break;

            default:
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BaseActivity.REGISTER_RESULT:
                if (resultCode == BaseActivity.REGISTER_RESULT_OK) {
                    String register_username = data.getStringExtra("register_username");
                    String register_password = data.getStringExtra("register_password");
                    mEdtUsername.setText(register_username);
                    mEdtPassword.setText(register_password);
                }
                break;

            default:
                break;
        }// end switch
    }

    @Override
    public void processMessage(Message msg) {
        if (msg.what == MsgType.MSG_ORDER_LOGIN) {
            if (msg.arg1 == MsgType.ERROR_CODE_SUCCESS) {
                //登录成功，记录用户名和密码
                SharedPreferencesUtils.put(this, "u_name", mUsername);
                SharedPreferencesUtils.put(this, "u_password", mPassword);

                mTvProgressText.setText("登录信息验证成功");
                mProgressBar.setProgress(10);
                //Toast.makeText(this, "登录信息验证成功", Toast.LENGTH_SHORT).show();
                if (!NetWorker.startNetChatThread()) {
                    Toast.makeText(this, "网络断开，快点排查～～", Toast.LENGTH_SHORT).show();
                    return;
                }
                //rl_progressbar.setVisibility(View.VISIBLE);
                //加载好友列表
                NetWorker.getFriendList();
                //UserSession.getInstance().loginUser.setu
            } else if (msg.arg1 == MsgType.ERROR_CODE_UNREGISTER) {
                Toast.makeText(this, "用户名未注册", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mTvProgressText.setText("");
                enableUI(true);
            } else if (msg.arg1 == MsgType.ERROR_CODE_INCORRECTPASSWORD) {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mTvProgressText.setText("");
                enableUI(true);
            } else {
                Toast.makeText(this, "登录失败"+msg.arg2, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mTvProgressText.setText("");
                enableUI(true);
            }


        } else if (msg.what == MsgType.MSG_ORDER_GET_FRIENDS_LIST) {
            //Toast.makeText(this, "加载好友列表信息成功", Toast.LENGTH_SHORT).show();
            //rl_progressbar.setVisibility(View.GONE);

            FriendList.setFriendList((FriendList) msg.obj);
            mProgressBar.setProgress(20);
            mTvProgressText.setText("加载好友列表信息成功");
            if (msg.arg1 == MsgType.ERROR_CODE_SUCCESS) {
                mTvProgressText.setText("");
                mProgressBar.setVisibility(View.GONE);
                mTvProgressText.setText("登陆成功");
                mTvProgressText.setText("");
                enableUI(true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "加载好友列表出错，登录失败", Toast.LENGTH_LONG).show();
            }
        } else if (msg.what == MsgType.msg_type_getgroupmembers) {
            ++_currentGroupNum;
            String progressInfo = String.format("加载第%d个群组信息成功", _currentGroupNum);
            mTvProgressText.setText(progressInfo);
            mProgressBar.incrementProgressBy(8);
            //Toast.makeText(this, progressInfo, Toast.LENGTH_SHORT).show();
            Log.i("LoginActivity", "currentGroupNum=" + _currentGroupNum + ", totalNum:" + UserSession.getInstance().getGroupNum());
            if (UserSession.getInstance().getGroupNum() <= _currentGroupNum) {
                mTvProgressText.setText("群组信息加载完毕");
                mTvProgressText.setText("登陆成功");
                mTvProgressText.setText("");
                //Toast.makeText(this, "群组信息加载完毕", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mTvProgressText.setText("");
                enableUI(true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }// end outer-if

    }

    @Override
    public void onBackPressed() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
            enableUI(true);
            mTvProgressText.setText("");
            //提示用户取消登陆
///            Toast.makeText(this, R.string.login_activity_usercancelinglogining, Toast.LENGTH_LONG).show();
            return;
        }

        //断开网络
        NetWorker.uninit();
        MyDbUtil.uninit();
        dbInitialized = false;
        //结束其他Activity，但保留当前LoginActivity
        ((MyApplication) getApplicationContext()).getAppManager().finishAllActivity(this);
        super.onBackPressed();
    }

    private void enableUI(boolean enabled) {
        //mTvServerSet.setEnabled(enabled);
        mEdtUsername.setEnabled(enabled);
        mEdtPassword.setEnabled(enabled);
        mBtnLogin.setEnabled(enabled);
        mBtnRegister.setEnabled(enabled);
    }

    private void saveLoginUserInfoToSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit();
        editor.putString(SHARED_PREFERENCE_USER, mUsername);
        editor.putString(SHARED_PREFERENCE_PASSWORD, mPassword);
        editor.commit();
    }

    private void getLoginUserInfoFromSharedPreferences() {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE);
        mUsername = pref.getString(SHARED_PREFERENCE_USER, "");
        mPassword = pref.getString(SHARED_PREFERENCE_PASSWORD, "");
    }
}