package com.zzsong.demo.sms.provider

import cn.idealframework.lang.StringUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * 短信服务提供商发送短信接口
 *
 * @author 宋志宗 on 2021/9/15
 */
interface SmsProvider {

  fun getProviderCode(): String

  /** 发送短信, 相同短信内容群发 */
  suspend fun send(sendRequest: SendRequest): List<SendResult>

  /** 发送短信, 不同短信内容群发 */
  suspend fun send(sendRequestList: List<SendRequest>): List<SendResult> {
    val list = ArrayList<Deferred<List<SendResult>>>()
    return coroutineScope {
      for (request in sendRequestList) {
        list.add(async { send(request) })
      }
      list.map { it.await() }.flatten()
    }
  }

  fun formatContent(content: String, params: Map<String, String>?): String {
    if (content.isBlank() || params == null || params.isEmpty()) {
      return content
    }
    var result = content
    params.forEach { (k, v) ->
      result = StringUtils.replace(result, "\${$k}", v)
    }
    return result
  }
}
