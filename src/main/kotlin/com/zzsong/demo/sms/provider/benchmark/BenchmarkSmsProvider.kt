package com.zzsong.demo.sms.provider.benchmark

import cn.idealframework.util.Asserts
import com.zzsong.demo.sms.infrastructure.await
import com.zzsong.demo.sms.provider.SendRequest
import com.zzsong.demo.sms.provider.SendResult
import com.zzsong.demo.sms.provider.SmsProvider
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
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
class BenchmarkSmsProvider(
  private val webClient: WebClient,
  private val benchmarkProperties: BenchmarkProperties
) : SmsProvider {
  companion object {
    const val PROVIDER_CODE = "benchmark"
    private val log: Logger = LoggerFactory.getLogger(BenchmarkSmsProvider::class.java)
  }

  private val springWebClient: org.springframework.web.reactive.function.client.WebClient

  init {
    val httpClient = HttpClient
      .create(ConnectionProvider.create("httpClient", 2048))
      .responseTimeout(Duration.ofSeconds(5))
      .keepAlive(true)
    springWebClient = org.springframework.web.reactive.function.client.WebClient.builder()
      .clientConnector(ReactorClientHttpConnector(httpClient))
      .uriBuilderFactory(
        DefaultUriBuilderFactory()
          .apply {
            encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
          })
      .build()
  }

  override fun getProviderCode() = PROVIDER_CODE

  override suspend fun send(sendRequest: SendRequest): List<SendResult> {
    val template = sendRequest.template
    val mobiles = sendRequest.mobiles
    val params = sendRequest.params ?: emptyMap()
    val content = formatContent(template.content, params)
    val mobileStr = mobiles.joinToString(",")
    val body = mapOf(
      "template" to template.providerTemplate,
      "mobiles" to mobileStr,
      "params" to params
    )
    val targetUrl = benchmarkProperties.targetUrl
    Asserts.notBlank(targetUrl, "Benchmark target url is blank")
    when (benchmarkProperties.clientType) {
      BenchmarkProperties.HttpClient.SPRING -> {
        sendBySpringWebclient(body, targetUrl!!)
      }
      BenchmarkProperties.HttpClient.VERTX -> {
        sendByVertxWebclient(body, targetUrl!!)
      }
    }
    return mobiles.map { mobile ->
      SendResult().also {
        it.success = true
        it.description = "success"
        it.mobile = mobile
        it.content = content
        it.templateId = template.templateId
      }
    }
  }

  private suspend fun sendByVertxWebclient(body: Map<String, Any>, targetUrl: String) {
    val response = webClient.postAbs(targetUrl).sendJson(body).await()
    log.debug("response: {}", response.bodyAsString())
  }

  private suspend fun sendBySpringWebclient(body: Map<String, Any>, targetUrl: String) {
    val result = springWebClient.post().uri(targetUrl)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(body))
      .exchangeToMono { it.bodyToMono(String::class.java) }
      .await()
    log.debug("response: {}", result)
  }
}
