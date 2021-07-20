package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.User.JobPlacementActivity;
import com.gachugusville.development.serviced.Utils.Provider;
import com.gachugusville.development.serviced.Utils.Skills;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ProviderSkillsAdapter extends RecyclerView.Adapter<ProviderSkillsAdapter.SkillViewHolder> {

    List<Skills> skillsList;
    Context context;
    Provider provider;
    String distance;

    public ProviderSkillsAdapter(List<Skills> skillsList, Context context, Provider provider, String distance) {
        this.skillsList = skillsList;
        this.context = context;
        this.provider = provider;
        this.distance = distance;
    }

    @NonNull
    @Override
    public ProviderSkillsAdapter.SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.skills_view_layout, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderSkillsAdapter.SkillViewHolder holder, int position) {
        holder.skillCard_layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, JobPlacementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("skill_data", skillsList.get(position));
            bundle.putSerializable("provider_details", provider);
            bundle.putString("distance", distance);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        holder.txt_skill_name.setText(skillsList.get(position).getSkill_identity());
        holder.txt_payValue.setText(String.format("$%s", skillsList.get(position).getSkill_price()));
        holder.txt_payDurationType.setText(skillsList.get(position).getSkill_pay_mode());
        holder.txt_num_of_jobs.setText(String.valueOf(skillsList.get(position).getSkill_jobs_number()));

    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView skillCard_layout;
        TextView txt_skill_name, txt_payValue, txt_payDurationType,
                txt_experience, txt_num_of_jobs;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            skillCard_layout = itemView.findViewById(R.id.skillCard_layout);
            txt_skill_name = itemView.findViewById(R.id.txt_skill_name);
            txt_payValue = itemView.findViewById(R.id.txt_payValue);
            txt_payDurationType = itemView.findViewById(R.id.txt_payDurationType);
            txt_experience = itemView.findViewById(R.id.txt_experience);
            txt_num_of_jobs = itemView.findViewById(R.id.txt_num_of_jobs);


        }
    }
}
