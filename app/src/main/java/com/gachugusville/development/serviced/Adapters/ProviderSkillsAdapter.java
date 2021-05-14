package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Skills;

import java.util.List;

public class ProviderSkillsAdapter extends RecyclerView.Adapter<ProviderSkillsAdapter.SkillViewHolder> {

    List<Skills> skillsList;
    Context context;

    public ProviderSkillsAdapter(List<Skills> skillsList, Context context) {
        this.skillsList = skillsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProviderSkillsAdapter.SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.skills_view_layout, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderSkillsAdapter.SkillViewHolder holder, int position) {
        holder.txt_skill_name.setText(skillsList.get(position).getSkillName());
        holder.txt_payValue.setText(String.format("$%s", skillsList.get(position).getPayValue()));
        holder.txt_payDurationType.setText(skillsList.get(position).getPayDurationType());
        holder.txt_experience.setText(String.valueOf(skillsList.get(position).getExperience()));
        holder.txt_num_of_jobs.setText(String.valueOf(skillsList.get(position).getNum_of_jobs()));

    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView txt_skill_name, txt_payValue, txt_payDurationType,
                txt_experience, txt_num_of_jobs;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_skill_name = itemView.findViewById(R.id.txt_skill_name);
            txt_payValue = itemView.findViewById(R.id.txt_payValue);
            txt_payDurationType = itemView.findViewById(R.id.txt_payDurationType);
            txt_experience = itemView.findViewById(R.id.txt_experience);
            txt_num_of_jobs = itemView.findViewById(R.id.txt_num_of_jobs);


        }
    }
}
