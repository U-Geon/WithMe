package com.example.withme.administrator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
//    private var currentAdminDepositList: ArrayList<AdminDepositList> = arrayListOf()

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
                        val sharedPrefs: SharedPreferences = (activity as AppCompatActivity).getSharedPreferences("admin", Context.MODE_PRIVATE)

                        // 클릭된 사용자의 정보를 가져오도록 수정
                        val clickedUserId = getUserIdFromItem(item)
                        val clickedUserName = getUserNameFromItem(item)

//                        Toast.makeText(context, "id: ${clickedUserId}", Toast.LENGTH_SHORT).show()

                        sharedPrefs.edit().putString("admin_deposit_id", clickedUserId).apply()
                        if (item.admindeposit == "$clickedUserId ($clickedUserName)") {
                            val intent = Intent(requireContext(), AdminDepositManagementActivity::class.java)
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

    // 클릭된 아이템에서 사용자 정보를 추출하는 도우미 함수 추가
    private fun getUserIdFromItem(item: AdminDepositList): String {
        val regexResult = Regex("""(\w+) \(\w+\)""").find(item.admindeposit)
        return regexResult?.groupValues?.get(1) ?: ""
    }

    private fun getUserNameFromItem(item: AdminDepositList): String {
        val regexResult = Regex("""\w+ \((\w+)\)""").find(item.admindeposit)
        return regexResult?.groupValues?.get(1) ?: ""
    }


    // 검색창 서버 실행 코드
    private fun executeServerRequest(query: String) {
        val searchUrl = resources.getString(R.string.ip) + "/search_users/"

        val searchParams = JSONObject()
        searchParams.put("search_query", query)  // query: 검색창에서 입력받은 검색어

        val searchRequest = JsonObjectRequest(
            Request.Method.POST,
            searchUrl,
            searchParams,
            Response.Listener { response ->
                try {
                    originalAdminDepositList.clear()  // 기존 데이터를 제거

                    userId = response.getString("id")
                    name = response.getString("name")

                    val adminDepositItem = AdminDepositList("$userId ($name)")
                    originalAdminDepositList.add(adminDepositItem)

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
                        val sharedPrefs: SharedPreferences = (activity as AppCompatActivity).getSharedPreferences("admin", Context.MODE_PRIVATE)

                        // 클릭된 사용자의 정보를 가져오도록 수정
                        val clickedUserId = getUserIdFromItem(item)
                        val clickedUserName = getUserNameFromItem(item)

                        sharedPrefs.edit().putString("admin_deposit_id", clickedUserId).apply()
                        if (item.admindeposit == "$clickedUserId ($clickedUserName)") {
                            val intent = Intent(requireContext(), AdminDepositManagementActivity::class.java)
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
                    "존재하지 않는 아이디입니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        // Volley 요청을 큐에 추가
        Volley.newRequestQueue(requireContext()).add(searchRequest)
    }
}
