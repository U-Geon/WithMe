package com.example.withme.administrator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.withme.R
import com.example.withme.databinding.AdminDepositListItemBinding


class AdminDepositAdapter(private val adminList: ArrayList<AdminDepositList>, private var onItemClick: (AdminDepositList) -> Unit) : RecyclerView.Adapter<AdminDepositAdapter.MyListViewHolder>() {

    class MyListViewHolder(val binding: AdminDepositListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        // 변수 view: list_item의 정보를 가져와서 어뎁터와 연결.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_deposit_list_item, parent, false)
        val viewHolder = MyListViewHolder(AdminDepositListItemBinding.bind(view))
        view.setOnClickListener {
            onItemClick.invoke(adminList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    // 리스트뷰를 계속 사용할 때, onBindViewHolder가 지속적으로 호출이 되면서 모든 데이터들을 연결.
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.binding.tvAdmindeposit.text =
            adminList.get(position).admindeposit   // position: 현재 클릭한 것의 인덱스 번호.

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener { onItemClick(adminList[position]) }
    }

    // onItemClick 콜백을 외부에서 설정할 수 있도록 하는 메서드
    fun setOnItemClickListener(listener: (AdminDepositList) -> Unit) {
        onItemClick = listener
    }
}