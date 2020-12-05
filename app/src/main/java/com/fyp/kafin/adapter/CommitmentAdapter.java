package com.fyp.kafin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.model.Commitment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {
    ArrayList<Commitment> commitmentList;
    Context context;
    Locale MY = new Locale("en", "MY");
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        final Commitment commitment = commitmentList.get(position);

        holder.title.setText(commitment.getComName());
        holder.amount.setText(moneyFormat(commitment.getComAmount()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure to delete this commitment?")
                        .setCancelable(true)
                        .setTitle("Delete commitment")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String commitmentName = commitment.getComName();
                                DatabaseReference comRef = dbRef.child("commitments").child(user.getUid()).child(commitment.getComID());
                                comRef.removeValue();
                                Toast.makeText(v.getContext(), String.format("Commitment (%s) deleted successfully", commitmentName), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commitmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.commitment_title);
            amount = itemView.findViewById(R.id.commitment_amount);
            card = itemView.findViewById(R.id.commitment_card);
        }
    }

    public String moneyFormat(float value) {
        return NumberFormat.getCurrencyInstance(MY).format(value);
    }
}
