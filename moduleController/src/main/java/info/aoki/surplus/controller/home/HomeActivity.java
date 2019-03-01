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

    // Widgets
    private ImageView mAddImageView, mSettingImageView;
    private TextView mMoneyTextView, mDateTextView;
    private TextInputLayout mMoneyTextInputLayout, mMemoTextInputLayout;
    private AppCompatSpinner mTypeAppCompatSpinner;
    /**
     * <h3>Nav layout</h3>
     * <p>change current display fragment, and it can show user witch fragment is showing</p>
     */
    private TabLayout mTabLayout;
    /**
     * <h3>Content Fragment ViewPager</h3>
     */
    private ViewPager mViewPager;

    /**
     * <h3>Customer Dialog</h3>
     * <p>User input record info in this dialog</p>
     * <code>TextInputLayout</code>: {@link HomeActivity#mMoneyTextInputLayout} - Input cost money
     * <code>TextInputLayout</code>: {@link HomeActivity#mMemoTextInputLayout} - Input record memo
     * <code>AppCompatSpinner</code>: {@link HomeActivity#mTypeAppCompatSpinner} - Select cost type
     */
    private Dialog mDialog;
    private boolean mExistFlag; // Exit flag: double click back key to exit
    private View mDialogView; // Dialog view
    private Animation mScaleAnimation; // Image view click animation
    private ArrayList<Fragment> mFragmentList; // Content fragments
    private TimeChangeReceiver mTimeChangeReceiver; // Time change broadcastReceiver

    @Override
    protected void initData() {
        this.mExistFlag = true;

        // Inflate dialog view.
        this.mDialogView = View.inflate(this, R.layout.controller_home_dialog_add_record, null);

        // Image click animation (scale).
        this.mScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_touch);

        // Prepare fragments
        this.mFragmentList = new ArrayList<>();
        this.mFragmentList.add(new YearlyFragment());
        this.mFragmentList.add(new MonthlyFragment());
        this.mFragmentList.add(new WeeklyFragment());
        this.mFragmentList.add(new DailyFragment());
    }

    @Override
    protected void bindWidget() {
        // Widget in activity
        this.mSettingImageView = findViewById(R.id.activity_home_iv_setting);
        this.mAddImageView = findViewById(R.id.activity_home_iv_add);
        this.mTabLayout = findViewById(R.id.activity_home_tl_nav);
        this.mViewPager = findViewById(R.id.activity_home_vp_content_container);
        this.mMoneyTextView = findViewById(R.id.activity_home_tv_money);
        this.mDateTextView = findViewById(R.id.activity_home_tv_date);

        // Widget in dialog
        this.mTypeAppCompatSpinner = mDialogView.findViewById(R.id.dialog_add_sp_type);
        this.mMemoTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_memo);
        this.mMoneyTextInputLayout = mDialogView.findViewById(R.id.dialog_add_edt_money);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initView() {
        initDialog();
        initTab();
        initFragmentContainer();

        // Set listeners
        this.mSettingImageView.setOnClickListener(v -> {
            // Goto setting activity
            mSettingImageView.startAnimation(mScaleAnimation);
            this.gotoSettingActivity();
        });
        this.mAddImageView.setOnClickListener(v -> {
            // Open add record dialog
            mAddImageView.startAnimation(mScaleAnimation);
            this.openAddRecordDialog();
        });
        this.mDialogView.findViewById(R.id.dialog_add_btn_note).setOnClickListener(v -> {
            // Insert record to databases
            this.addRecord();
        });
        this.mTabLayout.setOnTabSelectedListener(this);
        this.mViewPager.setOnPageChangeListener(this);

        // Edit text input listener
        Objects.requireNonNull(mMoneyTextInputLayout.getEditText()).addTextChangedListener(this);

        // Set data on view
        mPresenter.setCurrentTime();
        mPresenter.setConsumedMoney();

        setBroadcast();
    }

    /**
     * Start receive time changed broadcast
     */
    private void setBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK); // Minute passed
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // System time be modify
        mTimeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(mTimeChangeReceiver, intentFilter);
    }

    /**
     * Crate Customer Dialog
     */
    private void initDialog() {
        this.mDialog = new Dialog(this, 0);
        // Set dialog animation
        Objects.requireNonNull(mDialog.getWindow()).setWindowAnimations(R.style.DialogWindowAnimation);
        // Set dialog window background
        this.mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_add);
        // Ser dialog view
        this.mDialog.setContentView(mDialogView);
    }

    /**
     * Init nav tab
     */
    private void initTab() {
        // Create tab item
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.yearly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.monthly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.weekly));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.daily));
        // Set tab item text color, normal color and active color
        mTabLayout.setTabTextColors(R.color.white, R.color.white);
        // Set tab active color
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
    }

    /**
     * Create
     */
    private void initFragmentContainer() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(viewPagerAdapter);
        Objects.requireNonNull(mTabLayout.getTabAt(3)).select();
        mViewPager.setCurrentItem(3);
    }

    /*
     * Start activity with animation
     */

    private void gotoSettingActivity() {
        ARouter.getInstance().build("/controller/setting").navigation();
        // Set animation
        this.overridePendingTransition(R.anim.anim_translate_left_enter, R.anim.anim_translate_right_out);
    }
    /*
     * Open dialog: add record
     */

    private void openAddRecordDialog() {
        this.mDialog.show();
    }

    /**
     * Call presenter, save user input data
     */
    private void addRecord() {
        if (this.getUserInputMoney().equals("")) {
            // User didn't input money, set error tip
            mMoneyTextInputLayout.setErrorEnabled(true);
            mMoneyTextInputLayout.setError(getResources().getString(R.string.please_input_money));
            return;
        }
        // Save user input record
        mPresenter.addRecord();
        // Clear edit text, dismiss dialog
        this.resetDialog();
        // Update consume money
        mPresenter.setConsumedMoney();
    }

    /**
     * Dismiss dialog, clear edit text
     * <p>If add cost record success, Reset </p>
     */
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
        // Double click to exit
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
        // If user input number is illegal, stop input
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
        // Close error tips when user modified input text
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

    /**
     * <p>Record type</p>
     */
    public enum RecordType {
        RECORD_TYPE_FOOD,
        RECORD_TYPE_TRAFFIC,
        RECORD_TYPE_SHOPPING,
        RECORD_TYPE_OTHER
    }

    /**
     * ViewPager Adapter
     * <p>Content fragments adapter</p>
     */
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

    /**
     * BroadcastReceiver of time changed.
     * <p>When user modify system time and minute passed, it will transfer {@link HomePresenter#setCurrentTime()}</p>
     */
    private class TimeChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                default:
                    break;
                case Intent.ACTION_TIME_CHANGED:
                    // User modify system time setting
                    mPresenter.setCurrentTime();
                    break;
                case Intent.ACTION_TIME_TICK:
                    // Minute passed
                    mPresenter.setCurrentTime();
                    break;
            }
        }
    }
}
