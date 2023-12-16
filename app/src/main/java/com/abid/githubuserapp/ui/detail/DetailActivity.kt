package com.abid.githubuserapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.abid.githubuserapp.R
import com.abid.githubuserapp.data.response.DetailResponse
import com.abid.githubuserapp.database.Favorite
import com.abid.githubuserapp.databinding.ActivityDetailBinding
import com.abid.githubuserapp.ui.ViewModelFactory
import com.abid.githubuserapp.ui.setting.SettingPreference
import com.abid.githubuserapp.ui.setting.datastore
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(application.datastore)
        detailViewModel = obtainViewModel(this@DetailActivity, pref)

        val login = intent.getStringExtra("username")
        if(login != null){
            val detailUser = detailViewModel.getUserDetail(login)
            if(detailUser) Toast.makeText(this@DetailActivity, "Cannot retrieve user detail", Toast.LENGTH_SHORT).show()
        }

        supportActionBar?.show()

        detailViewModel.userDetail.observe(this){ userData ->
            setUserDetail(userData)
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        var check = false

        detailViewModel.checkFav(login.toString()).observe(this){ userFav ->
            check = if (userFav != null){
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(binding.fabFavorite.context, R.drawable.ic_favorite_fill))
                true
            }else{
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(binding.fabFavorite.context, R.drawable.ic_favorite_baseline))
                false
            }
        }

        binding.fabFavorite.setOnClickListener{
            if (check){
                detailViewModel.userDetail.observe(this){ userData ->
                    deleteFromFavorite(userData)
                }
            }else{
                detailViewModel.userDetail.observe(this){ userData ->
                    addToFavorite(userData)
                }
            }
        }
    }

    private fun setUserDetail(userDetail: DetailResponse){
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .into(binding.imgProfile)
        binding.tvName.text = userDetail.name
        binding.tvUserName.text = userDetail.login
        binding.tvCompany.text = userDetail.company
        binding.tvLocation.text = userDetail.location
        binding.tvFollowers.text = userDetail.followers.toString()
        binding.tvFollowing.text = userDetail.following.toString()
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun addToFavorite(userDetail: DetailResponse){
        val favUser = userDetail.login?.let {
            Favorite(
                it,
                userDetail.avatarUrl
            )
        }
        detailViewModel.insertFav(favUser as Favorite)
    }

    private fun deleteFromFavorite(userDetail: DetailResponse){
        val favUser = userDetail.login?.let {
            Favorite(
                it,
                userDetail.avatarUrl
            )
        }
        detailViewModel.deleteFav(favUser as Favorite)
    }

    private fun obtainViewModel(activity: AppCompatActivity, pref: SettingPreference): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
}
