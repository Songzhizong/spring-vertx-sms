package com.zzsong.demo.sms.configure

import cn.idealframework.util.Asserts
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @author 宋志宗 on 2021/9/15
 */
@Configuration
class BeanConfigure(val serverProperties: ServerProperties) {

  @Bean
  fun vertx(): Vertx {
    return Vertx.vertx()
  }

  @Bean
  fun webClient(vertx: Vertx): WebClient {
    val options = WebClientOptions()
    options.isKeepAlive = true
    return WebClient.create(vertx, options)
  }

  @Bean
  fun sqlPool(vertx: Vertx): Pool {
    val datasource = serverProperties.datasource
    val uri = datasource.url
    Asserts.notBlank(uri, "Datasource url is blank")
    // 连接池配置
    val poolOptions = PoolOptions().setMaxSize(datasource.maxPoolSize)
    return MySQLPool.pool(vertx, uri, poolOptions)
  }
}
