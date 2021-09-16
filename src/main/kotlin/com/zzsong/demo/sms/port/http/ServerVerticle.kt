package com.zzsong.demo.sms.port.http

import cn.idealframework.transmission.Result
import cn.idealframework.transmission.exception.VisibleException
import cn.idealframework.transmission.exception.VisibleRuntimeException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.zzsong.demo.sms.infrastructure.toJsonString
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ServerVerticle : CoroutineVerticle() {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(ServerVerticle::class.java)
    const val SERVER_PORT_CONF = "server.port"
    lateinit var routerHandler: RouterHandler

    private var dateTimeFormatter: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private var dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    private val javaTimeModule: SimpleModule = JavaTimeModule()
      .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
      .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))
      .addSerializer(LocalDate::class.java, LocalDateSerializer(dateFormatter))
      .addDeserializer(LocalDate::class.java, LocalDateDeserializer(dateFormatter))
      .addSerializer(LocalTime::class.java, LocalTimeSerializer(timeFormatter))
      .addDeserializer(LocalTime::class.java, LocalTimeDeserializer(timeFormatter))

    private val longModule: SimpleModule = SimpleModule()
      .addSerializer(Long::class.java, ToStringSerializer.instance)
      .addSerializer(Long::class.javaObjectType, ToStringSerializer.instance)
      .addSerializer(java.lang.Long.TYPE, ToStringSerializer.instance)

    val objectMapper: ObjectMapper = ObjectMapper()
      .registerModule(longModule)
      .registerModule(javaTimeModule)
      .registerModule(KotlinModule())
      .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
      .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"))
      .findAndRegisterModules()

    private val benchmarkResult = Result.success<Void>().toJsonString()
  }

  override suspend fun start() {
    val router = Router.router(vertx)

    router.route()
      .handler { ctx ->
        val startTime = System.currentTimeMillis()
        ctx.put("startTime", startTime)
        ctx.next()
      }

    // 基准测试
    router.route("/benchmark")
      .handler(BodyHandler.create())
      .handler { ctx ->
        val handler = CoroutineExceptionHandler { _, e -> ctx.exceptionHandler(e) }
        launch(handler) {
          delay(300)
          ctx.writeJsonResponse(benchmarkResult)
        }
      }

    // 发送短信
    router.post("/sms/send")
      .handler(BodyHandler.create())
      .handler { ctx -> ctx.launch { routerHandler.sendSms(ctx) } }

    // 批量发送发送短信
    router.post("/sms/send/batch")
      .handler(BodyHandler.create())
      .handler { ctx -> ctx.launch { routerHandler.batchSendSms(ctx) } }

    // 新增短信模板
    router.post("/template")
      .handler(BodyHandler.create())
      .handler { ctx -> ctx.launch { routerHandler.createTemplate(ctx) } }

    // 新增短信服务提供商模板信息
    router.post("/template/provider")
      .handler(BodyHandler.create())
      .handler { ctx -> ctx.launch { routerHandler.createProviderTemplate(ctx) } }

    val config = context.config()
    val port = config.getInteger(SERVER_PORT_CONF, 8080)
    vertx.createHttpServer().requestHandler(router).listen(port).await()
    log.info("HTTP server started on port {}", port)
  }

  private fun RoutingContext.launch(function: suspend () -> Result<*>) {
    val handler = CoroutineExceptionHandler { _, e -> exceptionHandler(e) }
    launch(handler) {
      val result = function.invoke()
      writeJsonResponse(result)
    }
  }

  private fun RoutingContext.writeJsonResponse(result: Result<*>) {
    val jsonString = objectMapper.writeValueAsString(result)
    writeJsonResponse(jsonString)
  }

  private fun RoutingContext.writeJsonResponse(result: String) {
    response().putHeader("Content-Type", "application/json;charset=utf-8")
    writeResponse(result)
  }

  private fun RoutingContext.writeResponse(jsonString: String) {
    val startTime = get<Long>("startTime")
    val uri = request().uri()
    end(jsonString)
    log.info("Request uri: {} 耗时 {}ms", uri, System.currentTimeMillis() - startTime)
  }

  private fun RoutingContext.exceptionHandler(throwable: Throwable) {
    when (throwable) {
      is VisibleRuntimeException,
      is VisibleException -> {
        val result = Result.exception<Unit>(throwable)
        writeJsonResponse(result)
      }
      else -> {
        log.info("未处理的异常信息: ", throwable)
        val result = Result.exception<Unit>(throwable)
        writeJsonResponse(result)
      }
    }
  }
}
