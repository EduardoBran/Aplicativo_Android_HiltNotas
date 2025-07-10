package com.luizeduardobrandao.hiltnotas.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.hiltnotas.R
import com.luizeduardobrandao.hiltnotas.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

// * Fragment de boas-vindas, sem menu.

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    // private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Infla o layout de boas-vindas
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navega para a lista ao clicar em "Iniciar"
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}