package com.example.search.presentation.components

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.example.search.R
import com.example.search.presentation.utils.setOnQueryTextListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class CustomSearchView : SearchView {

    private lateinit var cursorAdapter: SimpleCursorAdapter
    private lateinit var cursor: MatrixCursor

    companion object {
        private const val WAIT_TO_CALL_SERVICE = 500L
    }

    lateinit var searchView: SearchView

    // region Listeners
    private lateinit var _shouldSuggest: (inputText: String) -> Boolean
    private lateinit var _hideKeyBoard: () -> Unit
    private lateinit var _onSuggestionsSearch: (inputText: String) -> Unit
    private lateinit var _onSearch: (inputText: String) -> Unit
    // endregion

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.custom_search_view, this)
    }

    fun setupView(
        inputText: String,
        shouldSuggest: (inputText: String) -> Boolean,
        hideKeyBoard: () -> Unit,
        onSuggestionsSearch: (inputText: String) -> Unit,
        onSearch: (inputText: String) -> Unit
    ) {
        _shouldSuggest = shouldSuggest
        _hideKeyBoard = hideKeyBoard
        _onSuggestionsSearch = onSuggestionsSearch
        _onSearch = onSearch

        searchView = this
        this.post { setQuery(inputText, false) }
        setupCursorAdapter()
        setupListeners()
    }

    private fun setupCursorAdapter() {
        cursorAdapter = SimpleCursorAdapter(
            context,
            R.layout.search_suggestions_item,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(R.id.item_label),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        this.suggestionsAdapter = cursorAdapter

        cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
    }

    private fun setupListeners() {
        val editText =
            this.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setOnEditorActionListener { _, actionId, event ->
            if (
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && event.action == KeyEvent.ACTION_DOWN
            ) {
                _hideKeyBoard()
                _onSearch(this.query.toString())
            }
            false
        }
        setupQueryChangeListener()
        this.setOnSuggestionListener(object : OnSuggestionListener {

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                _hideKeyBoard()
                val cursor = cursorAdapter.getItem(position) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))

                searchView.setOnQueryTextListener()
                searchView.setQuery(selection, false)
                setupQueryChangeListener()

                _onSearch(selection)
                return true
            }
        })
    }

    private fun setupQueryChangeListener() {
        searchView.setOnQueryTextListener()
            .debounce(WAIT_TO_CALL_SERVICE, TimeUnit.MILLISECONDS)
            .filter {
                _shouldSuggest(it)
            }
            .map { inputText -> inputText.toLowerCase(Locale.getDefault()).trim() }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                _onSuggestionsSearch(it)
            }
            .doOnComplete { _hideKeyBoard() }
            .subscribe()
    }

    fun setSuggestedList(items: List<String>) {
        cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        items.forEachIndexed { index, item ->
            cursor.addRow(arrayOf(index, item))
        }
        cursorAdapter.changeCursor(cursor)
    }
}