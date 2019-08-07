package com.example.timple.zdpigapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.entity.FattenInfo;
import com.example.timple.zdpigapp.entity.SelectPigEntity;

import java.util.List;

public class PigSingleInfoAdapter extends RecyclerView.Adapter<PigSingleInfoAdapter.ViewHolder> {

    private Context context;
    private List<FattenInfo> list;

    public PigSingleInfoAdapter(Context context, List<FattenInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_info, parent, false);
        return new PigSingleInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof ViewHolder) {
            holder.tv_content.setText(list.get(position).getResult());
            holder.tv_name.setText(list.get(position).getTag_name());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
