package com.zzsong.demo.sms.configure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author 宋志宗 on 2021/9/15
 */
@Component
@ConfigurationProperties("sms")
public class ServerProperties {

  @NotNull
  @NestedConfigurationProperty
  private ProviderProperties provider = new ProviderProperties();

  @NotNull
  @NestedConfigurationProperty
  private DatasourceProperties datasource = new DatasourceProperties();

  public @NotNull ProviderProperties getProvider() {
    return provider;
  }

  public void setProvider(@NotNull ProviderProperties provider) {
    this.provider = provider;
  }

  public @NotNull DatasourceProperties getDatasource() {
    return datasource;
  }

  public void setDatasource(@NotNull DatasourceProperties datasource) {
    this.datasource = datasource;
  }
}
