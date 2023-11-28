package com.example.withme

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.withme.databinding.ActivityServiceUsageHistoryBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.withme.administrator.AdminServiceActivity
import org.json.JSONException
import org.json.JSONObject

data class UsageHistoryItem (
    val date: String,
    val hospital: String,
    val detail: String
)

class ServiceUsageHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityServiceUsageHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "서비스 사용 내역"

        // JSON 데이터 요청

        val adapter = UsageHistoryAdapter(this)
        binding!!.recyclerView.adapter = adapter

        // RecyclerView에 LinearLayoutManager 설정
        val layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.layoutManager = layoutManager

        val url = resources.getString(R.string.ip) + "/main/serviceUsageHistory"
        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {
                    val jsonObject = JSONObject(response)
                    val usageHistory = jsonObject.getJSONArray("service") // jsonArray 받아오기

                    for (i in 0 until usageHistory.length()) {
                        val usageObject = usageHistory.getJSONObject(i)
                        adapter.addItem(
                            UsageHistoryItem(
                                usageObject.getString("date"), // 날짜
                                usageObject.getString("hospital"), // 병원
                                usageObject.getString("detail") // 세부 내역
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
        Volley.newRequestQueue(this).add(stringRequest)
    }
}

class UsageHistoryAdapter(private val activity: Activity) : RecyclerView.Adapter<UsageHistoryAdapter.ViewHolder>() {

    private val usageHistoryList = mutableListOf<UsageHistoryItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage_history, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usage = usageHistoryList[position]

        // 첫 번째, 두 번째 값은 TextView에 띄운다.
        holder.dateTextView.text = usage.date
        holder.hospitalTextView.text = usage.hospital

        // 세 번째 값은 화살표를 누르면 세부적으로 볼 수 있게끔 한다.
        holder.detailButton.setOnClickListener {
            // 세부 내역을 보여주는 팝업 창을 띄운다.
            val dialogBuilder = AlertDialog.Builder(holder.itemView.context)

            dialogBuilder.setMessage(usage.detail)
                .setTitle("세부 내역")
                .setCancelable(true)
                .setNegativeButton("닫기") { dialog, _ ->
                    dialog.dismiss() // 다이얼로그를 닫습니다.
                }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return usageHistoryList.size
    }

    fun addItem(item: UsageHistoryItem) {
        usageHistoryList.add(item)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val hospitalTextView: TextView = itemView.findViewById(R.id.hospitalTextView)
        val detailButton: Button = itemView.findViewById(R.id.detailButton)
    }
}