package com.isoftinc.film.retrofit


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class ApiClient {

    var BASE_URL = "http://3.140.12.126:3008"
    var retrofit: Retrofit? = null
    var okHttpClient: OkHttpClient = OkHttpClient()

    var gson = GsonBuilder()
            .setLenient()
            .create()

    fun getClient(): Retrofit {
        okHttpClient =  OkHttpClient.Builder().build()

            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(nullOnEmptyConverterFactory)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

        return retrofit!!
    }

    val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }
}
