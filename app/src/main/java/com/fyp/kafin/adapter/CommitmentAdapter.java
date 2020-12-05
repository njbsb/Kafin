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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {
    ArrayList<Commitment> commitmentList;
    Context context;
    static DecimalFormat df2 = new DecimalFormat("#.##");
    Locale MY = new Locale("en", "MY");

    public CommitmentAdapter(Context context, ArrayList<Commitment> commitmentList) {
        this.commitmentList = commitmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commitment_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commitment commitment = commitmentList.get(position);

        holder.title.setText(commitment.getComName());
        holder.amount.setText(moneyFormat(commitment.getComAmount()));
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

    public String moneyFormat(float value) {
        return NumberFormat.getCurrencyInstance(MY).format(value);
    }
}
