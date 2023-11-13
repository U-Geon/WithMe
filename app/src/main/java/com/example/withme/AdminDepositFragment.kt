package com.example.withme.administrator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.withme.databinding.ActivityAdminDepositManagementBinding
import com.example.withme.databinding.FragmentAdminDepositBinding


class AdminDepositFragment : Fragment() {
    private var binding : FragmentAdminDepositBinding? = null

    companion object {
        fun newInstance() : AdminDepositFragment {
            return AdminDepositFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminDepositBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    private fun initSearchView() {
        // init SearchView
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // @TODO
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // @TODO
                return true
            }
        })
    }

}