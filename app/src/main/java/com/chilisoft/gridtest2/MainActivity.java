package com.chilisoft.gridtest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ArrayList<MyModel> images = new ArrayList<>();
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // generate random image widths
        for (int i = 0; i < 100; i++) {
            final int min = 5;
            final int max = 100;
            final int random = new Random().nextInt((max - min) + 1) + min;

            MyModel myModel = new MyModel();
            myModel.width = 200 + random * 5;
            myModel.height = 300;
            images.add(myModel);
        }

        long start = System.currentTimeMillis();

        // get row count
        float sum = 0;
        for (MyModel image : images) {
            sum += image.width;
        }

        float rowsCount = Math.round(sum / (1080f));

        // split items by rows, to know which item will be placed in each row
        final ArrayList<ArrayList<MyModel>> rows = new ArrayList<>();
        int lasIndex = 0;
        for (int i = 0; i < rowsCount; i++) {
            int sum2 = 0;
            ArrayList<MyModel> row = new ArrayList<>();
            if (rows.size() > 0) {
                lasIndex += rows.get(i - 1).size();
            }

            for (int j = lasIndex; j < images.size(); j++) {
                MyModel image = images.get(j);
                if (sum2 < 1080) {
                    sum2 += image.width;
                    row.add(image);
                } else {
                    break;
                }
            }

            rows.add(row);
        }

        // normalize items withs, so they can fill entire screen
        for (ArrayList<MyModel> row : rows) {
            float sum3 = 0;
            for (MyModel myModel : row) {
                sum3 += myModel.width;
            }

            if (sum3 > 1080) {
                float mnacord = sum3 - 1080;
                float tarberutyun = mnacord / row.size();

                for (MyModel myModel : row) {
                    myModel.width -= tarberutyun;
                }

            } else {

                float mnacord = 1080 - sum3;
                float tarberutyun = mnacord / row.size();
                for (MyModel myModel : row) {
                    myModel.width += tarberutyun;
                }
            }
        }

        // flatten rows so adapter can display them correctly
        images.clear();
        for (ArrayList<MyModel> row : rows) {
            images.addAll(row);
        }

        long end = System.currentTimeMillis();
        Toast.makeText(this, "time past " + (end - start) + " ms", Toast.LENGTH_SHORT).show();

        mRecyclerView = findViewById(R.id.recycler);
        final Adapter adapter = new Adapter(images);
        final GridLayoutManager manager = new GridLayoutManager(this, 1080);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return images.get(position).width;
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
    }

    public class MyModel {
        public int width;
        public int height;

        public String getUrl() {
            return String.format(Locale.ENGLISH, "https://picsum.photos/%d/%d", width, height);
        }
    }
}
