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

    protected T mPresenter;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        this.mPresenter = initPresenter();

        initData();
        bindWidget();
        initView();
        fetchData();
    }

    protected abstract int getLayoutId();

    /**
     * 初始化 Presenter
     *
     * @return Presenter
     */
    protected abstract T initPresenter();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定控件
     */
    protected abstract void bindWidget();

    /**
     * 初始化 View
     */
    protected abstract void initView();

    /**
     * 获取数据
     * <p>在此处获取异步数据</p>
     */
    protected abstract void fetchData();
}
