package com.zzsong.demo.sms.domain.model.template

import cn.idealframework.lang.StringUtils
import com.zzsong.demo.sms.configure.ServerProperties
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 宋志宗 on 2021/9/15
 */
@Service
class TemplateDomainService(private val serverProperties: ServerProperties) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(TemplateDomainService::class.java)
  }

  private var providerTemplateCache = emptyMap<String, ProviderTemplateDo>()


  fun findProviderTemplate(templateCode: String): ProviderTemplateDo? {
    return providerTemplateCache[templateCode]
  }

  suspend fun refreshProviderTemplateCache() {
    val enabledProvider = serverProperties.provider.enabled
    if (StringUtils.isBlank(enabledProvider)) {
      log.error("未配置短信服务提供商, 请配置 sms.provider.enabled")
      return
    }
    providerTemplateCache = coroutineScope {
      val templatesAsync = async { TemplateDo.findAll() }
      val providerTemplatesAsync = async { ProviderTemplateDo.findAll() }
      val templates = templatesAsync.await()
      val templateMap = templates.associateBy { it.id }
      val providerTemplates = providerTemplatesAsync.await()
      val filter = providerTemplates.filter { it.providerCode == enabledProvider }
      val providerTemplateCache = HashMap<String, ProviderTemplateDo>()
      for (providerTemplateDo in filter) {
        val templateId = providerTemplateDo.templateId
        val templateDo = templateMap[templateId]
        if (templateDo != null) {
          providerTemplateCache[templateDo.code] = providerTemplateDo
        }
      }
      log.debug("刷新 {} 条短信模板信息", providerTemplateCache.size)
      providerTemplateCache
    }
  }
}
