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

import com.atrio.donateblood.NotifiyActivity;
import com.atrio.donateblood.R;
import com.atrio.donateblood.model.RecipientDetail;

import java.util.ArrayList;


public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder>{
    private Context c;
    ArrayList<RecipientDetail> list_data;

    public RecycleviewAdapter(Context context, ArrayList<RecipientDetail> arrayList) {
        this.c = context;
        this.list_data = arrayList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notification_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_text.setText(list_data.get(position).getBody());
        holder.msg_id = list_data.get(position).getMsg_id();
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
        public ImageView img_noti;
        public String msg_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_noti = (ImageView) itemView.findViewById(R.id.im_noti);
            tv_text = (TextView) itemView.findViewById(R.id.tv_noti);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NotifiyActivity.class);
            Log.i("tittle44",""+msg_id);
            intent.putExtra("msg_id",msg_id);
            view.getContext().startActivity(intent);

        }
    }
}


