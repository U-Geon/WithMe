package com.example.withme.administrator

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.FragmentAdminHomeBinding
import org.json.JSONException
import org.json.JSONObject

data class Service (
    val startLocation: String,
    val middleLocation: String,
    val finalLocation: String,
    val kidName: String,
    val phoneNumber: String,
    val rrn: String,
    val status: String
)

class AdminHomeFragment: Fragment() {
    private var binding : FragmentAdminHomeBinding? = null

    companion object {
        fun newInstance() : AdminHomeFragment {
            return AdminHomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)

        // JSON 데이터 요청
        val url = "https://15.164.94.136:8000/admin" // GET 매핑할 URL

        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {

                    /**
                     * {
                     *   "result" : [
                     *      {
                     *          "start" : 출발 장소 이름,
                     *          "middle" : 중간 장소 이름,
                     *          "final" : 도착 장소 이름,
                     *          "kidName": 아이 이름,
                     *          "phoneNumber": 전화번호,
                     *          "rrn": 주민등록번호,
                     *          "status": 아이 상태
                     *       }
                     *       ...
                     *   ],
                     * }
                     */

                    val jsonObject = JSONObject(response)
                    val serviceResult = jsonObject.getJSONArray("result") // jsonArray 받아오기

                    val adapter = ItemAdapter()

                    for (i in 0 until serviceResult.length()) {
                        val serviceResultObject = serviceResult.getJSONObject(i)
                        adapter.addItem(
                            Service(
                                serviceResultObject.getString("start"), // 출발 장소
                                serviceResultObject.getString("middle"), // 경유 장소
                                serviceResultObject.getString("final"), // 도착 장소
                                serviceResultObject.getString("kidName"), // 아이 이름
                                serviceResultObject.getString("phoneNumber"), // 전화번호
                                serviceResultObject.getString("rrn"), // 주민등록번호
                                serviceResultObject.getString("status") // 아이 상태
                            )
                        )
                    }

                    // RecyclerView를 뷰에 추가
                    binding!!.serviceRecyclerView.adapter = adapter

                } catch (e: JSONException) {
                    // json 객체 에러
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // 네트워크 에러 처리
                Log.e("Volley", error.message!!)
            }
        ){}

        // 요청 대기열에 요청 추가
        Volley.newRequestQueue(requireContext()).add(stringRequest)

        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val itemList = mutableListOf<Service>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_service_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = itemList[position]

        // 버튼 제목 설정
        val text = service.startLocation + " - " + service.middleLocation + " - " + service.finalLocation

        holder.button.text = text

        holder.button.setOnClickListener {
            // 모달 띄우는 로직을 여기에 추가
            showCustomDialog(service, holder)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(item: Service) {
        itemList.add(item)
        notifyDataSetChanged()
    }

    fun showCustomDialog(itemName: Service, holder: ViewHolder) {
        val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
        dialogBuilder.setMessage("Clicked on $itemName")
            .setCancelable(true)
            .setPositiveButton("동행") { dialog, _ ->
                // 버튼 눌렀을 때 동작
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.service_result_button)
    }
}