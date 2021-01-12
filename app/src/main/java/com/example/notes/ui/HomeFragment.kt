package com.example.notes.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.Adapter.MyAdapter
import com.example.notes.R
import com.example.notes.databinding.FragmentHomeBinding
import com.example.notes.db.Notes
import com.example.notes.db.NotesDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    //FragmentHomeBinding class is automatically generated by viewBinding Plugin
    private var _binding:FragmentHomeBinding?=null
    //Read Only Variable
    private val binding get()=_binding!!
    lateinit var list:List<Notes>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding= FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _binding?.myLayout.apply {
            this?.setHasFixedSize(true)
            this?.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }

        lifecycleScope.launch{
            list=NotesDataBase(activity!!).getDao().getAllNotes()
            _binding?.myLayout?.adapter=MyAdapter(list)
        }

        _binding?.addBtn?.setOnClickListener {
            //HomeFragmentDirections class is automatically generated by Navigation architecture
            val action=HomeFragmentDirections.addNotes()
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_items,menu)
        menu.findItem(R.id.delete).isVisible = false
        menu.findItem(R.id.delete_all).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.delete_all){
            userChoice()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun userChoice(){
        val builder= AlertDialog.Builder(requireContext())
        builder.setTitle("Delete everthing?")
        builder.setMessage("Are you sure you want to delete all the notes!")
        builder.setPositiveButton("Yes"){ _,_ ->
            deleteAllNotes()
            Toast.makeText(requireContext(),"All the notes are successfully deleted!!",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.create().show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun deleteAllNotes() {
        lifecycleScope.launch{
            NotesDataBase(activity!!).getDao().deleteAll()
            list=NotesDataBase(activity!!).getDao().getAllNotes()
            _binding?.myLayout?.adapter=MyAdapter(list)
        }
    }

}
