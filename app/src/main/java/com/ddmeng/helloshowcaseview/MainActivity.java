package com.ddmeng.helloshowcaseview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View viewById = findViewById(R.id.hello);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello World clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        View showButton = findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowcaseView.Builder(MainActivity.this)
                        .setContentView(R.layout.show_case_content_view)
                        .setContentDismissButton(R.id.end_button)
                        .setTarget(viewById)
                        .setMaskColor(Color.argb(180, 0, 0, 0))
                        .setShape(new RectShape(0, 0, 0))
                        .setFadeInEnabled(true)
                        .setFadeInDuration(1000)
                        .setFadeOutEnabled(true)
                        .setFadeOutDuration(2000)
                        .show();
            }
        });

    }
}
