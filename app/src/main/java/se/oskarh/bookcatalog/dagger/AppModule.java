package se.oskarh.bookcatalog.dagger;

import android.app.Application;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import se.oskarh.bookcatalog.BuildConfig;
import se.oskarh.bookcatalog.network.api.BookCatalogApi;
import se.oskarh.bookcatalog.network.api.BookCatalogService;

@Module
public class AppModule {

    public static final String GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/";

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    BookCatalogService provideBookCatalogService() {
        return new Retrofit.Builder()
                .baseUrl(GOOGLE_BOOKS_BASE_URL)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(BookCatalogService.class);
    }

    @Provides
    @Singleton
    BookCatalogApi provideBookCatalogApi(BookCatalogService bookCatalogService) {
        return new BookCatalogApi(bookCatalogService);
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(createLoggingInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor createLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }
}
