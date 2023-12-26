package pt.pedro.ccti.weatherapp.screens.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Search.Search
import pt.pedro.ccti.weatherapp.repository.SearchRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchData = MutableStateFlow<DataOrException<Search, Boolean, Exception>>(DataOrException(loading = true))
    val searchData = _searchData.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun performSearch() {
        viewModelScope.launch {
            _isSearching.value = true
            try {
                val result = repository.getSearchResults(stringQuery = _searchText.value)
                _searchData.value = result
            } catch (e: Exception) {
                _searchData.value = DataOrException(e = e)
            } finally {
                _isSearching.value = false
            }
        }
    }
}
