package info.aoki.surplus.controller.setting;

import com.alibaba.android.arouter.facade.annotation.Route;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.resource.BasePresenter;

/**
 * <h3>Setting activity</h3>
 * <p>Create time:12/1/18 2:22 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Route(path = "/controller/setting")
public final class SettingActivity extends BaseActivity {
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
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.controller_setting_activity_setting;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.anim_translate_right_enter, R.anim.anim_translate_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        // arg1: Home activity enter animation; arg2: Setting activity exit animation
        this.overridePendingTransition(R.anim.anim_translate_right_enter, R.anim.anim_translate_left_out);
    }
}
