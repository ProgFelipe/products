package com.example.search.presentation.utils

import androidx.appcompat.widget.SearchView
import io.reactivex.subjects.PublishSubject

fun SearchView.setOnQueryTextListener(): PublishSubject<String> {
    val subject = PublishSubject.create<String?>()
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            subject.onComplete()
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            subject.onNext(newText)
            return false
        }
    })
    return subject
}