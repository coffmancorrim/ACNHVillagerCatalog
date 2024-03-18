package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.ui.ListOfNamesAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.ui.VillagerAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class VillagersListFragment : Fragment(){
    private lateinit var villagerRecyclerView: RecyclerView
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var parentView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_villagers_list, container, false)
        villagerRecyclerView = rootView.findViewById(R.id.recycler_villager)

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        val searchView = view.findViewById<SearchView>(R.id.search_villagers)

        if(!mainViewModel.isListClickable.value){
            searchView.visibility = View.GONE
        }

        parentView = view.parent as View

        val fragmentManager = requireActivity().supportFragmentManager

        if (parentView.parent != null && parentView.parent is View){
            parentView = parentView.parent as View
        }

        when (parentView.id){
            R.id.layout_discover -> {
                villagerRecyclerView.layoutManager = LinearLayoutManager(view.context)
                viewLifecycleOwner.lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        mainViewModel.villagerListFiltered.collect{list ->
                            mainViewModel.updateFilteredList()
                            val villagerAdapter = VillagerAdapter(
                                mainViewModel.copyRandomElements(list, 10),
                                fragmentManager,
                                mainViewModel,
                                R.id.item_discover,
                                bottomNavigationView
                            )
                            villagerRecyclerView.adapter = villagerAdapter
                            setupSearchView(searchView, villagerAdapter)
                        }
                    }
                }
            }

            R.id.layout_favorites -> {
                villagerRecyclerView.layoutManager = LinearLayoutManager(view.context)
                viewLifecycleOwner.lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        mainViewModel.toggleFavoritesBoolean()

                        mainViewModel.favoritesList.collect{list ->
                            val villagerAdapter = VillagerAdapter(
                                list,
                                fragmentManager,
                                mainViewModel,
                                R.id.item_favorites,
                                bottomNavigationView
                            )
                            villagerRecyclerView.adapter = villagerAdapter
                            setupSearchView(searchView, villagerAdapter)
                        }
                    }
                }
            }

            //R.id.layout_custom_lists
            else -> {
                villagerRecyclerView.layoutManager = LinearLayoutManager(view.context)
                viewLifecycleOwner.lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                        mainViewModel.dictOfVillagerLists.collect{dictOfLists ->
                            val villagerList = dictOfLists.get(mainViewModel.customKey)
                            if (villagerList is List<Villager>){
                                val villagerAdapter = VillagerAdapter(
                                    villagerList,
                                    fragmentManager,
                                    mainViewModel,
                                    R.id.item_custom,
                                    bottomNavigationView
                                )
                                villagerRecyclerView.adapter = villagerAdapter
                                setupSearchView(searchView, villagerAdapter)
                            }
                        }
                    }
                }
            }

        }


    }

    private fun setupSearchView(searchView: SearchView, adapter: VillagerAdapter) {
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter(newText) // Filter the adapter based on the new query text
                    return true
                }
            }
        )
    }


}