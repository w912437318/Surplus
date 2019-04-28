package edu.wayne.service.daily;

import android.view.View;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.mikephil.charting.charts.BarChart;

import info.aoki.surplus.resource.BaseFragment;

/**
 * <h3>DailyFragment</h3>
 * <p>Create time:12/5/18 2:32 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Route(path = "/daily/daily",group = "daily")
public class DailyFragment extends BaseFragment<DailyPresenter> implements IDailyFragment {

    private BarChart mTodayRecordChart;
    private ListView mToadyRecordListView;

    @Override
    protected int getLayoutResId() {
        return R.layout.daily_fragment_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findWidget(View view) {
        this.mTodayRecordChart = view.findViewById(R.id.fragment_daily_today_cost);
        this.mToadyRecordListView = view.findViewById(R.id.fragment_daily_lv_today_record);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected DailyPresenter initPresenter() {
        return new DailyPresenter(mContext, this);
    }

    @Override
    protected void fetchData() {
        mPresenter.initCharts();
        mPresenter.loadChartData();
    }

    @Override
    public BarChart getTodayRecordChart() {
        return this.mTodayRecordChart;
    }

    @Override
    public ListView getToadyRecordListView() {
        return mToadyRecordListView;
    }
}
