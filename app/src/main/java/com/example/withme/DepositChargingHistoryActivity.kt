package com.example.withme

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.withme.databinding.ActivityDepositChargingHistoryBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import android.app.Activity

data class DepositChargeHistory (
    val date: String,
    val balance: Int,
    val amount: Int
)

class DepositChargingHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDepositChargingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로 가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "예치금 내역"

        // JSON 데이터 요청

        val adapter = DepositAdapter()
        binding.depositRecyclerView.adapter = adapter

        // RecyclerView에 LinearLayoutManager 설정
        val layoutManager = LinearLayoutManager(this)
        binding.depositRecyclerView.layoutManager = layoutManager

        val sharedPreference = getSharedPreferences("other", 0)
        val userId = sharedPreference.getString("id", "")

        val url = resources.getString(R.string.ip) + "/select_deposit" + "?userId=" + userId

        val stringRequest = object : StringRequest (
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {
                    val jsonObject = JSONObject(response)
                    val usageHistory = jsonObject.getJSONArray("예치금목록") // jsonArray 받아오기

                    /**
                     * {
                     *     "예치금목록" : [
                     *     {
                     *      "날짜" : ,
                     *      "잔액" : ,
                     *      "지출" :
                     *     },
                     *     ...
                     *    ]
                     *  }
                     */

                    for (i in 0 until usageHistory.length()) {
                        val usageObject = usageHistory.getJSONObject(i)
                        adapter.addItem(
                            DepositChargeHistory(
                                usageObject.getString("날짜"), // 날짜
                                usageObject.getInt("잔액"), // 잔액
                                usageObject.getInt("지출") // 사용 금액
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

class DepositAdapter : RecyclerView.Adapter<DepositAdapter.ViewHolder>() {

    private val depositHistoryList = mutableListOf<DepositChargeHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deposit_charge_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val depositChargeHistory = depositHistoryList[position]

        holder.chargeDateTime.text = depositChargeHistory.date
        holder.balanceTextView.text = "잔액 " + depositChargeHistory.balance.toString() + "원"
        holder.amountTextView.text = depositChargeHistory.amount.toString() + "원"
    }

    override fun getItemCount(): Int {
        return depositHistoryList.size
    }

    fun addItem(item: DepositChargeHistory) {
        depositHistoryList.add(item)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chargeDateTime: TextView = itemView.findViewById(R.id.chargeDateTime)
        val balanceTextView: TextView = itemView.findViewById(R.id.balanceTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    }
}
