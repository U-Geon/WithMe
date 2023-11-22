package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.withme.PaymentActivity
import com.example.withme.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var binding : FragmentHomeBinding? = null

    companion object {
        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }

    // 뷰가 생성되었을 때 - 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 안심동행 신청 버튼 클릭 시
        binding?.btnEnroll?.setOnClickListener {
            // 다음 액티비티로 이동하는 인텐트 생성
            val intent = Intent(requireActivity(), ServiceActivity::class.java)
            startActivity(intent)
        }

        // 예치금 입금 바로가기 버튼 클릭 시
        binding?.btnDeposit?.setOnClickListener {
            // 다음 액티비티로 이동하는 인텐트 생성
            val intent = Intent(requireActivity(), PaymentActivity::class.java)
            startActivity(intent)
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}