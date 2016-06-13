package se.oskarh.bookcatalog.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.oskarh.bookcatalog.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK_ID = "extra_book_id";

    public static final String EXTRA_IMAGE_URL = "extra_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

        String bookId = bundle.getString(EXTRA_BOOK_ID);
        String imageUrl = bundle.getString(EXTRA_IMAGE_URL);

        DetailFragment detailFragment = DetailFragment.newInstance(bookId, imageUrl);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_detail, detailFragment, "")
                .commit();
    }
}
