package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.withme.MyList
import com.example.withme.MyListAdapter
import com.example.withme.databinding.FragmentMyBinding

private lateinit var binding: FragmentMyBinding

class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null

    companion object {
        fun newInstance() : MyFragment {
            return MyFragment()
        }
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        val view = binding.root

        val MyList = arrayListOf(
            MyList("예치금 입금하기"),
            MyList("예치금 충전 내역"),
            MyList("아동상태 및 병원동행 결과 모아보기"),
        )

        binding.rvMy.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMy.setHasFixedSize(true)

//        binding.rvMy.adapter = MyListAdapter(profileList)

        binding.rvMy.adapter = MyListAdapter(MyList) { item ->
            if (item.mytext == "예치금 입금하기") {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                startActivity(intent)
            } else if (item.mytext == "예치금 충전 내역") {
                val intent = Intent(requireContext(), DepositChargingHistoryActivity::class.java)
                startActivity(intent)
            } else if (item.mytext == "아동상태 및 병원동행 결과 모아보기") {
                val intent = Intent(requireContext(), ServiceUsageHistoryActivity::class.java)
                startActivity(intent)
            }
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
