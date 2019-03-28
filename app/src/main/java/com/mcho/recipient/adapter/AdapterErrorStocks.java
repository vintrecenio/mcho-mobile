package com.mcho.recipient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mcho.recipient.MainActivity;
import com.mcho.recipient.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterErrorStocks extends RecyclerView.Adapter<AdapterErrorStocks.ViewHolder> {

    private String err_msg;
    private MainActivity activity;

    public AdapterErrorStocks(String err_msg, MainActivity activity) {
        this.activity = activity;
        this.err_msg = err_msg;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stocks_error, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.error.setText(err_msg);
    }


    @Override
    public int getItemCount() {
        return 1; //must return one otherwise none item is shown
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button retry;
        TextView error;

        ViewHolder(View itemView) {
            super(itemView);

            retry = itemView.findViewById(R.id.btnRetry);
            error = itemView.findViewById(R.id.tvErrorMsg);

            retry.setOnClickListener(view -> {
                activity.retryGetStocks();
            });


        }
    }
}
