package example.com.sqlitedemo.Activity

import android.app.Dialog
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import kotlinx.android.synthetic.main.dialog_add_person.*
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import example.com.sqlitedemo.Adapter.PersonListAdapter
import example.com.sqlitedemo.Database.DatabaseQueryClass
import example.com.sqlitedemo.Model.Person
import example.com.sqlitedemo.R
import example.com.sqlitedemo.Utils.isValidMail
import example.com.sqlitedemo.Utils.toast
import example.com.sqlitedemo.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val mAdapter by lazy {
        PersonListAdapter(this, this)
    }
    private val databaseQueryClass = DatabaseQueryClass(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.app_name)

        rvShowList.layoutManager = LinearLayoutManager(this)
        rvShowList.setHasFixedSize(true)
        rvShowList.adapter = mAdapter

        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                openDialog()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.dialog_add_person)

        dialog.show()
        dialog.btnInsertData.setOnClickListener {
            val nameString = dialog.etName.text.toString()
            val occupation = dialog.etOccupation.text.toString()
            val phoneString = dialog.etPhone.text.toString()
            val emailString = dialog.etEmail.text.toString()

            if (checkValidation(nameString, occupation, phoneString, emailString)) {
                val person =
                    Person(-1, nameString, phoneString, emailString, occupation)

                val databaseQueryClass = DatabaseQueryClass(this)

                val id = databaseQueryClass.insertPerson(person)

                if (id > 0) {
                    person.id = id
                    getData()
                    dialog.dismiss()
                }
            }
        }
        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun checkValidation(
        nameString: String,
        occupation: String,
        phoneString: String,
        emailString: String
    ): Boolean {
        return when {
            nameString.isEmpty() -> {
                toast(this, getString(R.string.enter_name))
                false
            }
            occupation.isEmpty() -> {
                toast(this, getString(R.string.enter_occupation))
                false
            }
            phoneString.isEmpty() -> {
                toast(this, getString(R.string.enter_phone))
                false
            }
            emailString.isEmpty() -> {
                toast(this, getString(R.string.enter_emailid))
                false
            }
            !isValidMail(emailString) -> {
                toast(this, getString(R.string.enter_valid_email))
                false
            }
            else -> {
                true
            }
        }
    }

    private fun getData() {
        mAdapter.setList(databaseQueryClass.allPersonList)
    }

    override fun onEditClicked(pos: Int, student: Person) {
        onUpdateData(pos, student.id)
    }

    private fun onUpdateData(pos: Int, registrationNumber: Long) {

        val mStudent = databaseQueryClass.getPersonById(registrationNumber)

        if (mStudent != null) {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setContentView(R.layout.dialog_add_person)
            dialog.btnInsertData.text = getString(R.string.update_data)
            dialog.etName.setText(mStudent.name)
            dialog.etOccupation.setText(mStudent.occupation)
            dialog.etPhone.setText(mStudent.phoneNumber)
            dialog.etEmail.setText(mStudent.email)
            dialog.show()

            dialog.btnInsertData.setOnClickListener {
                val nameString = dialog.etName.text.toString()
                val occupation = dialog.etOccupation.text.toString()
                val phoneString = dialog.etPhone.text.toString()
                val emailString = dialog.etEmail.text.toString()

                if (checkValidation(nameString, occupation, phoneString, emailString)) {
                    mStudent.name = nameString
                    mStudent.phoneNumber = phoneString
                    mStudent.email = emailString
                    mStudent.occupation = occupation

                    val id = databaseQueryClass.updatePersonInfo(mStudent)

                    if (id > 0) {
                        mAdapter.update(pos, mStudent)
                        dialog.dismiss()

                    }
                }
            }
            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

        }
    }

    override fun onDeleteClicked(adapterPosition: Int, studentBean: Person) {
        val count = databaseQueryClass.deletePersonById(studentBean.id)

        if (count > 0) {
            mAdapter.removeAt(adapterPosition)

            Toast.makeText(this, getString(R.string.deleted_success), Toast.LENGTH_LONG).show()
        } else
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
    }

}
