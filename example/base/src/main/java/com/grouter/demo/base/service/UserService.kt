package com.grouter.demo.base.service

import com.grouter.demo.base.model.User

interface UserService {
    fun getUser(uid: Int): User
    fun listUser(): List<User>
    fun sayHello()
}