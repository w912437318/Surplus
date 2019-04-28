package edu.wayne.service.setting;

import com.alibaba.android.arouter.facade.annotation.Route;

import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.resource.BasePresenter;

/**
 * <h3>Setting activity</h3>
 * <p>Create time:12/1/18 2:22 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Route(path = "/setting/setting")
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
    protected void fetchData() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.setting_activity_main;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.anim_translate_right_enter, R.anim.anim_translate_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.anim_translate_right_enter, R.anim.anim_translate_left_out);
    }
}
