package com.gineuscrypticalsoft.busticketsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.model.CarListModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    Context context;
    List<CarListModel> carListModel;
    public CarListAdapter(Context context, List<CarListModel> carListModel){
        this.context= context;
        this.carListModel= carListModel;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cal_list_item, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarListModel item= carListModel.get(position);
        holder.carItemName.setText(item.getCarName());
        holder.carItemPrice.setText(item.getSeatRent()+" TK");
        holder.carItemAcNon.setText(item.getAcType());
        holder.carItemDTime.setText(item.getStartTime());
        holder.carItemATime.setText(item.getEndTime());
        holder.carItemFrom.setText("From: "+item.getFromCity());
        holder.carItemTo.setText("To: "+item.getToCity());
        Picasso.get().load(item.getImage()).into(holder.carItemImage);
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "get_id: "+carListModel.size());
        return carListModel.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {

        TextView carItemName, carItemPrice, carItemAcNon, carItemDTime, carItemATime, carItemFrom, carItemTo;
        ImageView carItemImage;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carItemName= itemView.findViewById(R.id.car_item_name);
            carItemPrice= itemView.findViewById(R.id.car_item_price);
            carItemAcNon= itemView.findViewById(R.id.car_item_acorno);
            carItemDTime= itemView.findViewById(R.id.car_item_d_time);
            carItemATime= itemView.findViewById(R.id.car_item_a_time);
            carItemFrom= itemView.findViewById(R.id.car_item_from);
            carItemTo= itemView.findViewById(R.id.car_item_to);
            carItemImage= itemView.findViewById(R.id.car_item_image);
        }
    }
}
