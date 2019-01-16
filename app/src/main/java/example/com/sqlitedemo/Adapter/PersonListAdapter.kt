package example.com.sqlitedemo.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.sqlitedemo.Model.Person
import example.com.sqlitedemo.R
import example.com.sqlitedemo.listener.OnItemClickListener
import kotlinx.android.synthetic.main.row_person_list.view.*

class PersonListAdapter(
    private var context: Context,
    private var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {

    private var mArrayList: ArrayList<Person> = ArrayList()

    fun setList(mArrayList: ArrayList<Person>) {
        this.mArrayList.clear()
        this.mArrayList = mArrayList
        notifyDataSetChanged()
    }

    fun update(pos: Int, studentBean: Person) {
        this.mArrayList[pos] = studentBean
        notifyItemChanged(pos)
    }

    fun removeAt(pos: Int) {
        this.mArrayList.removeAt(pos)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_person_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(personBean: Person) {
            itemView.tvPersonName.text = personBean.name
            itemView.tvPersonEmail.text = personBean.email
            itemView.tvPersonPhone.text = personBean.phoneNumber
            itemView.tvPersonOccupation.text = personBean.occupation

            itemView.ivDelete.setOnClickListener {
                onItemClickListener.onDeleteClicked(adapterPosition, personBean)
            }

            itemView.ivEdit.setOnClickListener {
                onItemClickListener.onEditClicked(adapterPosition, personBean)
            }
        }
    }
}