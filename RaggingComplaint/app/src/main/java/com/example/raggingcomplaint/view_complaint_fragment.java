package com.example.raggingcomplaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class view_complaint_fragment  extends Fragment {
    private String compid,comname;
    FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
private TextView viewcompname,viewcomploc,viewcomp,viewoldcompstatus,changestatus,viewmediauploaded,viewcompdate,viewimage1,viewimage2,playaudio,playvideo;
private EditText newstatus;
private Button updatestatusbt;
//private Model model;
    String a,b,c,d,mediaid;
private int mediauploaded=2,img1uploaded=2,img2uploaded=2,audiouploaded=2,videouploaded=2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.view_complaint_fragment,container,false);
    //   compid=model.getcompid();
     //  viewcompname=view.findViewById(R.id.cdesp);
    //   viewcompname.setText(compid);
Bundle bundle=this.getArguments();
        if (bundle != null) {
            compid=bundle.getString("key");
            //.makeText(getActivity(),compid,Toast.LENGTH_LONG).show();
        }//  Toast.makeText(getActivity(),"complaint id not received",Toast.LENGTH_LONG).show();

//showcompid=view.findViewById(R.id.textView5);
  //      String ex="you have selected to view the complaint of complaint id :" +compid;
    //
        viewimage1=view.findViewById(R.id.viewimage1);
        viewimage2=view.findViewById(R.id.viewimage2);
        playaudio=view.findViewById(R.id.viewaudio);
        playvideo=view.findViewById(R.id.viewvideo);
        viewcompdate=view.findViewById(R.id.viewcomdate);
       viewcomp=view.findViewById(R.id.viewcompdesp);
       viewcomploc=view.findViewById(R.id.viewcomploc);
        viewcompname=view.findViewById(R.id.viewcompname);
        viewoldcompstatus=view.findViewById(R.id.viewoldcompstatus);
        changestatus=view.findViewById(R.id.changestatustext);
        newstatus=view.findViewById(R.id.getnewstatus);
        updatestatusbt=view.findViewById(R.id.updatestatusbutton);
viewmediauploaded=view.findViewById(R.id.showmedia);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference= firebaseFirestore.collection("complaints").document(compid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                if(documentSnapshot.exists()) {
                   viewcompname.setText(documentSnapshot.getString("Complaint name"));
                   comname=documentSnapshot.getString("Complaint name");
                   viewcomploc.setText(documentSnapshot.getString("Complaint location"));
                   viewoldcompstatus.setText(documentSnapshot.getString("Complaint status"));
                   viewcomp.setText(documentSnapshot.getString("Complaint"));
                   viewcompdate.setText(documentSnapshot.getString("Date"));
                   String s=documentSnapshot.getString("media uploaded");
                     a=documentSnapshot.getString("image1 uploaded");
                     b=documentSnapshot.getString("image2 uploaded");
                     c=documentSnapshot.getString("audio uploaded");
                     d=documentSnapshot.getString("video uploaded");

                   if(s.equals("yes")){
                       mediauploaded=1;
                       viewmediauploaded.setText(R.string.mediahasuploaded);
                       mediaid=documentSnapshot.getString("media id");
                      // mediadownloadresult.setText(R.string.viewdownloadedmedia);
                       //mediadownloadresult.setVisibility(View.VISIBLE);
                   }else {
                       viewmediauploaded.setText(R.string.mediahasnotuploaded);
                       mediauploaded=0;
                   }
                   if(a.equals("yes")){
                    viewimage1.setVisibility(View.VISIBLE);
                   }
                    if(b.equals("yes")){
                        viewimage2.setVisibility(View.VISIBLE);
                    }
                    if(c.equals("yes")){
                        playaudio.setVisibility(View.VISIBLE);
                    }
                    if(d.equals("yes")){
                        playvideo.setVisibility(View.VISIBLE);
                    }
               }else{
                   Toast.makeText(getActivity(),"complaint do not exist",Toast.LENGTH_SHORT).show();
               }
            }
        });
       /* if (mediauploaded == 1) {
            viewmediauploaded.setText(R.string.mediahasuploaded);
            mediadownloadresult.setText(R.string.downloadmedia);
            mediadownloadresult.setVisibility(View.VISIBLE);
        }else if(mediauploaded==0){
            viewmediauploaded.setText(R.string.mediahasnotuploaded);
        }

        */
        changestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newstatus.setVisibility(View.VISIBLE);
                updatestatusbt.setVisibility(View.VISIBLE);
            }

        });
                updatestatusbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String getnewstatus=newstatus.getText().toString().trim();
                        FirebaseFirestore db=FirebaseFirestore.getInstance();
                        Map<String,Object> status = new HashMap<>();
                        status.put("Complaint status",getnewstatus);
                        db.collection("complaints").document(compid).update(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               Toast.makeText(getActivity(),"status updated successfully",Toast.LENGTH_SHORT).show();
                               viewoldcompstatus.setText(getnewstatus);
                                newstatus.setVisibility(View.INVISIBLE);
                                updatestatusbt.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"status update failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

viewimage1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       /* final int error = 2;
        StorageReference storageReference=FirebaseStorage.getInstance().getReference();
        StorageReference img1ref,img2ref,audioref,videoref;
        if(a.equals("yes")){
            img1ref=storageReference.child("complaints/"+compid+"/image1.jpg");
            File img1file=new File(Environment.getExternalStorageDirectory(),"raggingComplaint/"+comname);
         if(!img1file.exists()){
             img1file.mkdirs();
         }
         final File localfile=new File(img1file,"image1.jpg");
            img1ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(getActivity(),"image1 downloaded",Toast.LENGTH_SHORT).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   error =1;
               }
           });
        }
        if(b.equals("yes")){
            img2ref=storageReference.child("complaints/"+compid+"/image2.jpg");
            File img2file=new File(Environment.getExternalStorageDirectory(),"image2.jpg");
            if(!img2file.exists()){
                img2file.mkdirs();
            }
            final File localfile=new File(img2file,"image2.jpg");
            img2ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"image2 downloaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    error =1;
                }
            });
        }
        if(c.equals("yes")){
            audioref=storageReference.child("complaints/"+compid+"/audio.mpeg");
            File audiofile= new File(Environment.getExternalStorageDirectory(),"RaggingComplaint/"+comname);
            if(!audiofile.exists()){
                audiofile.mkdirs();
            }
            final File localfile=new File(audiofile,"audio.mpeg");
            audioref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"audio downloaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    error =1;
                }
            });
        }
        if(d.equals("yes")){
            videoref=storageReference.child("complaints/"+compid+"/video.mp4");
            File videofile=new File(Environment.DIRECTORY_DOWNLOADS,"video.mp4");
            videoref.getFile(videofile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"video downloaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    error =1;
                }
            });
        }
if(error ==1){
    Toast.makeText(getActivity(),"error downloading some attachments",Toast.LENGTH_SHORT).show();
}
    }
    */
        Bundle bundle=new Bundle();
        bundle.putString("key",mediaid);
        viewmedia fragment=new viewmedia();
        fragment.setArguments(bundle);
        AppCompatActivity activity=(AppCompatActivity)v.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).addToBackStack(null).commit();

     }
});
        viewimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("key",mediaid);
                viewimage2 fragment=new viewimage2();
                fragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).addToBackStack(null).commit();

            }
        });
        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("key",mediaid);
                viewvideo fragment=new viewvideo();
                fragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).addToBackStack(null).commit();

            }
        });
        playaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("key",mediaid);
               viewaudio fragment=new viewaudio();
                fragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).addToBackStack(null).commit();
           }
        });
        return view;
    }


}
