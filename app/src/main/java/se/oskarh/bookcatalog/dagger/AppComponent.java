package se.oskarh.bookcatalog.dagger;

import javax.inject.Singleton;

import dagger.Component;
import se.oskarh.bookcatalog.ui.browse.BrowseFragment;
import se.oskarh.bookcatalog.ui.detail.DetailFragment;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(BrowseFragment browseFragment);

    void inject(DetailFragment detailFragment);
}
