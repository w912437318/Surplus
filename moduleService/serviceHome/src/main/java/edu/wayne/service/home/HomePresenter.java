package edu.wayne.service.home;

import android.content.Context;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import info.aoki.surplus.resource.BasePresenter;
import info.aoki.surplus.system.base.SimpleObserver;
import info.aoki.surplus.system.model.home.HomeModel;
import info.aoki.surplus.system.model.home.IHomeModel;
import info.aoki.surplus.system.pojo.RecordBean;

public class HomePresenter extends BasePresenter<HomeView, IHomeModel> {

    private String[] mRecordTypes;
    private SimpleDateFormat mSimpleDateFormat;

    HomePresenter(Context context, HomeView view) {
        super(context, view);
        this.mRecordTypes = mContext.getResources().getStringArray(R.array.consumed_record_type);
        this.mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    /**
     * 添加一笔消费记录
     */
    public void addConsumedRecord() {
        if (checkInputMoney()) {
            RecordBean recordBean = new RecordBean(Float.valueOf(mView.getUserInputMoney().trim()), mRecordTypes[mView.getSelectedConsumedTypeId()]);
            if (!TextUtils.isEmpty(mView.getUserInputMemo())) {
                recordBean.setR_memo(mView.getUserInputMemo());
            }
            mModel.insertRecord(recordBean, new SimpleObserver<Boolean>() {
                @Override
                public void onNext(Boolean isSuccess) {
                    if (isSuccess) {
                        mView.showHint(R.string.save_success);
                        mView.resetInputInfo();
                        setConsumedMoney();
                    }
                }
            });
        } else {
            mView.setInputMoneyErrorHint(mContext.getString(R.string.please_input_money));
        }
    }

    /**
     * 设置当前时间
     */
    public void setCurrentTime() {
        mView.setCurrentDate(mSimpleDateFormat.format(System.currentTimeMillis()));
    }

    /**
     * 设置本月的花费金额
     */
    public void setConsumedMoney() {
        mModel.getCurrentMonthConsumed(new SimpleObserver<Float>() {
            @Override
            public void onNext(Float aFloat) {
                mView.setConsumedMoney(mContext.getString(R.string.money_flag) + "\t" + aFloat);
            }
        });
    }

    private boolean checkInputMoney() {
        return !(TextUtils.isEmpty(mView.getUserInputMoney()) || Float.valueOf(mView.getUserInputMoney().trim()) == 0f);
    }

    @Override
    protected IHomeModel initModel() {
        return new HomeModel(mContext);
    }
}
