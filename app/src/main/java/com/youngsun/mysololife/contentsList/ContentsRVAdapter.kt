package com.youngsun.mysololife.contentsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youngsun.mysololife.R

class ContentsRVAdapter( val items : ArrayList<ContentModel> ) : RecyclerView.Adapter<ContentsRVAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.contents_rv_item, parent, false )


        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContentsRVAdapter.ViewHolder, position: Int) {
        holder.bindItems( items[position] )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems( item : ContentModel ) {

            val title : TextView = itemView.findViewById(R.id.textArea)
            title.text = item.title


            val image : ImageView = itemView.findViewById(R.id.imageArea)


        }
    }
}