package me.proton.coffmancorrim.acnhvillagercatalog.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentVillagersListBinding
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.ui.VillagerAdapter
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class VillagersListFragment : Fragment(){
    private lateinit var villagerRecyclerView: RecyclerView
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var parentView: View
    private lateinit var binding: FragmentVillagersListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVillagersListBinding.inflate(inflater, container, false)
        villagerRecyclerView = binding.recyclerVillager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.fillVillagerData()

        val bottomNavigationView = (requireActivity() as MainActivity).binding.bottomNav
        val searchView = binding.searchVillagers

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
                setupObservers(fragmentManager, bottomNavigationView, searchView)
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

    private fun setupObservers(fragmentManager: FragmentManager, bottomNavigationView: BottomNavigationView, searchView: SearchView) {
        lifecycleScope.launch {
            villagerRecyclerView.layoutManager = LinearLayoutManager(view?.context)
            val textError = view?.findViewById<TextView>(R.id.error_message)
            val progressBar = view?.findViewById<ProgressBar>(R.id.progress_bar)
            mainViewModel.villagerList.collect { event ->
                when (event) {
                    MainViewModel.VillagerEvent.Error -> {
                        textError?.visibility = View.VISIBLE
                        progressBar?.visibility = View.GONE
                        villagerRecyclerView.visibility = View.GONE

                    }
                    MainViewModel.VillagerEvent.Loading -> {
                        textError?.visibility = View.GONE
                        progressBar?.visibility = View.VISIBLE
                        villagerRecyclerView.visibility = View.GONE

                    }
                    is MainViewModel.VillagerEvent.Success -> {
                        textError?.visibility = View.GONE
                        progressBar?.visibility = View.GONE
                        villagerRecyclerView.visibility = View.VISIBLE

                        val villagerAdapter = VillagerAdapter(
                            mainViewModel.filterListForFavorites(event.villagersList, mainViewModel.favoritesList.value),
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
    }



}