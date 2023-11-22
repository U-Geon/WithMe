package com.example.withme.administrator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.withme.databinding.FragmentAdminDepositBinding


class AdminDepositFragment : Fragment() {
    private var binding : FragmentAdminDepositBinding? = null

    companion object {
        fun newInstance() : AdminDepositFragment {
            return AdminDepositFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminDepositBinding.inflate(layoutInflater)

        return binding.root
    }
}