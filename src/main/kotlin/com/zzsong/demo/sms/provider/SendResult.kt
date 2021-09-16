package com.zzsong.demo.sms.provider

/**
 * @author 宋志宗 on 2021/9/15
 */
class SendResult {
  /** 是否发送成功  */
  var success = false

  /** 描述信息  */
  var description = ""

  /** 手机号码  */
  var mobile = ""

  /** 短信内容  */
  var content = ""

  /** 模板id */
  var templateId = 0L
}
