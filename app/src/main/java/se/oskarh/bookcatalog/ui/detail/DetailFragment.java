package se.oskarh.bookcatalog.ui.detail;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.SingleSubscriber;
import se.oskarh.bookcatalog.BookCatalogApplication;
import se.oskarh.bookcatalog.R;
import se.oskarh.bookcatalog.network.api.BookCatalogApi;
import se.oskarh.bookcatalog.network.model.BookDetails;
import timber.log.Timber;

public class DetailFragment extends Fragment {

    @Inject
    BookCatalogApi bookApi;

    @BindView(R.id.image_book)
    ImageView bookImage;

    @BindView(R.id.text_book_title)
    TextView title;

    @BindView(R.id.text_author)
    TextView author;

    @BindView(R.id.text_publisher)
    TextView publisher;

    @BindView(R.id.text_published)
    TextView published;

    @BindView(R.id.text_description)
    TextView description;

    @BindView(R.id.layout_activity_detail)
    LinearLayout detailLayout;

    public static DetailFragment newInstance(String id, String imageUrl) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(DetailActivity.EXTRA_BOOK_ID, id);
        args.putString(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        ((BookCatalogApplication) getActivity().getApplication()).getComponent().inject(this);
        String bookId = getArguments().getString(DetailActivity.EXTRA_BOOK_ID);
        String imageUrl = getArguments().getString(DetailActivity.EXTRA_IMAGE_URL);
        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.image_not_found)
                .into(bookImage);
        fetchBook(bookId);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookApi.unsubscribe();
    }

    private void fetchBook(final String bookId) {
        bookApi.getBookDetails(bookId, new SingleSubscriber<BookDetails>() {
            @Override
            public void onSuccess(BookDetails bookDetails) {
                if (bookDetails != null) {
                    populateCard(bookDetails);
                } else {
                    Timber.d("Failed to download book details %s", bookId);
                    displayErrorView();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.d("Failed to download book details: %s", Log.getStackTraceString(throwable));
                displayErrorView();
            }
        });
    }

    // TODO: Implement better error handling
    private void displayErrorView() {
        Snackbar.make(detailLayout, R.string.network_request_failed, Snackbar.LENGTH_LONG).show();
    }

    private void populateCard(BookDetails bookDetails) {
        BookDetails.DetailsVolumeInfo volumeInfo = bookDetails.getVolumeInfo();
        title.setText(volumeInfo.getTitle());
        author.setText(authorsAsString(volumeInfo.getAuthors()));
        publisher.setText(volumeInfo.getPublisher());
        published.setText(volumeInfo.getPublishedDate());
        if (volumeInfo.getDescription() != null) {
            description.setText(Html.fromHtml(volumeInfo.getDescription()));
        }
    }

    private String authorsAsString(List<String> authors) {
        return authors == null ? "" : TextUtils.join(", ", authors);
    }
}
