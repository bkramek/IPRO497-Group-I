package com.example.ipro497_group_i.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.DataBaseViewModal;
import com.example.ipro497_group_i.MainActivity;
import com.example.ipro497_group_i.R;
import com.example.ipro497_group_i.databinding.FragmentHomeBinding;
import com.example.ipro497_group_i.ui.CustomTimePickerDialog;
import com.example.ipro497_group_i.ui.LocData;
import com.example.ipro497_group_i.ui.OnSwipeTouchListener;
import com.example.ipro497_group_i.ui.checkinout.CheckInOutFragment;
import com.example.ipro497_group_i.ui.gallery.GalleryFragment;
import com.example.ipro497_group_i.ui.slideshow.ReserveData;
import com.example.ipro497_group_i.ui.slideshow.SlideshowFragment;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements HomeRV.RoomListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "HomeFrag";
    Calendar date;
    private DataBaseViewModal viewModel;
    private Long userId;
    CheckInOutFragment qrFrag = new CheckInOutFragment();
    SlideshowFragment reserveFrag = new SlideshowFragment();
    BottomNavigationView bottomNavigationView;
    private FragmentHomeBinding binding;
    private EditText search;
    HomeRV adapter;
    private RecyclerView listRV;
    private ArrayList<LocData> locDataArrayList = new ArrayList<LocData>();

    public void errorPopup(String title, String msg) {
        androidx.appcompat.app.AlertDialog.Builder aDBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        aDBuilder.setTitle(title);
        aDBuilder.setMessage(msg);
        aDBuilder.setIcon(R.drawable.ic_baseline_email_24);
        aDBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = aDBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModal.class);

        userId = viewModel.getMyUserId();
        viewModel.setMyUserId(userId);
        Log.d(TAG, "User ID: "+userId);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Filler Data for Video
        /*locDataArrayList.add(new LocData("Ed Kaplan Building", "105", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Ed Kaplan Building", "105", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Ed Kaplan Building", "107", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Ed Kaplan Building", "201", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Ed Kaplan Building", "205", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Galvin Library", "001", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Galvin Library", "004", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Galvin Library", "103", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Galvin Library", "108", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Hermann Hall", "001", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Hermann Hall", "003", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Hermann Hall", "004", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Hermann Hall", "105", "Large, collaborative room"));
        locDataArrayList.add(new LocData("John T. Rettaliata Engineering Center", "111", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("John T. Rettaliata Engineering Center", "214", "Large, collaborative room"));
        locDataArrayList.add(new LocData("John T. Rettaliata Engineering Center", "114", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("John T. Rettaliata Engineering Center", "217", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("McCormick Tribune Campus Center", "115", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("McCormick Tribune Campus Center", "112", "Large, collaborative room"));
        locDataArrayList.add(new LocData("McCormick Tribune Campus Center", "103", "Large, collaborative room"));
        locDataArrayList.add(new LocData("McCormick Tribune Campus Center", "105", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Perlstein Hall", "123", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Perlstein Hall", "104", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Perlstein Hall", "102", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Perlstein Hall", "201", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Robert A. Pritzker Science Center", "203", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Robert A. Pritzker Science Center", "205", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Robert A. Pritzker Science Center", "212", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Siegel Hall", "105", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Siegel Hall", "103", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Siegel Hall", "312", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Stuart Building", "212", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Stuart Building", "204", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Stuart Building", "105", "Large, collaborative room"));
        locDataArrayList.add(new LocData("Wishnick Hall", "315", "Small, quiet, private study room"));
        locDataArrayList.add(new LocData("Wishnick Hall", "206", "Small, quiet, private study room"));*/

        adapter = new HomeRV(locDataArrayList, this);

        db.collection("rooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (locDataArrayList.size() < task.getResult().size()) {
                            locDataArrayList.add(new LocData(
                                    doc.getString("building_name"),
                                    doc.getString("room_number"),
                                    doc.getString("room_description")
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        listRV = (RecyclerView) root.findViewById(R.id.rv_home);
        listRV.setHasFixedSize(true);
        listRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        listRV.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        search = (EditText) root.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        return root;
    }

    @Override
    public void onRoomClick(int position) {
        LocData roomData = locDataArrayList.get(position);

        new SingleDateAndTimePickerDialog.Builder(getContext())
                .backgroundColor(Color.WHITE)
                .mainColor(Color.BLUE)
                .titleTextColor(Color.WHITE)
                //.bottomSheet()
                //.curved()
                //.stepSizeMinutes(15)
                //.displayHours(false)
                .displayMinutes(false)
                //.todayText("aujourd'hui")
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        // Retrieve the SingleDateAndTimePicker
                    }

                    public void onClosed(SingleDateAndTimePicker picker) {
                        // On dialog closed
                    }
                })
                .title("Choose Date and Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {

                        db.collection("reservations").whereEqualTo("room_id", position + 1)
                                .whereEqualTo("time_start", (date.getTime() / 1000) - 3300)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() == 0) {
                                        Map<String, Object> reservationData = new HashMap<>();
                                        reservationData.put("time_start", (date.getTime() / 1000) - 3300);
                                        reservationData.put("time_end", (date.getTime() / 1000) + 300);
                                        reservationData.put("checked_in", false);
                                        reservationData.put("checked_out", false);
                                        reservationData.put("institution_id", 1);
                                        reservationData.put("rating_submitted", false);
                                        reservationData.put("room_id", position + 1);
                                        reservationData.put("check_in_time", -1);
                                        reservationData.put("check_out_time", -1);
                                        reservationData.put("user_id", userId);

                                        db.collection("reservations").document().set(reservationData);
                                        errorPopup("Registration Confirmed!", "Your reservation for " + roomData.getBuilding() + ", " + roomData.getRoom() + " is confirmed!");
                                    } else {
                                        errorPopup("Registration Unavailable!", roomData.getBuilding() + ", " + roomData.getRoom() + " already has a registration at this date and time.");
                                    }
                                }
                            }
                        });
                    }
                }).display();
    }

    public class OnItemClickListener {
    }

    void filter(String text){
        ArrayList<LocData> temp = new ArrayList();
        for(LocData d: locDataArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBuilding().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}