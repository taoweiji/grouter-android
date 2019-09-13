package com.grouter.demo

import com.grouter.GActivityBuilder
import com.grouter.GRouter
import io.reactivex.Observable


fun GActivityBuilder.result(): Observable<Any> {
    return Observable.just(null)
}

fun GRouter.resultOnFinish(any: Any){

}

fun GRouter.result(any: Any){

}