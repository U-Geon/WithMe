package com.example.withme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.withme.R
import com.example.withme.databinding.MyListItemBinding


class MyListAdapter(private val myList: ArrayList<MyList>, private val onItemClick: (MyList) -> Unit) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    class MyListViewHolder(val binding: MyListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        // 변수 view: list_item의 정보를 가져와서 어뎁터와 연결.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item, parent, false)
        val viewHolder = MyListViewHolder(MyListItemBinding.bind(view))

        view.setOnClickListener {
            onItemClick.invoke(myList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    // 리스트뷰를 계속 사용할 때, onBindViewHolder가 지속적으로 호출이 되면서 모든 데이터들을 연결.
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.binding.tvMytext.text = myList.get(position).mytext   // position: 현재 클릭한 것의 인덱스 번호.
    }
}