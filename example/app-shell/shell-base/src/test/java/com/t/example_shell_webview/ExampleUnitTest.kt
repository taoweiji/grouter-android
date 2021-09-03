package com.t.example_shell_webview

import com.alibaba.fastjson.JSON
import com.grouter.demo.base.model.User
import com.grouter.demo.base.service.UserService
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.internal.util.MockUtil
import org.mockito.mock.MockCreationSettings
import org.mockito.plugins.MockMaker
import org.mockito.plugins.MockitoPlugins
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        System.setProperty("org.mockito.android.target",File("test").absolutePath)
//        MockUtil.createMock()
//        MockUtil.createMock(AndroidByteBuddyMockMaker())
        val userService = Mockito.mock(UserService::class.java)
        Mockito.`when`(userService.getUser(1)).thenReturn(User(1,"Mockito"))
        val user = userService.getUser(1)
        println(JSON.toJSONString(user))
        assertEquals("Mockito", user.name)
    }
}
