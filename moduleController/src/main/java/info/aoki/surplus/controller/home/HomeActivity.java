package info.aoki.surplus.controller.home;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.Objects;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BaseActivity;
import info.aoki.surplus.controller.home.fragment.monthly.MonthlyFragment;
import info.aoki.surplus.controller.home.fragment.yearly.YearlyFragment;
import info.aoki.surplus.controller.home.fragment.daily.DailyFragment;
import info.aoki.surplus.controller.home.fragment.weekly.WeeklyFragment;
import info.aoki.surplus.system.ToastUtil;

@Route(path = "/controller/home")
public class HomeActivity
        extends BaseActivity<HomePresenter>
        implements HomeView, TextWatcher, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    private ImageView mAddImageView, mSettingImageView;
    private TextView mMoneyTextView, mDateTextView;
    private TextInputLayout mMoneyTextInputLayout, mMemoTextInputLayout;
    private AppCompatSpinner mTypeAppCompatSpinner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Dialog mDialog;
    private boolean mExistFlag;
    private View mDialogView;
    private Animation mScaleAnimation;
    private ArrayList<Fragment> mFragmentList;
    private TimeChangeReceiver mTimeChangeReceiver;

    @Override
    protected void initData() {
        mExistFlag = true;
        mDialogView = View.inflate(this, R.layout.controller_home_dialog_add_record, null);
        mScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_touch);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new YearlyFragment());
        mFragmentList.add(new MonthlyFragment());
        mFragmentList.add(new WeeklyFragment());
        mFragmentList.add(new DailyFragment());
    }

    @Override
    protected void bindWidget() {
        mSettingImageView = findViewById(R.id.activity_home_iv_setting);
        mAddImageView = findViewById(R.id.activity_home_iv_add);
        mTabLayout = findViewById(R.id.activity_home_tl_nav);
        mViewPager = findViewById(R.id.activity_home_vp_content_container);
        mMoneyTextView = findViewById(R.id.activity_home_tv_money);
        mDateTextView = findViewById(R.id.activity_home_tv_date);

        mTypeAppCompatSpinner = mDialogView.findViewById(R.id.dialog_add_sp_type);
        mMemoTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_memo);
        mMoneyTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_money);
    }

    @Override
    protected void initView() {
        initDialog();
        initTab();
        initFragmentContainer();

        mSettingImageView.setOnClickListener(v -> {
            mSettingImageView.startAnimation(mScaleAnimation);
            this.gotoSettingActivity();
        });
        mAddImageView.setOnClickListener(v -> {
            mAddImageView.startAnimation(mScaleAnimation);
            this.openAddRecordDialog();
        });
        mDialogView.findViewById(R.id.dialog_add_btn_note).setOnClickListener(v -> {
            this.addRecord();
        });
        mTabLayout.setOnTabSelectedListener(this);
        mViewPager.setOnPageChangeListener(this);

        Objects.requireNonNull(mMoneyTextInputLayout.getEditText()).addTextChangedListener(this);

        mPresenter.setCurrentTime();
        mPresenter.setConsumedMoney();

        setBroadcast();
    }

    @Override
    protected void fetchData() {

    }

    private void setBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mTimeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(mTimeChangeReceiver, intentFilter);
    }

    private void initDialog() {
        mDialog = new Dialog(this, 0);
        Objects.requireNonNull(mDialog.getWindow()).setWindowAnimations(R.style.DialogWindowAnimation);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_add);
        mDialog.setContentView(mDialogView);
    }

    /**
     * Init nav tab
     */
    private void initTab() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.yearly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.monthly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.weekly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.daily));

        mTabLayout.setTabTextColors(R.color.white, R.color.white);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
    }

    private void initFragmentContainer() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(viewPagerAdapter);
        Objects.requireNonNull(mTabLayout.getTabAt(3)).select();
        mViewPager.setCurrentItem(3);
    }

    private void gotoSettingActivity() {
        ARouter.getInstance().build("/controller/setting").navigation();
        this.overridePendingTransition(R.anim.anim_translate_left_enter, R.anim.anim_translate_right_out);
    }

    private void openAddRecordDialog() {
        this.mDialog.show();
    }

    private void addRecord() {
        if (this.getUserInputMoney().equals("")) {
            mMoneyTextInputLayout.setErrorEnabled(true);
            mMoneyTextInputLayout.setError(getResources().getString(R.string.please_input_money));
            return;
        }
        mPresenter.addRecord();
        this.resetDialog();
        mPresenter.setConsumedMoney();
    }

    private void resetDialog() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Objects.requireNonNull(mMoneyTextInputLayout.getEditText()).setText("");
        Objects.requireNonNull(mMemoTextInputLayout.getEditText()).setText("");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.controller_home_activity_home;
    }

    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter(this, this);
    }

    @Override
    public void onBackPressed() {
        if (this.mExistFlag) {
            this.mExistFlag = false;
            ToastUtil.showToast(R.string.click_again_to_exit);
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.mExistFlag = true;
            }).start();
        } else {
            super.onBackPressed();
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
        assert this.mMoneyTextInputLayout.getEditText() != null;
        return this.mMoneyTextInputLayout.getEditText().getText().toString();
    }

    @Override
    public String getUserInputMemo() {
        assert this.mMemoTextInputLayout.getEditText() != null;
        return this.mMemoTextInputLayout.getEditText().getText().toString();
    }

    @Override
    public int getUserSelectType() {
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
