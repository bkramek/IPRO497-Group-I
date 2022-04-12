package com.example.ipro497_group_i.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipro497_group_i.databinding.FragmentGalleryBinding;
import com.example.ipro497_group_i.ui.admin.Admin;
import com.example.ipro497_group_i.ui.admin.ViewModal;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private RecyclerView RoomRV;
    private static final int ADD_ROOM_REQUEST = 1;
    private static final int EDIT_ROOM_REQUEST = 2;
    private ViewModal viewmodal;
    private View view;
    private PreviewView PV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //GalleryViewModel galleryViewModel =new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}