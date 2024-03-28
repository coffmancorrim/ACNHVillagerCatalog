package me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.util.FragmentUtil
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel


class HomeFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mainViewModel.isListClickable.value){
            mainViewModel.toggleIsListClickableBoolean()
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        val fragmentManager = requireActivity().supportFragmentManager

        val discoverFragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_discover_container)
        val discoverOnClick = {
            bottomNavigationView.selectedItemId = R.id.item_discover
            if (mainViewModel.reloadVillagerData){
                mainViewModel.toggleReloadVillagerData()
            }
        }
        replaceFragmentForList(
            fragmentManager,
            DiscoverFragment(),
            discoverFragmentContainerView,
            bottomNavigationView,
            R.id.item_discover,
            discoverOnClick
        )

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    mainViewModel.favoritesList.collect{list ->
                        val favoritesFragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_favorite_container)
                        if (list.isNotEmpty()){
                            replaceFragmentForList(fragmentManager, FavoriteFragment(), favoritesFragmentContainerView, bottomNavigationView, R.id.item_favorites)
                        }
                    }
                }
                launch {
                    mainViewModel.listOfNames.collect{list ->
                        val customListsFragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_custom_container)
                        if (list.isNotEmpty()) {
                            replaceFragmentForList(fragmentManager, CustomListsFragment(), customListsFragmentContainerView, bottomNavigationView, R.id.item_custom)
                        }
                    }
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()

        if (!mainViewModel.isListClickable.value){
            mainViewModel.toggleIsListClickableBoolean()
        }
    }

    private fun replaceFragmentForList(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        fragmentContainerView: FragmentContainerView,
        bottomNavigationView: BottomNavigationView,
        itemId: Int,
        onClick: (() -> Unit)? = null
    ) {
        fragmentContainerView.visibility = View.VISIBLE
        if(onClick != null){
            fragmentContainerView.setOnClickListener {
                onClick.invoke()
            }
        }else{
            fragmentContainerView.setOnClickListener {
                bottomNavigationView.selectedItemId = itemId
            }
        }
        FragmentUtil.replaceFragment(fragmentManager, fragment, fragmentContainerView.id)
    }

}