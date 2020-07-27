package com.tailor.app.manager

interface Passable<in T> {

    fun passData(t: T)

}
