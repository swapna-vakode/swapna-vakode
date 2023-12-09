package com.example.raggingcomplaint;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

public class Complaintfragment extends Fragment {
    int yy,mm,dd;
   private EditText compname,comploc,comp,date;
   private TextView addimg1,addimg2,addaudio,addvideo,img1view,img2view,audioview,videoview,img1upload,img2upload,audioupload,videoupload;
   Button submit;
   FirebaseAuth fAuth;
   StorageReference storageReference;
   private FirebaseUser firebaseUser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private int img1=0,img2=0,audio=0,video=0,x=0;
    private Uri imguri1,imguri2,audiouri,videouri;
String randomcompid,datesubmitted;
    private String img1id,img2id,audioid,videoid,recipientmail;
    private int img1res=2,img2res=2,audiores=2,videores=2;
    private onsubmit listener;
    private String mediapath,img1path,img2path,audiopath,videopath;
    private int flag=2;
            private int imger1ror=2,img2err=2,audioerr=2,videoerr=2;
    // final ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.complaintfragment,container,false);
        compname=view.findViewById(R.id.compname);
img1view=view.findViewById(R.id.addimg1view);
        img2view=view.findViewById(R.id.addimg2view);
        audioview=view.findViewById(R.id.addaudioview);
        videoview=view.findViewById(R.id.addvideoview);
        comploc=view.findViewById(R.id.comploc);
        comp=view.findViewById(R.id.comp);
        addimg1=view.findViewById(R.id.addimg1);
        addimg2=view.findViewById(R.id.addimg2);
        addaudio=view.findViewById(R.id.addaudio);
        addvideo=view.findViewById(R.id.addvideo);
        submit=view.findViewById(R.id.submitcomp);
        fAuth = FirebaseAuth.getInstance();
        date=view.findViewById(R.id.compdate);
randomcompid= UUID.randomUUID().toString();
        final Calendar calendar=Calendar.getInstance();
//final ProgressDialog progressDialog=new ProgressDialog(getActivity());
  //    progressDialog.setTitle("Uploading...");
    //    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       // progressDialog.setMessage("Uploading media...Please wait...");
      final FirebaseFirestore  fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        final String userId = fAuth.getCurrentUser().getUid();
        firebaseUser = fAuth.getCurrentUser();
        DocumentReference documentReference=fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    recipientmail=documentSnapshot.getString("email");
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          yy=calendar.get(Calendar.YEAR);
          mm=calendar.get(Calendar.MONTH);
          dd=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datesubmitted=dayOfMonth+"/"+month+"/"+year;
                        date.setText(datesubmitted);
                    }
                },yy,mm,dd);
                datePickerDialog.show();
            }
        });
        addimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               img1=1;x=1;
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
        addimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img2=1;x=2;
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        }
        });
        addaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio=1;x=3;
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
        addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video=1;x=4;
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000 );

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  progressDialog.show();
                String com = comp.getText().toString().trim();
                String comname = compname.getText().toString().trim();
                String comloc = comploc.getText().toString().trim();

                String comdate=date.getText().toString().trim();
                String img1uploadresult=img1view.getText().toString();
                String img2uploadresult=img2view.getText().toString();
                String audiouploadresult=audioview.getText().toString();
                String videouploadresult=videoview.getText().toString();

                if(TextUtils.isEmpty(com)){
                    comp.setError("Complaint is required");
                    return;
                }
                if(TextUtils.isEmpty(comname)){
                    compname.setError("Complaint name is required");
                    return;
                }
                if(TextUtils.isEmpty(comloc)){
                    comploc.setError("Complaint location is required");
                    return;
                }
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                Map<String,Object> user = new HashMap<>();
                user.put("Complaint name",comname);
                user.put("Complaint location",comloc);
                user.put("Complaint",com);
                user.put("Complaint status","submitted");
                user.put("Date",comdate);
                   if(img1==1 && img1uploadresult.equals("uploaded successfully")){
                           imger1ror=1;
                     user.put("image1 uploaded","yes");

                   }else{
                       imger1ror=0;
                       user.put("image1 uploaded","no");
                   }

                        if(img2==1 && img2uploadresult.equals("uploaded successfully")){
                            img2err=1;
                                user.put("image2 uploaded","yes");
                        }else{
                            img2err=0;
                            user.put("image2 uploaded","no");
                        }
                        if(audio==1 && audiouploadresult.equals("uploaded successfully")){
                            audioerr=1;
                                user.put("audio uploaded","yes");
                        }else{
                            audioerr=0;
                            user.put("audio uploaded","no");
                        }
                        if(video==1 && videouploadresult.equals("uploaded successfully")){
                            videoerr=1;
                                user.put("video uploaded","yes");
                        }else{
                            videoerr=0;
                            user.put("video uploaded","no");
                        }
                        if(imger1ror==1 || img2err==1 || audioerr==1|| videoerr==1){
                            user.put("media uploaded","yes");
                            user.put("media id",randomcompid);
                        }else{
                            user.put("media uploaded","no");
                        }

                db.collection("complaints").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference1) {
                        Log.d(getTag(), "onSuccess: complaint  file is created with id : "+ documentReference1.getId());
                        String compid=documentReference1.getId();
/*
                        if(img1==1){
                           // int img1res;
                            img1path="complaints/"+compid+"/image1.jpg";
                          uploadImageToFirebase(imguri1,img1path);
                            String result= (String) img1view.getText();
                            if(result.equals("uploaded successfully")){
                                imger1ror=1;
                              //  progressDialog.dismiss();
                            }else if(result.equals("upload failed!")){
                                imger1ror=0;
                            }
                        }
                        if(img2==1){
                          //  int img2res;

                            img2path="complaints/"+compid+"/image2.jpg";
                            uploadImage2ToFirebase(imguri2,img2path);
                            String result= (String) img2view.getText();
                            if(result.equals("uploaded successfully")){
                                img2err=1;
                            }else if(result.equals("upload failed!")){
                                img2err=0;

                            }
                          //  progressDialog2.dismiss();
                        }
                        if(audio==1){
                            //int audiores;
                            audiopath="complaints/"+compid+"/audio.mpeg";
                          uploadaudioToFirebase(audiouri,audiopath);
                            String result= (String) audioview.getText();
                            if(result.equals("uploaded successfully")){
                                audioerr=1;
                            }else if(result.equals("upload failed!")){
                                audioerr=0;
                            }
                        }
                        if(video==1){
                           // int videores;
                          videopath="complaints/"+compid+"/video.mp4";
                            uploadvideoToFirebase(videouri,videopath);
                            String result= (String) videoview.getText();
                            if(result.equals("uploaded successfully")){
                                videoerr=1;
                            }else if(result.equals("upload failed!")){
                                videoerr=0;
                            }
                        }


 */
                            Toast.makeText(getActivity(), "your complaint is submitted with complaint id : " + compid, Toast.LENGTH_SHORT).show();
                          sendmail(compid);
                            listener.onsubmitclicked();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(getTag(), "onFailure:error adding complaint " + e.toString());
                        Toast.makeText(getActivity(),"complaint cannot be submitted",Toast.LENGTH_SHORT).show();
                    }
                });
            }});
        return view;
    }

    private void sendmail(String compid) {
      //  String mail="swapnav833@gmail.com";
 String subject="Complaint registered successfully!";
        String message="Your complaint has been successfully registered with complaint id : "+ compid +". Save this complaint id for checking the status of your complaint.";
JavaMailApi javaMailApi=new JavaMailApi(getActivity(),recipientmail,subject,message);
javaMailApi.execute();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                final ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading media...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                Uri imguri = data.getData();
                Cursor cursor = getActivity().managedQuery(imguri, null, null, null, null);
               cursor.moveToFirst();
               long siz=cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
               double size=siz/1024;
              //  uploadImageToFirebase(imageUri);
              //  return  imguri;
                switch(x){
                    case 1:
                        if(size>1025){
                            img1view.setText(R.string.imagesizeexceeded);
                            img1view.setVisibility(View.VISIBLE);
                            break;
                        }else {
                        imguri1= imguri;
                        uploadImageToFirebase(imguri1,"complaints/"+randomcompid+"/image1.jpg");
                        //progressDialog.dismiss();
                        break;}
                    case 2:
                        if(size>1025){
                            img2view.setText(R.string.imagesizeexceeded);
                            img2view.setVisibility(View.VISIBLE);
                           // progressDialog.dismiss();
                            break;
                        }else {
                        imguri2= imguri;
                        uploadImage2ToFirebase(imguri2,"complaints/"+randomcompid+"/image2.jpg");
                   //     progressDialog.dismiss();
                        break;}
                    case 3:
                        if(size>2049){
                            audioview.setText(R.string.audiosizeexceeded);
                            audioview.setVisibility(View.VISIBLE);
                      //      progressDialog.dismiss();
                            break;
                        }else{
                        audiouri= imguri;
                        uploadaudioToFirebase(audiouri,"complaints/"+randomcompid+"/audio.mpeg");
                        //progressDialog.dismiss();
                        break;}
                    case 4:
                        if(size>4097){
                            videoview.setText(R.string.videosizeerror);
                            videoview.setVisibility(View.VISIBLE);
                          //  progressDialog.dismiss();
                            break;
                        }else {
                            videouri = imguri;
                            uploadvideoToFirebase(videouri, "complaints/" + randomcompid + "/video.mp4");
                           // progressDialog.dismiss();
                            break;
                        }
                    default:
                        x=0;
                }
            progressDialog.dismiss();
            }

        }
    }

    public void uploadImageToFirebase(Uri imageUri,String path) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading image...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        flag=2;
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child(path);
       // UploadTask uploadTask = fileRef.putFile(imageUri);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                img1view.setText(R.string.uploaded_successfully);
                img1view.setVisibility(View.VISIBLE);
              //  Toast.makeText(getActivity(),"image uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                img1view.setText(R.string.notuploadedd);
                img1view.setVisibility(View.VISIBLE);
            }
        });
