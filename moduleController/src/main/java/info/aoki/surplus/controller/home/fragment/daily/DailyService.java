package info.aoki.surplus.controller.home.fragment.daily;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Router Service
 * <hr/>
 * User add record in activity, call this interface's method by router
 */
public interface DailyService extends IProvider {
    public void appendTodayAllConsumedRecordChartEntry(long LastQueryTime);
}
