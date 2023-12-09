package com.example.raggingcomplaint;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Customadapter extends RecyclerView.Adapter<Viewholder> {
    private admin_home_fragment adminHomeFragment;
    admindashboard admindash=new admindashboard();
    private List<Model> modelList;
    Context context;
    Model model;
    private sendata sd;
   // private view_complaint_fragment viewComplaintFragment=new view_complaint_fragment();

    Customadapter(admin_home_fragment adminHomeFragment, List<Model> modelList) {
        this.adminHomeFragment = adminHomeFragment;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_layout,viewGroup,false);
       Viewholder viewholder=new Viewholder(itemView);
       viewholder.setonclicklistener(new Viewholder.Clicklistener() {
           @Override
           public void onitemclick(View view, int position) {
String id=modelList.get(position).getId();
            //   Toast.makeText(,id,Toast.LENGTH_SHORT).show();
           }
       });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int i) {
holder.name.setText(modelList.get(i).getTitle());
        holder.description.setText(modelList.get(i).getDescription());
        holder.date.setText(modelList.get(i).getDate());
        final String comid=modelList.get(i).getId();
       // viewComplaintFragment.setCompid(comid);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(adminHomeFragment.getActivity(),"item "+ comid +"is selected",Toast.LENGTH_SHORT).show();
                 //model.setcompid(comid);
               //  viewComplaintFragment.setCompid(comid);
                // adminHomeFragment.sm.sendid(comid);
                //admindash.onitemselected();
//sd.sendiddata(comid);
                Bundle bundle=new Bundle();
                bundle.putString("key",comid);
                view_complaint_fragment fragment=new view_complaint_fragment();
                fragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public interface sendata{
        public void sendiddata(String data);
    }
}
