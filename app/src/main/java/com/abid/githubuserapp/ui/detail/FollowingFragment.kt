package com.abid.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.databinding.FragmentFollowingBinding
import com.abid.githubuserapp.ui.main.UserAdapter

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var fragmentViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        fragmentViewModel.userDetail.observe(viewLifecycleOwner){ followingsData ->
            followingsData.login?.let{
                val followings =fragmentViewModel.getFollowing(it)
                if(followings) Toast.makeText(requireContext(), "Cannot retrieve users data", Toast.LENGTH_SHORT).show()
            }
        }

        fragmentViewModel.getFollowing.observe(viewLifecycleOwner){ followingsData ->
            setUserData(followingsData)
        }

        fragmentViewModel.fragmentLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setUserData(followers: List<ItemsItem>){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListFollowing.layoutManager = layoutManager
        val adapter = UserAdapter{userClicked ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("username", userClicked.login)
            startActivity(intent)
        }
        adapter.submitList(followers)
        binding.rvListFollowing.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}