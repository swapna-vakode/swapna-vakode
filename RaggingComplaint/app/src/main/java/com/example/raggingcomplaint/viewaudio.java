package com.example.raggingcomplaint;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class viewaudio extends Fragment {
     int otime=0,stime=0,etime=0;
    String compid;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.viewaudio,container,false);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            compid=bundle.getString("key");
        }
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading audio...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        seekBar=view.findViewById(R.id.seekbar);
         final ImageButton play=view.findViewById(R.id.play);
        final ImageButton pause=view.findViewById(R.id.pause);
       // ImageButton stop=view.findViewById(R.id.stop);

         mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        seekBar.setClickable(false);
        pause.setEnabled(false);

        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference audioref=storageReference.child("complaints/"+compid+"/audio.mpeg");
        audioref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    mediaPlayer.setDataSource(getActivity(),uri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

       // mediaPlayer.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                pause.setEnabled(false);
                play.setEnabled(true);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mediaPlayer.start();
etime=mediaPlayer.getDuration();
stime=mediaPlayer.getCurrentPosition();
if(otime==0){
    seekBar.setMax(etime);
    otime=1;
}
seekBar.setProgress(stime);
handler.postDelayed(Updatesongtime,100);
pause.setEnabled(true);
play.setEnabled(false);
            }
        });

        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()){
        mediaPlayer.stop();
        mediaPlayer.release();}
    }

    Runnable Updatesongtime=new Runnable() {
        @Override
        public void run() {
            stime=mediaPlayer.getCurrentPosition();
            seekBar.setProgress(stime);
            handler.postDelayed(this,100);
        }
    };
}
