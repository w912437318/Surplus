package info.aoki.surplus.system.model.home;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.aoki.surplus.system.LogUtil;
import info.aoki.surplus.system.model.BaseModel;
import info.aoki.surplus.system.model.RecordBeanDao;
import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <h3>Home Model</h3>
 * <p>Create time:12/4/18 3:30 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class HomeModel extends BaseModel implements IHomeModel {
    private final String TAG = HomeModel.class.getName();
    private RecordBeanDao mRecordBeanDao;

    public HomeModel(Context context) {
        super(context);
        this.mRecordBeanDao = mDaoSession.getRecordBeanDao();
    }

    @Override
    public void insertRecord(RecordBean recordBean, Observer<Boolean> callback) {
        Observable.just(recordBean).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(recordBean1 -> {
                    try {
                        mRecordBeanDao.insert(recordBean1);
                        return true;
                    } catch (Exception e) {
                        LogUtil.e(TAG, "Insert record filed!");
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Insert record failed!");
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    @Override
    public void getCurrentMonthConsumed(Observer<Float> callBack) {
        long firstDayOfMonth = getFirstDayInMonth();
        RecordBeanDao recordBeanDao = mDaoSession.getRecordBeanDao();
        Observable.just(firstDayOfMonth).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(aLong -> {
                    try {
                        List<RecordBean> queryResults = recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(firstDayOfMonth)).
                                list();
                        float monthlyConsumed = 0;
                        for (RecordBean recordBean : queryResults) {
                            monthlyConsumed += recordBean.getR_money();
                        }
                        return monthlyConsumed;
                    } catch (Exception e) {
                        LogUtil.e(TAG, "Query record failed!");
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Query record failed!");

                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(callBack);
    }

    private long getFirstDayInMonth() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(simpleDateFormat.format(date)), date.getMonth(), 1, 0, 0, 0);
        return calendar.getTime().getTime();
    }
}
