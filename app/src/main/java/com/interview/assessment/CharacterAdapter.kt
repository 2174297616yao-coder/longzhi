package com.interview.assessment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CharacterAdapter(
    private var characterList: List<Character>,
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 定义两种类型：普通数据 和 底部提示
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2

    private var isLoading = false
    private var hasNextPage = true

    // 更新底部的加载状态
    fun setLoadingState(isLoading: Boolean, hasNextPage: Boolean) {
        this.isLoading = isLoading
        this.hasNextPage = hasNextPage
        notifyItemChanged(itemCount - 1) // 只刷新底部那一个
    }

    // 设置数据
    fun updateData(newList: List<Character>) {
        characterList = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        // 如果是最后一个位置，就是 Footer
        return if (position == characterList.size) TYPE_FOOTER else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        // 如果还有下一页或者正在加载，总数+1（为了显示Footer）
        return if (hasNextPage || isLoading) characterList.size + 1 else characterList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOTER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            FooterViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val character = characterList[position]
            holder.tvName.text = character.name
            holder.tvStatus.text = character.status
            holder.tvSpecies.text = character.species

            Glide.with(holder.itemView.context)
                .load(character.image)
                .into(holder.ivAvatar)

            when (character.status.lowercase()) {
                "alive" -> {
                    holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")) // 深绿字
                    holder.tvStatus.background.setTint(Color.parseColor("#E8F5E9")) // 浅绿底
                }
                "dead" -> {
                    holder.tvStatus.setTextColor(Color.parseColor("#C62828")) // 深红字
                    holder.tvStatus.background.setTint(Color.parseColor("#FFEBEE")) // 浅红底
                }
                else -> {
                    holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")) // 深橙字
                    holder.tvStatus.background.setTint(Color.parseColor("#FFF3E0")) // 浅橙底
                }
            }

            holder.itemView.setOnClickListener { onItemClick(character) }
        } else if (holder is FooterViewHolder) {
            // 处理底部文字和进度条
            if (isLoading) {
                holder.progressBar.visibility = View.VISIBLE
                holder.tvLoadingText.text = "正在加载..."
            } else if (!hasNextPage) {
                holder.progressBar.visibility = View.GONE
                holder.tvLoadingText.text = "到底啦，没有更多数据了~"
            } else {
                // 正常情况下，如果没在加载且还有下一页，可以显示“上拉加载更多”
                holder.progressBar.visibility = View.GONE
                holder.tvLoadingText.text = "上拉加载更多"
            }
        }
    }

    // 角色卡片的 ViewHolder
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvSpecies: TextView = view.findViewById(R.id.tvSpecies)
    }

    // 底部提示的 ViewHolder
    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarLoading)
        val tvLoadingText: TextView = view.findViewById(R.id.tvLoadingText)
    }
}