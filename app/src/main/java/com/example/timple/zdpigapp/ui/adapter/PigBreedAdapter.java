package com.example.timple.zdpigapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.entity.BreedInfo;
import com.example.timple.zdpigapp.entity.FattenInfo;

import java.util.List;

public class PigBreedAdapter extends RecyclerView.Adapter<PigBreedAdapter.ViewHolder> {

    private Context context;
    private List<BreedInfo> list;

    public PigBreedAdapter(Context context, List<BreedInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_title_text_info, parent, false);
        return new PigBreedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof ViewHolder) {
            holder.tv_name.setText(list.get(position).getDetail().get(0).getTag_name());
            holder.tv_content.setText(list.get(position).getDetail().get(0).getResult());
            holder.tv_name2.setText(list.get(position).getDetail().get(1).getTag_name());
            holder.tv_content2.setText(list.get(position).getDetail().get(1).getResult());
            holder.tv_categrey.setText(list.get(position).getBase_tag_name());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;
        TextView tv_name;
        TextView tv_content2;
        TextView tv_name2;
        TextView tv_categrey;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_name2 = itemView.findViewById(R.id.tv_title2);
            tv_content2 = itemView.findViewById(R.id.tv_content2);
            tv_categrey = itemView.findViewById(R.id.tv_categrey);
        }
    }
}
