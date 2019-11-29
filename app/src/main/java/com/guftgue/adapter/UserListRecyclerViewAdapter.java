package com.guftgue.adapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guftgue.model.DataModel;
import com.guftgue.R;
import com.squareup.picasso.Picasso;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListRecyclerViewAdapter.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    private AppCompatActivity activity;
    private SpringyAdapterAnimator mAnimator;



    public UserListRecyclerViewAdapter(Context mContext , ArrayList<DataModel> arrayList, RecyclerView recyclerView,ItemListener itemListener) {

        this.mContext=mContext;
        this.mValues=arrayList;
        this.mListener=itemListener;
        mAnimator = new SpringyAdapterAnimator(recyclerView);
        mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
        mAnimator.addConfig(85,15);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTvUserName;
        public ImageView mImgProfile;
        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            mTvUserName = (TextView) v.findViewById(R.id.TvUserName);
            mImgProfile = (ImageView) v.findViewById(R.id.ImgProfile);
             activity = (AppCompatActivity) v.getContext();
        }
        @Override
        public void onClick(View view) {
            String value = mValues.get(getAdapterPosition()).getEncription_type();
            String value1 = mValues.get(getAdapterPosition()).getName();

            mListener.onClick1(value,value1);

        }
    }

    @Override
    public UserListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_userlist, parent, false);
        mAnimator.onSpringItemCreate(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.mTvUserName.setText(mValues.get(position).getName());
        Picasso.get()
                .load(mValues.get(position).getImage())
                .placeholder(R.drawable.logo)
                .into(Vholder.mImgProfile);

       mAnimator.onSpringItemBind(Vholder.itemView, position);

     /*   Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.
            }
        });*/
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onClick1(String adapterPosition, String adapterPosition1);
    }
}