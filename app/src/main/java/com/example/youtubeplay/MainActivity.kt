package com.example.youtubeplay

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var etKey: EditText
    private lateinit var btnSearch: Button
    private lateinit var recyclerView: RecyclerView
    private val youtubeApiKey: String = "AIzaSyCnWbiIBN_OQ_uBqHuezs-vj03WFFnSocY"
    private lateinit var hashMap: LinkedHashMap<Int, YoutubeThumbnail>
    private lateinit var adapter: CustomAdapter
    private lateinit var initializedListener: YouTubePlayer.OnInitializedListener
    private lateinit var youTubePlayerFragment: YouTubePlayerSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        etKey = findViewById(R.id.etKey)
        btnSearch = findViewById(R.id.btnSearch)
        recyclerView = findViewById(R.id.recyclerView)
        hashMap = LinkedHashMap()
        adapter = CustomAdapter(this, hashMap)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        recyclerView.adapter = adapter

        etKey.setText("GOT")

        btnSearch.setOnClickListener {
            hashMap.clear()
            val query = etKey.text.toString().trim()
            val youtubeURL =
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=$query&type=video&maxResults=10&key=$youtubeApiKey"

            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET, youtubeURL,
                Response.Listener<String> { response ->
                    parseYoutubeSearchResponse(response)
                    adapter.notifyDataSetChanged()
                },
                Response.ErrorListener { Log.d(TAG, "Error") })

            queue.add(stringRequest)
        }
    }

    private fun parseYoutubeSearchResponse(response: String) {
        val jsonObject = JSONObject(response)
        val itemsArray = jsonObject["items"] as JSONArray

        for (i in 0 until itemsArray.length()) {
            val objectList = itemsArray.get(i) as JSONObject
            val itemID = objectList["id"] as JSONObject
            val videoID = itemID["videoId"].toString()
            // val thumbnailsURL = "https://img.youtube.com/vi/$videoID/maxresdefault.jpg"
            val snippetObject = objectList["snippet"] as JSONObject
            val thumbnailsObject = snippetObject["thumbnails"] as JSONObject
            val defaultObject = thumbnailsObject["default"] as JSONObject
            val thumbnailsURL = defaultObject["url"].toString()
            // Log.d("")

            val youtubeThumbnail = YoutubeThumbnail()
            youtubeThumbnail.videoID = videoID
            youtubeThumbnail.thumbnailURL = thumbnailsURL

            hashMap[hashMap.size] = youtubeThumbnail
        }
    }

    fun setItemVideo(position: Int) {
        val videoID = hashMap[position]!!.videoID
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.youtubeLayout, youTubePlayerFragment as Fragment).commit()

        initializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    player.loadVideo(videoID)
                    player.play()
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
                val errorMessage = error.toString()
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        youTubePlayerFragment.initialize(youtubeApiKey, initializedListener)
    }

    companion object {
        val TAG = "Youtube_Response"
    }
}
