package com.luizeduardobrandao.hiltnotas.ui.noteslist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luizeduardobrandao.hiltnotas.R
import com.luizeduardobrandao.hiltnotas.databinding.FragmentNotesListBinding
import com.luizeduardobrandao.hiltnotas.helper.NoteConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// * Fragment que exibe a lista de notas.

@AndroidEntryPoint
class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesListViewModel by viewModels()

    private lateinit var adapter: NotesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) registrar a toolbar deste fragmento como ActionBar
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbarList)
        // 2) configurar NavigationUI para exibição de seta e título automático
        val navController = findNavController()
        // só o welcomeFragment é top-level (sem up)
        val appBarConfig = AppBarConfiguration(setOf(R.id.welcomeFragment))
        NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfig)

        setupAdapter()
        observeNotes()
        setupFab()
    }

    private fun setupAdapter() {
        adapter = NotesAdapter(
            notes = emptyList(),
            onItemClick = { note -> navigateToEdit(note.id) },
            onToggleComplete = {note -> viewModel.onToggleComplete(note)}
        )
        binding.recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NotesListFragment.adapter
        }
    }

    private fun observeNotes(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collectLatest { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    private fun setupFab(){
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_edit)
        }
    }

    private fun navigateToEdit(id: Long) {
        findNavController().navigate(
            R.id.action_list_to_edit,
            Bundle().apply { putLong(NoteConstants.BUNDLE.NOTEID, id) }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}