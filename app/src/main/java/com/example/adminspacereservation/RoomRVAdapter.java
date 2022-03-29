package com.example.adminspacereservation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RoomRVAdapter extends ListAdapter<Space, RoomRVAdapter.ViewHolder> {

    // creating a variable for on item click listener.
    private OnItemClickListener listener;

    // creating a constructor class for our adapter class.
    RoomRVAdapter() {
        super(DIFF_CALLBACK);
    }

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<Space> DIFF_CALLBACK = new DiffUtil.ItemCallback<Space>() {
        @Override
        public boolean areItemsTheSame(Space oldItem, Space newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Space oldItem, Space newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getRoomName().equals(newItem.getRoomName()) &&
                    oldItem.getRoomDescription().equals(newItem.getRoomDescription()) &&
                    oldItem.getRoomDuration().equals(newItem.getRoomDuration());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is use to inflate our layout
        // file for each item of our recycler view.
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_rv_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // below line of code is use to set data to
        // each item of our recycler view.
        Space model = getCourseAt(position);
        holder.roomNameTV.setText(model.getRoomName());
        holder.roomDescTV.setText(model.getRoomDescription());
        holder.roomDurationTV.setText(model.getRoomDuration());
    }

    // creating a method to get course modal for a specific position.
    public Space getCourseAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // view holder class to create a variable for each view.
        TextView roomNameTV, roomDescTV, roomDurationTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing each view of our recycler view.
            roomNameTV = itemView.findViewById(R.id.idTVRoomName);
            roomDescTV = itemView.findViewById(R.id.idTVRoomDescription);
            roomDurationTV = itemView.findViewById(R.id.idTVRoomDuration);

            // adding on click listener for each item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inside on click listener we are passing
                    // position to our item of recycler view.
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Space model);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
