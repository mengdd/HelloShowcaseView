package com.ddmeng.helloshowcaseview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.hello);
        new ShowcaseView.Builder(this)
                .setContentView(R.layout.show_case_content_view)
                .setTarget(viewById)
                .show();
    }
}
