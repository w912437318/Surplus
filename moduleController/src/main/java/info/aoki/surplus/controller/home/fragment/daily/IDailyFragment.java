package info.aoki.surplus.controller.home.fragment.daily;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import info.aoki.surplus.resource.BaseView;

/**
 * <h3></h3>
 * <p>Create time:12/5/18 8:46 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public interface IDailyFragment extends BaseView {
    public PieChart getMonthlyCostChart();

    public BarChart getTodayAllConsumedRecordChart();

    public void notifyTodayAllConsumedRecordChartDataSetChange();

    public void notifyMonthlyCostChartDataSetChange();
}
