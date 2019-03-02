package info.aoki.surplus.controller.home;

import android.annotation.SuppressLint;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import java.text.SimpleDateFormat;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.controller.home.fragment.daily.DailyService;
import info.aoki.surplus.resource.BasePresenter;
import info.aoki.surplus.system.model.home.HomeModel;
import info.aoki.surplus.system.model.home.IHomeModel;
import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observer;

public class HomePresenter extends BasePresenter<HomeView, IHomeModel> {

    private final String TAG = HomePresenter.class.getName();
    private String[] mRecordTypes;
    private SimpleDateFormat mSimpleDateFormat;

    private long mLastQueryTodayConsumedTime;
    private DailyService mDailyService;

    @SuppressLint("SimpleDateFormat")
    HomePresenter(Context context, HomeView view) {
        super(context, view);
        this.mRecordTypes = mContext.getResources().getStringArray(R.array.dialog_add_record_spinner_items);
        this.mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDailyService = (DailyService) ARouter.getInstance().build("/controller/daily").navigation();
    }

    public void addRecord() {
        RecordBean recordBean = new RecordBean(processInputMoney(), mRecordTypes[mView.getUserSelectType()]);
        if (!mView.getUserInputMemo().equals("")) recordBean.setR_memo(mView.getUserInputMemo());
        mModel.insertRecord(recordBean, new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
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

    public void setCurrentTime() {
        mView.setCurrentDate(mSimpleDateFormat.format(System.currentTimeMillis()));
    }

    public void setConsumedMoney() {
        mModel.getCurrentMonthConsumed(new Observer<Float>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Float aFloat) {
                mView.setConsumedMoney("¥\t" + aFloat);
            }
        });
    }

    private float processInputMoney() {
        String money = mView.getUserInputMoney();
        return Float.valueOf(money.trim());
    }

    @Override
    protected IHomeModel initModel() {
        return new HomeModel(mContext);
    }
}
