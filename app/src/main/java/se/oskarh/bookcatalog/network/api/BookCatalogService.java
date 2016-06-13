package se.oskarh.bookcatalog.network.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;
import se.oskarh.bookcatalog.network.model.BookDetails;
import se.oskarh.bookcatalog.network.model.BookList;

public interface BookCatalogService {

    @GET("volumes")
    Single<BookList> getBooks(@Query("q") String query);

    @GET("volumes/{id}")
    Single<BookDetails> getBookById(@Path("id") String id);
}
