package com.zzsong.demo.sms.infrastructure

import cn.idealframework.json.JsonUtils
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KClass

/** 对象转json字符串 */
fun Any.toJsonString() = JsonUtils.toJsonString(this)

/** json字符串转对象 */
fun <T : Any> String.parseJson(clazz: KClass<T>) = JsonUtils.parse(this, clazz.java)

/** json字符串转对象集合 */
fun <T : Any> String.parseJsonList(clazz: KClass<T>): List<T> =
  JsonUtils.parseList(this, clazz.java)

suspend fun <T> Mono<T>.await(): T {
  return suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
    doOnNext { cont.resume(it as T) }
      .doOnError { cont.resumeWithException(it) }
      .subscribe()
  }
}

suspend fun <T> Flux<T>.await(): List<T> {
  return suspendCancellableCoroutine { cont: CancellableContinuation<List<T>> ->
    collectList()
      .doOnNext { cont.resume(it.map { t: T -> t }) }
      .doOnError { cont.resumeWithException(it) }
      .subscribe()
  }
}
