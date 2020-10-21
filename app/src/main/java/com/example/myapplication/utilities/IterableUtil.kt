package com.example.myapplication.utilities

import androidx.appcompat.widget.AppCompatButton


fun MutableList<String>.getRandomListIndex() = (0 until this.count()).random()

fun Iterable<() -> Unit>.runIterateUnit() {
    for (unit in this) unit()
}

fun Iterable<AppCompatButton>.clickable(clickable: Boolean) {
    for (element in this) element.isClickable = clickable
}