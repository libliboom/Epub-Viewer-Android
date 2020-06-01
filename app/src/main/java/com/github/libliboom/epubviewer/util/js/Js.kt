package com.github.libliboom.epubviewer.util.js

object Js {

    fun setStyle(): String {
        return """javascript:(function() {
                var custom = 'img{display: inline; height: auto; max-width: 100%;}';
                custom += 'h1{font-size: xx-large;}';
                var style = document.createElement('style');
                style.innerHTML = custom;
                document.head.appendChild(style);
                })()""".trimIndent()
    }

    fun callLoad(nth: String): String = "javascript:load($nth)"
    fun loadJs(): String {
        return """javascript:function load(nth) {
            var height = window.innerHeight;
            window.scrollTo(0, height*nth);
            }""".trimIndent()
    }

    fun callHNth(): String = "javascript:getHNth()"
    fun getHNthJs(): String {
        return """javascript:function getHNth() {
            return Math.floor(window.pageXOffset/window.innerWidth)+1;
            }""".trimIndent()
    }

    fun callNth(): String = "javascript:getNth()"
    fun getNthJs(): String {
        return """javascript:function getNth() {
            return Math.floor(window.scrollY/window.innerHeight)+1;
            }""".trimIndent()
    }

    fun calcPage4VerticalJs(): String {
        return """javascript:function getPageCount() {
            var d = document.getElementsByTagName('body')[0];
            var innerH = window.innerHeight;
            var fullH = d.offsetHeight;
            var pageCount = Math.floor(fullH/innerH)+1;
            return pageCount;
            }""".trimIndent()
    }

    fun callColumns(): String = "javascript:columns()"
    fun columns4HorizontalJs(): String {
        return """javascript:function columns() {
            var d = document.getElementsByTagName('body')[0];
            var innerH = window.innerHeight;
            var fullH = d.offsetHeight;
            d.style.height = innerH+'px';
            d.style.webkitColumnGap = '0px';
            d.style.margin = 0;
            d.style.webkitColumnCount = 1;
            }""".trimIndent()
    }

    fun callPageCount(): String = "javascript:getPageCount()"
    fun calcPage4HorizontalJs(): String {
        return """javascript:function getPageCount() {
            var wholeWidth = document.documentElement.scrollWidth;
            var innerWidth = document.documentElement.clientWidth;
            var pageCount = wholeWidth/innerWidth;
            return pageCount;
            }""".trimIndent()
    }

    fun sumPageCount(page: String): String = "javascript:Android.sumPageCount($page)"
}