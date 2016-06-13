package se.oskarh.bookcatalog;

import android.app.Application;

import se.oskarh.bookcatalog.dagger.AppComponent;
import se.oskarh.bookcatalog.dagger.AppModule;
import se.oskarh.bookcatalog.dagger.DaggerAppComponent;
import timber.log.Timber;

public class BookCatalogApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}
