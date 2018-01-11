package com.riso.android.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class StepActivity extends AppCompatActivity {
    private static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        int position = bundle.getInt(POSITION);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
//            StepsFragment sf = new StepsFragment();
//            sf.setArguments(bundle);
//            ft.add(android.R.id.content, sf, "tag1").commit();

            if (position == 0) {
                DetailFragment df = new DetailFragment();
                df.setArguments(bundle);
                ft.add(android.R.id.content, df).commit();
            } else {
                StepsFragment sf = new StepsFragment();
                sf.setArguments(bundle);
                ft.add(android.R.id.content, sf).commit();
            }

        } else {
            fragmentManager.findFragmentByTag("tag1");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
