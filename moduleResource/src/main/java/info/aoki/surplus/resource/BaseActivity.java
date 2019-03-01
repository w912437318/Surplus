package info.aoki.surplus.resource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * <h3>BaseActivity</h3>
 *
 * @param <T> Presenter
 */
public abstract class BaseActivity<T extends BasePresenter>
        extends AppCompatActivity
        implements BaseView {

    /**
     * Presenter
     * <hr/>
     * <p>Service,  all logic method in this object</p>
     */
    protected T mPresenter;

    // Can't not modify this method
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate activity content view
        setContentView(getLayoutId());

        // Start init activity data

        // init presenter
        this.mPresenter = initPresenter();
        // init data
        initData();
        // find widget
        bindWidget();
        // create adapter or set listener
        initView();
    }

    /**
     * Get activity layout id
     *
     * @return Layout id
     */
    protected abstract int getLayoutId();

    /**
     * Create presenter
     * <p>It's run before {@link BaseActivity#initData()}, So you count do anything in constructor</p>
     *
     * @return Presenter
     */
    protected abstract T initPresenter();

    protected abstract void initData();

    protected abstract void bindWidget();

    protected abstract void initView();
}
