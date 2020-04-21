package com.rithikjain.projectgists.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.gists.File
import kotlinx.android.synthetic.main.gist_recycler_view_item.view.*

class GistListAdapter : RecyclerView.Adapter<GistListAdapter.GistViewHolder>() {

    var gistList: MutableList<File> = mutableListOf()

    fun updateGists(newGists: List<File>) {
        gistList = newGists as MutableList<File>
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        gistList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GistViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.gist_recycler_view_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: GistViewHolder, position: Int) {
        holder.bind(gistList[position])
    }

    override fun getItemCount() = gistList.size

    class GistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val fileNameText = view.fileNameText
        private val descriptionText = view.descriptionText

        fun bind(file: File) {
            fileNameText.text = file.Filename
            descriptionText.text = file.Description
        }
    }

}