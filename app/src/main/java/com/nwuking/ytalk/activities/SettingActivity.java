package com.nwuking.ytalk.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nwuking.ytalk.AppData;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.SharedPreferencesUtils;
import com.nwuking.ytalk.TabbarEnum;
import com.nwuking.ytalk.db.MyDbUtil;
import com.nwuking.ytalk.net.ChatSessionMgr;
import com.nwuking.ytalk.net.NetWorker;

public class SettingActivity extends BaseActivity {
    /*
     * 设置
     *
     * @s
     */
    private TextView txt_modifypass;
    private TextView txt_aboutus;
    private Button btn_endsession;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.txt_modifypass:
                startActivity(ChangePasswordActivity.class);
                break;

            case R.id.txt_aboutus:
                startActivity(AboutUsActivity.class);
                break;

            case R.id.btn_endsession:
                NetWorker.disconnectChatServer();
                application.setTabIndex(TabbarEnum.MY);
                startActivity(LoginActivity.class);
                BaseActivity.bLogin = false;
                // 设置第一次使用flag为false
                SharedPreferencesUtils.put(this, "isFirstUse", true);
                //application.getAppManager().finishAllActivity();
                //application.setMemberEntity(null);
                AppData.clears();
                ChatSessionMgr.getInstance().clear();
                MyDbUtil.uninit();
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setData() {
        txt_modifypass.setOnClickListener(this);
        txt_aboutus.setOnClickListener(this);
        btn_endsession.setOnClickListener(this);
    }

}
