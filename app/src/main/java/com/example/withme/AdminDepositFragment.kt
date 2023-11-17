package com.example.withme.administrator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.example.withme.AdminDepositList
import com.example.withme.DepositChargingHistoryActivity
import com.example.withme.MyList
import com.example.withme.MyListAdapter
import com.example.withme.PaymentActivity
import com.example.withme.ServiceUsageHistoryActivity
import com.example.withme.databinding.FragmentAdminDepositBinding
import com.example.withme.databinding.FragmentMyBinding
import org.json.JSONException
import org.json.JSONObject


class AdminDepositFragment : Fragment() {
    private var _binding : FragmentAdminDepositBinding? = null

    companion object {
        fun newInstance() : AdminDepositFragment {
            return AdminDepositFragment()
        }
    }

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAdminDepositBinding.inflate(inflater, container, false)
        val view = binding.root


        // id, 사용자 이름 통신 코드
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
                        binding.tvAdmindeposit.text = "$id ($name)"

                        // 마이 페이지 리스트
                        val AdminDepositList = arrayListOf(
                            AdminDepositList("$id ($name)")
                        )

                        binding.rvAdmindeposit.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvAdmindeposit.setHasFixedSize(true)

                        binding.rvAdmindeposit.adapter = MyListAdapter(MyList) { item ->
                            if (item.admindeposit == "$id ($name)") {
                                val intent = Intent(requireContext(), PaymentActivity::class.java)
                                startActivity(intent)
                            }
                        }

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
        return view
    }
}

//    private fun initSearchView() {
//        // init SearchView
//        binding.search.isSubmitButtonEnabled = true
//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // @TODO
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // @TODO
//                return true
//            }
//        })
//    }

}