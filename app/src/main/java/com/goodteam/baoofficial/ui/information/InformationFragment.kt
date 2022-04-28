package com.goodteam.baoofficial.ui.information

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.goodteam.baoofficial.databinding.FragmentInformationBinding
import com.goodteam.baoofficial.ui.resources.ResourcesFragmentDirections
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
        val informationViewModel =
            ViewModelProvider(this)[InformationViewModel::class.java]

        _binding = FragmentInformationBinding.inflate(inflater, container, false)

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

        binding.tvAboutUs.setOnClickListener {
            this.findNavController()
                .navigate(InformationFragmentDirections.actionInformationFragmentToAboutUsFragment())
        }

        binding.tvShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val body = "Source code of this app is available at: "
            val sub =
                "https://github.com/quytttb/BaoOfficial"
            intent.putExtra(Intent.EXTRA_TEXT, body)
            intent.putExtra(Intent.EXTRA_TEXT, sub)
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}