package com.zzsong.demo.sms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SpringVertxDemoApplication

fun main(args: Array<String>) {
  runApplication<SpringVertxDemoApplication>(*args)
}
