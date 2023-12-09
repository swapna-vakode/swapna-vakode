package com.example.raggingcomplaint;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class viewvideo extends Fragment {
    String compid;
MediaController mediaController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.viewvideo,container,false);
      Bundle bundle=this.getArguments();
      if(bundle!=null){
          compid=bundle.getString("key");
      }
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading video...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        final VideoView videoView=view.findViewById(R.id.videoView);
        mediaController=new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference videoref=storageReference.child("complaints/"+compid+"/video.mp4");
        videoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoView.setVideoURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
        videoView.requestFocus();
        videoView.start();

        return view;
    }
}
