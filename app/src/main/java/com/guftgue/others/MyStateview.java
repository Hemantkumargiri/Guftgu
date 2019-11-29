package com.guftgue.others;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.guftgue.R;
import com.github.nukc.stateview.StateView;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by akash on 12/12/1.
 */

public class MyStateview {
    private ImageView iv_loading;
    private Context context;
    private ProgressClickListener mListener;
    private StateView mStateView;

    public MyStateview(Activity activity, View view, int emptyView, int retryView, int loadingView) {
        this.context = activity;
        mListener = (ProgressClickListener) activity;
        if (view == null) {
            mStateView = StateView.inject(activity);
        } else {
            mStateView = StateView.inject(view);
        }
        mStateView.setRetryResource(retryView);
        mStateView.setEmptyResource(emptyView);
        mStateView.setLoadingResource(loadingView);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mListener.onRetryClick();
            }
        });
    }

    public MyStateview(Activity activity, View view) {
        this.context = activity;
        mListener = (ProgressClickListener) activity;
        if (view == null) {
            mStateView = StateView.inject(activity);
        } else {
            mStateView = StateView.inject(view);
        }
        mStateView.setRetryResource(R.layout.view_retry);
        mStateView.setEmptyResource(R.layout.view_empty);
        mStateView.setLoadingResource(R.layout.loading);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mListener.onRetryClick();
            }
        });
    }

    public MyStateview(Fragment fragment, View view) {
        this.context = fragment.getActivity();

        mListener = (ProgressClickListener) fragment;
        if (view == null) {
            mStateView = StateView.inject(fragment.getView());
            mStateView.animate();
        } else {
            mStateView = StateView.inject(view);
        }

        AVLoadingIndicatorView img = new AVLoadingIndicatorView(context);
        mStateView.setRetryResource(R.layout.view_retry);
        mStateView.setEmptyResource(R.layout.view_empty);
        mStateView.setLoadingResource(R.layout.loading);

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // iv_loading = inflater.inflate(R.layout.loading, (ViewGroup) mStateView.getParent(), false).findViewById(R.id.iv_loading);
        //   Animation animation = AnimationUtils.loadAnimation(context, R.anim.sequential);
        // iv_loading.setAnimation(animation);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mListener.onRetryClick();
            }
        });
    }

    public MyStateview(Fragment fragment, View view, ProgressClickListener progressClickListener) {
        this.context = fragment.getActivity();
        mListener = progressClickListener;
        if (view == null) {
            mStateView = StateView.inject(fragment.getView());
        } else {
            mStateView = StateView.inject(view);
        }

        mStateView.setRetryResource(R.layout.view_retry);
        mStateView.setEmptyResource(R.layout.view_empty);
        mStateView.setLoadingResource(R.layout.loading);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mListener.onRetryClick();
            }
        });
    }


    public MyStateview(Activity activity, View view, ProgressClickListener progressClickListener) {
        this.context = activity;
        mListener = progressClickListener;
        if (view == null) {
            mStateView = StateView.inject(activity);
        } else {
            mStateView = StateView.inject(view);
        }
        mStateView.setRetryResource(R.layout.view_retry);
        mStateView.setEmptyResource(R.layout.view_empty);
        mStateView.setLoadingResource(R.layout.loading);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mListener.onRetryClick();
            }
        });
    }

    public void showLoading() {
        mStateView.showLoading();
    }

    public void showRetry() {
        mStateView.showRetry();
    }

    public void showContent() {
        mStateView.showContent();
    }


    public void showEmpty() {
        mStateView.showEmpty();
    }

    public void setEmptyResource(int emptyResource) {
        mStateView.setEmptyResource(emptyResource);
    }

    public void setLoadingResource(int emptyResource) {
        mStateView.setLoadingResource(emptyResource);
    }

    public void setRetryResource(int emptyResource) {
        mStateView.setRetryResource(emptyResource);
    }

}
