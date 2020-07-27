package com.tailor.app.custom_exception

/**
 * Created by Soham Robinkumar Sheth on 21/6/18.
 */
class ApplicationException constructor(message: String = "") : Throwable() {

    var type: Type? = null
    override var message: String = ""

    init {
        this.message = message
    }

    enum class Type {
        NO_INTERNET, NO_DATA, VALIDATION
    }
}