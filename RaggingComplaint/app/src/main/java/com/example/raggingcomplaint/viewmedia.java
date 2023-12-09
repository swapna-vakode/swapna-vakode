package com.example.raggingcomplaint;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class viewmedia extends Fragment {
    ImageView  viewimage2;
    String compid;
FirebaseFirestore firebaseFirestore;
StorageReference storageReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.viewmedia, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            compid = bundle.getString("key");
        }

        viewimage2 = view.findViewById(R.id.viewimage);

final ProgressDialog progressDialog=new ProgressDialog(getActivity());
progressDialog.setMessage("Downloading image...");
progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
progressDialog.show();
       firebaseFirestore = FirebaseFirestore.getInstance();
         storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference image1ref = storageReference.child("complaints/"+compid+"/image1.jpg");
            image1ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                   // viewimage1.setVisibility(View.VISIBLE);
                    Picasso.get().load(uri).into(viewimage2);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                }
            });


        return view;

    }

}
