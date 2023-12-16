package com.abid.githubuserapp.ui.detail

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abid.githubuserapp.data.response.DetailResponse
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.data.retrofit.ApiConfig
import com.abid.githubuserapp.database.Favorite
import com.abid.githubuserapp.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {
    private val _userDetail = MutableLiveData<DetailResponse>()
    val userDetail: LiveData<DetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _fragmentLoading = MutableLiveData<Boolean>()
    val fragmentLoading: LiveData<Boolean> = _fragmentLoading

    private val _getFollowers = MutableLiveData<List<ItemsItem>>()
    val getFollowers: LiveData<List<ItemsItem>> = _getFollowers

    private val _getFollowing = MutableLiveData<List<ItemsItem>>()
    val getFollowing: LiveData<List<ItemsItem>> = _getFollowing

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getUserDetail(username: String): Boolean{
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    _userDetail.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
        return false
    }

    fun getFollowers(username: String): Boolean{
        _fragmentLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if(response.isSuccessful){
                    _fragmentLoading.value = false
                    _getFollowers.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
        return false
    }

    fun getFollowing(username: String): Boolean{
        _fragmentLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if(response.isSuccessful){
                    _fragmentLoading.value = false
                    _getFollowing.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
        return false
    }

    fun checkFav(username: String): LiveData<Favorite> {
        return mFavoriteRepository.check(username)
    }

    fun insertFav(favorite: Favorite){
        mFavoriteRepository.insert(favorite)
    }

    fun deleteFav(favorite: Favorite){
        mFavoriteRepository.delete(favorite)
    }
}