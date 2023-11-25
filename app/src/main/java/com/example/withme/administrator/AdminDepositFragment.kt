package com.example.withme.administrator

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.FragmentAdminDepositBinding
import org.json.JSONException
import org.json.JSONObject

class AdminDepositFragment : Fragment() {
    private var _binding: FragmentAdminDepositBinding? = null
    private val binding get() = _binding!!

    private var originalAdminDepositList: ArrayList<AdminDepositList> = arrayListOf()
    private var currentAdminDepositList: ArrayList<AdminDepositList> = arrayListOf()

    private var userId: String = ""
    private var name: String = ""

    companion object {
        fun newInstance() : AdminDepositFragment {
            return AdminDepositFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminDepositBinding.inflate(inflater, container, false)
        val view = binding.root

        // id, 사용자 이름 전체를 받아오기
        val url = resources.getString(R.string.ip) + "/find_all_account/"
        val params = JSONObject()

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    originalAdminDepositList.clear()  // 기존 데이터를 제거

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        userId = jsonObject.getString("id")
                        name = jsonObject.getString("name")
                        val adminDepositItem = AdminDepositList("$userId ($name)")
                        originalAdminDepositList.add(adminDepositItem)
                    }

                    // 마이 페이지 리스트
                    val adminDepositAdapter = AdminDepositAdapter(originalAdminDepositList) { item ->
                        if (item.admindeposit == "$userId ($name)") {
                            val intent = Intent(
                                requireContext(),
                                AdminDepositManagementActivity::class.java
                            )
                            startActivity(intent)
                        }
                    }

                    // 어댑터에 클릭 이벤트 재설정 -> 클릭되었을 때 어떤 동작을 수행할지를 정의하기 위함
                    adminDepositAdapter.setOnItemClickListener { item ->
                        if (item.admindeposit == "$userId ($name)") {
                            val intent = Intent(
                                requireContext(),
                                AdminDepositManagementActivity::class.java
                            )
                            startActivity(intent)
                        }
                    }

                    binding.rvAdmindeposit.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvAdmindeposit.setHasFixedSize(true)
                    binding.rvAdmindeposit.adapter = adminDepositAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "네트워크 오류가 발생했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        // Volley 요청을 큐에 추가
        Volley.newRequestQueue(requireContext()).add(request)

        // 검색창 부분
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {  // 엔터 키를 눌렀을 때
                if (!query.isNullOrBlank()) {
                    executeServerRequest(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 텍스트가 변경될 때의 동작 (나중에 필요하면 추가하기)
                return true
            }
        })
        return view
    }

    // 검색창 서버 실행 코드
    private fun executeServerRequest(query: String) {
        val searchUrl = "http://192.168.80.102:8000//"
        val searchParams = JSONObject().apply {
            put("search_query", query)  // query: 검색창에서 입력받은 검색어
        }

        val searchRequest = JsonObjectRequest(
            Request.Method.POST,
            searchUrl,
            searchParams,
            Response.Listener { response ->
                try {
                    val searchDataArray = response.getJSONArray("data")
                    val searchResultList = ArrayList<AdminDepositList>()

                    for (i in 0 until searchDataArray.length()) {
                        val searchDataObject = searchDataArray.getJSONObject(i)
                        val userId = searchDataObject.getString("id")
                        val name = searchDataObject.getString("name")
                        val adminDepositItem = AdminDepositList("$userId ($name)")
                        searchResultList.add(adminDepositItem)
                    }

                    // 검색 결과를 사용하여 RecyclerView 업데이트
                    currentAdminDepositList.clear()
                    currentAdminDepositList.addAll(searchResultList)
                    binding.rvAdmindeposit.adapter?.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "네트워크 오류가 발생했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        // Volley 요청을 큐에 추가
        Volley.newRequestQueue(requireContext()).add(searchRequest)
    }
}
