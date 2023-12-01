package com.example.withme.administrator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.ActivityKidInformationBinding
import com.example.withme.databinding.FragmentAdminHomeBinding
import com.example.withme.databinding.FragmentHomeBinding
import com.example.withme.databinding.FragmentSettingBinding
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

        val adapter = ItemAdapter()
        binding!!.serviceRecyclerView.adapter = adapter

        // RecyclerView에 LinearLayoutManager 설정
        val layoutManager = LinearLayoutManager(requireContext())
        binding!!.serviceRecyclerView.layoutManager = layoutManager

        // JSON 데이터 요청
        val url = resources.getString(R.string.ip) + "/admin"

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

                    adapter.notifyDataSetChanged()

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

        return binding?.root
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
        val utf8Start = String(service.startLocation.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
        val utf8Middle = String(service.middleLocation.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
        val utf8Final = String(service.finalLocation.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
        val title = utf8Start + " - " + utf8Middle + " - " + utf8Final


        holder.textView!!.text = title

        holder.layout.setOnClickListener {
            // 모달 띄우는 로직을 여기에 추가
            val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
            val start = service.startLocation
            val utf8Start = String(start.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
            val middle = service.middleLocation
            val utf8Middle = String(middle.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
            val final = service.finalLocation
            val utf8Final = String(final.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)

            val kidName = service.kidName
            val phoneNumber = service.phoneNumber
            val rrn = service.rrn

            val status = service.status
            val utf8Status = String(status.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)



            dialogBuilder.setMessage("**동행 정보**\n$utf8Start - $utf8Middle - $utf8Final\n\n**아이 인적사항**\n이름: $kidName\n전화번호: $phoneNumber\n주민등록번호: $rrn\n\n**아이 상태**\n$utf8Status")
//                .setCancelable(true)
                .setPositiveButton("동행") { dialog, _ ->
                    // 버튼 눌렀을 때 동작
                    val intent = Intent(holder.itemView.context, AdminServiceActivity::class.java) // 지도 액티비티로 이동!
                    intent.putExtra("kidName",kidName)
                    intent.putExtra("phoneNumber",phoneNumber)
                    intent.putExtra("rrn",rrn)
                    intent.putExtra("status",utf8Status)
                    holder.itemView.context.startActivity(intent)
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss() // 다이얼로그를 닫습니다.
                }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
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

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val button: Button = itemView.findViewById(R.id.service_result_button)
        val layout: ConstraintLayout = itemView.findViewById(R.id.list_item)
        val textView: TextView? = itemView.findViewById(R.id.service_result_text)
    }
}