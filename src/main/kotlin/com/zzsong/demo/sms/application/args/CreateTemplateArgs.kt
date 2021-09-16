package com.zzsong.demo.sms.application.args

import com.zzsong.demo.sms.domain.model.template.TemplateParam

/**
 * @author 宋志宗 on 2021/9/15
 */
class CreateTemplateArgs {

  /** 模板名称 */
  var name = ""

  /** 模板编码 */
  var code: String? = null

  /** 模板描述 */
  var description: String? = null

  /** 模板参数列表 */
  var params: Set<TemplateParam>? = null
}
