package com.t.example_shell_webview

import com.grouter.*
import com.grouter.demo.base.model.User
import com.grouter.demo.base.service.AccountService
import com.grouter.demo.base.service.UserService
import org.mockito.Mockito
import java.lang.Exception

class DebugMockInterceptor : GRouterInterceptor() {

    override fun process(request: TaskRequest): Boolean {
        if (request.path == "getLoginUser") {
            request.onContinueResult(User(123, "202cb962ac59075b964b07152d234b70"))
            return true
        }
        return super.process(request)
    }

    override fun onError(request: TaskRequest, exception: Exception): Boolean {
        if (request.path == "getLoginUser") {
            request.onContinueResult(User(123, "202cb962ac59075b964b07152d234b70"))
            return true
        }
        return super.onError(request, exception)
    }

    override fun process(request: ComponentRequest): Boolean {
        if (request.protocol == UserService::class.java) {
            val userService = Mockito.mock(UserService::class.java)
            Mockito.`when`(userService.getUser(1)).thenReturn(User(1, "Mockito"))
            request.onContinue(userService)
            return true
        } else if (request.protocol == AccountService::class.java) {
            val accountService = Mockito.mock(AccountService::class.java)
            Mockito.`when`(accountService.getLoginUser()).thenReturn(User(101, "Mockito"))
            request.onContinue(accountService)
            return true
        }
        return super.process(request)
    }

    override fun onError(request: ComponentRequest, exception: Exception): Boolean {
        if (request.protocol == UserService::class.java) {
            val userService: UserService = Mockito.mock(UserService::class.java)
            Mockito.`when`(userService.getUser(1)).thenReturn(User(1, "Mockito"))
            request.onContinue(userService)
            return true
        }
        return super.onError(request, exception)
    }

}