package com.goodteam.baoofficial.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.goodteam.baoofficial.databinding.FragmentInformationBinding
import kotlin.system.exitProcess

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[InformationViewModel::class.java]

        _binding = FragmentInformationBinding.inflate(inflater, container, false)

/*
        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCloseApp.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("EXIT").setMessage("Do you want to close this app?")
            builder.setPositiveButton(
                "Yes"
            ) { _, _ ->
                finishAffinity(requireActivity())
                exitProcess(0)
            }
            builder.setNegativeButton(
                "No"
            ) { _, _ -> }
            builder.show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}