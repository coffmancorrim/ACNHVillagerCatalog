package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.ui.ListOfNamesAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.ui.VillagerAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class CustomListsFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_custom_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        val addButton = view.findViewById<ImageView>(R.id.image_add_icon)
        val searchView = view.findViewById<SearchView>(R.id.search_custom_lists)

        if(!mainViewModel.isListClickable.value){
            searchView.visibility = View.GONE
        }

        if (!mainViewModel.isListClickable.value){
            addButton.visibility = View.GONE
        }else{
            addButton.visibility = View.VISIBLE
        }

        val customListsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_custom_list)
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