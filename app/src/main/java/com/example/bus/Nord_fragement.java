package com.example.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Nord_fragement extends Fragment {

    private DatabaseReference ref;
    private ListView listView;
    private ArrayAdapter<String> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.nord_fragement,container,false);
        Bundle bundle=getArguments();
        final String ligne=bundle.getString("ligne");
        String direction=bundle.getString("direction");
        listView=root.findViewById(R.id.liste_view);
        ref= FirebaseDatabase.getInstance().getReference().child("bus_numero").child(ligne).child(direction);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    ArrayList<String> liste=new ArrayList<String>();
                    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                    {
                        liste.add(dataSnapshot1.getValue().toString());
                    }
                    adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,liste);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str=(String)listView.getItemAtPosition(position);
            Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();

        }
    });

        return root;

    }
}
