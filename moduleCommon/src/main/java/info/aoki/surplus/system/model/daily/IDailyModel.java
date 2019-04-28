package info.aoki.surplus.system.model.daily;

import java.util.List;

import info.aoki.surplus.system.pojo.RecordBean;
import io.reactivex.Observer;

/**
 * <h3>IDailyModel</h3>
 * <p>Create time:12/5/18 8:47 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public interface IDailyModel {

    public void queryTodayConsumed(Observer<Float> callback);

    public void queryTodayAllConsumedRecord(Observer<List<RecordBean>> callback);
}
