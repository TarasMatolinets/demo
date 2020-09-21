package com.demo.extension

import android.content.Context
import android.content.Context.*
import android.net.ConnectivityManager
import android.net.NetworkInfo

val Context.networkInfo: NetworkInfo? get() = (this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
