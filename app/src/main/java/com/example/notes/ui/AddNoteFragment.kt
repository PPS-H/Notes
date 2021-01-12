package com.example.notes.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentAddNoteBinding
import com.example.notes.db.Notes
import com.example.notes.db.NotesDataBase
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {
    private var _Binding:FragmentAddNoteBinding?=null
    private val binding get()=_Binding!!
    private val args by navArgs<AddNoteFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _Binding= FragmentAddNoteBinding.inflate(inflater, container, false)
        binding.note.setText(args.updateTxt?.note)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.saveBtn.setOnClickListener{view->
            var text=binding.note.text.toString()
            if(text.isEmpty()){
                binding.note.error="Enter some notes to save it!!!"
                binding.note.requestFocus()
                return@setOnClickListener
            }
            var note=Notes(text)
            //while we can call NoteDataBase() with parenthesis,it will automatically call the invoke function
            lifecycleScope.launch {
                if(args.updateTxt?.note==null){
                    NotesDataBase(activity!!).getDao().addNotes(note)
                }else{
                    note.id= args.updateTxt?.id!!
                    NotesDataBase(activity!!).getDao().updateNotes(note)
                }
                var action=AddNoteFragmentDirections.backToHome()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_items,menu)
        menu.findItem(R.id.delete).isVisible = true
        menu.findItem(R.id.delete_all).isVisible = false
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.delete){
            userChoice()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun userChoice(){
        val builder=AlertDialog.Builder(requireContext())
        builder.setTitle("Delete?")
        builder.setMessage("Are you sure you want to delete")
        builder.setPositiveButton("Yes"){ _, _ ->
            deleteNote()
            Toast.makeText(requireContext(),"Successfully Deleted!!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.backToHome)
        }
        builder.setNegativeButton("No"){_, _->}
        builder.create().show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun deleteNote() {
        lifecycleScope.launch {
            NotesDataBase(activity!!).getDao().delete(args.updateTxt!!)
        }
    }

}