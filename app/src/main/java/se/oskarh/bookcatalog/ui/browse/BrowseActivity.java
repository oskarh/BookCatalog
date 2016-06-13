package se.oskarh.bookcatalog.ui.browse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.oskarh.bookcatalog.R;

public class BrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
