package com.interview.assessment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var btnRetry: MaterialButton
    private lateinit var etSearch: EditText // 这里改成了原生 EditText
    private lateinit var adapter: CharacterAdapter

    private val viewModel: CharacterViewModel by viewModels()

    // 用于实现防抖的 Job
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorLayout = findViewById(R.id.errorLayout)
        btnRetry = findViewById(R.id.btnRetry)
        etSearch = findViewById(R.id.etSearch)

        adapter = CharacterAdapter(emptyList()) { character ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("CHARACTER_ID", character.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnRetry.setOnClickListener {
            viewModel.fetchCharacters()
        }

        // 监听搜索框输入，防抖 500ms
        etSearch.doAfterTextChanged { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchCharacters(text.toString())
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !recyclerView.canScrollVertically(1)) {
                    viewModel.fetchCharacters(isLoadMore = true)
                }
            }
        })

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is CharacterUiState.Loading -> {
                                progressBar.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                            }
                            is CharacterUiState.Success -> {
                                progressBar.visibility = View.GONE
                                if (state.characters.isEmpty()) {
                                    recyclerView.visibility = View.GONE
                                    errorLayout.visibility = View.VISIBLE
                                } else {
                                    recyclerView.visibility = View.VISIBLE
                                    errorLayout.visibility = View.GONE
                                    adapter.updateData(state.characters)
                                }
                            }
                            is CharacterUiState.Error -> {
                                progressBar.visibility = View.GONE
                                recyclerView.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                Toast.makeText(this@MainActivity, "加载失败: ${state.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                launch {
                    viewModel.isLoadingMore.collect { isLoading ->
                        adapter.setLoadingState(isLoading, viewModel.hasNextPage.value)
                    }
                }

                launch {
                    viewModel.hasNextPage.collect { hasNext ->
                        adapter.setLoadingState(viewModel.isLoadingMore.value, hasNext)
                    }
                }
            }
        }
    }
}