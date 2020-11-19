package com.fyp.kafin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.model.Commitment;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {
    private ArrayList<Commitment> commitmentList;
    private LayoutInflater layoutInflater;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CommitmentAdapter(Context context, ArrayList<Commitment> commitmentList) {
        this.commitmentList = commitmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        layoutInflater = LayoutInflater.from(context);
//        View viewType = layoutInflater.inflate(R.layout.commitment_card, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commitment_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commitment commitment = commitmentList.get(position);
        holder.title.setText(commitment.getComName());
        holder.amount.setText(commitment.getComAmount());
    }

    @Override
    public int getItemCount() {
        return commitmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.commitment_title);
            amount = itemView.findViewById(R.id.commitment_amount);
        }
    }
}
