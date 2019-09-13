package com.grouter.demo


import android.content.Context

import com.grouter.RouterDelegate
import com.grouter.RouterDelegateConstructor
import com.grouter.RouterDelegateMethod
import com.grouter.demo.base.model.User

@RouterDelegate
class Account2Service {
    //    private MyApplication application;
    private var context: Context? = null

    @RouterDelegateConstructor
    constructor() {
    }

    @RouterDelegateConstructor
    constructor(context: Context, uid: Int, uids: IntArray, userMap: Map<String, User>, users: List<User>, user: User, names: Array<String>) {
        this.context = context
    }

    @RouterDelegateConstructor
    constructor(stringBuilder: StringBuilder) {

    }


    @RouterDelegateMethod
    fun login(uid: String, pwd: String): User {
        return User("Wiki")
    }

    @RouterDelegateMethod
    fun logout(context: Context, map: Map<String, String>, users: List<User>, a: IntArray, dad: Int) {

    }

    @RouterDelegateMethod
    fun showUser() {

    }

    @RouterDelegateMethod
    fun showUser2(): Int {
        return 0
    }

    @RouterDelegateMethod
    fun showUser3(): User? {
        return null
    }


    @RouterDelegateMethod
    fun showUser4(): IntArray? {
        return null
    }


    //    @RouterDelegateMethod
    //    public Account getAccount() {
    //        return null;
    //    }
    //
    //    @RouterDelegateMethod
    //    public void updateToken(Account account) {
    //
    //    }

}
