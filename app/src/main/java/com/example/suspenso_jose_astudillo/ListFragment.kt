package com.example.suspenso_jose_astudillo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class ListFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var conexionSQL: ConexionSQL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        listView = view.findViewById(R.id.list_view)
        conexionSQL = ConexionSQL(requireContext())

        val dataList = conexionSQL.getAllData()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter

        return view
    }
}