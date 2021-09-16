package com.zzsong.demo.sms.infrastructure

import cn.idealframework.id.IDGenerator
import io.vertx.sqlclient.Pool

/**
 * @author 宋志宗 on 2021/9/15
 */
object DatabaseUtils {
  lateinit var pool: Pool
  lateinit var idGenerator: IDGenerator

  fun generateId() = idGenerator.generate()
}
