package edu.wayne.service.daily;

import android.content.Context;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import edu.wayne.service.daily.adapter.TodayRecordAdapter;
import info.aoki.surplus.resource.BasePresenter;
import info.aoki.surplus.system.base.SimpleObserver;
import info.aoki.surplus.system.model.daily.DailyModel;
import info.aoki.surplus.system.model.daily.IDailyModel;
import info.aoki.surplus.system.pojo.RecordBean;

/**
 * <h3>DailyPresenter</h3>
 * <p>Create time:12/5/18 8:45 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class DailyPresenter extends BasePresenter<IDailyFragment, IDailyModel> {
    private final String TAG = DailyPresenter.class.getName();

    private BarChart mTodayRecordChart;
    private ListView mTodayAllRecordListView;
    private TodayRecordAdapter mRecordAdapter;
    private List<RecordBean> mToadyRecordList;

    DailyPresenter(Context context, IDailyFragment view) {
        super(context, view);
        mToadyRecordList = new ArrayList<>();
    }

    public void initCharts() {
        this.mTodayRecordChart = mView.getTodayRecordChart();
        this.mTodayAllRecordListView = mView.getToadyRecordListView();
        initTodayRecordChartStyle();
    }

    private void initTodayRecordChartStyle() {
        // Disable chart zoom
        mTodayRecordChart.setDoubleTapToZoomEnabled(false);
        mTodayRecordChart.setPinchZoom(false);

        // Disable chart grid & borders
        mTodayRecordChart.setNoDataText(mContext.getResources().getString(R.string.you_have_no_any_records));
        mTodayRecordChart.setDrawGridBackground(false);
        mTodayRecordChart.setDrawBorders(false);

        // Disable chart description
        Description description = new Description();
        description.setEnabled(false);
        mTodayRecordChart.setDescription(description);

        // Set chart animation
        mTodayRecordChart.animateY(1000);
        mTodayRecordChart.animateX(1000);

        // Disable X axis
        mTodayRecordChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mTodayRecordChart.getXAxis().setDrawLabels(false);
        mTodayRecordChart.getXAxis().setDrawGridLines(false);
        mTodayRecordChart.getXAxis().setAxisMinimum(8f);

        // Disable Y axis
        mTodayRecordChart.getAxisRight().setEnabled(false);
        mTodayRecordChart.getAxisLeft().setEnabled(false);

        // Disable legend
        mTodayRecordChart.getLegend().setEnabled(false);
    }

    public void loadChartData() {
        mModel.queryTodayAllConsumedRecord(new SimpleObserver<List<RecordBean>>() {
            @Override
            public void onNext(List<RecordBean> recordBeans) {
                if (recordBeans.size() != 0) {
                    mToadyRecordList.clear();
                    mToadyRecordList.addAll(recordBeans);
                    setDataOnChart();
                    setDataOnListView();
                }
            }
        });
    }

    private void setDataOnChart() {
        ArrayList<BarEntry> yEntries = new ArrayList<>();
        for (int i = 0; i < mToadyRecordList.size(); i++) {
            yEntries.add(new BarEntry(i, mToadyRecordList.get(i).getR_money()));
        }
        BarDataSet barDataSet = new BarDataSet(yEntries, "");
        barDataSet.setColor(ColorTemplate.PASTEL_COLORS[0]);
        BarData barData = new BarData(barDataSet);
        mTodayRecordChart.setData(barData);
    }

    private void setDataOnListView() {
        mRecordAdapter = new TodayRecordAdapter(mContext, mToadyRecordList);
        mTodayAllRecordListView.setAdapter(mRecordAdapter);
        this.mTodayRecordChart.notifyDataSetChanged();
    }

    public void updateTodayConsumedRecord() {
        System.out.println("更新信息");
        mModel.queryTodayAllConsumedRecord(new SimpleObserver<List<RecordBean>>() {
            @Override
            public void onNext(List<RecordBean> recordBeans) {
                if (recordBeans.size() != 0) {
                    mToadyRecordList.clear();
                    mToadyRecordList.addAll(recordBeans);
                    mRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected IDailyModel initModel() {
        return new DailyModel(mContext);
    }
}
