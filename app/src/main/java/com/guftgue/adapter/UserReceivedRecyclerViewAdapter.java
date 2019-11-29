package com.guftgue.adapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guftgue.R;
import com.guftgue.model.DataModel;
import com.squareup.picasso.Picasso;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class UserReceivedRecyclerViewAdapter extends RecyclerView.Adapter<UserReceivedRecyclerViewAdapter.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    private AppCompatActivity activity;
    private SpringyAdapterAnimator mAnimator;



    public UserReceivedRecyclerViewAdapter(Context mContext , ArrayList<DataModel> arrayList, RecyclerView recyclerView, ItemListener mListener) {

        this.mContext=mContext;
        this.mValues=arrayList;
        this.mListener=mListener;
        mAnimator = new SpringyAdapterAnimator(recyclerView);
        mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
        mAnimator.addConfig(85,15);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTvUserName;
        public ImageView mImgProfile;
        DataModel item;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            mTvUserName = (TextView) v.findViewById(R.id.TvUserName);
            mImgProfile = (ImageView) v.findViewById(R.id.ImgProfile);
             activity = (AppCompatActivity) v.getContext();
        }

        @Override
        public void onClick(View v) {
            String value = mValues.get(getAdapterPosition()).encription_type;
            String value1 = mValues.get(getAdapterPosition()).getName();
            mListener.onItemClick(value,value1);
        }
    }

    @Override
    public UserReceivedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_receiveduser, parent, false);
        mAnimator.onSpringItemCreate(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.mTvUserName.setText(mValues.get(position).getName());
        Picasso.get()
                .load(mValues.get(position).getImage())
                .placeholder(R.drawable.logo)
                .into(Vholder.mImgProfile);
        mAnimator.onSpringItemBind(Vholder.itemView, position);
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(String encryption_type,String name);
    }
}