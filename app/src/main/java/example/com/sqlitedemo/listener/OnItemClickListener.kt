package example.com.sqlitedemo.listener

import example.com.sqlitedemo.Model.Person

interface OnItemClickListener {
    fun onEditClicked(pos: Int, student: Person)

    fun onDeleteClicked(adapterPosition: Int, studentBean: Person)
}