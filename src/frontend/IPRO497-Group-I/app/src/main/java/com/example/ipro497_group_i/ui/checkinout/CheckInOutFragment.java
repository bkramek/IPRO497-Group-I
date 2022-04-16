package com.example.ipro497_group_i.ui.checkinout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Date.*;
import java.util.regex.*;

public class CheckInOutFragment extends Fragment {

    private PreviewView PV;
    private ListenableFuture<ProcessCameraProvider> CPF;
    private static final String TAG = "CheckInOut";
    private static final int PERMISSION_CAMERA = 0;
    private FragmentCheckInOutBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void requestCamera() {
        handleCheckInOut("inst_000001_rm_000001");
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
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
                startCamera();
            } else {
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

    boolean handlingQRC = false;

    public void handleCheckInOut(String qrc) {
        if (!handlingQRC) {
            handlingQRC = true;
        } else {
            return;
        }
        // Temporary for demo purposes, REMOVE IN FINAL PRODUCT
        //Toast.makeText(getContext(), qrc, Toast.LENGTH_SHORT).show();
        if (Pattern.matches("^inst_[0-9]{6}_rm_[0-9]{6}$", qrc)) {
            // QR code is in a valid format (inst_######_rm_######)
            String instId = qrc.substring(5, 11);
            String rmId = qrc.substring(15);
            //Toast.makeText(getContext(), "valid qr code", Toast.LENGTH_SHORT).show();
            db.collection("reservations").whereEqualTo("institution_id", Integer.parseInt(instId))
                    .whereEqualTo("room_id", Integer.parseInt(rmId))
                    .whereEqualTo("user_id", 1) // ONLY FOR TESTING PURPOSES, USE THE ACTUAL USER ID ONCE IMPLEMENTED
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> resTask) {
                    if (resTask.isSuccessful()) {
                        Log.v(TAG,"resTask successful");
                        for (QueryDocumentSnapshot document : resTask.getResult()) {
                            long currentTime = System.currentTimeMillis();
                            // Get the data for the room that the user is currently trying to check in/out of
                            Map<String, Object>[] roomData = new Map[]{null};
                            Log.v(TAG,"Getting room: " + document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                            DocumentReference roomDoc = db.collection("rooms").document(document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                            Log.v(TAG,"Listening for document get complete");
                            roomDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.v(TAG,"task complete");
                                    if (task.isSuccessful()) {
                                        Log.v(TAG,"roomTask successful");
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            Log.v(TAG, "room doc exists");
                                            roomData[0] = doc.getData();
                                            Log.v(TAG, "got document data");
                                            if (!document.getBoolean("checked_in") && (document.getLong("time_start") * 1000) >= currentTime - 600000) {
                                                // Not checked in and within 10 minutes of scheduled check in time
                                                db.collection("reservations").document(document.getId()).update("checked_in", true);
                                                db.collection("reservations").document(document.getId()).update("check_in_time", currentTime / 1000);
                                                //Toast.makeText(getContext(), "Checked in to " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"), Toast.LENGTH_SHORT).show();
                                            } else if (document.getBoolean("checked_in") == true) {
                                                // Already checked in, check out now
                                                db.collection("reservations").document(document.getId()).update("checked_out", true);
                                                db.collection("reservations").document(document.getId()).update("check_out_time", currentTime / 1000);
                                                Log.v(TAG, "hello");
                                                //Toast.makeText(getContext(), "Checked out of " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"), Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Not checked in, but too early to check in right now
                                                //Toast.makeText(getContext(), "Too early to check in", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.v(TAG, "room doc does NOT exist");
                                        }
                                    } else {
                                        Log.v(TAG, "resTask NOT successful");
                                    }
                                }
                            });

                        }
                    } else {
                        //Toast.makeText(getContext(), "resTask not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // QR code is not in a valid format
            //Toast.makeText(getContext(), "Invalid QR code, please try again", Toast.LENGTH_SHORT).show();
        }
        handlingQRC = false;
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

        ImageAnalysis imgAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imgAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this.getContext()), new QRScanner(new QRListener() {
            @Override
            public void onQRCodeFound(String _qrc) {
                handleCheckInOut(_qrc);
            }

            @Override
            public void qrCodeNotFound() {
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imgAnalysis, preview);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CheckInOutViewModel checkInOutViewModel = new ViewModelProvider(this).get(CheckInOutViewModel.class);

        binding = FragmentCheckInOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PV = root.findViewById(R.id.check_in_out_previewView);
        System.out.println(PV);
        CPF = ProcessCameraProvider.getInstance(this.getContext());
        requestCamera();

        //final TextView textView = binding.textCheckInOut;
        //checkInOutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}