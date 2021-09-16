package com.zzsong.demo.sms.port.scheduler

import com.zzsong.demo.sms.domain.model.template.TemplateDomainService
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author 宋志宗 on 2021/9/16
 */
@Component
class TemplateScheduler(private val templateDomainService: TemplateDomainService) {

  @Scheduled(initialDelay = 5 * 60 * 1000L, fixedRate = 5 * 60 * 1000L)
  fun refreshProviderTemplateCache() {
    runBlocking {
      templateDomainService.refreshProviderTemplateCache()
    }
  }
}
