package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.ui.VillagerAdapter
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

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.villagerListFiltered.collect{list ->
                    if (list.isNotEmpty()){
                        val fragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_discover_container)
                        fragmentContainerView.visibility = View.VISIBLE
                        fragmentContainerView.setOnClickListener {
                            Log.d("HOME_FRAGMENT", "ON_CLICK")
                          bottomNavigationView.selectedItemId = R.id.item_discover
                        }

                        replaceFragment(DiscoverFragment(), fragmentContainerView.id)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.favoritesList.collect{list ->
                    if (list.isNotEmpty()){
                        val fragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_favorite_container)
                        fragmentContainerView.visibility = View.VISIBLE
                        fragmentContainerView.setOnClickListener {
                            Log.d("HOME_FRAGMENT", "ON_CLICK")
                            bottomNavigationView.selectedItemId = R.id.item_favorites
                        }

                        replaceFragment(FavoriteFragment(), fragmentContainerView.id)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.listOfNames.collect{list ->
                    if (list.isNotEmpty()){
                        val fragmentContainerView = view.findViewById<FragmentContainerView>(R.id.fragment_custom_container)
                        fragmentContainerView.visibility = View.VISIBLE
                        fragmentContainerView.setOnClickListener {
                            Log.d("HOME_FRAGMENT", "ON_CLICK")
                            bottomNavigationView.selectedItemId = R.id.item_custom
                        }

                        replaceFragment(CustomListsFragment(), fragmentContainerView.id)
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

    private fun replaceFragment(fragment: Fragment, fragmentContainerId: Int, fragmentManager: FragmentManager = childFragmentManager){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentContainerId, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}