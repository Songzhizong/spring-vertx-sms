package com.zzsong.demo.sms.provider.benchmark

import cn.idealframework.http.WebClients
import com.zzsong.demo.sms.provider.SendRequest
import com.zzsong.demo.sms.provider.SendResult
import com.zzsong.demo.sms.provider.SmsProvider
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * 用于基准测试的的短信服务提供商
 *
 * @author 宋志宗 on 2021/9/17
 */
@Component
@ConditionalOnProperty(
  prefix = "sms.provider",
  name = ["enabled"],
  havingValue = BenchmarkSmsProvider.PROVIDER_CODE
)
class BenchmarkSmsProvider(vertx: Vertx) : SmsProvider {
  companion object {
    const val PROVIDER_CODE = "benchmark"
    private val log: Logger = LoggerFactory.getLogger(BenchmarkSmsProvider::class.java)
  }

  private val springWebClient = WebClients
    .createWebClientBuilder(Duration.ofSeconds(5))
    .build()
  private val vertxWebClient = WebClient.create(vertx, WebClientOptions().setKeepAlive(true))

  override fun getProviderCode() = PROVIDER_CODE

  override suspend fun send(sendRequest: SendRequest): List<SendResult> {
    TODO("Not yet implemented")
  }
}
