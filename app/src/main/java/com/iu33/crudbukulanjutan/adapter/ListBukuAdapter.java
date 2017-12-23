package com.iu33.crudbukulanjutan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iu33.crudbukulanjutan.R;
import com.iu33.crudbukulanjutan.helper.MyConstant;
import com.iu33.crudbukulanjutan.model.DataBukuItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.OnItemClick;

/**
 * Created by hp on 12/11/2017.
 */

public class ListBukuAdapter extends RecyclerView.Adapter<ListBukuAdapter.MyViewHolder> {
    Context c;
    OnItemClicked clicked;
    List<DataBukuItem> dataBukuItems;

    public ListBukuAdapter(Context c, List<DataBukuItem> listdatabuku) {
        this.c = c;
        dataBukuItems = listdatabuku;
    }

    @Override
    public ListBukuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.tampilanlistbuku, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListBukuAdapter.MyViewHolder holder, final int position) {
        holder.txtnamabuku.setText(dataBukuItems.get(position).getBuku());
        Picasso.with(c).load(MyConstant.IMAGE_URL + dataBukuItems
                .get(position).getFotoBuku().toString()).error(R.drawable.noimage)
                .placeholder(R.drawable.noimage).into(holder.imgbuku);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgbuku;
        TextView txtnamabuku;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgbuku = (ImageView) itemView.findViewById(R.id.imgbuku);
            txtnamabuku = (TextView) itemView.findViewById(R.id.txtmakanan);
        }
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }
    public void setOnClick(OnItemClicked onClick){
        clicked = onClick;
    }
}
