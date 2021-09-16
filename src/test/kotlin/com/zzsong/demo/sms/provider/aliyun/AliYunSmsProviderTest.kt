package com.zzsong.demo.sms.provider.aliyun

import cn.idealframework.json.JsonUtils
import com.zzsong.demo.sms.domain.model.template.ProviderTemplateDo
import com.zzsong.demo.sms.provider.SendRequest
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

/**
 * @author 宋志宗 on 2021/9/15
 */
class AliYunSmsProviderTest {

  @Test
  @DelicateCoroutinesApi
  fun send() {
    val properties = AliYunProperties()
      .apply {
        signName = "美丽友联"
        accessKeyId = "ak"
        accessSecret = "sk"
      }
    val provider = AliYunSmsProvider(properties)

    val template = ProviderTemplateDo()
      .apply {
        id = 269454731720523776
        content = "验证码\${code}，您正在进行身份验证，打死不要告诉别人哦！"
        providerCode = "ali_yun"
        providerTemplate = "SMS_201125008"
        templateId = 269433648736894976
      }
    val sendRequest = SendRequest(template, listOf("mobile"), mapOf("code" to "867251"))
    runBlocking {
      val send = provider.send(sendRequest)
      val result = send.firstOrNull()
      Assert.assertTrue(result != null)
      Assert.assertFalse(result!!.description.startsWith("exception:"))
      println(JsonUtils.toJsonString(send))
    }
  }
}
