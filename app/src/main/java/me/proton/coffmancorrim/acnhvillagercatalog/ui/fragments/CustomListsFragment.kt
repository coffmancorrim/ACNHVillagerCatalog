package me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.MainActivity
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentCustomListsBinding
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentDiscoverBinding
import me.proton.coffmancorrim.acnhvillagercatalog.ui.ListOfNamesAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class CustomListsFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentCustomListsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = (requireActivity() as MainActivity).binding.bottomNav
        val addButton = binding.imageAddIcon
        val searchView = binding.searchCustomLists

        if (!mainViewModel.isListClickable.value) {
            searchView.visibility = View.GONE
            addButton.visibility = View.GONE
        } else {
            addButton.visibility = View.VISIBLE
        }

        val customListsRecyclerView = binding.recyclerCustomList
        customListsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.listOfNames.collect{list ->
                    val customAdapter = ListOfNamesAdapter(
                        list,
                        requireActivity().supportFragmentManager,
                        mainViewModel,
                        R.id.item_custom,
                        bottomNavigationView
                    )
                    customListsRecyclerView.adapter = customAdapter
                    addButton.setOnClickListener {
                        mainViewModel.addCustomListHelper()
                        customAdapter.notifyDataSetChanged()
                    }
                    setupSearchView(searchView, customAdapter)
                }
            }
        }
    }

    private fun setupSearchView(searchView: SearchView, adapter: ListOfNamesAdapter) {
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter(newText)
                    return true
                }
            }
        )
    }

}