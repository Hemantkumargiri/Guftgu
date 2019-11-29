package com.guftgue.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guftgue.R;
import com.guftgue.model.Model;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class ChooseSpclCharacterRVAdapter extends RecyclerView.Adapter<ChooseSpclCharacterRVAdapter.ViewHolder> {

    public boolean isClickable = true;
    ArrayList<Model> mValues;
    Context mContext;
   private SpringyAdapterAnimator mAnimator;

    public SelectSpclItemInterface calback;



    public ChooseSpclCharacterRVAdapter(Context mContext , ArrayList<Model> arrayList, RecyclerView recyclerView, SelectSpclItemInterface mcalback) {

        this.mContext=mContext;
        this.mValues=arrayList;
        this.calback=mcalback;
        mAnimator = new SpringyAdapterAnimator(recyclerView);
        mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
        mAnimator.addConfig(85,15);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvAlphabet;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            mTvAlphabet = (TextView) v.findViewById(R.id.TvAlphabet);
            }

        @Override
        public void onClick(View view) {
            calback.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public ChooseSpclCharacterRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_choosechar, parent, false);
       mAnimator.onSpringItemCreate(view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
       mAnimator.onSpringItemBind(Vholder.itemView, position);
        Vholder.mTvAlphabet.setText(mValues.get(position).mTvAlphabet);

        }


    @Override
    public int getItemCount() {

        return mValues.size();
    }


    public interface SelectSpclItemInterface {

        void onItemClick(View view, int position);
    }
}