package com.example.raggingcomplaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class admin_home_fragment extends Fragment implements Customadapter.sendata {
    sendcomid sm;
//    public onFragmentbtnselected listener;
//private ListView listview;
//private List<String> namelist=new ArrayList<>();
//private List<String> datelist=new ArrayList<>();
FirebaseFirestore db;
StorageReference storageReference;
RecyclerView mRecyclerview;
List<Model> modelList=new ArrayList<>();
LinearLayoutManager layoutManager;
Customadapter adapter;
String receivedid;

    public admin_home_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_home_fragment,container,false);
        db=FirebaseFirestore.getInstance();
     /*   Button viewcomplaint=view.findViewById(R.id.view_complaint)  ;
        viewcomplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onbtnselected();
            }
        });

      */
  /*   listview=view.findViewById(R.id.listview);
     storageReference= FirebaseStorage.getInstance().getReference();

     db.collection("complaints").addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            namelist.clear();
            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                namelist.add(snapshot.getString("Complaint name"));
            }
             ArrayAdapter<String>adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_selectable_list_item,namelist);
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
         }
     });
   */

mRecyclerview=view.findViewById(R.id.recyclerview);
mRecyclerview.setHasFixedSize(true);
layoutManager=new LinearLayoutManager(getActivity());
mRecyclerview.setLayoutManager(layoutManager);
        db.collection("complaints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                     Model model =new Model(documentSnapshot.getId(),
                            documentSnapshot.getString("Complaint name"),
                            documentSnapshot.getString("Date"),
                            documentSnapshot.getString("Complaint"));
                    modelList.add(model);
                }
                adapter=new Customadapter(admin_home_fragment.this,modelList);
                mRecyclerview.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

       try{
           sm=(sendcomid)context;
       }catch (ClassCastException e){
           throw new ClassCastException("error occured in sending id");
       }
    }



    @Override
    public void sendiddata(String data) {
        this.receivedid=data;
        Bundle bundle = new Bundle();
        bundle.putString("key",data);
        view_complaint_fragment fragment = new view_complaint_fragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).commit();

    //  sm.sendid(receivedid);
    }

    public interface sendcomid{
        public void sendid(String comid);
    }






}
