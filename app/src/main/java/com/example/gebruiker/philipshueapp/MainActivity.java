package com.example.gebruiker.philipshueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HueListener {

    private RecyclerView recyclerView;
    private HueAdapter adapter;
    private ArrayList<Light> lights = new ArrayList<>();
    private HueManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //HueManager manager
        manager = HueManager.getInstance(this, this);

        recyclerView = findViewById(R.id.hueListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //New activity
                        Intent intent = new Intent(getApplicationContext(), HueActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Config.LIGHT, lights.get(position));
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

        adapter = new HueAdapter(this.lights);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        manager.volleyGet();
    }

    @Override
    public void onResponse(ArrayList<Light> lights) {
        this.lights.clear();
        Log.d("Response: ", lights.size() + "");


        this.lights.addAll(lights);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Error error) {
        Log.d("Error: ", error.toString());
    }
}
