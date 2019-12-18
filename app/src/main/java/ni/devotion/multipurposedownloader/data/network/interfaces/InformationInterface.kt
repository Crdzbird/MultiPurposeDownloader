package ni.devotion.multipurposedownloader.data.network.interfaces

import ni.devotion.multipurposedownloader.models.Information
import retrofit2.http.GET

interface InformationInterface {
    @GET("raw/wgkJgazE")
    suspend fun requestCountry(): List<Information>
}