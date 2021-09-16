package com.zzsong.demo.sms.provider

import com.zzsong.demo.sms.domain.model.template.ProviderTemplateDo

/**
 * @author 宋志宗 on 2021/9/15
 */
data class SendRequest(
  /** 提供方短信模板信息 */
  val template: ProviderTemplateDo,
  /** 手机号码列表 */
  val mobiles: Collection<String>,
  /** 参数列表 */
  val params: Map<String, String>?
)
