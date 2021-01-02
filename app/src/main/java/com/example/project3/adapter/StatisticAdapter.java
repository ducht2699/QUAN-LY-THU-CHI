package com.example.project3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.R;
import com.example.project3.model.StatisticType;

import java.util.ArrayList;
import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private List<StatisticType> statisticTypes;

    public StatisticAdapter() {
    }

    public StatisticAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        this.statisticTypes = new ArrayList<>();
        statisticTypes.add(new StatisticType("Tài chính hiện tại", R.drawable.ic_current_finance));
        statisticTypes.add(new StatisticType("Tình hình thu chi", R.drawable.ic_ie_situation));
        statisticTypes.add(new StatisticType("Phân tích chi tiêu", R.drawable.ic_ie_analysis));
        statisticTypes.add(new StatisticType("Phân tích thu", R.drawable.ic_income_analysis));
        statisticTypes.add(new StatisticType("Theo dõi vay nợ", R.drawable.ic_dept_loan));
        statisticTypes.add(new StatisticType("Đối tượng thu chi", R.drawable.ic_ie_object));
        statisticTypes.add(new StatisticType("Chuyến đi/sự kiện", R.drawable.ic_event));
        statisticTypes.add(new StatisticType("Phân tích tài chính", R.drawable.ic_financial_analytics));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new StatisticAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imgAvatarItem.setImageResource(statisticTypes.get(position).getTypeImage());
        holder.tvName.setText(statisticTypes.get(position).getTypeName());
        holder.relativeItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.imgAvatarItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, statisticTypes.get(position).getTypeName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return statisticTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imgAvatarItem;
        private RelativeLayout relativeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textList);
            imgAvatarItem = itemView.findViewById(R.id.img_avatarItem);
            relativeItem = itemView.findViewById(R.id.relative_item);
        }
    }
}
