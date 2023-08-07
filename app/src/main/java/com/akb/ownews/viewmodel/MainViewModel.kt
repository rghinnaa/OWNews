package com.akb.ownews.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.akb.ownews.data.local.entity.Bookmark
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.model.CategoryModel
import com.akb.ownews.data.model.FilterCategoryModel
import com.akb.ownews.data.model.FilterCountryModel
import com.akb.ownews.data.model.FilterSourceModel
import com.akb.ownews.data.model.SourceModel
import com.akb.ownews.data.remote.Result
import com.akb.ownews.data.remote.repository.MainRepository
import com.akb.ownews.utils.Const.SavedStateHandle.CATEGORY_SAVED_STATE_KEY
import com.akb.ownews.utils.Const.SavedStateHandle.SOURCE_SAVED_STATE_KEY
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.livevent.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = mainRepository
    private val stateHandler = savedStateHandle

    var country = "us"

    private var _trending: MutableLiveData<Result<List<ArticleModel>>> = MutableLiveData()
    val trending: LiveData<Result<List<ArticleModel>>> get() = _trending

    private var _bookmark: MutableLiveData<List<Bookmark>> = MutableLiveData()
    val bookmark: LiveData<List<Bookmark>> get() = _bookmark

    private var _searchBookmark: MutableLiveData<List<Bookmark>> = MutableLiveData()
    val searchBookmark: LiveData<List<Bookmark>> get() = _searchBookmark

    private var _source: MutableLiveData<Result<List<SourceModel>>> = MutableLiveData()
    val source: LiveData<Result<List<SourceModel>>> get() = _source

    private var _filterCategory: MutableLiveData<Event<FilterCategoryModel>> = MutableLiveData()
    val filterCategory: LiveData<Event<FilterCategoryModel>> get() = _filterCategory

    private var _filterSource: MutableLiveData<Event<FilterSourceModel>> = MutableLiveData()
    val filterSource: LiveData<Event<FilterSourceModel>> get() = _filterSource

    private var _filterCountry: MutableLiveData<Event<FilterCountryModel>> = MutableLiveData()
    val filterCountry: LiveData<Event<FilterCountryModel>> get() = _filterCountry

    fun requestTrending(country: String) = repository.requestTrending(country)
        .onEach { result ->
            _trending.value = result
        }.launchIn(viewModelScope)

    fun requestHeadlinePaging(
        country: String = emptyString,
        category: String = emptyString,
        sources: String = emptyString,
        search: String = emptyString
    ) = repository.requestHeadlinePaging(country, category, sources, search).asLiveData()

    fun requestSource(country: String = emptyString) = repository.requestSource(country)
        .onEach { result ->
            _source.value = result
        }.launchIn(viewModelScope)

    fun getBookmark() = repository.getBookmark()
        .onEach { result ->
            _bookmark.value = result
        }.launchIn(viewModelScope)

    fun insertBookmark(bookmark: Bookmark) = viewModelScope.launch {
        repository.insertBookmark(bookmark)
    }

    fun deleteBookmark(id: Int?) = viewModelScope.launch {
        repository.deleteBookmark(id)
    }

    fun searchBookmark(query: String) = repository.searchBookmark(query)
        .onEach { result ->
            _searchBookmark.value = result
        }.launchIn(viewModelScope)

    fun setCategory(categoryRequest: FilterCategoryModel) {
        _filterCategory.value = Event(categoryRequest)
    }

    fun setCategoryNothing() {
        _filterCategory.value = Event(FilterCategoryModel(state = false))
    }

    fun setSource(sourceRequest: FilterSourceModel) {
        _filterSource.value = Event(sourceRequest)
    }

    fun setSourceNothing() {
        _filterSource.value = Event(FilterSourceModel(state = false))
    }

    fun setCountry(countryRequest: FilterCountryModel) {
        _filterCountry.value = Event(countryRequest)
    }

    fun setCountryNothing() {
        _filterCountry.value = Event(FilterCountryModel(state = false))
    }

}