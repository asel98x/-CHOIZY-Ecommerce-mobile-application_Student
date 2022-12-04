package com.n21.choizy.ui.Choizymap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.SearchBranchAdapter;
import com.n21.choizy.model.Branch;
import com.n21.choizy.model.Company;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoizyMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoizyMap extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private StudentDB_Helper db_helper;
    private ExecutorService executors;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION, COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private SupportMapFragment mapFragment;
    private static GoogleMap map;
    private AutoCompleteTextView searchTxt;
    private SearchBranchAdapter searchAdapter;
    private FusedLocationProviderClient location;

    public ChoizyMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoizyMap.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoizyMap newInstance(String param1, String param2) {
        ChoizyMap fragment = new ChoizyMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choizy_map, container, false);
        db_helper = new StudentDB_Helper(getContext());
        executors = Executors.newSingleThreadExecutor();


        searchTxt = view.findViewById(R.id.SearchBranchTxt);
        searchAdapter = new SearchBranchAdapter(getContext(), 0);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Iterator<Boolean> iterator = result.values().iterator();

                    while (iterator.hasNext()) {
                        boolean granted = iterator.next();
                        if (!granted) {
                            Toast.makeText(getContext(), "Permission failed", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!iterator.hasNext()) {
                                getCurrentLocation();
                            }
                        }
                    }
                });


        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(new String[]{FINE_LOCATION, COARSE_LOCATION});
        }

        location = LocationServices.getFusedLocationProviderClient(getContext());


        searchTxt.setAdapter(searchAdapter);
        searchAdapter.setNotifyOnChange(true);
        searchTxt.setOnItemClickListener(this);


        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.ChoizyMape_Map, mapFragment)
                    .commit();
            mapFragment.getMapAsync(this);
        }


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                searchTxt.clearFocus();
            }
        });


        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.my_map_style));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }
        getCurrentLocation();
        db_helper.getCompanyList().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Company> companies = new ArrayList<>();

                companies.clear();
                for (DataSnapshot one : snapshot.getChildren()) {
                    Company comp = one.getValue(Company.class);
                    comp.setKey(one.getKey());
                    companies.add(comp);
                }
                searchAdapter.clear();
                if (companies.size() > 0) {
                    for (int i = 0; i < companies.size(); i++) {
                        int finalI = i;
                        db_helper.getBranchList(companies.get(i).getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                db_helper.removeEventListener(this);

                                for (DataSnapshot one : snapshot.getChildren()) {
                                    Branch branch = one.getValue(Branch.class);
                                    branch.setID(one.getKey());
                                    searchAdapter.add(branch);
                                    LatLng latLng = new LatLng(branch.getLatitude(), branch.getLongitude());


                                    if (!executors.isShutdown()) {
                                        executors.submit(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!executors.isShutdown()) {
                                                    try {
                                                        Bitmap bitmap = BitmapFactory.decodeStream(new URL(companies.get(finalI).getImageURL()).openStream());
                                                        updateUI(latLng, bitmap);
                                                    } catch (IOException e) {
                                                        // ignore the because of the thread is kill it will give error ignor that

                                                        System.out.println(e);
                                                    }
                                                } else {

                                                    Thread.currentThread().interrupt();

                                                }


                                            }
                                        });
                                    }


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        com.google.android.gms.tasks.Task<Location> locationTask = location.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(Location location) {
                if(location == null){
                    Toast.makeText(getContext(), "Location Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My location");
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                //  map.addMarker(markerOptions);
            }
        });
    }

    private void updateUI(LatLng latLng,Bitmap bitmap){
        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap,150,100,false);
        getView().post(new Runnable() {
            @Override
            public void run() {
                map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(finalBitmap)));
            }
        });
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(map != null){

            Branch branch = searchAdapter.getItem(i);
            LatLng latLng = new LatLng(branch.getLatitude(),branch.getLongitude());

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        }
    }

    @Override
    public void onStop() {
        if(executors != null){
//            executors.;
            executors.shutdownNow();
        }
        if(mapFragment != null){
            mapFragment.onStop();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        if(mapFragment != null){
            mapFragment.onResume();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        if(mapFragment != null){
            mapFragment.onPause();

        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mapFragment != null){
            mapFragment.onDestroy();
        }

        super.onDestroy();
    }
}