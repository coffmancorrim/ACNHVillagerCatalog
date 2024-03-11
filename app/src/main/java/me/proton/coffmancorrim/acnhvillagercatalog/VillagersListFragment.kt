package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.proton.coffmancorrim.acnhvillagercatalog.ui.VillagerAdapter

class VillagersListFragment : Fragment(){
    private lateinit var villagerRecyclerView: RecyclerView

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

        villagerRecyclerView.adapter = VillagerAdapter()
        villagerRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }
}