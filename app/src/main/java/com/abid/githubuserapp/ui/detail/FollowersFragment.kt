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
import com.abid.githubuserapp.databinding.FragmentFollowersBinding
import com.abid.githubuserapp.ui.main.UserAdapter

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var fragmentViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        fragmentViewModel.userDetail.observe(viewLifecycleOwner){ followersData ->
            followersData.login?.let{
                val followers =fragmentViewModel.getFollowers(it)
                if(followers) Toast.makeText(requireContext(), "Cannot retrieve users data", Toast.LENGTH_SHORT).show()
            }
        }

        fragmentViewModel.getFollowers.observe(viewLifecycleOwner){ followersData ->
            setUserData(followersData)
        }

        fragmentViewModel.fragmentLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setUserData(followers: List<ItemsItem>){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListFollowers.layoutManager = layoutManager
        val adapter = UserAdapter{userClicked ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("username", userClicked.login)
            startActivity(intent)
        }
        adapter.submitList(followers)
        binding.rvListFollowers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}