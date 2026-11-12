package com.interview.assessment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    // 新增了 name 参数，默认为 null
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null
    ): CharacterResponse

    @GET("api/character/{id}")
    suspend fun getCharacterDetail(@Path("id") id: Int): Character
}

object RetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/"
    val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}