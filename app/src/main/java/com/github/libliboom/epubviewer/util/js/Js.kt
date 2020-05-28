package com.github.libliboom.epubviewer.util.js

object Js {

    fun callLoad(nth: String): String = "javascript:load($nth)"
    fun loadJs(): String {
        return "javascript:function load(nth) {" +
            "var height = window.innerHeight; " +
            "window.scrollTo(0, height*nth);" +
            "}"
    }

    fun getNthJs(): String {
        return "javascript:function getNth() {" +
            "return Math.floor(window.scrollY/window.innerHeight)+1; " +
            "}"
    }

    fun calcPage4VerticalJs(): String {
        return "javascript:function getPageCount() {" +
            "var d = document.getElementsByTagName('body')[0];" +
            "var innerH = window.innerHeight; " +
            "var fullH = d.offsetHeight; " +
            "var pageCount = Math.floor(fullH/innerH)+1;" +
            "return pageCount;" +
            "}"
    }

    fun callColumns(): String = "javascript:columns()"
    fun columns4HorizontalJs(): String {
        return "javascript:function columns() {" +
            "var d = document.getElementsByTagName('body')[0];" +
            "var innerH = window.innerHeight; " +
            "var fullH = d.offsetHeight; " +
            "d.style.height = innerH+'px';" +
            "d.style.webkitColumnGap = '0px'; " +
            "d.style.margin = 0; " +
            "d.style.webkitColumnCount = 1;" +
            "}"
    }

    fun callPageCount(): String = "javascript:getPageCount()"
    fun calcPage4HorizontalJs(): String {
        return "javascript:function getPageCount() {" +
            "var wholeWidth = document.documentElement.scrollWidth; " +
            "var innerWidth = document.documentElement.clientWidth; " +
            "var pageCount = wholeWidth/innerWidth;" +
            "return pageCount;" +
            "}"
    }

    fun sumPageCount(page: String): String = "javascript:Android.sumPageCount($page)"
}