package info.aoki.surplus.controller.home.fragment.daily;

import android.annotation.SuppressLint;
import android.content.Context;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.aoki.surplus.controller.R;
import info.aoki.surplus.resource.BasePresenter;
import info.aoki.surplus.resource.SimpleObserver;
import info.aoki.surplus.system.ApplicationConfig;
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
    /**
     * Show monthly cost and balance
     */
    private PieChart mMonthlyCostChart;
    /**
     * Show today all consumed record bar chart
     */
    private BarChart mTodayAllConsumedRecordChart;
    /**
     * Data format
     * <p>hh:mm -- Hour:Minutes</p>
     */
    private SimpleDateFormat mSimpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    DailyPresenter(Context context, IDailyFragment view) {
        super(context, view);
        this.mSimpleDateFormat = new SimpleDateFormat("hh:mm");
    }

    public void initCharts() {
        // Get chart widgets
        this.mMonthlyCostChart = mView.getMonthlyCostChart();
        this.mTodayAllConsumedRecordChart = mView.getTodayAllConsumedRecordChart();
        initTodayAllConsumedRecordChart();
        initMonthlyCostChart();
        initTodayAllConsumedRecordChartData();

        initTodayAllConsumedRecordChart();
    }

    /**
     * Init TodayAllConsumedRecordChart - BarChart
     * <p>Set chart style</p>
     */
    private void initTodayAllConsumedRecordChart() {
        if (mTodayAllConsumedRecordChart == null) return;
        mTodayAllConsumedRecordChart.setNoDataText(mContext.getResources().getString(R.string.you_have_no_any_records)); // Set noe data message
        mTodayAllConsumedRecordChart.setDrawGridBackground(false); // Disable draw grid background
        mTodayAllConsumedRecordChart.setDrawBorders(false); // Disable draw border
        // Disable chart description
        Description description = new Description();
        description.setEnabled(false);
        mTodayAllConsumedRecordChart.setDescription(description);
        // Chart animation
        mTodayAllConsumedRecordChart.animateY(1000);
        mTodayAllConsumedRecordChart.animateX(1000);
        // XAxis customer settings
        mTodayAllConsumedRecordChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Set X axis position : Bottom
        mTodayAllConsumedRecordChart.getXAxis().setDrawLabels(false); // Disable X axis labels
        mTodayAllConsumedRecordChart.getXAxis().setDrawGridLines(false); // Disable X axis grid line
        mTodayAllConsumedRecordChart.getXAxis().setAxisMaximum(8f);
        // Y axis settings
        mTodayAllConsumedRecordChart.getAxisRight().setEnabled(false); // Disable right Y axis
        mTodayAllConsumedRecordChart.getAxisLeft().setDrawGridLines(false); // Disable left Y axis draw lines
        mTodayAllConsumedRecordChart.getAxisLeft().setDrawAxisLine(false);
        // Custom Y axis value
        mTodayAllConsumedRecordChart.getAxisLeft().setValueFormatter((value, axis) -> "¥" + value);
        // Disable legend
        mTodayAllConsumedRecordChart.getLegend().setEnabled(false);
        // Set data on chart
        initTodayAllConsumedRecordChartData();
    }

    /**
     * Init MonthlyCostChart - PieChart
     * <p>Set chart style</p>
     */
    private void initMonthlyCostChart() {
        if (mMonthlyCostChart == null) return;
        mMonthlyCostChart.setDrawHoleEnabled(false); // Disable center hole
        mMonthlyCostChart.setDrawEntryLabels(false); // Disable entry labels
        mMonthlyCostChart.setNoDataText(mContext.getResources().getString(R.string.you_have_no_any_records)); // Set noe data message
        mMonthlyCostChart.setRotationEnabled(false); // Disable chart rotate
        mMonthlyCostChart.setDrawEntryLabels(false);
        // Disable description
        Description description = new Description();
        description.setEnabled(false);
        mMonthlyCostChart.setDescription(description);
        mMonthlyCostChart.animateY(1000, Easing.EasingOption.EaseInOutQuad); // Set chart animation
        mMonthlyCostChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL); // Set legend position
        // Set data on chart
        initMonthlyCostChartData();
    }

    private void initMonthlyCostChartData() {
        mModel.queryTodayConsumed(new SimpleObserver<Float>() {
            @Override
            public void onNext(Float aFloat) {
                if (aFloat != 0f) {
                    setMonthlyCostChartData(aFloat);
                }
            }
        });
    }

    /**
     * Init TodayAllConsumedRecordChart Data
     * <p>First draw chart</p>
     */
    private void initTodayAllConsumedRecordChartData() {
        mModel.queryTodayAllConsumedRecord(new SimpleObserver<List<RecordBean>>() {
            @Override
            public void onNext(List<RecordBean> recordBeans) {
                if (recordBeans.size() != 0) {
                    setTodayAllConsumedRecordChartData(recordBeans);
                }
            }
        });
    }

    private void setTodayAllConsumedRecordChartData(List<RecordBean> recordBeans) {
        // YAxis items
        ArrayList<BarEntry> yEntries = new ArrayList<>();
        for (int i = 0; i < recordBeans.size(); i++) {
            // Read query result, and add data to BarDataSet
            yEntries.add(new BarEntry(i, recordBeans.get(i).getR_money()));
        }
        // Create data set
        BarDataSet barDataSet = new BarDataSet(yEntries, "");
        // Create bar data
        BarData barData = new BarData(barDataSet);
        mTodayAllConsumedRecordChart.setData(barData);
        this.mView.notifyTodayAllConsumedRecordChartDataSetChange();
    }

    private void setMonthlyCostChartData(Float dailyConsumed) {
        float monthLimit = ApplicationConfig.getConfig().getMonthLimit();
        List<PieEntry> yEntries = new ArrayList<>();
        yEntries.add(new PieEntry(dailyConsumed, "今日花费"));
        yEntries.add(new PieEntry(monthLimit - dailyConsumed, "本月剩余"));
        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(14f);
        mMonthlyCostChart.setData(pieData);
    }

    @Override
    protected IDailyModel initModel() {
        return new DailyModel(mContext);
    }
}
