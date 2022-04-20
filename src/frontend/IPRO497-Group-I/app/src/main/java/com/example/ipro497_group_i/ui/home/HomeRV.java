package com.example.ipro497_group_i.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ipro497_group_i.R;
import com.example.ipro497_group_i.ui.LocData;
import java.util.ArrayList;

public class HomeRV extends RecyclerView.Adapter<HomeRV.ViewHolder>{
    private ArrayList<LocData> locDataArrayList = new ArrayList<>();
    private RoomListener roomListener;
    public CardView cv;


    public HomeRV(ArrayList<LocData> locDataArrayList, RoomListener roomListener){
        this.locDataArrayList = locDataArrayList;
        this.roomListener = roomListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search,parent,false);
        return new ViewHolder(item, roomListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String location = locDataArrayList.get(position).getBuilding() +", " +locDataArrayList.get(position).getRoom();
        holder.room.setText(locDataArrayList.get(position).getDesc());
        holder.loc.setText(location);

    }

    @Override
    public int getItemCount() { return locDataArrayList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView loc, room;
        ImageView roomLook;
        RoomListener roomListener;

        public ViewHolder(@NonNull View item, RoomListener roomListener) {
            super(item);
            // initializing each view of our recycler view.
            room = item.findViewById(R.id.room);
            loc = item.findViewById(R.id.loc);
            //roomLook - item.setImageResource(R.id.bimage);
            cv = item.findViewById(R.id.cr_search);
            cv.setOnClickListener(v -> {
                roomListener.onRoomClick(getAdapterPosition());
            });
            //this.officialListener = officialListener;

            //item.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            roomListener.onRoomClick(getAdapterPosition());
        }
    }

    public interface RoomListener{
        void onRoomClick(int position);
    }

    public void updateList(ArrayList<LocData> list){
        locDataArrayList = list;
        notifyDataSetChanged();
    }
}
