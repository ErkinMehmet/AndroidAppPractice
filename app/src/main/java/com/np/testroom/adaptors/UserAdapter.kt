package com.np.testroom.adaptors
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.np.testroom.data.User
import com.np.testroom.databinding.ItemUserBinding // gets the item-user layout and render it

class UserAdapter(
    private val rowsPerSum: Int=1
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(

    ) {

    private var userList: MutableList<User> = mutableListOf()  // Change to MutableList
    private var sum=0
    private var rowNum=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        sum+=user.age

        if ((rowNum % rowsPerSum==0|| position==userList.size-1) && position!=0) {
            holder.putSum(sum)
            sum=0
        }
        rowNum++
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    // Update the list of users and notify the adapter that the data has changed
    fun updateList(newUsers: List<User>) {
        userList.clear() // Now you can call clear() since userList is mutable
        userList.addAll(newUsers)
        notifyDataSetChanged() // Notify the adapter to refresh the view
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.textViewName.text = user.name
            binding.textViewAge.text = user.age.toString()
            binding.textViewSum.text=""
            binding.textViewSumCard.visibility = View.GONE
        }

        fun putSum(sum:Int){
            binding.textViewSum.text="Sum:$sum"
            binding.textViewSumCard.visibility = View.VISIBLE
        }
    }
}
