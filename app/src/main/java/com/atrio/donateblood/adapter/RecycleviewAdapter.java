/*
package com.atrio.donateblood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atrio.quesapp.R;
import com.atrio.quesapp.SeriesActivity;
import com.atrio.quesapp.model.ShowData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

*/
/**
 * Created by Admin on 14-06-2017.
 *//*


public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder>{
    private Context c;
    ArrayList<ShowData> list_data;

    public RecycleviewAdapter(Context context, ArrayList<ShowData> arrayList) {
        this.c = context;
        this.list_data = arrayList;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tv_text.setText(list_data.get(position).getSub());
        Glide.with(c).load(list_data.get(position).getImg()).placeholder(R.drawable.book).into(holder.img_sub);
        holder.tittle = list_data.get(position).getSub();


    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public TextView tv_text;
        public ImageView img_sub;
        public String tittle;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_sub = (ImageView) itemView.findViewById(R.id.sub_pic);
            tv_text = (TextView) itemView.findViewById(R.id.sub_tittle);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SeriesActivity.class);
            Log.i("tittle44",""+tittle);
            intent.putExtra("Sub",tittle);
            view.getContext().startActivity(intent);

        }
    }
}


*/
