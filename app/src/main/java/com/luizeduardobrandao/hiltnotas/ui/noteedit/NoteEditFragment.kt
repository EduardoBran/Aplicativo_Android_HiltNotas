package com.luizeduardobrandao.hiltnotas.ui.noteedit

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.luizeduardobrandao.hiltnotas.R
import com.luizeduardobrandao.hiltnotas.databinding.FragmentNoteEditBinding
import com.luizeduardobrandao.hiltnotas.helper.NoteConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteEditFragment : Fragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteEditViewModel by viewModels()
    private var currentId: Long = -1L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera o argumento passado e define se vai para Fragment Criação/Edição
        currentId = arguments?.getLong(NoteConstants.BUNDLE.NOTEID) ?: -1L
        if (currentId >= 0) viewModel.loadNote(currentId)


        // --- 1) configura toolbar como ActionBar e seta de voltar ---
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbarEdit)

        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(setOf(R.id.welcomeFragment))
        NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfig)

        // --- 2) define título conforme criação ou edição ---
        val isEditMode = currentId >=0
        binding.toolbarEdit.title = if (isEditMode)
            getString(R.string.label_note_edit)
        else
            getString(R.string.label_new_note)

        // --- 3) MenuProvider para inflar o menu de delete somente em edição ---
        val menuHost: MenuHost = activity
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (isEditMode) menuInflater.inflate(R.menu.menu_note_edit, menu)
                // se for criação, não inflamos nada → só haverá a seta
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        navController.navigateUp()
                        true
                    }
                    R.id.action_delete -> {
                        viewModel.delete()
                        Toast.makeText(context, R.string.msg_deleted, Toast.LENGTH_LONG).show()
                        navController.navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED )


        // Preenche campos existentes
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.note.collectLatest { note ->
                    note?.apply {
                        binding.editTextTitle.setText(title)
                        binding.editTextDescription.setText(description)
                        binding.checkBoxComplete.isChecked = isComplete
                    }
                }
            }
        }

        // Observa validação de formulário
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isFormValid.collectLatest { valid ->
                    binding.buttonSave.isEnabled = valid
                }
            }
        }

        // Observa alterações de texto
        binding.editTextTitle.doOnTextChanged { t, _, _, _ ->
            viewModel.onInputChanged(t.toString(), binding.editTextDescription.text.toString())
        }
        binding.editTextDescription.doOnTextChanged { d, _, _, _ ->
            viewModel.onInputChanged(binding.editTextTitle.text.toString(), d.toString())
        }

        // Salvar nota
        binding.buttonSave.setOnClickListener {
            viewModel.save(
                binding.editTextTitle.text.toString().trim(),
                binding.editTextDescription.text.toString().trim(),
                binding.checkBoxComplete.isChecked
            )
            Toast.makeText(
                context, R.string.msg_saved, Toast.LENGTH_LONG
            ).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}