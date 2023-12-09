package com.example.raggingcomplaint;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Viewholder extends RecyclerView.ViewHolder {
    TextView name,date,description;
    View mview;
    public CardView cardView;

    public Viewholder(@NonNull View itemView) {
        super(itemView);

        mview=itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclicklistener.onitemclick(v,getAdapterPosition());
            }
        });

        cardView=itemView.findViewById(R.id.cardview);
        name=itemView.findViewById(R.id.cname);
        date=itemView.findViewById(R.id.cdate);
        description=itemView.findViewById(R.id.cdesp);
    }
    private Viewholder.Clicklistener mclicklistener;
    public interface Clicklistener{
        void onitemclick(View view,int position);
        //void onitenlongclick(View view,int position);

    }
    public void setonclicklistener(Viewholder.Clicklistener clicklistener){
        mclicklistener=clicklistener;

    }
}
