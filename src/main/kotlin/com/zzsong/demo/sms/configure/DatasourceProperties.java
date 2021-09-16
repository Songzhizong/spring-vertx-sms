package com.zzsong.demo.sms.configure;

/**
 * @author 宋志宗 on 2021/9/15
 */
public class DatasourceProperties {

  /** 数据库连接信息 */
  private String url;

  /** 连接池最大连接数, 默认核心数 x 4 */
  private int maxPoolSize = Runtime.getRuntime().availableProcessors() << 2;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }
}
