package com.zzsong.demo.sms.provider.aliyun;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 宋志宗 on 2021/7/29
 */
@Component("cloudSmsAliYunProperties")
@ConfigurationProperties("sms.provider.ali-yun")
public class AliYunProperties {
  private String baseUrl = "https://dysmsapi.aliyuncs.com";
  /**
   * 短信签名名称
   */
  private String signName;

  /**
   * 地域ID
   */
  private String regionId = "cn-hangzhou";

  /**
   * RAM账号的AccessKey ID
   */
  private String accessKeyId;

  /**
   * RAM账号AccessKey Secret
   */
  private String accessSecret;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getSignName() {
    return signName;
  }

  public void setSignName(String signName) {
    this.signName = signName;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessSecret() {
    return accessSecret;
  }

  public void setAccessSecret(String accessSecret) {
    this.accessSecret = accessSecret;
  }
}
