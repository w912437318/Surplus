package info.aoki.surplus.system.model.daily;

import java.util.List;

import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observer;

/**
 * <h3>IDailyModel</h3>
 * <p>Create time:12/5/18 8:47 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public interface IDailyModel {
    /**
     * Query consumed today
     *
     * @param callback RxJava observer
     */
    public void queryTodayConsumed(Observer<Float> callback);

    /**
     * Query all consumed record in today.
     *
     * @param callback RxJava observer
     */
    public void queryTodayAllConsumedRecord(Observer<List<RecordBean>> callback);

    /**
     * Query consumed record that after specified time
     *
     * @param timeLimit Specified time
     * @param callback  RxJava observer
     */
    public void queryConsumedRecordAfterTimeLimit(long timeLimit, Observer<List<RecordBean>> callback);
}
