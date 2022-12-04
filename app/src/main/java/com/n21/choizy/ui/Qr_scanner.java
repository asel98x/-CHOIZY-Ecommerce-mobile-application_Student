package com.n21.choizy.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.n21.choizy.R;
import com.n21.choizy.ui.payment.PaymentFragViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Qr_scanner extends Fragment {

    private ExecutorService cameraExucuter;
    private PreviewView cameraPreview;
    private ListenableFuture qrCameraListener;
    private PaymentFragViewModel viewModel;
    private Runnable myrun;
    private NavController navController;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    public Qr_scanner() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_qr_scanner, container, false);

        cameraPreview = view.findViewById(R.id.qrCameraPreview);
        cameraExucuter = Executors.newSingleThreadExecutor();
        qrCameraListener = ProcessCameraProvider.getInstance(getContext());
        viewModel = new ViewModelProvider(requireActivity()).get(PaymentFragViewModel.class);
        navController = NavHostFragment.findNavController(this);


        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    for (boolean granted : result.values()) {
                        if(!granted){
                            Toast.makeText(getContext(), "Permission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        myrun = new Runnable() {
            @Override
            public void run() {
                navController.popBackStack(R.id.paymentFragment,false);
            }
        };

        qrCameraListener.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != (PackageManager.PERMISSION_GRANTED)) {
                        requestPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA});
                    }
                    ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) qrCameraListener.get();
                    Startpreview(processCameraProvider);


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));



        return view;
    }

    private void Startpreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1200,720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExucuter, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                scanImage(image);
            }
        });
        try {
            cameraProvider.unbindAll();

            cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageAnalysis);

        }catch (Exception e){

        }

    }



    private void scanImage(ImageProxy image){
        @SuppressLint({"UnsafeOptInUsageError"}) Image image1 = image.getImage();
        assert image1 != null;
        InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC).build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        Task<List<Barcode>> result =scanner.process(inputImage);
        result.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
            @Override
            public void onSuccess(@NonNull List<Barcode> barcodes) {
                for (Barcode barcode : barcodes) {
                    String[] qrCode = barcode.getRawValue().split(",");

                    if(qrCode.length == 4){
                        if (qrCode[0].equals("ChoizyPayment")) {
                           // viewModel.setmText(qrCode);
                            System.out.println("Going back .............\n\n\n\n");
                            navController.getPreviousBackStackEntry().getSavedStateHandle().set("PaymentDetails", qrCode);
//                            Navigation.findNavController(getView()).navigate(Qr_scannerDirections.actionQrScannerToPaymentFragment());
//                            NavController navController = NavHostFragment.findNavController(Qr_scanner.this);
//                            navController.navigateUp();
                            myrun.run();

                            return;
                        }
                        break;
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
            @Override
            public void onComplete(@NonNull Task<List<Barcode>> task) {
                image.close();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

    }



}