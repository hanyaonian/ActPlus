package com.example.dell.actplus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/5/28.
 */

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private List<Map<String, Object>> optionList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageButton;
        TextView option_text;
        public ViewHolder(View view) {
            super(view);
            imageButton = (ImageView)view.findViewById(R.id.option_img);
            option_text = (TextView)view.findViewById(R.id.option_name);
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> temp = optionList.get(position);
        holder.option_text.setText((String)temp.get("optionName"));
        holder.imageButton.setImageResource((int)temp.get("optionImg"));
    }
    public OptionAdapter(List<Map<String, Object>> list) {
        this.optionList = list;
    }
    @Override
    public int getItemCount() {
        return optionList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}
