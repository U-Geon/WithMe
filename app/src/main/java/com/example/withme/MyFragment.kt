package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.MyList
import com.example.withme.MyListAdapter
import com.example.withme.databinding.FragmentMyBinding
import org.json.JSONException
import org.json.JSONObject

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


        // 사용자 이름, 이용 횟수, 현재 예치금 통신 코드
        val userId = "csw1234"

        if (userId.isNotEmpty()) {
            val url = "http://192.168.80.102:8000/select_name_money_count/"

            val params = JSONObject()
            params.put("id", userId)

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                Response.Listener { response ->
                    try {
                        val name = response.getString("name")
                        binding.tvNickname.text = "$name 님, 안녕하세요"

                        val uses = response.getString("uses")
                        binding.tvUses.text = "현재까지 이용 횟수는 ${uses}건입니다."

                        val money = response.getString("money")
                        binding.tvMoney.text = "${money}원"
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            )
            // Volley 요청을 큐에 추가
            Volley.newRequestQueue(requireContext()).add(request)
        } else {
            Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }


        // 마이 페이지 리스트
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
