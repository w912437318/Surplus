package info.aoki.surplus.resource;

import android.content.Context;

/**
 * <h3>BasePresenter</h3>
 *
 * @param <T> Model
 */
public abstract class BasePresenter<T extends BaseView, Y> {
    protected Context mContext;
    protected T mView;
    protected Y mModel;

    public BasePresenter(Context mContext, T mView) {
        this.mContext = mContext;
        this.mView = mView;
        this.mModel = initModel();
    }

    protected abstract Y initModel();
}
