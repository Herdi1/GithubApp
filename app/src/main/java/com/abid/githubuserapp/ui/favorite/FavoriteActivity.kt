package com.abid.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.databinding.ActivityFavoriteBinding
import com.abid.githubuserapp.ui.ViewModelFactory
import com.abid.githubuserapp.ui.detail.DetailActivity
import com.abid.githubuserapp.ui.main.UserAdapter
import com.abid.githubuserapp.ui.setting.SettingPreference
import com.abid.githubuserapp.ui.setting.datastore

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val pref = SettingPreference.getInstance(application.datastore)
        viewModel = obtainViewModel(this@FavoriteActivity, pref)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorites.addItemDecoration(itemDecoration)

        viewModel.getAllFavorites().observe(this){users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.name, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setUserData(items)
        }
    }

    private fun setUserData(userData: List<ItemsItem>){
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.layoutManager = layoutManager
        val adapter = UserAdapter{userClicked ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", userClicked.login)
            startActivity(intent)
        }
        adapter.submitList(userData)
        binding.rvFavorites.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity, pref: SettingPreference): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}