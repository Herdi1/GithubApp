package com.abid.githubuserapp.data.retrofit

import com.abid.githubuserapp.data.response.DetailResponse
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.data.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/search/users")
    @Headers("Authorization: token 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")
    fun getUser(
        @Query("q") username : String
    ): Call<UsersResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}