package com.example.notes.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.databinding.ItemViewBinding
import com.example.notes.db.Notes
import com.example.notes.ui.HomeFragmentDirections

class MyAdapter(): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private lateinit var list:List<Notes>

     constructor(list: List<Notes>) : this() {
         this.list=list
     }
    private lateinit var binding:ItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder:MyAdapter.MyViewHolder, position: Int) {
       binding.noteTxt.text=list[position].note

       holder.itemView.setOnClickListener{
           var action=HomeFragmentDirections.addNotes(list[position])
           Navigation.findNavController(it!!).navigate(action)
       }

    }

    override fun getItemCount()=list.size

     inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         init {
             binding= ItemViewBinding.bind(itemView)
         }
    }
}