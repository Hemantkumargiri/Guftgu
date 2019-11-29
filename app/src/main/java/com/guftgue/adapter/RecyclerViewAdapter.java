package com.guftgue.adapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.guftgue.model.DataModel;
import com.guftgue.R;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    private AppCompatActivity activity;
   private SpringyAdapterAnimator mAnimator;



    public RecyclerViewAdapter(Context mContext , ArrayList<DataModel> arrayList, RecyclerView recyclerView) {

        this.mContext=mContext;
        this.mValues=arrayList;
        mAnimator = new SpringyAdapterAnimator(recyclerView);
        mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
        mAnimator.addConfig(85,15);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTvEncryption;
        DataModel item;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            mTvEncryption = (TextView) v.findViewById(R.id.TvEncryption);
             activity = (AppCompatActivity) v.getContext();
        }


        public void setData(DataModel item) {
            this.item = item;

            mTvEncryption.setText(item.mTvEncryption);
          //  relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
       mAnimator.onSpringItemCreate(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
      mAnimator.onSpringItemBind(Vholder.itemView, position);
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}