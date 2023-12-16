package com.abid.githubuserapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abid.githubuserapp.R
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.databinding.ActivityMainBinding
import com.abid.githubuserapp.ui.ViewModelFactory
import com.abid.githubuserapp.ui.detail.DetailActivity
import com.abid.githubuserapp.ui.favorite.FavoriteActivity
import com.abid.githubuserapp.ui.setting.SettingActivity
import com.abid.githubuserapp.ui.setting.SettingPreference
import com.abid.githubuserapp.ui.setting.datastore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val pref = SettingPreference.getInstance(application.datastore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(pref, application)).get(
            MainViewModel::class.java
        )

        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvList.addItemDecoration(itemDecoration)

        viewModel.listItem.observe(this){userData ->
            setUserData(userData)
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    if(!searchView.text.isNullOrEmpty()){
                        searchBar.text = searchView.text
                        val users = viewModel.getUser(searchView.text.toString())
                        searchView.hide()
                        if(users) Toast.makeText(this@MainActivity, "Cannot retrieve users data", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
        }

        viewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setUserData(userData: List<ItemsItem>){
        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val adapter = UserAdapter{userClicked ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", userClicked.login)
            startActivity(intent)
        }
        adapter.submitList(userData)
        binding.rvList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_setting -> {
                val moveIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.action_favorite -> {
                val moveIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
