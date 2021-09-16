package com.zzsong.demo.sms.port.http

import cn.idealframework.transmission.Result
import com.zzsong.demo.sms.application.SmsService
import com.zzsong.demo.sms.application.TemplateService
import com.zzsong.demo.sms.application.args.CreateProviderTemplateArgs
import com.zzsong.demo.sms.application.args.CreateTemplateArgs
import com.zzsong.demo.sms.application.args.SendSmsArgs
import com.zzsong.demo.sms.infrastructure.parseJson
import com.zzsong.demo.sms.infrastructure.parseJsonList
import io.vertx.ext.web.RoutingContext
import org.springframework.stereotype.Component

/**
 * @author 宋志宗 on 2021/9/15
 */
@Component
class RouterHandler(
  private val smsService: SmsService,
  private val templateService: TemplateService
) {

  /**
   * 发送短信
   *
   * @return 任务id
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun sendSms(ctx: RoutingContext): Result<Long> {
    val bodyAsString = ctx.bodyAsString
    val args = bodyAsString.parseJson(SendSmsArgs::class)
    smsService.send(args)
    return Result.success()
  }

  /**
   * 批量发送短信
   *
   * @return 任务id
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun batchSendSms(ctx: RoutingContext): Result<Long> {
    val bodyAsString = ctx.bodyAsString
    val args = bodyAsString.parseJsonList(SendSmsArgs::class)
    smsService.send(args)
    return Result.success()
  }

  /**
   * 创建短信模板
   *
   * @return 模板id
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun createTemplate(ctx: RoutingContext): Result<Long> {
    val bodyAsString = ctx.bodyAsString
    val args = bodyAsString.parseJson(CreateTemplateArgs::class)
    val templateId = templateService.createTemplate(args)
    return Result.data(templateId)
  }

  /**
   * 新增短信服务提供方短信模板
   *
   * @return 主键
   * @author 宋志宗 on 2021/9/15
   */
  suspend fun createProviderTemplate(ctx: RoutingContext): Result<Long> {
    val bodyAsString = ctx.bodyAsString
    val args = bodyAsString.parseJson(CreateProviderTemplateArgs::class)
    val templateId = templateService.createProviderTemplate(args)
    return Result.data(templateId)
  }
}
