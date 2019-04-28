package info.aoki.surplus.system.model.daily;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import info.aoki.surplus.system.model.BaseModel;
import info.aoki.surplus.system.model.RecordBeanDao;
import info.aoki.surplus.system.pojo.RecordBean;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                        List<RecordBean> recordBeanList = recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(mTodayStartTime)).
                                list();
                        for (RecordBean recordBean : recordBeanList) {
                            todayConsumed += recordBean.getR_money();
                        }
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
                        return recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(mTodayStartTime)).
                                list();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Query failed");
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    public void queryConsumedRecordAfterTimeLimit(long timeLimit, Observer<List<RecordBean>> callback) {
        RecordBeanDao recordBeanDao = mDaoSession.getRecordBeanDao();
        Observable.just(timeLimit).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(aLong -> {
                    try {
                        return recordBeanDao.
                                queryBuilder().
                                where(RecordBeanDao.Properties.R_time.ge(timeLimit)).
                                list();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Query failed");
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(callback);
    }

    private long getTodayStartTime() {
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
}
