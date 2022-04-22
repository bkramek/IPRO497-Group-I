package com.example.ipro497_group_i.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.databinding.AdminPageBinding;

public class GalleryFragment extends Fragment {

    private AdminPageBinding binding;
    private RecyclerView RoomRV;
    private static final int ADD_ROOM_REQUEST = 1;
    private static final int EDIT_ROOM_REQUEST = 2;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = AdminPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}