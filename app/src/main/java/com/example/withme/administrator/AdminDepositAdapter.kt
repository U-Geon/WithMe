package com.example.withme.administrator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.withme.R
import com.example.withme.databinding.AdmindepositListItemBinding


class AdminDepositAdapter(private val adminList: ArrayList<AdminDepositList>, private val onItemClick: (AdminDepositList) -> Unit) : RecyclerView.Adapter<AdminDepositAdapter.MyListViewHolder>() {

    class MyListViewHolder(val binding: AdmindepositListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        // 변수 view: list_item의 정보를 가져와서 어뎁터와 연결.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admindeposit_list_item, parent, false)
        val viewHolder = MyListViewHolder(AdmindepositListItemBinding.bind(view))

        view.setOnClickListener {
//            onItemClick.invoke(adminList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    // 리스트뷰를 계속 사용할 때, onBindViewHolder가 지속적으로 호출이 되면서 모든 데이터들을 연결.
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.binding.tvAdmindeposit.text = adminList.get(position).admindeposit   // position: 현재 클릭한 것의 인덱스 번호.
    }
}