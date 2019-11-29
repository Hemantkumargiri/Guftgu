package com.guftgue.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.guftgue.R;
import com.guftgue.model.ContactListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    public List<ContactListModel.UserList> contactlist ;
    public ArrayList<ContactListModel.UserList> arrayList ;
    ContactListModel list;
    boolean checked = false;
    public SelectItem selectItem;
    Context context;
    View vv;

    public RecyclerAdapter(Context context, List<ContactListModel.UserList> items, SelectItem mselectItem) {
        this.context = context;
        this.contactlist = items;
        this.arrayList = new ArrayList<ContactListModel.UserList>();
        this.arrayList.addAll(contactlist);
        this.selectItem=mselectItem;
    }

    public void filter(String text) {
        Log.d("name","--------------------");
        text = text.toLowerCase(Locale.getDefault());
        contactlist.clear();
        if (text.length() == 0) {
            Log.d("namevbn","--------------------");
            contactlist.addAll(arrayList);
        } else {
            for (ContactListModel.UserList wp : arrayList) {
                if (wp.getFirstName().toLowerCase(Locale.getDefault())
                        .contains(text)) {
                    Log.d("name","---------------------------"+wp.getFirstName());
                    contactlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.from(context).inflate(R.layout.contactlist_row, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(contactlist.get(position).getFirstName());
        holder.phone.setText(contactlist.get(position).getPhone());
        Picasso.get()
                .load(contactlist.get(position).getImage())
                .placeholder(R.drawable.logo)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView phone;
        public ImageView photo;
        public LinearLayout contact_select_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            title = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.no);
            photo = (ImageView) itemView.findViewById(R.id.ImgProfile);
            contact_select_layout = (LinearLayout) itemView.findViewById(R.id.contact_select_layout);
            contact_select_layout.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            selectItem.onClick(contactlist.get(getAdapterPosition()).getFirstName(), contactlist.get(getAdapterPosition()).getPhone()
                    ,contactlist.get(getAdapterPosition()).getUserId());

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface SelectItem{

        void onClick(String position, String position1,String Receiver_userId);
    }

}