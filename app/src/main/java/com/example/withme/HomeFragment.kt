package com.example.withme

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        // 동행상황 바로가기 버튼 클릭 시
        binding?.btnSituation?.setOnClickListener {
            val sharedPrefs: SharedPreferences = (activity as AppCompatActivity).getSharedPreferences("status", Context.MODE_PRIVATE)
            if(sharedPrefs.getBoolean("status", false)) {
                val intent = Intent(ServiceActivity(), PaymentActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(activity, "서비스 사용 중이 아닙니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 로그아웃 버튼 클릭 시
        binding?.btnLogout?.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("로그아웃하시겠습니까?")

            alertDialogBuilder.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                var sharedPreferences : SharedPreferences = (activity as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("id")
                editor.apply()

                val intent = Intent(requireActivity(), LoginActivity::class.java)  // 로그아웃 후, 로그인 액티비티로 이동
                startActivity(intent)
            })

            alertDialogBuilder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show()
            })

            alertDialogBuilder.create().show()
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}