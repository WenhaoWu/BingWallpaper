package wenhao.practice.bingwallpaper.infra;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import wenhao.practice.bingwallpaper.util.DialogCreator;

public abstract class BaseView<V extends BaseContract, T extends BasePresenter<V>>
        extends AppCompatActivity
        implements BaseContract {

    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();

        //noinspection unchecked
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();

        this.onTaskComplete();

        super.onDestroy();
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    public void onTaskError(Throwable throwable) {

        if (ensureContext() == null)
            return;

        DialogCreator dialog = DialogCreator.getInstance(ensureContext());

        dialog.showGeneralErrorDialog();
    }

    @Override
    public void onTaskComplete() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.unSubscribe();
        this.onTaskComplete();
    }

    @Override
    public Context ensureContext() {

        Context context = this.getBaseContext();

        if (context != null)
            return context;
        else
            throw new IllegalStateException(this + " as BaseView does not have context");
    }

    protected abstract T createPresenter();

}