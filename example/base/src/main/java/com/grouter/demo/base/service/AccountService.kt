package com.grouter.demo.base.service

import com.grouter.demo.base.model.User

interface AccountService {
    fun sayHi()
    fun getUser(uid: Int): User?
    fun getLoginUser(): User?
}
