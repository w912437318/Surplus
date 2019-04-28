package edu.wayne.service.daily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.wayne.service.daily.R;
import info.aoki.surplus.system.pojo.RecordBean;

public class TodayRecordAdapter extends BaseAdapter {

    private List<RecordBean> mItems;
    private LayoutInflater mInflater;
    private SimpleDateFormat mDateFormat;
    private Context mContext;

    public TodayRecordAdapter(Context context, List<RecordBean> items) {
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mDateFormat = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.daily_item_consumed_record, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }
        touchDataOnView((ViewHolder) view.getTag(), mItems.get(i));
        return view;
    }

    private void touchDataOnView(ViewHolder viewHolder, RecordBean recordBean) {
        viewHolder.dailyItemTvMoney.setText(mContext.getString(R.string.money_flag) + recordBean.getR_money());
        viewHolder.dailyItemTvType.setText(recordBean.getR_type());
        viewHolder.dailyItemTvMemo.setText(recordBean.getR_memo());
        viewHolder.dailyItemTvTime.setText(mDateFormat.format(recordBean.getR_time()));
    }

    private class ViewHolder {
        TextView dailyItemTvMoney;
        TextView dailyItemTvType;
        TextView dailyItemTvMemo;
        TextView dailyItemTvTime;

        private ViewHolder(View convertView) {
            dailyItemTvMoney = convertView.findViewById(R.id.daily_item_tv_money);
            dailyItemTvType = convertView.findViewById(R.id.daily_item_tv_type);
            dailyItemTvMemo = convertView.findViewById(R.id.daily_item_tv_memo);
            dailyItemTvTime = convertView.findViewById(R.id.daily_item_tv_time);
        }
    }
}
