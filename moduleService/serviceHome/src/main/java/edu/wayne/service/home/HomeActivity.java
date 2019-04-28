package edu.wayne.service.home;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.Objects;

import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.system.ThreadScheduler;

@Route(path = "/home/home",group = "home")
public class HomeActivity
        extends BaseActivity<HomePresenter>
        implements HomeView, TextWatcher, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, View.OnClickListener {

    private TextView mMoneyTextView, mDateTextView;
    private TextInputLayout mMoneyTextInputLayout, mMemoTextInputLayout;
    private AppCompatSpinner mTypeAppCompatSpinner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Dialog mInputDialog;
    private boolean mExistFlag;
    private View mDialogView;
    private ArrayList<Fragment> mFragmentList;
    private TimeChangeReceiver mTimeChangeReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_main;
    }

    @Override
    protected void initData() {
        mExistFlag = true;

        mFragmentList = new ArrayList<>();
        mFragmentList.add((Fragment) ARouter.getInstance().build("/yearly/yearly").navigation());
        mFragmentList.add((Fragment) ARouter.getInstance().build("/monthly/monthly").navigation());
        mFragmentList.add((Fragment) ARouter.getInstance().build("/weekly/weekly").navigation());
        mFragmentList.add((Fragment) ARouter.getInstance().build("/daily/daily").navigation());
    }

    @Override
    protected void bindWidget() {
        mTabLayout = findViewById(R.id.activity_home_tl_nav);
        mViewPager = findViewById(R.id.activity_home_vp_content_container);
        mMoneyTextView = findViewById(R.id.activity_home_tv_money);
        mDateTextView = findViewById(R.id.activity_home_tv_date);


        mDialogView = View.inflate(this, R.layout.home_dialog_add_record, null);
        mTypeAppCompatSpinner = mDialogView.findViewById(R.id.dialog_add_sp_type);
        mMemoTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_memo);
        mMoneyTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_money);
    }

    @Override
    protected void initView() {
        initNavTab();
        initViewPager();
        initDialog();

        findViewById(R.id.activity_home_iv_setting).setOnClickListener(this);
        findViewById(R.id.activity_home_iv_add).setOnClickListener(this);
        mDialogView.findViewById(R.id.dialog_add_btn_note).setOnClickListener(this);
        mTabLayout.setOnTabSelectedListener(this);
        mViewPager.setOnPageChangeListener(this);
        Objects.requireNonNull(mMoneyTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter(this, this);
    }

    private void initNavTab() {
        // 设置内容
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.yearly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.monthly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.weekly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.daily));
        // 设置颜色
        mTabLayout.setTabTextColors(R.color.white, R.color.white);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
    }

    private void initDialog() {
        mInputDialog = new Dialog(this, 0);
        Objects.requireNonNull(mInputDialog.getWindow()).setWindowAnimations(R.style.DialogWindowAnimation);
        mInputDialog.getWindow().setBackgroundDrawableResource(R.drawable.home_dialog_add_bg);
        mInputDialog.setContentView(mDialogView);
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
        // 选中最后一个菜单项，即每日账单页面
        Objects.requireNonNull(mTabLayout.getTabAt(mFragmentList.size() - 1)).select();
        mViewPager.setCurrentItem(mFragmentList.size());
    }

    @Override
    protected void fetchData() {
        mPresenter.setCurrentTime();
        mPresenter.setConsumedMoney();
        setBroadcast();
    }

    private void setBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mTimeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(mTimeChangeReceiver, intentFilter);
    }

    private void gotoSettingActivity() {
        ARouter.getInstance().build("/setting/setting").navigation();
        this.overridePendingTransition(R.anim.anim_translate_left_enter, R.anim.anim_translate_right_out);
    }

    private void openAddRecordDialog() {
        this.mInputDialog.show();
    }

    private void addConsumedRecord() {
        mPresenter.addConsumedRecord();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_home_iv_setting) {
            this.gotoSettingActivity();
        } else if (view.getId() == R.id.activity_home_iv_add) {
            this.openAddRecordDialog();
        } else if (view.getId() == R.id.dialog_add_btn_note) {
            this.addConsumedRecord();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().contains(".")) {
            if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > 2) {
                charSequence = charSequence.toString().subSequence(0,
                        charSequence.toString().indexOf(".") + 3);
                Objects.requireNonNull(this.mMoneyTextInputLayout.getEditText()).setText(charSequence);
                this.mMoneyTextInputLayout.getEditText().setSelection(charSequence.length());
            }
        }

        if (charSequence.toString().trim().equals(".")) {
            charSequence = "0" + charSequence;
            Objects.requireNonNull(this.mMoneyTextInputLayout.getEditText()).setText(charSequence);
            this.mMoneyTextInputLayout.getEditText().setSelection(2);
        }

        if (charSequence.toString().startsWith("0")
                && charSequence.toString().trim().length() > 1) {
            if (!charSequence.toString().substring(1, 2).equals(".")) {
                Objects.requireNonNull(this.mMoneyTextInputLayout.getEditText()).setText(charSequence.subSequence(0, 1));
                this.mMoneyTextInputLayout.getEditText().setSelection(1);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mMoneyTextInputLayout.setErrorEnabled(false);
    }

    @Override
    public String getUserInputMoney() {
        return Objects.requireNonNull(this.mMoneyTextInputLayout.getEditText()).getText().toString();
    }

    @Override
    public String getUserInputMemo() {
        return Objects.requireNonNull(this.mMemoTextInputLayout.getEditText()).getText().toString();
    }

    @Override
    public int getSelectedConsumedTypeId() {
        return this.mTypeAppCompatSpinner.getSelectedItemPosition();
    }

    @Override
    public void setConsumedMoney(String consumedMoney) {
        this.mMoneyTextView.setText(consumedMoney);
    }

    @Override
    public void setCurrentDate(String currentDate) {
        this.mDateTextView.setText(currentDate);
    }

    @Override
    public void setInputMoneyErrorHint(String errorHint) {
        mMoneyTextInputLayout.setErrorEnabled(true);
        mMoneyTextInputLayout.setError(errorHint);
    }

    @Override
    public void resetInputInfo() {
        if (mInputDialog.isShowing()) {
            mInputDialog.dismiss();
        }
        Objects.requireNonNull(mMoneyTextInputLayout.getEditText()).setText("");
        Objects.requireNonNull(mMemoTextInputLayout.getEditText()).setText("");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Objects.requireNonNull(mTabLayout.getTabAt(position)).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        if (mExistFlag) {
            mExistFlag = false;
            showHint(R.string.click_again_to_exit);
            ThreadScheduler.getExecutor().execute(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mExistFlag = true;
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeChangeReceiver);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    private class TimeChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                default:
                    break;
                case Intent.ACTION_TIME_CHANGED:
                    mPresenter.setCurrentTime();
                    break;
                case Intent.ACTION_TIME_TICK:
                    mPresenter.setCurrentTime();
                    break;
            }
        }
    }
}
