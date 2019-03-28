package com.mcho.recipient.adapter;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mcho.recipient.DispatchActivity;
import com.mcho.recipient.MainActivity;
import com.mcho.recipient.R;
import com.mcho.recipient.model.Stocks;
import com.mcho.recipient.utils.Global;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterStocks extends RecyclerView.Adapter<AdapterStocks.ViewHolder> {

    private ArrayList<Stocks> stocks;
    private Context context;
    private MainActivity activity;

    public AdapterStocks(Context context, ArrayList<Stocks> stocks, MainActivity activity) {
        this.context = context;
        this.stocks = stocks;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int tmpStock = stocks.get(position).getStock();
        String tmpUnit = stocks.get(position).getUnit();
        Character lastChar = tmpUnit.charAt(tmpUnit.length() - 1);

        holder.description.setText(stocks.get(position).getDescription());
        holder.stock.setText(String.valueOf(stocks.get(position).getStock()));
        holder.unit.setText(tmpStock > 1 ? (lastChar.equals("e") ? tmpUnit+"s" : tmpUnit+"es") : tmpUnit);

        Glide.with(context)
                .load(stocks.get(position).getCategory().equals("medicine") ? Global.createAvatarMedicine(context) : Global.createAvatarSupply(context))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DispatchActivity.class);
                i.putExtra("item", stocks.get(position).getItemId());
                i.putExtra("description", stocks.get(position).getDescription());
                i.putExtra("stock", stocks.get(position).getStock());
                i.putExtra("unit", stocks.get(position).getUnit());
                i.putExtra("category", stocks.get(position).getCategory());

                Pair<View, String> pair1 = Pair.create(holder.avatar, holder.avatar.getTransitionName());
//                Pair<View, String> pair2 = Pair.create(holder.description, holder.description.getTransitionName());

                ActivityOptions options = null;

                options = ActivityOptions.makeSceneTransitionAnimation(activity, pair1);
                activity.startActivity(i, options.toBundle());

            }
        });

    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDescription) TextView description;
        @BindView(R.id.tvStock) TextView stock;
        @BindView(R.id.tvUnit) TextView unit;
        @BindView(R.id.ivAvatar) ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }


}
