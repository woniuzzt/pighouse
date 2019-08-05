package com.example.timple.zdpigapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.entity.PigHouseEntity;
import com.example.timple.zdpigapp.ui.activity.scan.ScanEarTagActivity;
import com.example.timple.zdpigapp.utils.SPUtils;

import java.util.List;

public class PigHouseAdapter extends RecyclerView.Adapter<PigHouseAdapter.ViewHolder> {
    private Context context;
    private List<PigHouseEntity> list;
    private int type = 0;

    public PigHouseAdapter(Context context, List<PigHouseEntity> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pighouse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof ViewHolder) {
            holder.btn_mzs.setText(list.get(position).getName());
            holder.btn_mzs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtils.getInstance().put("houseId", list.get(position).getId());
                    Intent intent = new Intent(context, ScanEarTagActivity.class);
                    intent.putExtra("type", type + "");
                    if (type == 0)
                        intent.putExtra("name", list.get(position).getName());
                    else if (type == 1)
                        intent.putExtra("name", "转群 → " + list.get(position).getName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_mzs;

        public ViewHolder(View itemView) {
            super(itemView);
            btn_mzs = itemView.findViewById(R.id.btn_mzs);
        }
    }
}
