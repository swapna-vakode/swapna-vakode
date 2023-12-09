package com.example.raggingcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class admindashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,admin_home_fragment.sendcomid
{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
String receivedid;
    View navheader;
    Toolbar toolbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        toolbar=(Toolbar)findViewById(R.id.admintoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.admin_fragment,new admin_home_fragment());
        fragmentTransaction.commit();
        drawerLayout=(DrawerLayout) findViewById(R.id.admin_drawer);
        navigationView=(NavigationView) findViewById(R.id.admin_navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navheader=navigationView.getHeaderView(0);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
       String userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()) {
            case R.id.admin_home:
                toolbar.setTitle("Home");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_fragment, new admin_home_fragment());
                fragmentTransaction.commit();
                break;
            case R.id.admin_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(admindashboard.this, phoneregister.class));
                finish();
                break;
        }
        return true;
    }
    @Override
    public void sendid(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("key",data);
        view_complaint_fragment fragment = new view_complaint_fragment();
            fragment.setArguments(bundle);
                      getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment,fragment).commit();
            //Toast.makeText(this,"cant load fragment", Toast.LENGTH_SHORT).show();
        }


  /*
    public void onitemselected() {

        toolbar.setTitle("Complaint");
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment,new view_complaint_fragment());
        fragmentTransaction.commit();
    }




    @Override
    public void sendid(String comid) {
        toolbar.setTitle("Complaint");
       fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment,new view_complaint_fragment());
        fragmentTransaction.commit();


    view_complaint_fragment frag=(view_complaint_fragment)fragmentManager.findFragmentById(R.id.viewcomplaintfragment);
        if (frag != null) {
            frag.setCompid(comid);
        }else{
                     Toast.makeText(this,"cant load fragment", Toast.LENGTH_SHORT).show();
        }




        // onitemselected();
    }

   */
}
