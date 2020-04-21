package com.rithikjain.projectgists.ui.gists

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.adapter.GistListAdapter
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.File
import com.rithikjain.projectgists.util.*
import com.rithikjain.projectgists.util.PrefHelper.set
import kotlinx.android.synthetic.main.fragment_gists.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class GistsFragment : Fragment() {

    private lateinit var files: MutableList<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gistsViewModel by sharedViewModel<GistsViewModel>()
        gistsViewModel.fileName = ""
        gistsViewModel.fileCode = ""
        gistsViewModel.fileDescription = ""

        val sharedPref = PrefHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        sharedPref[Constants.PREF_EDIT_CODE] = ""
        sharedPref[Constants.PREF_EDIT_DESCRIPTION] = ""

        val firebaseAuth = FirebaseAuth.getInstance()
        val photoUri = firebaseAuth.currentUser!!.photoUrl

        gistsProgress.setIndeterminateDrawable(WanderingCubes())
        gistsProgress.hide()
        gistsRefresh.isRefreshing = false
        noGistsText.hide()

        toolbarTitle.text = "My Gists"
        toolbarSubtitle.text = "${firebaseAuth.currentUser!!.displayName}"

        Glide.with(this)
            .load(photoUri)
            .centerCrop()
            .placeholder(R.drawable.ic_github_logo)
            .into(profileImage)

        profileImage.setOnClickListener {
            val action = GistsFragmentDirections.actionGistsFragmentToProfileFragment()
            findNavController().navigate(action)
        }

        val gistListAdapter = GistListAdapter()
        gistsRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = gistListAdapter
        }

        getGists(gistsViewModel, gistListAdapter)

        gistsRefresh.setOnRefreshListener {
            if (isNetworkAvailable()) {
                getGists(gistsViewModel, gistListAdapter)
            } else {
                gistsRefresh.isRefreshing = false
                requireContext().shortToast("No Internet")
            }
        }

        gistsRecyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val content = files[position].Content
                val language = files[position].Language.toLowerCase(Locale.ROOT)
                val filename = files[position].Filename
                val description = files[position].Description
                val gistID = files[position].GistID
                Log.d("esh", language)
                val action = GistsFragmentDirections.actionGistsFragmentToCodeFragment(
                    content,
                    filename,
                    true,
                    description,
                    gistID
                )
                findNavController().navigate(action)
            }
        })

        gistsRecyclerView.addOnItemLongClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val deleteGistRequest = DeleteGistRequest(
                    gistListAdapter.gistList[position].GistID,
                    gistListAdapter.gistList[position].Filename
                )

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(gistListAdapter.gistList[position].Filename)
                    .setMessage("Delete this gist?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Yes") { dialog, _ ->
                        gistsViewModel.deleteGist(deleteGistRequest)
                            .observe(viewLifecycleOwner, Observer {
                                when (it.status) {
                                    Result.Status.LOADING -> {

                                    }
                                    Result.Status.SUCCESS -> {
                                        gistListAdapter.remove(position)

                                        dialog.dismiss()
                                        requireContext().shortToast("Deleted")
                                    }
                                    Result.Status.ERROR -> {
                                        if (it.message != "404 Not Found") {
                                            requireContext().shortToast("Error Occurred!")
                                        } else {
                                            gistListAdapter.remove(position)
                                            dialog.dismiss()
                                            requireContext().shortToast("Deleted")
                                        }
                                    }
                                }
                            })
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        })

        addGistBtn.setOnClickListener {
            if (isNetworkAvailable()) {
                val action = GistsFragmentDirections.actionGistsFragmentToAddGistFragment()
                findNavController().navigate(action)
            } else {
                requireContext().shortToast("Need internet to create")
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getGists(gistsViewModel: GistsViewModel, gistListAdapter: GistListAdapter) {
        gistsViewModel.viewAllGists().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    gistsRefresh.disable()
                }
                Result.Status.SUCCESS -> {
                    gistsRefresh.enable()
                    gistsRefresh.isRefreshing = false

                    files = it.data as MutableList<File>
                    if (files.isNotEmpty()) {
                        gistListAdapter.updateGists(files)
                        noGistsText.hide()
                    } else {
                        noGistsText.show()
                    }
                }
                Result.Status.ERROR -> {
                    if (!(it.message == getString(R.string.internet_error) || it.message == "404 Not Found")) {
                        requireContext().shortToast("Fatal Error")
                    }
                    gistsRefresh.enable()
                    gistsRefresh.isRefreshing = false
                    Log.d("esh", it.message)
                }
            }
        })
    }
}
