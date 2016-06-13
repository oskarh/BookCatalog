package se.oskarh.bookcatalog.network.api;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import se.oskarh.bookcatalog.network.model.BookDetails;
import se.oskarh.bookcatalog.network.model.BookList;

public class BookCatalogApi {

    BookCatalogService bookCatalogService;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public BookCatalogApi(BookCatalogService rxBookCatalogService) {
        this.bookCatalogService = rxBookCatalogService;
    }

    public void getBooks(String query, SingleSubscriber<BookList> subscriber) {
        compositeSubscription.add(bookCatalogService.getBooks(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    public void getBookDetails(String id, SingleSubscriber<BookDetails> subscriber) {
        compositeSubscription.add(bookCatalogService.getBookById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    public void unsubscribe() {
        compositeSubscription.unsubscribe();
        compositeSubscription = new CompositeSubscription();
    }
}
