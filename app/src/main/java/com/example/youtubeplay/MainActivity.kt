package com.example.youtubeplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.HashMap
import kotlin.collections.LinkedHashMap

class MainActivity : AppCompatActivity() {
    private lateinit var etKey: EditText
    private lateinit var btnSearch: Button
    private lateinit var recyclerView: RecyclerView
    private val youtubeApiKey: String = "AIzaSyCnWbiIBN_OQ_uBqHuezs-vj03WFFnSocY"
    private lateinit var hashMap: LinkedHashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        etKey = findViewById(R.id.etKey)
        btnSearch = findViewById(R.id.btnSearch)
        recyclerView = findViewById(R.id.recyclerView)
        hashMap = LinkedHashMap()

        etKey.setText("GOT")

        btnSearch.setOnClickListener {
            val query = etKey.text.toString().trim()
            val youtubeURL =
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=$query&type=video&maxResults=2&key=$youtubeApiKey"

            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET, youtubeURL,
                Response.Listener<String> { response ->
                    Log.d(TAG, response)

                    var collectionType = object : TypeToken<HashMap<String, Any>>() {}.type

                    var hashMap: HashMap<String, Any> = Gson().fromJson(response, collectionType)

                    for (entry in hashMap.entries) {
                        Log.d(TAG, "${entry.key} - ${entry.value}")
                    }

                    val jsonObject = JSONObject(response)
                    val value = jsonObject["items"]

                    Log.d(TAG, value.toString())

                    collectionType = object : TypeToken<HashMap<String, Any>>() {}.type

                    hashMap = Gson().fromJson(value.toString(), collectionType)

                    Log.d(TAG, hashMap.size.toString())

                    for (entry in hashMap.entries) {
                        Log.d(TAG, "${entry.key} - ${entry.value}")
                    }
                },
                Response.ErrorListener { Log.d(TAG, "Error") })

            queue.add(stringRequest)
        }
    }

    companion object {
        val TAG = "Youtube_Response"
    }
}
