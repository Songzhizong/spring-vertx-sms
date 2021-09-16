package com.zzsong.demo.sms.application

import cn.idealframework.json.JsonUtils
import cn.idealframework.transmission.exception.ResourceNotFoundException
import com.zzsong.demo.sms.application.args.SendSmsArgs
import com.zzsong.demo.sms.domain.model.template.ProviderTemplateDo
import com.zzsong.demo.sms.domain.model.template.TemplateDomainService
import com.zzsong.demo.sms.provider.SendRequest
import com.zzsong.demo.sms.provider.SmsProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 宋志宗 on 2021/9/15
 */
@Service
class SmsService(
  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  private val smsProvider: SmsProvider,
  private val templateDomainService: TemplateDomainService
) {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SmsService::class.java)
  }

  suspend fun send(args: SendSmsArgs) {
    if (log.isDebugEnabled) {
      log.debug("短信参数: {}", JsonUtils.toJsonString(args))
    }
    val templateCode = args.templateCode
    val mobiles = args.mobiles
    val params = args.params
    val template: ProviderTemplateDo = templateDomainService
      .findProviderTemplate(templateCode)
      ?: kotlin.run {
        val providerCode = smsProvider.getProviderCode()
        log.warn("找不到短信模板配置信息: {} - {}", providerCode, templateCode)
        throw ResourceNotFoundException("找不到短信模板配置信息")
      }
    val request = SendRequest(template, mobiles, params)
    smsProvider.send(request)
  }

  suspend fun send(argsList: List<SendSmsArgs>) {
    val sendRequestList = argsList
      .map { args ->
        val templateCode = args.templateCode
        val template: ProviderTemplateDo = templateDomainService
          .findProviderTemplate(templateCode)
          ?: kotlin.run {
            log.error("找不到短信模板配置信息: {}", templateCode)
            throw ResourceNotFoundException("找不到短信模板配置信息")
          }
        val mobiles = args.mobiles
        val params = args.params
        SendRequest(template, mobiles, params)
      }
    smsProvider.send(sendRequestList)
  }
}
