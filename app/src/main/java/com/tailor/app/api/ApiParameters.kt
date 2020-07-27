package com.tailor.app.api

/**
 * Created by Soham Robinkumar Sheth on 11/6/18.
 */
interface ApiParameters {
    companion object {
        /*
                * Post Parameters
                */
        const val PARAMETER_EMAIL = "email"
        const val USER_ID = "user_id"
        const val PARAMETER_PASSWORD = "password"
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_TOKEN = "device_token"

        const val TYPE = "type"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val PAGE: String = "page"

        const val REQUEST_QUEUE_ID = "request_queue_id"
        const val PREPARATION_TIME = "estimate_wait_time"
        const val COMMENT = "comment"


        const val GOOGLE_PARAMETER_KEY = "key"
        const val GOOGLE_PARAMETER_ORIGIN = "origins"
        const val GOOGLE_PARAMETER_DESTINATION = "destinations"
    }
}