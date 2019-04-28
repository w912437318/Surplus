package edu.wayne.service.weekly;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import info.aoki.surplus.resource.BaseFragment;
import info.aoki.surplus.resource.BasePresenter;

/**
 * <h3></h3>
 * <p>Create time:12/5/18 2:33 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Route(path = "/weekly/weekly")
public class WeeklyFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.weekly_fragment_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findWidget(View rootView) {

    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void fetchData() {

    }
}
