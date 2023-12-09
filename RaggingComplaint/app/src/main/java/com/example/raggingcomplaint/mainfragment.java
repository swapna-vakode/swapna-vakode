package com.example.raggingcomplaint;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class mainfragment extends Fragment {
    private onFragmentbtnselected listener;
   // private  getlocationblock alistener;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId,latitude,longitude,country,locality,address;
    FirebaseUser user;
    StorageReference storageReference;
    EditText getcomid;
    ImageButton checkstatus;
    TextView viewstatus;
 //   protected LocationManager locationManager;
   // protected LocationListener locationListener;
FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mainfragment,container,false);
        Button addcomplaint=view.findViewById(R.id.addcomplaint);
         Button sendlocation=view.findViewById(R.id.sendlocation);
        getcomid=view.findViewById(R.id.checkstatus);
        checkstatus=view.findViewById(R.id.imageButton);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
      //  storageReference = FirebaseStorage.getInstance().getReference();
     userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        viewstatus=view.findViewById(R.id.viewcompstatus);

        sendlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                //       && (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED))
                //{
                getlocationsms();
                //  listener.getlocation();
          /*  }else
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},41);
                Toast.makeText(getActivity(),"Provide access permissions and try again.",Toast.LENGTH_SHORT).show();
                //getlocationsms();
            }


           */
            }
        });
        addcomplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onbtnselected();
            }
        });
        checkstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String compid=getcomid.getText().toString().trim();

                DocumentReference documentReference = fStore.collection("complaints").document(compid);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                      //  assert documentSnapshot != null;
                        if(documentSnapshot.exists()){
                          String status=documentSnapshot.getString("Complaint status");
                          viewstatus.setText(status);
                            viewstatus.setVisibility(View.VISIBLE);
                        }else {
                            Log.d("tag", "onEvent: Document do not exists");
                            getcomid.setError("Complaint id does not exist");
                        }
                    }
                });




            }
        });
        return view;
    }
    public void getlocationsms()
     {
        final String[] message = new String[1];
        final String phoneno="7019863523";
         final String[] uri = new String[1];
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null){
                    try {
                    //    String uri = "http://maps.google.com/maps?saddr=" + currentLocation.getLatitude()+","+currentLocation.getLongitude();
                        Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                   latitude= String.valueOf(addresses.get(0).getLatitude());
                        longitude= String.valueOf(addresses.get(0).getLongitude());
                        locality= String.valueOf(addresses.get(0).getLocality());
                        country= String.valueOf(addresses.get(0).getCountryName());
                        address= String.valueOf(addresses.get(0).getAddressLine(0));
                        message[0] ="Ragging has been detected at location "+latitude+","+longitude+","+locality+" .   ";
                        uri[0] = "Ragging has been detected at location http://maps.google.com/maps?saddr=" + latitude+","+longitude;
                        Toast.makeText(getActivity(),"location captured",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneno,null, uri[0],null,null);
                    Toast.makeText(getActivity(),"message sent",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof onFragmentbtnselected){
            listener=(onFragmentbtnselected)context;
           // alistener=(getlocationblock)context;
        }

        else
            {
                    throw new ClassCastException(context.toString()+"must implement listener");
        }
    }

    public interface onFragmentbtnselected {
        public void onbtnselected();

        public void getlocation();
    }
}
