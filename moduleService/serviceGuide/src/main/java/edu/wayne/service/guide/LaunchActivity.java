package edu.wayne.service.guide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.system.PermissionUtil;

@Route(path = "/guide/launch")
public class LaunchActivity
        extends BaseActivity<LaunchPresenter>
        implements LaunchView {

    private final String TAG = LaunchActivity.class.getName();

    @Override
    protected int getLayoutId() {
        return R.layout.launch_activity_main;
    }

    @Override
    protected LaunchPresenter initPresenter() {
        return new LaunchPresenter(this, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindWidget() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void fetchData() {
        initPermission();
    }

    private void initPermission() {
        if (!PermissionUtil.checkSelfPermission(Manifest.permission.READ_PHONE_STATE, this)) {
            if (PermissionUtil.shouldRequestPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                PermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE);
            } else {
                gotoHomeActivity();
            }
        } else {
            gotoHomeActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CODE_REQUEST_PERMISSION) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.w(TAG, "READ_PHONE_STATE permission has been refuse!");
            }
        }
        gotoHomeActivity();
    }

    private void gotoHomeActivity() {
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                ARouter.getInstance().build("/home/home").navigation();
                LaunchActivity.this.overridePendingTransition(R.anim.anim_alpha_enter, R.anim.anim_alpha_exit);
                LaunchActivity.this.finish();
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            default:
                break;
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
