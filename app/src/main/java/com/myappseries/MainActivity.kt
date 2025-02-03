package com.myappseries

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.myappseries.databinding.ActivityMainBinding
import com.myappseries.model.Serie
import com.myappseries.model.SeriesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding

    private lateinit var adapter: SerieAdapter
    private val seriesList = mutableListOf<Serie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initRecyclerView()
        getSeries("series")

    }

    private fun initRecyclerView() {
        adapter = SerieAdapter(seriesList)
        binding.rvSeries.layoutManager = LinearLayoutManager(this)
        binding.rvSeries.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://peticiones.online/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSeries(query : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<SeriesResponse> = getRetrofit()
                .create(ApiService::class.java).getAllSeries(query)

            val series: SeriesResponse? = call.body()
            if (series != null) {
                for (miSerie: Serie in series) {
                    Log.v("Series", miSerie.toString())
                }
            }
            runOnUiThread {
                if (call.isSuccessful) {
                    val seriesL = series ?: emptyList()
                    seriesList.clear()
                    seriesList.addAll(seriesL)
                    adapter.notifyDataSetChanged()

                } else {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}