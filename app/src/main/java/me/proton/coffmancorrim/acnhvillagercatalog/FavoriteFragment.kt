package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class FavoriteFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val fragmentTransaction = childFragmentManager.beginTransaction()
        val villagersListFragment = VillagersListFragment()
        fragmentTransaction.replace(R.id.fragment_inner_container_view, villagersListFragment)
        fragmentTransaction.commit()
    }

}