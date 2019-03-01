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
    /**
     * <p>Save user input record</p>
     *
     * @param recordBean {@link RecordBean}
     */
    public void insertRecord(RecordBean recordBean, Observer<Boolean> callBack);

    /**
     * Get Month Consumed
     * <p>Query all month cost,calc consumed</p>
     *
     * @param callBack {@link Observer}
     */
    public void getCurrentMonthConsumed(Observer<Float> callBack);
}
