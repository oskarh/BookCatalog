package se.oskarh.bookcatalog.ui.browse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.SingleSubscriber;
import se.oskarh.bookcatalog.BookCatalogApplication;
import se.oskarh.bookcatalog.R;
import se.oskarh.bookcatalog.network.api.BookCatalogApi;
import se.oskarh.bookcatalog.network.model.Book;
import se.oskarh.bookcatalog.network.model.BookList;
import se.oskarh.bookcatalog.ui.detail.DetailActivity;
import se.oskarh.bookcatalog.ui.recycler.BookAdapter;
import timber.log.Timber;

public class BrowseFragment extends Fragment implements BookClickListener {

    @Inject
    BookCatalogApi bookCatalogApi;

    @BindView(R.id.recycler_books)
    RecyclerView bookList;

    @BindView(R.id.search)
    EditText searchView;

    @BindView(R.id.text_no_books_found)
    TextView noBooksFound;

    @BindView(R.id.layout_fragment_browse)
    LinearLayout browseLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        ButterKnife.bind(this, view);
        ((BookCatalogApplication) getActivity().getApplication()).getComponent().inject(this);
        init();
        return view;
    }

    private void init() {
        bookList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookList.setAdapter(new BookAdapter(new ArrayList<Book>(), this, this));
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView searchView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchBooks(searchView.getText().toString());
                    hideKeyboard();
                    return true;
                }
                return false;
            }

            private void hideKeyboard() {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void searchBooks(String query) {
        bookCatalogApi.getBooks(query, new SingleSubscriber<BookList>() {
            @Override
            public void onSuccess(BookList newBooks) {
                if (newBooks != null) {
                    if (newBooks.getBooks() != null && !newBooks.getBooks().isEmpty()) {
                        noBooksFound.setVisibility(View.GONE);
                        bookList.setVisibility(View.VISIBLE);
                        BookAdapter bookAdapter = (BookAdapter) bookList.getAdapter();
                        bookAdapter.setData(newBooks.getBooks());
                    } else {
                        noBooksFound.setVisibility(View.VISIBLE);
                        bookList.setVisibility(View.GONE);
                    }
                } else {
                    Timber.d("No books returned, display error view");
                    displayErrorView();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.d("Failed to fetch books: %s", Log.getStackTraceString(throwable));
                displayErrorView();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookCatalogApi.unsubscribe();
    }

    // TODO: Implement better error handling
    private void displayErrorView() {
        Snackbar.make(browseLayout, R.string.network_request_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBookClicked(String id, String imageUrl) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_BOOK_ID, id);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        startActivity(intent);
    }
}
