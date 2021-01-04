package com.isoftinc.film.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceStore(val context: Context){
    // Shared Preferences
    var pref: SharedPreferences? = null

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null

    // Context
    var _context: Context?= null

    var appConstants = AppConstants()




    // Constructor
    init{
        this._context = context
        pref = _context!!.getSharedPreferences(appConstants.PREF_NAME, appConstants.PRIVATE_MODE)
        editor = pref!!.edit()
    }



    fun getUserDetail(): String? {
        return pref!!.getString(appConstants.USER_DETAILS, null)
    }

    fun setUserDetail(userDtls: String) {

        editor!!.putString(appConstants.USER_DETAILS, userDtls)
        editor!!.commit()
    }

    @Synchronized
    fun setIsLogin(isLogin: Boolean) {
        editor!!.putBoolean(appConstants.IS_LOGIN, isLogin)
        editor!!.commit()
    }

    @Synchronized
    fun getIsLogin(): Boolean {
        return pref!!.getBoolean(appConstants.IS_LOGIN, false)
    }

    fun setToken(token : String){
        editor!!.putString(appConstants.FCM_TOKEN, token)
        editor!!.commit()
    }

    fun getToken() : String? {
        return pref!!.getString(appConstants.FCM_TOKEN, null)
    }

    fun clearPreference(){
        editor!!.clear()
        editor!!.commit()
    }


}
