package com.example.ipro497_group_i.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.DataBaseViewModal;
import com.example.ipro497_group_i.MainActivity;
import com.example.ipro497_group_i.R;
import com.example.ipro497_group_i.databinding.ReservationPageBinding;
import com.example.ipro497_group_i.ui.LocData;
import com.example.ipro497_group_i.ui.home.HomeFragment;
import com.example.ipro497_group_i.ui.home.HomeRV;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment implements SlideR.ResListener{

    private static final String TAG = "SlideshowFragment";
    private DataBaseViewModal viewModel;
    private ReservationPageBinding binding;
    private Long userId;
    private RecyclerView rv;
    SlideR adapter;
    private ArrayList<ReserveData> resDataArrayList = new ArrayList<ReserveData>();
    private Object MainActivity;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModal.class);

        userId = viewModel.getMyUserId();
        Log.d(TAG, ""+userId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = ReservationPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv = (RecyclerView) root.findViewById(R.id.reserve_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SlideR(resDataArrayList, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                resDataArrayList.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Reservation Cancelled", Toast.LENGTH_LONG).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResClick(int position) {
        adapter.notifyDataSetChanged();
    }

    public void addElement(ReserveData element) {
        resDataArrayList.add(element);
    }

}