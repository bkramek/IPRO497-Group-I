package com.example.ipro497_group_i.ui.checkinout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.ipro497_group_i.MainActivity;
import com.example.ipro497_group_i.R;
import com.example.ipro497_group_i.databinding.FragmentCheckInOutBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CheckInOutFragment extends Fragment {

    private PreviewView PV;
    private ListenableFuture<ProcessCameraProvider> CPF;

    private static final int PERMISSION_CAMERA = 0;
    private FragmentCheckInOutBinding binding;

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("yes1");
            startCamera();
        } else {
            System.out.println("no1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("yes2");
                startCamera();
            } else {
                System.out.println("no2");
                return;
            }
        }
    }

    private void startCamera() {
        System.out.println("attempting to start camera");
        CPF.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = CPF.get();
                bindCameraPreview(cameraProvider);
                System.out.println("startCamera worked");
            } catch (ExecutionException | InterruptedException e) {
                System.out.println("Error starting camera " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this.getContext()));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        System.out.println("binding camera preview");
        PV.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(PV.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CheckInOutViewModel checkInOutViewModel = new ViewModelProvider(this).get(CheckInOutViewModel.class);

        binding = FragmentCheckInOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PV = root.findViewById(R.id.check_in_out_previewView);
        System.out.println(PV);
        CPF = ProcessCameraProvider.getInstance(this.getContext());
        requestCamera();

        final TextView textView = binding.textCheckInOut;
        checkInOutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}