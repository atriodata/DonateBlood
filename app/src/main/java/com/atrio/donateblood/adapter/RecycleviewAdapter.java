package com.atrio.donateblood.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atrio.donateblood.NotifiyActivity;
import com.atrio.donateblood.R;
import com.atrio.donateblood.model.RecipientDetail;

import java.util.ArrayList;


public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder> {
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
        holder.msg_id = list_data.get(position).getMsg_id();
//        Log.i("type44",""+list_data.get(position).getType());
        if (list_data.get(position).getType().equals("donorwilling")) {
            holder.donor_string = "Donor is willing to donate Blood for " + list_data.get(position).getBloodgroup()
                    + "\n" + "Contact on this Number" + list_data.get(position).getPhoneno();
            holder.tv_text.setText(holder.donor_string);
            holder.call_no = list_data.get(position).getPhoneno();

        } else {
            holder.recipient_string = list_data.get(position).getBody();
            holder.tv_text.setText(holder.recipient_string);
            holder.msg_id = list_data.get(position).getMsg_id();
            holder.tokenId = list_data.get(position).getTokenId();
            holder.blood_data = list_data.get(position).getBloodgroup();
            holder.recipient_phn=list_data.get(position).getPhoneno();


        }
    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_text;
        public ImageView img_noti;
        public String msg_id, donor_string, recipient_string, call_no,blood_data,tokenId,recipient_phn;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_noti = (ImageView) itemView.findViewById(R.id.im_noti);
            tv_text = (TextView) itemView.findViewById(R.id.tv_noti);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (tv_text.getText() != donor_string) {
                Intent intent = new Intent(view.getContext(), NotifiyActivity.class);
                intent.putExtra("msg_id", msg_id);
                intent.putExtra("blood_data", blood_data);
                intent.putExtra("token_id",tokenId);
                intent.putExtra("recipient_phn",recipient_phn);
                view.getContext().startActivity(intent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + call_no));
               //* Log.i("tittlec44", "" + call_no);*/

                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                view.getContext().startActivity(callIntent);

            }

        }
    }




}


