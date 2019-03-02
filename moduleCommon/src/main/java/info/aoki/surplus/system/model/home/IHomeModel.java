package info.aoki.surplus.system.model.home;

import info.aoki.surplus.system.pojo.RecordBean;
import rx.Observer;

/**
 * <h3></h3>
 * <p>Create time:12/4/18 3:30 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public interface IHomeModel {
    public void insertRecord(RecordBean recordBean, Observer<Boolean> callBack);

    public void getCurrentMonthConsumed(Observer<Float> callBack);
}
