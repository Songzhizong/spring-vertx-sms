package com.zzsong.demo.sms.configure

import cn.idealframework.id.IDGeneratorFactory
import com.zzsong.demo.sms.domain.model.template.TemplateDomainService
import com.zzsong.demo.sms.infrastructure.DatabaseUtils
import com.zzsong.demo.sms.port.http.RouterHandler
import com.zzsong.demo.sms.port.http.ServerVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.Pool
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration

/**
 * @author 宋志宗 on 2021/9/15
 */
@Configuration
class ServerRunner(
  private val vertx: Vertx,
  @Value("\${server.port:8080}")
  private val port: Int,
  private val sqlPool: Pool,
  private val routerHandler: RouterHandler,
  private val idGeneratorFactory: IDGeneratorFactory,
  private val templateDomainService: TemplateDomainService
) : ApplicationRunner, SmartInitializingSingleton {

  override fun run(args: ApplicationArguments?) {
    var instance = 0
    vertx.nettyEventLoopGroup().forEach { _ -> instance++ }
    val config = JsonObject()
    config.put(ServerVerticle.SERVER_PORT_CONF, port)
    val options = DeploymentOptions()
      .setInstances(instance)
      .setConfig(config)
    vertx.deployVerticle(ServerVerticle::class.java, options)
  }

  override fun afterSingletonsInstantiated() {
    val idGenerator = idGeneratorFactory.getGenerator("database")
    DatabaseUtils.idGenerator = idGenerator
    DatabaseUtils.pool = sqlPool
    ServerVerticle.routerHandler = routerHandler
    runBlocking {
      templateDomainService.refreshProviderTemplateCache()
    }
  }
}
