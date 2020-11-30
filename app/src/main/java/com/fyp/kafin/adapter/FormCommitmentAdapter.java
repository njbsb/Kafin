package com.fyp.kafin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.model.Commitment;

import java.util.ArrayList;

public class FormCommitmentAdapter extends RecyclerView.Adapter<FormCommitmentAdapter.ViewHolder> {

    Context context;
    ArrayList<Commitment> commitments;

    @NonNull
    @Override
    public FormCommitmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_form_commitment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormCommitmentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
