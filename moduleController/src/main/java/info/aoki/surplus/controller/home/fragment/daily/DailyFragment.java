package info.aoki.surplus.controller.home.fragment.daily;

import android.content.Context;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BaseFragment;

/**
 * <h3>DailyFragment</h3>
 * <p>Create time:12/5/18 2:32 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Route(path = "/controller/daily")
public class DailyFragment
        extends BaseFragment<DailyPresenter>
        implements IDailyFragment, DailyService {

    /**
     * BarChart to show user today consumed
     * <hr/>
     * On fragment inti, load all today consumed record on this chart<br/>
     * User every time add consumed record, add an entry to this chart<br/>
     */
    private BarChart mTodayAllConsumedRecordChart;

    /**
     * PieChart to show this month consumption analysis
     * <hr/>
     * Calc today all consumed
     */
    private PieChart mMonthlyCostChart;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void findWidget(View view) {
        this.mTodayAllConsumedRecordChart = view.findViewById(R.id.fragment_daily_today_cost);
        this.mMonthlyCostChart = view.findViewById(R.id.fragment_daily_today_of_month);
        this.mTodayAllConsumedRecordChart.setDoubleTapToZoomEnabled(false);
        // Init charts, set charts style, first draw data on chart
        mPresenter.initCharts();
    }

    @Override
    protected DailyPresenter initPresenter() {
        return new DailyPresenter(mContext, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.controller_home_fragment_daily;
    }

    @Override
    public PieChart getMonthlyCostChart() {
        return this.mMonthlyCostChart;
    }

    @Override
    public BarChart getTodayAllConsumedRecordChart() {
        return this.mTodayAllConsumedRecordChart;
    }

    @Override
    public void notifyTodayAllConsumedRecordChartDataSetChange() {
        this.mTodayAllConsumedRecordChart.notifyDataSetChanged();
    }

    @Override
    public void notifyMonthlyCostChartDataSetChange() {
        this.mMonthlyCostChart.notifyDataSetChanged();
    }

    @Override
    public void appendTodayAllConsumedRecordChartEntry(long LastQueryTime) {
        // TODO query consumed
    }

    @Override
    public void init(Context context) {

    }
}
