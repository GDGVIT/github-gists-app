package com.rithikjain.projectgists.ui.gists

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.util.hide
import com.rithikjain.projectgists.util.shortToast
import com.rithikjain.projectgists.util.show
import kotlinx.android.synthetic.main.fragment_add_gist.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddGistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_gist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gistsViewModel by sharedViewModel<GistsViewModel>()

        requireActivity().window.setBackgroundDrawableResource(R.drawable.bg)

        addGistProgress.setIndeterminateDrawable(WanderingCubes())
        addGistProgress.hide()

        fileCodeText.editText!!.setText(gistsViewModel.fileCode)
        fileNameText.editText!!.setText(gistsViewModel.fileName)
        fileDescriptionText.editText!!.setText(gistsViewModel.fileDescription)

        addGistToolbar.title = "Create Gist"
        addGistToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        addGistToolbar.setNavigationOnClickListener {
            gistsViewModel.fileName = ""
            gistsViewModel.fileCode = ""
            gistsViewModel.fileDescription = ""
            findNavController().navigateUp()
        }

        addGistToolbar.inflateMenu(R.menu.add_gist_toolbar_menu)
        addGistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.previewCode -> {
                    if (validateFields(requireContext())) {
                        gistsViewModel.fileName = fileNameText.editText!!.text.toString()
                        gistsViewModel.fileCode = fileCodeText.editText!!.text.toString()
                        gistsViewModel.fileDescription =
                            fileDescriptionText.editText!!.text.toString()
                        val action = AddGistFragmentDirections.actionAddGistFragmentToCodeFragment(
                            fileCodeText.editText!!.text.toString(),
                            fileNameText.editText!!.text.toString(),
                            false
                        )
                        findNavController().navigate(action)
                    }
                }
                R.id.saveCode -> {
                    if (validateFields(requireContext())) {
                        var isPublic = true
                        if (publicOrPrivate.checkedRadioButtonId == R.id.isPrivate) {
                            isPublic = false
                        }

                        val createGistRequest = CreateGistRequest(
                            fileDescriptionText.editText!!.text.toString(),
                            isPublic,
                            fileNameText.editText!!.text.toString(),
                            fileCodeText.editText!!.text.toString()
                        )

                        gistsViewModel.createGist(createGistRequest)
                            .observe(viewLifecycleOwner, Observer { res ->
                                when (res.status) {
                                    Result.Status.LOADING -> {
                                        detailsView.hide()
                                        addGistProgress.show()
                                    }
                                    Result.Status.SUCCESS -> {
                                        if (res.data!!.Status == 201) {
                                            addGistProgress.hide()
                                            gistsViewModel.fileDescription = ""
                                            gistsViewModel.fileCode = ""
                                            gistsViewModel.fileName = ""
                                            findNavController().navigateUp()
                                            requireContext().shortToast("Gist Created")
                                        } else {
                                            requireContext().shortToast("Hmm something went wrong!")
                                        }
                                    }
                                    Result.Status.ERROR -> {
                                        detailsView.show()
                                        addGistProgress.hide()
                                        requireContext().shortToast("Error in creating gist!")
                                    }
                                }
                            })
                    }
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun validateFields(context: Context): Boolean {
        if (fileNameText.editText!!.text.isBlank()) {
            context.shortToast("Filename cannot be empty")
            return false
        }
        if (fileCodeText.editText!!.text.isBlank()) {
            context.shortToast("Code cannot be empty")
            return false
        }
        return true
    }
}
