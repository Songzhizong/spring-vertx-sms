package com.zzsong.demo.sms.application.args;

import org.jetbrains.annotations.Nullable;

/**
 * @author 宋志宗 on 2021/9/15
 */
public class CreateProviderTemplateArgs {

  /** 短信模板id */
  @Nullable
  private Long templateId;

  /** 短信服务提供方编码 */
  @Nullable
  private String providerCode;

  /**
   * 短信服务提供方模板编码
   * 有些短信服务提供商不需要建立短信模板, 因此非必填
   */
  @Nullable
  private String providerTemplate;

  /** 短信内容, 模板内动态参数使用占位符 ${param} (param为参数名) */
  @Nullable
  private String content;

  @Nullable
  public Long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(@Nullable Long templateId) {
    this.templateId = templateId;
  }

  @Nullable
  public String getProviderCode() {
    return providerCode;
  }

  public void setProviderCode(@Nullable String providerCode) {
    this.providerCode = providerCode;
  }

  @Nullable
  public String getProviderTemplate() {
    return providerTemplate;
  }

  public void setProviderTemplate(@Nullable String providerTemplate) {
    this.providerTemplate = providerTemplate;
  }

  @Nullable
  public String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }
}
