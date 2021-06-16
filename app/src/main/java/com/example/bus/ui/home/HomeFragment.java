package com.example.bus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bus.Main2Activity;
import com.example.bus.Nord_fragement;
import com.example.bus.R;
import com.example.bus.mapFragement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private Button btn_13,btn_42;
    private BottomSheetDialog bottomSheetDialog=null;
    private Button depart,arriver;
    private FirebaseDatabase database;
    private TextView txt_depart,txt_arrive;
    String id="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        //homeViewModel.getText().observe(this, new Observer<String>() {
            //@Override
            //public void onChanged(@Nullable String s) {
                //textView.setText(s);
            //}
        //});
        btn_13=root.findViewById(R.id.btn_13);
        btn_42=root.findViewById(R.id.btn_42);
        createDialog();
        btn_13.setOnClickListener(this);
        btn_42.setOnClickListener(this);

        BottomNavigationView bottomNav=root.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment selectedfragement=null;
                switch (item.getItemId())
                {
                    case  R.id.nav_home:
                        //Intent intent=new Intent(getActivity(), getActivity());
                        //startActivity(intent);
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new HomeFragment()).commit();
                        break;
                    case  R.id.nav_map:
                        //selectedfragement=new mapFragement();
                        /*
                        Intent intent2=new Intent(getActivity(), Main2Activity.class);
                        startActivity(intent2);

                         */
                        //getFragmentManager().beginTransaction().remove(new HomeFragment());
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new mapFragement()).commit();
                        break;
                    case R.id.nav_payement:
                            Intent intent3=new Intent(getActivity(), Main2Activity.class);
                            startActivity(intent3);

                }

                //getFragmentManager().beginTransaction().replace(R.id.fragement_container,selectedfragement).commit();
                return  true;
            }
        });

        return root;

    }
    private void createDialog()
    {
        if(bottomSheetDialog==null)
        {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.button_shet_dialog,null);
            bottomSheetDialog=new BottomSheetDialog(getContext());
            depart=view.findViewById(R.id.txt_depart);
            arriver=view.findViewById(R.id.txt_arrive);
            depart.setOnClickListener(this);
            arriver.setOnClickListener(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    @Override
    public void onClick(View v) {
        Button btn=(Button)v;

        if(v!=depart && v!=arriver)
        {
             id=btn.getText().toString().trim();
            database=FirebaseDatabase.getInstance();
            DatabaseReference ref=database.getReference().child("bus_numero").child(id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        depart.setText(dataSnapshot.child("Vers Nord").child("station_terminus").getValue().toString());
                        arriver.setText(dataSnapshot.child("Vers Sud").child("station_terminus").getValue().toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            bottomSheetDialog.show();
        }


         else if(v==depart)
        {

            String direction="Vers Nord";
            Bundle bundle=new Bundle();
            System.out.println(id);
            bundle.putString("ligne",id);
            bundle.putString("direction",direction);
            bottomSheetDialog.hide();
            Nord_fragement nord_fragement=new Nord_fragement();
            nord_fragement.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,nord_fragement).commit();
        }
         else
        {

            String direction="Vers Sud";
            Bundle bundle=new Bundle();
            bundle.putString("ligne",id);
            bundle.putString("direction",direction);
            bottomSheetDialog.hide();
            Nord_fragement nord_fragement=new Nord_fragement();
            nord_fragement.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,nord_fragement).commit();
        }

    }
}