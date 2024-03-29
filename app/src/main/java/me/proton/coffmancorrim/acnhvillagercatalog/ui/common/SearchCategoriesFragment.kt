package me.proton.coffmancorrim.acnhvillagercatalog.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentSearchCategoriesBinding

class SearchCategoriesFragment : Fragment() {
    private lateinit var binding: FragmentSearchCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

}