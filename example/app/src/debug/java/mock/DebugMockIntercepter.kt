package mock

import android.content.Context
import com.grouter.GRouterInterceptor
import com.grouter.GRouterTask
import com.grouter.RouterInterceptor
import com.grouter.TaskRequest
import com.grouter.demo.base.model.User
import com.grouter.demo.other.service.GetUserTask
import java.lang.Exception

class DebugMockIntercepter : GRouterInterceptor() {

    override fun process(request: TaskRequest): Boolean {
        if (request.taskClass == GetUserTask::class.java.name) {
            val uid = request.params["uid"].toString().toInt()
            request.onContinueResult(User(uid, "MockUser"))
            return true
        }
        if (request.path == "getLoginUser") {
            request.onContinueResult(User(123,"202cb962ac59075b964b07152d234b70"))
            return true
        }
        return super.process(request)
    }

    override fun onError(request: TaskRequest, exception: Exception): Boolean {
        if (request.path == "getLoginUser") {
            request.onContinueResult(User(123,"202cb962ac59075b964b07152d234b70"))
            return true
        }
        return super.onError(request, exception)
    }
}