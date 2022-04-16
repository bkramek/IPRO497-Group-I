package com.example.ipro497_group_i.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.R;

import java.util.ArrayList;

public class SlideR extends RecyclerView.Adapter<SlideR.ViewHolder>{
    private ArrayList<ReserveData> resDataArrayList = new ArrayList<>();
    private SlideR.ResListener resListener;
    public CardView building;


    public SlideR(ArrayList<ReserveData> resDataArrayList, SlideR.ResListener resListener){
        this.resDataArrayList = resDataArrayList;
        this.resListener = resListener;
    }

    @NonNull
    @Override
    public SlideR.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_reserve,parent,false);
        return new SlideR.ViewHolder(item, resListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideR.ViewHolder holder, int position) {
        holder.builder.setText(resDataArrayList.get(position).getBuilding());
        holder.timer.setText(resDataArrayList.get(position).getTime());
        holder.date.setText(resDataArrayList.get(position).getDate());
        holder.room.setText(resDataArrayList.get(position).getRoom());


    }

    @Override
    public int getItemCount() { return resDataArrayList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView builder, room, timer, date;
        ImageView roomLook;
        SlideR.ResListener resListener;

        public ViewHolder(@NonNull View item, SlideR.ResListener roomListener) {
            super(item);
            // initializing each view of our recycler view.
            room = item.findViewById(R.id.roomy);
            builder = item.findViewById(R.id.build);
            timer = item.findViewById(R.id.time);
            date = item.findViewById(R.id.myDate);
            //roomLook - item.setImageResource(R.id.bimage);
            building = item.findViewById(R.id.building);

            /*building.setOnClickListener(v -> {
                roomListener.onResClick(getAdapterPosition());
            });*/
            //this.officialListener = officialListener;

            //item.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            resListener.onResClick(getAdapterPosition());
        }
    }

    public interface ResListener{
        void onResClick(int position);
    }

}