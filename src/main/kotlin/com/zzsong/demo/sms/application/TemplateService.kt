package com.zzsong.demo.sms.application

import cn.idealframework.util.Asserts
import com.zzsong.demo.sms.application.args.CreateProviderTemplateArgs
import com.zzsong.demo.sms.application.args.CreateTemplateArgs
import com.zzsong.demo.sms.domain.model.template.ProviderTemplateDo
import com.zzsong.demo.sms.domain.model.template.TemplateDo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 宋志宗 on 2021/9/15
 */
@Service
class TemplateService {
  companion object {
    val log: Logger = LoggerFactory.getLogger(TemplateService::class.java)
  }

  /**
   * 新增短信模板
   *
   * @param args 短信模板信息
   * @return 主键
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun createTemplate(args: CreateTemplateArgs): Long {
    val code = args.code
    val name = args.name
    val description = args.description
    val params = args.params
    val templateDo = TemplateDo
      .create(name, code, description, params)
      .persist()
    log.info("新增短信模板 {} : {}", templateDo.name, templateDo.code)
    return templateDo.id
  }

  /**
   * 新增短信服务提供方短信模板
   *
   * @param args 模板信息
   * @return 主键
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun createProviderTemplate(args: CreateProviderTemplateArgs): Long {
    val templateId = args.templateId
    Asserts.nonnull(templateId, "短信模板id不能为空")
    val templateDo = TemplateDo.findRequiredById(templateId!!)
    val providerCode = args.providerCode
    Asserts.notBlank(providerCode, "providerCode不能为空")
    val providerTemplate = args.providerTemplate
    val content = args.content
    Asserts.notBlank(content, "content不能为空")
    val providerTemplateDo = ProviderTemplateDo
      .create(templateDo, providerCode!!, providerTemplate, content!!)
      .persist()
    log.info("新增服务提供商模板配置, {} : {}", providerCode, templateDo.name)
    return providerTemplateDo.id
  }
}
