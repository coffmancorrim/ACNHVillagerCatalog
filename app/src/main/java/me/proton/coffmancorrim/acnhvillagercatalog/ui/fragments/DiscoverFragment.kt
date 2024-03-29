package me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentDiscoverBinding
import me.proton.coffmancorrim.acnhvillagercatalog.ui.common.VillagersListFragment
import me.proton.coffmancorrim.acnhvillagercatalog.util.FragmentUtil
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class DiscoverFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentUtil.replaceFragment(childFragmentManager, VillagersListFragment(), R.id.fragment_inner_container_view)
    }

}