progressDialog.dismiss();
    }
        public void uploadImage2ToFirebase(Uri imageUri,String path) {
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading image...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            flag=2;
            // uplaod image to firebase storage
            final StorageReference fileRef2 = storageReference.child(path);
            // UploadTask uploadTask = fileRef.putFile(imageUri);
            fileRef2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 //   img2res=1;
                    img2view.setText(R.string.uploaded_successfully);
                    img2view.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                //    Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
                    img2view.setText(R.string.notuploadedd);
                    img2view.setVisibility(View.VISIBLE);
                    // img2res=0;
                }
            });
            progressDialog.dismiss();
        }
            public void uploadaudioToFirebase(Uri imageUri,String path) {
                final ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading audio...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                flag=2;
                // uplaod image to firebase storage
                final StorageReference fileRef3 = storageReference.child(path);
                // UploadTask uploadTask = fileRef.putFile(imageUri);
                fileRef3.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // audiores = 1;
                        audioview.setText(R.string.uploaded_successfully);
                        audioview.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(MainActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                       // audiores=0;
                        audioview.setText(R.string.notuploadedd);
                        audioview.setVisibility(View.VISIBLE);
                    }
                });
                progressDialog.dismiss();
            }
            public void uploadvideoToFirebase(Uri imageUri,String path) {
                  //  videoerr=2;
                    // uplaod image to firebase storage
                final ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading video...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                    final StorageReference fileRef4 = storageReference.child(path);
                    // UploadTask uploadTask = fileRef.putFile(imageUri);
                    fileRef4.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            videoview.setText(R.string.uploaded_successfully);
                            videoview.setVisibility(View.VISIBLE);
                           // videores=1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            videoview.setText(R.string.notuploadedd);
                            videoview.setVisibility(View.VISIBLE);
                            //  videores=0;
                            // Toast.makeText(MainActivity.this, "Failed.", Toast.LENGTH_SHORT).show();

                        }
                    });
                    progressDialog.dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onsubmit){
            listener=(onsubmit)context;
        }else{
            throw new ClassCastException(context.toString()+"must implement a listener");
        }
    }

    public  interface onsubmit{
        public void onsubmitclicked();
    }
}
