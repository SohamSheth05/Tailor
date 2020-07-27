package com.tailor.app.api


import okhttp3.HttpUrl


object URLFactory {

     lateinit var SCHEME: String
    lateinit var HOST: String
    lateinit var API_PATH: String
    fun provideHttpUrl(): HttpUrl {
       /* when (BuildConfig.BUILD_TYPE) {
            "staging" -> {
                SCHEME = "https"
                HOST = "api.staging.parcelpal.com"
                API_PATH = "api/merchant/"
            }
            "release" -> {
                SCHEME = "https"
                HOST = "api.parcelpal.com"
                API_PATH = "api/merchant/"
            }
            else -> {
                *//*    HOST = "192.168.1.225"
                    API_PATH = "parcelpal-backend-gitlab/public/api/merchant/"*//*
                SCHEME = "http"
                HOST = "192.168.1.222"
                API_PATH = "parcelpal-backend-gitlab/api/merchant/"

             *//*   SCHEME = "http"
                HOST = "192.168.1.222"
                API_PATH = "parcelpal-backend/api/merchant/"*//*
            }
        }*/
        return HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegments(API_PATH)
            .build()


    }



    const val LOGIN = "login"
    const val FORGOT_PASSWORD_URL = "reset-password"
    const val CHANGE_PASSWORD_URL = "change-password"

    const val GET_STORE_LIST_URL = "stores"

    const val GET_ORDERS = "orders"
    const val ACCEPT_ORDER = "order/accept"
    const val ORDER_UPDATE_ITEM = "order/update"

    const val NEW_MENU_STOCK_CATEGORIES_URL = "store/categories"
    const val NEW_MENU_STOCK_ITEM_URL = "store/items"
    const val NEW_ITEM_DETAILS_URL = "store/item/detail"
    const val MENU_STOCK_UPDATE_ITEM = "store/manage"

    const val STORE_TIMES_URL = "store-times"

    const val NOTIFICATION_URL = "notifications"

    const val STORE_EXCEPTION_URL = "store-times/exceptions"

    const val ORDER_PLACE_URL = "order/place"

    const val STORE_DETAILS_URL = "store/detail"

    const val RESEND_PAYMENT_URL = "order/resend/payment-link"

    const val CANCEL_ORDER_URL = "order/cancel"

    const val CHECK_EXISTENCE = "customer/check-existence"

    const val REQUEST_TYPE_URL = "request-types"

    const val LOGOUT = "logout"

    const val DISTANCE_MATRIX_API = "https://maps.googleapis.com/maps/api/distancematrix/json"


    /*------------------------- **************** ------------------------------------*/


}
