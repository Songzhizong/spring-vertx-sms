package com.zzsong.demo.sms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class Application

fun main(args: Array<String>) {
  val availableProcessors = Runtime.getRuntime().availableProcessors()
  val ioWorkerCount = availableProcessors shl 1
  System.setProperty("reactor.netty.ioWorkerCount", ioWorkerCount.toString())
  runApplication<Application>(*args)
}
