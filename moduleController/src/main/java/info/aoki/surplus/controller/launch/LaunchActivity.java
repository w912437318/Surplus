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
        // Set window full screen : no status bar.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Request READ_PHONE_STATE permission
        initPermission();
    }

    private void initPermission() {
        if (!PermissionUtil.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            // Don't has READ_PHONE_STATE permission, Request now!
            if (PermissionUtil.shouldRequestPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                // User did't refuse permission request, request now!
                PermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE);
            } else {
                // User has already refuse permission request! can't request again!
                gotoHomeActivity();
            }
        } else {
            // Has READ_PHONE_STATE permission, start activity now.
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
        // Request READ_PHONE_STATE permission complete, goto home activity now
        gotoHomeActivity();
    }

    /**
     * Goto HomeActivity with animation
     */
    private void gotoHomeActivity() {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                ARouter.getInstance().build("/controller/home").navigation();
                LaunchActivity.this.overridePendingTransition(R.anim.anim_alpha_enter, R.anim.anim_alpha_exit);
                // Finish this activity
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
                // Shield back key
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
