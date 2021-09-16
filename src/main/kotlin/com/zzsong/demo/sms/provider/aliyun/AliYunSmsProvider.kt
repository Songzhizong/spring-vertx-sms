package com.zzsong.demo.sms.provider.aliyun

import cn.idealframework.http.WebClients
import com.zzsong.demo.sms.infrastructure.await
import com.zzsong.demo.sms.infrastructure.parseJson
import com.zzsong.demo.sms.provider.SendRequest
import com.zzsong.demo.sms.provider.SendResult
import com.zzsong.demo.sms.provider.SmsProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.util.DefaultUriBuilderFactory
import java.time.Duration

/**
 * @author 宋志宗 on 2021/9/15
 */
@Component
@ConditionalOnProperty(
  prefix = "sms.provider",
  name = ["enabled"],
  havingValue = AliYunSmsProvider.PROVIDER_CODE
)
class AliYunSmsProvider(
  private val aliYunProperties: AliYunProperties
) : SmsProvider {
  companion object {
    const val PROVIDER_CODE = "ali_yun"
    private val log: Logger = LoggerFactory.getLogger(AliYunSmsProvider::class.java)
    private const val SUCCESS_CODE = "OK"
    private val webClient = WebClients
      .createWebClientBuilder(Duration.ofSeconds(5))
      .uriBuilderFactory(DefaultUriBuilderFactory()
        .apply {
          encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        })
      .build()
  }

  init {
    log.info("使用阿里云作为短信服务提供商")
  }

  override fun getProviderCode(): String {
    return PROVIDER_CODE
  }

  override suspend fun send(sendRequest: SendRequest): List<SendResult> {
    val template = sendRequest.template
    val mobiles = sendRequest.mobiles
    val params = sendRequest.params ?: emptyMap()
    val templateCode = template.providerTemplate
    val templateId = template.templateId
    val content = formatContent(template.content, params)
    val res = try {
      val url = AliYunSmsUtils.createUrl(templateCode, mobiles, params, aliYunProperties)
      log.debug("AliYun sms request: {}", url)
      val bodyAsString = webClient.get().uri(url)
        .exchangeToMono { it.bodyToMono(String::class.java) }
        .await()
      log.debug("阿里云短信服务响应结果: {}", bodyAsString)
      bodyAsString.parseJson(AliYunResponse::class)
    } catch (exception: Exception) {
      log.warn("Send sms ex: ", exception)
      val message = exception.message ?: exception.javaClass.name
      return mobiles.map { mobile ->
        SendResult().apply {
          success = false
          description = "exception: $message"
          this.mobile = mobile
          this.content = content
          this.templateId = templateId
        }
      }
    }
    val code = res.code
    var success = true
    var message = "success"
    if (!SUCCESS_CODE.equals(code, true)) {
      success = false
      message = res.message ?: "null"
    }
    return mobiles.map { mobile ->
      SendResult().apply {
        this.success = success
        description = message
        this.mobile = mobile
        this.content = content
        this.templateId = templateId
      }
    }
  }
}
