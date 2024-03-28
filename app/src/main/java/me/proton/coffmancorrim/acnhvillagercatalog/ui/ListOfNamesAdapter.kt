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
import me.proton.coffmancorrim.acnhvillagercatalog.util.FragmentUtil
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class ListOfNamesAdapter(
    private var listOfNames: List<ListWrapper>,
    private val fragmentManager: FragmentManager,
    private val mainViewModel: MainViewModel,
    private val parentId: Int,
    private val bottomNavigationView: BottomNavigationView
    )
    : RecyclerView.Adapter<ListOfNamesAdapter.ListOfNamesHolder>() {
    private var filteredList: List<ListWrapper> = listOfNames

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListOfNamesHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_name, viewGroup, false)
        return ListOfNamesHolder(itemView)
    }

    inner class ListOfNamesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listName = itemView.findViewById<TextView>(R.id.text_list_name)
        val listSize = itemView.findViewById<TextView>(R.id.text_list_size)
        val imageOptionsIcon = itemView.findViewById<ImageView>(R.id.icon_more_horizontal)
        val disableView = itemView.findViewById<View>(R.id.viewDisableLayout)
    }


    override fun onBindViewHolder(listOfNamesHolder: ListOfNamesHolder, position: Int) {
        val listWrapper = filteredList[position]
        listOfNamesHolder.listName.text = listWrapper.listName
        //TODO Do something with listSize text view
        listOfNamesHolder.imageOptionsIcon.setOnClickListener {
            showOptionsMenu(listOfNamesHolder, position)
        }

        listOfNamesHolder.itemView.setOnClickListener {
            mainViewModel.customKey = listWrapper.keyId
            FragmentUtil.replaceFragment(fragmentManager, VillagersListFragment(), R.id.fragment_container_view, true)
        }

        if(!mainViewModel.isListClickable.value){
            listOfNamesHolder.disableView.visibility = View.VISIBLE
            listOfNamesHolder.disableView.setOnClickListener {
                bottomNavigationView.selectedItemId = parentId
            }
        }else{
            listOfNamesHolder.disableView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    private fun showOptionsMenu(listOfNamesHolder: ListOfNamesHolder, position: Int) {
        val optionsMenu = PopupMenu(listOfNamesHolder.imageOptionsIcon.context, listOfNamesHolder.imageOptionsIcon)
        optionsMenu.inflate(R.menu.menu_custom_list_options)

        optionsMenu.setOnMenuItemClickListener { item ->
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
        optionsMenu.show()
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
        filteredList = if (query.isEmpty()) {
            listOfNames.toList()
        } else {
            val filtered = listOfNames.filter { item ->
                val nameContains = item.listName.contains(query, ignoreCase = true)
                val contains = nameContains
                contains
            }
            filtered
        }

        notifyDataSetChanged()
    }

}