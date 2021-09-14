package com.example.calendarandtasks.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.calendarandtasks.data.model.Group
import com.example.calendarandtasks.data.model.GroupDiffCallback
import com.example.calendarandtasks.databinding.GroupTaskItemLayoutBinding
import com.orhanobut.logger.Logger


class GroupAdapter(
    private val listener: GroupAdapterListener
) : ListAdapter<Group,GroupViewHolder>(GroupDiffCallback)
{
    interface GroupAdapterListener{
        fun onClicked(cardView : View, group: Group)
        fun onGroupLongPressed(group: Group) : Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder( GroupTaskItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
            listener)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
//    override fun createBinding(parent: ViewGroup): GroupTaskItemLayoutBinding {
//        return DataBindingUtil.inflate<GroupTaskItemLayoutBinding>(
//            LayoutInflater.from(parent.context),
//            R.layout.group_task_item_layout,
//            parent, false
//        ).apply {
//            //event click group card
//            this.root.setOnClickListener {
//                this.listener = listener
//            }
//        }
//    }

//    override fun bind(binding: GroupTaskItemLayoutBinding, item: Group) {
//        binding.title.text = item.titleGroup
//        binding.countTasks.text = item.sumTask.toString()
//        binding.cardView.setCardBackgroundColor(item.color)
//        binding.group = item
////        binding.image.setImageDrawable( android.graphics.drawable.Drawable.createFromPath(R.drawable.))
//    }
}