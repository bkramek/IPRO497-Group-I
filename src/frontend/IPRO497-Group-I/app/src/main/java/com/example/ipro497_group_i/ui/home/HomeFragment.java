package com.example.ipro497_group_i.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.R;
import com.example.ipro497_group_i.databinding.FragmentHomeBinding;
import com.example.ipro497_group_i.ui.LocData;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeRV.RoomListener{

    private FragmentHomeBinding binding;
    private EditText search;
    HomeRV adapter;
    private RecyclerView listRV;
    private ArrayList<LocData> locDataArrayList = new ArrayList<LocData>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //Filler Data for Video
        locDataArrayList.add(new LocData("Ed Kaplan Building", "105", "Small, quiet, private study room"));
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
        locDataArrayList.add(new LocData("Wishnick Hall", "206", "Small, quiet, private study room"));

        listRV = (RecyclerView) root.findViewById(R.id.rv_home);
        listRV.setHasFixedSize(true);
        listRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new HomeRV(locDataArrayList, this);
        listRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
        locDataArrayList.get(position);

    }

    public class OnItemClickListener {
    }
    void filter(String text){
        ArrayList<LocData> temp = new ArrayList();
        for(LocData d: locDataArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBuilding().contains(text)){
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