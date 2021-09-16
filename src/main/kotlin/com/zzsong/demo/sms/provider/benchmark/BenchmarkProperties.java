package com.zzsong.demo.sms.provider.benchmark;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基准测试配置信息
 *
 * @author 宋志宗 on 2021/9/17
 */
@Component
@ConfigurationProperties("sms.provider.benchmark")
public class BenchmarkProperties {
  /** 运行基准测试采用的http客户端 */
  @NotNull
  private HttpClient clientType = HttpClient.VERTX;

  /** 目标服务地址 */
  @Nullable
  private String targetUrl;

  @NotNull
  public HttpClient getClientType() {
    return clientType;
  }

  public void setClientType(@NotNull HttpClient clientType) {
    this.clientType = clientType;
  }

  @Nullable
  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(@Nullable String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public enum HttpClient {
    /** spring webclient */
    SPRING,
    /** vert.x webclient */
    VERTX
  }
}
