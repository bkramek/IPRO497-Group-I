package com.example.ipro497_group_i.ui.checkinout;

import android.Manifest;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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

    private final String TAG = "CheckInOut";
    private PreviewView PV;
    private ListenableFuture<ProcessCameraProvider> CPF;

    private static final int PERMISSION_CAMERA = 0;
    private FragmentCheckInOutBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void errorPopup(String title, String msg) {
        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(getContext());
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

    private void requestCamera() {
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
        Log.v(TAG, "attempting to start camera");
        CPF.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = CPF.get();
                bindCameraPreview(cameraProvider);
                Log.v(TAG, "startCamera worked");
            } catch (ExecutionException | InterruptedException e) {
                Log.v(TAG, "Error starting camera " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this.getContext()));
    }

    long lastScan = 0;

    public void handleCheckInOut(String qrc) {
        if (System.currentTimeMillis() - 3000 > lastScan) {
            lastScan = System.currentTimeMillis();
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
                        Log.v(TAG, "resTask successful");
                        Log.v(TAG, "resTask successful");
                        for (QueryDocumentSnapshot document : resTask.getResult()) {
                            long currentTime = System.currentTimeMillis();
                            // Get the data for the room that the user is currently trying to check in/out of
                            Map<String, Object>[] roomData = new Map[]{null};
                            Log.v(TAG, "Getting room: " + document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                            DocumentReference roomDoc = db.collection("rooms").document(document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                            Log.v(TAG, "Listening for document get complete");
                            roomDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.v(TAG, "task complete");
                                    if (task.isSuccessful()) {
                                        Log.v(TAG, "roomTask successful");
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            Log.v(TAG, "room doc exists");
                                            roomData[0] = doc.getData();
                                            Log.v(TAG, "got document data");

                                            Log.v(TAG, "Getting room: " + document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                                            DocumentReference roomDoc = db.collection("rooms").document(document.getLong("institution_id").toString() + "_" + document.getLong("room_id").toString());
                                            Log.v(TAG, "Listening for document get complete");
                                            roomDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    Log.v(TAG, "task complete");
                                                    if (task.isSuccessful()) {
                                                        Log.v(TAG, "roomTask successful");
                                                        DocumentSnapshot doc = task.getResult();
                                                        if (doc.exists()) {
                                                            Log.v(TAG, "room doc exists");
                                                            roomData[0] = doc.getData();
                                                            Log.v(TAG, "got document data");
                                                            if (!document.getBoolean("checked_in") && currentTime >= ((document.getLong("time_start") - 600) * 1000)) {
                                                                // Not checked in and within 10 minutes of scheduled check in time
                                                                db.collection("reservations").document(document.getId()).update("checked_in", true);
                                                                db.collection("reservations").document(document.getId()).update("check_in_time", currentTime / 1000);
                                                                //Toast.makeText(getContext(), "Checked in to " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"), Toast.LENGTH_SHORT).show();
                                                                errorPopup("Check In Successful!", "Successfully checked in to " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"));
                                                            } else if (document.getBoolean("checked_in") && !document.getBoolean("checked_out")) {
                                                                // Already checked in, check out now
                                                                db.collection("reservations").document(document.getId()).update("checked_out", true);
                                                                db.collection("reservations").document(document.getId()).update("check_out_time", currentTime / 1000);
                                                                Log.v(TAG, "hello");
                                                                //Toast.makeText(getContext(), "Checked out of " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"), Toast.LENGTH_SHORT).show();
                                                                errorPopup("Check Out Successful!", "Successfully checked out of " + roomData[0].get("building_name") + " " + roomData[0].get("room_number"));
                                                            } else if (document.getBoolean("checked_in") && document.getBoolean("checked_out")) {
                                                                errorPopup("Reservation Not Found!", "Sorry, you don't have a current reservation for this room.");
                                                            } else {
                                                                // Not checked in, but too early to check in right now
                                                                //Toast.makeText(getContext(), "Too early to check in", Toast.LENGTH_SHORT).show();
                                                                errorPopup("Too Early to Check In", "Sorry, the earliest you can check in is 10 minutes before your reserved time.");
                                                            }
                                                        } else {
                                                            Log.v(TAG, "room doc does NOT exist");
                                                        }
                                                    } else {
                                                        Log.v(TAG, "resTask NOT successful");
                                                        Log.v(TAG, "room doc does NOT exist");
                                                    }
                                                }
                                            });

                                        }
                                    } else {
                                        Toast.makeText(getContext(), "resTask not successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                }
            });
        }
    }


    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Log.v(TAG, "binding camera preview");
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
        Log.v(TAG, String.valueOf(PV));
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