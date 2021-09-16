package com.zzsong.demo.sms.provider.aliyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @author 宋志宗 on 2021/7/29
 */
public class AliYunResponse {

  @Nullable
  @JsonProperty("RequestId")
  private String requestId;

  @Nullable
  @JsonProperty("Message")
  private String message;

  @Nullable
  @JsonProperty("BizId")
  private String bizId;

  @Nullable
  @JsonProperty("Code")
  private String code;

  public @Nullable String getRequestId() {
    return requestId;
  }

  public void setRequestId(@Nullable String requestId) {
    this.requestId = requestId;
  }

  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public @Nullable String getBizId() {
    return bizId;
  }

  public void setBizId(@Nullable String bizId) {
    this.bizId = bizId;
  }

  public @Nullable String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
    this.code = code;
  }
}
