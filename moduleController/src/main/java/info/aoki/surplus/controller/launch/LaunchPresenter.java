package info.aoki.surplus.controller.launch;

import android.content.Context;

import info.aoki.surplus.resource.BasePresenter;

public class LaunchPresenter extends BasePresenter<LaunchView, Object> {
    LaunchPresenter(Context mContext, LaunchView mView) {
        super(mContext, mView);
    }

    @Override
    protected Object initModel() {
        return null;
    }
}
