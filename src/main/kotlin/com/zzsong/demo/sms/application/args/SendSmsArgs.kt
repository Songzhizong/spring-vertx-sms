package com.zzsong.demo.sms.application.args

/**
 * @author 宋志宗 on 2021/9/14
 */
class SendSmsArgs {

  /** 模板编码 */
  var templateCode = ""

  /** 手机号列表 */
  var mobiles: List<String> = emptyList()

  /** 模板参数列表 */
  var params: Map<String, String>? = null
}
