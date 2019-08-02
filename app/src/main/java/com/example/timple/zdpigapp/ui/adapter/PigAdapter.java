package com.example.timple.zdpigapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.entity.SelectPigEntity;

import java.util.List;

public class PigAdapter extends RecyclerView.Adapter<PigAdapter.ViewHolder> {

    private Context context;
    private List<SelectPigEntity> list;

    public PigAdapter(Context context, List<SelectPigEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pig, parent, false);
        return new PigAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof ViewHolder) {
            holder.rb_select.setClickable(list.get(position).isSelect());
            holder.tv_name.setText(list.get(position).getPigName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i != position) {
                            list.get(i).setSelect(false);
                        } else {
                            list.get(position).setSelect(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton rb_select;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            rb_select = itemView.findViewById(R.id.rb_select);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
