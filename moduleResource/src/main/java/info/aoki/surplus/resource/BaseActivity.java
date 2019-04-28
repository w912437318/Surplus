package info.aoki.surplus.resource;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

/**
 * <h3>BaseActivity</h3>
 *
 * <h4>Activity 初始化顺序</h4>
 * <ol>
 * <li><pre>getLayoutId()</pre></li>
 * <li><pre>initData()</pre></li>
 * <li><pre>bindWidget()</pre></li>
 * <li><pre>initView()</pre></li>
 * <li><pre>initPresenter()</pre></li>
 * <li><pre>fetchData()</pre></li>
 * </ol>
 * <p>
 * Presenter 初始化时，界面已经渲染完成，可以在 Presenter 的构造函数中获取控件
 * </p>
 *
 * @param <T> Presenter
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected T mPresenter;
    private Snackbar mSnackbar;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        prepare();
        initData();
        bindWidget();
        initView();
        this.mPresenter = initPresenter();
    }

    private void prepare() {
        this.mSnackbar = Snackbar.make(
                getWindow().getDecorView().getRootView(),
                "",
                Snackbar.LENGTH_SHORT);
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void bindWidget();

    protected abstract void initView();

    protected abstract T initPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        fetchData();
    }

    /**
     * 界面渲染完成，在此方法中获取数据
     */
    protected abstract void fetchData();

    @Override
    public void showHint(String hint) {
        mSnackbar.setText(hint);
        mSnackbar.show();
    }

    @Override
    public void showHint(int hintResId) {
        mSnackbar.setText(hintResId);
        mSnackbar.show();
    }
}
