package com.nwuking.ytalk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lidroid.xutils.DbUtils;
import com.nwuking.ytalk.MainActivity;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.ScreenUtils;
import com.nwuking.ytalk.SharedPreferencesUtils;

public class SplashActivity  extends BaseActivity{

    private RelativeLayout rl_splash;
    private ImageView iv_up;

    private final String IS_FIRST_USE_KEY = "isFirstUse";

    private boolean isAnimFinish = false;

    private DbUtils db;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        jump();

       // 渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimFinish = true;

                jump();
            }
        });
        alphaAnimation.setDuration(3000);
        rl_splash.startAnimation(alphaAnimation);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void initData() {
        db = DbUtils.create(this);
        // 初始化屏幕宽高，在后面用
        if (application != null) {
            application.setScreenHeight(ScreenUtils.getScreenHeight(this));
            application.setScreenWidth(ScreenUtils.getScreenWidth(this));
        }
    }

    @Override
    protected void setData() {

        //iv_up.setImageResource(R.drawable.splash_down);

    }

    /**
     * 跳转
     */
    private void jump() {
        if (isAnimFinish) {
            boolean isFirstUse = (Boolean) SharedPreferencesUtils.get(
                    SplashActivity.this, IS_FIRST_USE_KEY, true);
            Intent intent = new Intent();
            // 如果是第一次使用跳转到引导页，如果不是跳转到主界面
            if (isFirstUse) {
                intent.setClass(SplashActivity.this, LoginActivity.class);
            } else {
                if (application.getMemberEntity() == null) {
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                } else {
                    if (application.getMemberEntity().getStrAccountNo2() != null
                            && application.getMemberEntity().getPassword() != null) {
                        String mobilenumber = application.getMemberEntity()
                                .getStrAccountNo2();
                        String password = application.getMemberEntity()
                                .getPassword();
                        //con.login(mobilenumber, password, tms.User.LoginInfo.type.n_AccountNO2);
                    }
                    intent.setClass(SplashActivity.this, MainActivity.class);
                }
            }
            startActivity(intent);
            SplashActivity.this.finish();
            isAnimFinish = false;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
