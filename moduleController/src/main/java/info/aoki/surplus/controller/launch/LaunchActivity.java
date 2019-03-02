package info.aoki.surplus.controller.launch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.system.PermissionUtil;

@Route(path = "/controller/launch")
public class LaunchActivity extends BaseActivity<LaunchPresenter> implements LaunchView {
    private final String TAG = LaunchActivity.class.getName();

    @Override
    protected int getLayoutId() {
        return R.layout.controller_launch_activity_launch;
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initPermission();
    }

    @Override
    protected void fetchData() {

    }

    private void initPermission() {
        if (!PermissionUtil.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CODE_REQUEST_PERMISSION) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.w(TAG, "READ_PHONE_STATE permission has been refuse!");
            }
        }
        gotoHomeActivity();
    }

    private void gotoHomeActivity() {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                ARouter.getInstance().build("/controller/home").navigation();
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
