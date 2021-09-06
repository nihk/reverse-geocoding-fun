package nick.template.di

import android.content.Context
import android.location.Geocoder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nick.template.geo.AndroidGeocoderRepository
import nick.template.geo.GeocoderRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun geocoderRepository(repository: AndroidGeocoderRepository): GeocoderRepository

    companion object {
        @Provides
        fun geocoder(@ApplicationContext context: Context) = Geocoder(context)
    }
}
