package se.oskarh.bookcatalog.ui.recycler;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.oskarh.bookcatalog.R;
import se.oskarh.bookcatalog.network.model.Book;
import se.oskarh.bookcatalog.ui.browse.BookClickListener;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private static final String IMAGE_URL_NOT_FOUND = "http://not_found";
    private List<Book> books;
    private BookClickListener bookClickListener;
    private Fragment fragment;

    public BookAdapter(List<Book> books, BookClickListener bookClickListener, Fragment fragment) {
        this.books = books;
        this.bookClickListener = bookClickListener;
        this.fragment = fragment;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder viewHolder, int i) {
        Book book = books.get(i);
        viewHolder.setBookTitle(book.getVolumeInfo().getTitle());
        viewHolder.setId(book.getId());
        viewHolder.setBookImage(book.getVolumeInfo().getImageLinks() != null ?
                book.getVolumeInfo().getImageLinks().getSmallThumbnail() :
                IMAGE_URL_NOT_FOUND);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setData(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
        notifyDataSetChanged();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String id;
        private String imageUrl;

        @BindView(R.id.image_book_thumbnail)
        ImageView bookImage;

        @BindView(R.id.text_book_title)
        TextView bookTitle;

        public BookViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            bookClickListener.onBookClicked(id, imageUrl);
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setBookTitle(String title) {
            bookTitle.setText(title);
        }

        public void setBookImage(String imageUrl) {
            this.imageUrl = imageUrl;
            Glide.with(fragment)
                    .load(imageUrl)
                    .error(R.drawable.image_not_found)
                    .into(bookImage);
        }
    }
}
