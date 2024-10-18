package com.example.datamanager

import DataAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: DataAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi database helper
        dbHelper = DatabaseHelper(this)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter
        adapter = DataAdapter(ArrayList())
        recyclerView.adapter = adapter

        // Load data awal
        loadData()

        // Setup tombol tambah
        findViewById<Button>(R.id.addButton).setOnClickListener {
            showAddDataDialog()
        }
    }

    private fun loadData() {
        val dataList = dbHelper.getAllData()
        adapter.updateData(dataList)
    }

    private fun showAddDataDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_data, null)

        val titleEdit = dialogView.findViewById<EditText>(R.id.titleEditText)
        val descriptionEdit = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        builder.setView(dialogView)
            .setTitle("Tambah Data Baru")
            .setPositiveButton("Simpan") { _, _ ->
                val title = titleEdit.text.toString()
                val description = descriptionEdit.text.toString()

                if (title.isNotEmpty()) {
                    val data = Data(title = title, description = description)
                    dbHelper.insertData(data)
                    loadData()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}