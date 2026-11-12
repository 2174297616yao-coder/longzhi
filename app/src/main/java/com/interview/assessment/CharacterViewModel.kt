package com.interview.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException // 导入 HttpException

sealed interface CharacterUiState {
    object Loading : CharacterUiState
    data class Success(val characters: List<Character>) : CharacterUiState
    data class Error(val message: String) : CharacterUiState
}

class CharacterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Loading)
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasNextPage = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage.asStateFlow()

    private var currentPage = 1
    private var currentQuery: String? = null // 保存当前的搜索词
    private val allCharacters = mutableListOf<Character>()

    init {
        fetchCharacters()
    }

    // 新增：外部调用的搜索方法
    fun searchCharacters(query: String) {
        currentQuery = if (query.isBlank()) null else query
        fetchCharacters(isLoadMore = false) // 重新从第一页开始搜
    }

    fun fetchCharacters(isLoadMore: Boolean = false) {
        if (isLoadMore) {
            if (!_hasNextPage.value || _isLoadingMore.value) return
            _isLoadingMore.value = true
        } else {
            _uiState.value = CharacterUiState.Loading
            currentPage = 1
            _hasNextPage.value = true
            allCharacters.clear()
        }

        viewModelScope.launch {
            try {
                // 把 currentQuery 传给接口
                val response = RetrofitClient.api.getCharacters(currentPage, currentQuery)
                allCharacters.addAll(response.results)

                _hasNextPage.value = response.info.next != null
                _uiState.value = CharacterUiState.Success(allCharacters.toList())
                currentPage++
            } catch (e: Exception) {
                // 处理 404 错误（搜不到数据）
                if (e is HttpException && e.code() == 404) {
                    _uiState.value = CharacterUiState.Success(emptyList()) // 当作空列表处理
                } else {
                    _uiState.value = CharacterUiState.Error(e.message ?: "网络错误")
                }
            } finally {
                _isLoadingMore.value = false
            }
        }
    }
}