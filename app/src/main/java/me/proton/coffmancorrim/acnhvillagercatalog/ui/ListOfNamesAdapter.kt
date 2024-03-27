package me.proton.coffmancorrim.acnhvillagercatalog.ui

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.ui.common.VillagersListFragment
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class ListOfNamesAdapter(
    private val listOfNames: List<ListWrapper>,
    private val fragmentManager: FragmentManager,
    private val mainViewModel: MainViewModel,
    private val parentId: Int,
    private val bottomNavigationView: BottomNavigationView
    )
    : RecyclerView.Adapter<ListOfNamesAdapter.ListOfNamesHolder>() {

    private var filteredList: List<ListWrapper> = listOfNames

    inner class ListOfNamesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listName = itemView.findViewById<TextView>(R.id.text_list_name)
        val listSize = itemView.findViewById<TextView>(R.id.text_list_size)
        val imageOptionsIcon = itemView.findViewById<ImageView>(R.id.icon_more_horizontal)
        val disableView = itemView.findViewById<View>(R.id.viewDisableLayout)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListOfNamesHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_name, viewGroup, false)
        return ListOfNamesHolder(itemView)
    }

    override fun onBindViewHolder(listOfNamesHolder: ListOfNamesHolder, position: Int) {
        val listWrapper = filteredList[position]
        listOfNamesHolder.listName.text = listWrapper.listName
        listOfNamesHolder.listSize.text = "Size: Unknown"
        listOfNamesHolder.imageOptionsIcon.setOnClickListener {
            showPopupMenu(listOfNamesHolder, position)
        }

        listOfNamesHolder.itemView.setOnClickListener {
            mainViewModel.customKey = listWrapper.keyId

            val fragmentTransaction = fragmentManager.beginTransaction()
            val newFragment = VillagersListFragment()
            fragmentTransaction.replace(R.id.fragment_container_view, newFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        Log.d("IS_CLICKABLE", mainViewModel.isListClickable.value.toString())
        if(!mainViewModel.isListClickable.value){
            listOfNamesHolder.disableView.visibility = View.VISIBLE
            listOfNamesHolder.disableView.setOnClickListener {
                Log.d("IS_CLICKABLE", "mainViewModel.isListClickable.value.toString()")
                bottomNavigationView.selectedItemId = parentId
            }
        }else{
            listOfNamesHolder.disableView.visibility = View.GONE
            listOfNamesHolder.disableView.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    private fun showPopupMenu(listOfNamesHolder: ListOfNamesHolder, position: Int) {
        val popupMenu = PopupMenu(listOfNamesHolder.imageOptionsIcon.context, listOfNamesHolder.imageOptionsIcon)
        popupMenu.inflate(R.menu.menu_custom_list_options)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_rename -> {

                    showRenameDialog(listOfNamesHolder.listName, position, listOfNamesHolder.imageOptionsIcon.context)
                    true
                }
                R.id.action_delete -> {

                    mainViewModel.removeCustomList(filteredList[position])
                    notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showRenameDialog(textView: TextView, position: Int, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Rename Item")

        val input = EditText(context)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val newName = input.text.toString()
            textView.text = newName
            mainViewModel.renameListWrapper(newName, position)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun filter(query: String) {
        Log.d("Filter", "Filtering with query: $query")

        filteredList = if (query.isEmpty()) {
            Log.d("Filter", "Query is empty, resetting to original list")
            listOfNames.toList()
        } else {
            val filtered = listOfNames.filter { item ->
                val nameContains = item.listName.contains(query, ignoreCase = true)
                val contains = nameContains

                Log.d("Filter", "Item: ${item.listName}, Query: $query, Name Contains: $nameContains, Contains: $contains")
                contains
            }
            Log.d("Filter", "Filtered list size: ${filtered.size}")
            filtered
        }

        notifyDataSetChanged()
    }




}