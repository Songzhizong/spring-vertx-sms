package com.zzsong.demo.sms.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/9/15
 */
@Component
@ConfigurationProperties("sms")
public class ServerProperties {

  @Nonnull
  @NestedConfigurationProperty
  private ProviderProperties provider = new ProviderProperties();

  @Nonnull
  @NestedConfigurationProperty
  private DatasourceProperties datasource = new DatasourceProperties();

  @Nonnull
  public ProviderProperties getProvider() {
    return provider;
  }

  public void setProvider(@Nonnull ProviderProperties provider) {
    this.provider = provider;
  }

  @Nonnull
  public DatasourceProperties getDatasource() {
    return datasource;
  }

  public void setDatasource(@Nonnull DatasourceProperties datasource) {
    this.datasource = datasource;
  }
}
