package info.aoki.surplus.controller.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;

import java.text.SimpleDateFormat;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BasePresenter;
import info.aoki.surplus.controller.home.fragment.daily.DailyService;
import info.aoki.surplus.system.LogUtil;
import info.aoki.surplus.system.model.home.HomeModel;
import info.aoki.surplus.system.model.home.IHomeModel;
import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observer;

public class HomePresenter extends BasePresenter<HomeView, IHomeModel> {

    private final String TAG = HomePresenter.class.getName();
    /**
     * Cost type
     * <p>Define in <code>arrays.xml</code></p>
     */
    private String[] mRecordTypes;
    private SimpleDateFormat mSimpleDateFormat;

    private long mLastQueryTodayConsumedTime;
    private DailyService mDailyService;

    @SuppressLint("SimpleDateFormat")
    HomePresenter(Context context, HomeView view) {
        super(context, view);
        this.mRecordTypes = mContext.getResources().getStringArray(R.array.dialog_add_record_spinner_items);
        this.mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Init service
        mDailyService = (DailyService) ARouter.getInstance().build("/controller/daily").navigation();
    }

    /**
     * <h3>Insert cost record into database</h3>
     * <p>Check user input info is </p>
     */
    public void addRecord() {
        RecordBean recordBean = new RecordBean(processInputMoney(), mRecordTypes[mView.getUserSelectType()]);
        // If user input the memo, add it into record bean
        if (!mView.getUserInputMemo().equals("")) recordBean.setR_memo(mView.getUserInputMemo());
        // Start insert record.
        mModel.insertRecord(recordBean, new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                LogUtil.d(TAG, "Insert cost record complete.");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Insert record failed.");
                mView.showToast(R.string.save_failed);
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if (isSuccess) {
                    mView.showToast(R.string.save_success);
                }
            }
        });
    }

    /**
     * <p>Show current time by {@link HomeActivity#mDateTextView}</p>
     * <p>format: yyyy-MM-dd</p>
     * <strong>Upgrade time when time changed.</strong>
     */
    public void setCurrentTime() {
        mView.setCurrentDate(mSimpleDateFormat.format(System.currentTimeMillis()));
    }

    /**
     * <p>Query user cost money in this month</p>
     * <p>And show it by {@link HomeActivity#mMoneyTextView}</p>
     */
    public void setConsumedMoney() {
        mModel.getCurrentMonthConsumed(new Observer<Float>() {
            @Override
            public void onCompleted() {
                LogUtil.d(TAG, "Query monthly cost complete.");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "Query monthly cost failed! cause:" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Float aFloat) {
                mView.setConsumedMoney("¥\t" + aFloat);
            }
        });
    }

    /**
     * User input money process.
     *
     * @return Float: money
     */
    private float processInputMoney() {
        String money = mView.getUserInputMoney();
        return Float.valueOf(money.trim());
    }

    @Override
    protected IHomeModel initModel() {
        return new HomeModel(mContext);
    }
}
