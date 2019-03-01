package info.aoki.surplus.system.model.daily;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import info.aoki.surplus.system.model.BaseModel;
import info.aoki.surplus.system.model.RecordBeanDao;
import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <h3>DailyModel</h3>
 * <p>Create time:12/5/18 8:47 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class DailyModel extends BaseModel implements IDailyModel {
    private long mTodayStartTime;

    public DailyModel(Context context) {
        super(context);
        this.mTodayStartTime = getTodayStartTime();
    }

    @Override
    public void queryTodayConsumed(Observer<Float> callback) {
        RecordBeanDao recordBeanDao = mDaoSession.getRecordBeanDao();
        Observable.just(mTodayStartTime).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(aLong -> {
                    float todayConsumed = 0f;
                    try {
                        // Query the consumed after 12:00 AM
                        List<RecordBean> recordBeanList = recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(mTodayStartTime)).
                                list();
                        // Calc today consumed
                        for (RecordBean recordBean : recordBeanList) {
                            todayConsumed += recordBean.getR_money();
                        }
                        // Query success, return result
                        return todayConsumed;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Query failed");
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    @Override
    public void queryTodayAllConsumedRecord(Observer<List<RecordBean>> callback) {
        RecordBeanDao recordBeanDao = mDaoSession.getRecordBeanDao();
        Observable.just(mTodayStartTime).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(aLong -> {
                    try {
                        // Query success
                        return recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(mTodayStartTime)).
                                list();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Query failed, throw exception.
                    throw new RuntimeException("Query failed");
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    @Override
    public void queryConsumedRecordAfterTimeLimit(long timeLimit, Observer<List<RecordBean>> callback) {
        RecordBeanDao recordBeanDao = mDaoSession.getRecordBeanDao();
        Observable.just(timeLimit).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(aLong -> {
                    try {
                        // Query success.
                        return recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(timeLimit)).
                                list();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Query failed, throw exception.'
                    throw new RuntimeException("Query failed");
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    /**
     * Get toady 0:00:00 millis
     *
     * @return TimeMillis
     */
    private long getTodayStartTime() {
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime); // Set calendar.
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Set hour 0
        calendar.set(Calendar.MINUTE, 0); // Set minute 0
        calendar.set(Calendar.SECOND, 0); // Ser second 0
        return calendar.getTimeInMillis();
    }
}