package app.keyboardly.addon.sample.data.remote

import android.content.Context
import app.keyboardly.addon.sample.data.model.Province
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ProvinceService {

    @GET("d223b1f036a963360f57cebf363fb9bb/raw/d4292b351175c3e1ca77ddb98127b0f01babc0b4/id-provincy-list.json")
    suspend fun getList(): MutableList<Province>?

    companion object {
        private const val URL = "https://gist.github.com/fnzainal/"

        fun client(context: Context): ProvinceService {
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)

            httpClient.apply {
                addNetworkInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .build()
                )
                cache(null)
            }

            return Retrofit.Builder()
                .baseUrl(URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProvinceService::class.java)
        }

    }
}