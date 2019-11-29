package com.guftgue.adapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guftgue.model.Model1;
import com.guftgue.R;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class CreatedViewRVAdapter extends RecyclerView.Adapter<CreatedViewRVAdapter.ViewHolder> {

    ArrayList<Model1> mValues;
    Context mContext;
    protected ItemListener mListener;
    private AppCompatActivity activity;
    private SpringyAdapterAnimator mAnimator;




    public CreatedViewRVAdapter(Context mContext , ArrayList<Model1> arrayList, RecyclerView recyclerView) {

        this.mContext=mContext;
        this.mValues=arrayList;
        mAnimator = new SpringyAdapterAnimator(recyclerView);
        mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
        mAnimator.addConfig(85,15);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvCreatedView;
        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            mTvCreatedView = (TextView) v.findViewById(R.id.TvCreatedView);
             activity = (AppCompatActivity) v.getContext();
        }

        @Override
        public void onClick(View view) {
        }
    }

    @Override
    public CreatedViewRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_createdview, parent, false);
       mAnimator.onSpringItemCreate(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.mTvCreatedView.setText(mValues.get(position).mTvCreatedView);
        mAnimator.onSpringItemBind(Vholder.itemView, position);

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(Model1 item);
    }
}