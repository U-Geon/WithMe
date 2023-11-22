package com.example.withme.administrator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.databinding.FragmentAdminDepositBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale


class AdminDepositFragment : Fragment() {
    private var _binding: FragmentAdminDepositBinding? = null

    private var originalAdminDepositList: ArrayList<AdminDepositList> = arrayListOf()
    private var filteredAdminDepositList: ArrayList<AdminDepositList> = arrayListOf()

    companion object {
        fun newInstance(): AdminDepositFragment {
            return AdminDepositFragment()
        }
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminDepositBinding.inflate(inflater, container, false)
        val view = binding.root

        // id, 사용자 이름 통신 코드
        val url = "http://192.168.80.102:8000//"

        val params = JSONObject()

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            Response.Listener { response ->
                try {
                    val userId = response.getString("name")
                    val name = response.getString("name")

                    // 마이 페이지 리스트
                    val AdminDepositList = arrayListOf(
                        AdminDepositList("$userId ($name)")
                    )

                    filteredAdminDepositList.addAll(originalAdminDepositList)

                    binding.rvAdmindeposit.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvAdmindeposit.setHasFixedSize(true)

                    binding.rvAdmindeposit.adapter =
                        AdminDepositAdapter(AdminDepositList) { item ->
                            if (item.admindeposit == "$userId ($name)") {
                                val intent = Intent(
                                    requireContext(),
                                    AdminDepositManagementActivity::class.java
                                )
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

        initSearchView()  // SearchView 초기화

        return view
    }

    // 검색창 부분
    private fun initSearchView() {
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 사용자가 검색 버튼을 눌렀을 때의 동작 추가해야 함
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경될 때 호출되는 부분
                filterAdminDepositList(newText)
                return true
            }
        })
    }

    private fun filterAdminDepositList(query: String?) {
        filteredAdminDepositList.clear()

        if (query.isNullOrBlank()) {
            filteredAdminDepositList.addAll(originalAdminDepositList)
        } else {
            val lowerCaseQuery = query.toLowerCase(Locale.getDefault())

            for (item in originalAdminDepositList) {
                if (item.admindeposit.toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    filteredAdminDepositList.add(item)
                }
            }
        }

        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        binding.rvAdmindeposit.adapter?.notifyDataSetChanged()
    }


}