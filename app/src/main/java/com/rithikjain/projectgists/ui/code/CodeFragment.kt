package com.rithikjain.projectgists.ui.code

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.util.shortToast
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.android.synthetic.main.fragment_code.*


class CodeFragment : Fragment() {

    private val args: CodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val gistID = args.gistID
        val description = args.description
        val codeString = args.codeString
        val filename = args.fileName
        val canEdit = args.canEdit

        codeToolbar.title = filename
        codeToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        codeToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (canEdit) {
            codeToolbar.inflateMenu(R.menu.code_toolbar_menu)
            codeToolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.editButton) {
                    if (isNetworkAvailable()) {
                        val action = CodeFragmentDirections.actionCodeFragmentToEditGistFragment(
                            gistID,
                            filename,
                            description,
                            codeString
                        )
                        findNavController().navigate(action)
                    } else {
                        requireContext().shortToast("Need internet to edit")
                    }
                }
                return@setOnMenuItemClickListener false
            }
        }

        codeView.setOptions(
            Options.Default.get(requireContext())
                .withCode(codeString)
                .withTheme(ColorTheme.MONOKAI)
        )
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
