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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SlideshowFragment extends Fragment implements SlideR.ResListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        adapter = new SlideR(resDataArrayList, this);

        db.collection("reservations").whereEqualTo("user_id", userId)
                .whereEqualTo("checked_in", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (resDataArrayList.size() < task.getResult().size()) {
                            db.collection("rooms").document(String.valueOf(doc.getLong("institution_id")) + "_" + String.valueOf(doc.getLong("room_id")))
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                    if (task2.isSuccessful()) {
                                        Log.v(TAG, "adding reservation");
                                        resDataArrayList.add(new ReserveData(
                                                "",
                                                String.valueOf(new Date(doc.getLong("time_start") * 1000L)),
                                                task2.getResult().getString("building_name"),
                                                task2.getResult().getString("room_number")
                                        ));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        rv = (RecyclerView) root.findViewById(R.id.reserve_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        /*rv = root.findViewById(R.id.reserve_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);*/
        //adapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                ReserveData resData = resDataArrayList.get(position);
                resDataArrayList.remove(position);
                adapter.notifyDataSetChanged();

                Date tempDate = new Date(resData.getDate());

                //Toast.makeText(getContext(), String.valueOf(tempDate.getTime()), Toast.LENGTH_SHORT).show();

                db.collection("reservations").whereEqualTo("user_id", userId)
                        .whereEqualTo("time_start", tempDate.getTime() / 1000).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    db.collection("reservations").document(task.getResult().getDocuments().get(0).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                }
                            }
                        });

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