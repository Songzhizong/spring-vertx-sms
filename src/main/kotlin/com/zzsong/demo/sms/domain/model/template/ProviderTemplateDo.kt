package com.zzsong.demo.sms.domain.model.template

import com.zzsong.demo.sms.infrastructure.DatabaseUtils
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.templates.SqlTemplate

/**
 * @author 宋志宗 on 2021/9/15
 */
@Suppress("unused")
class ProviderTemplateDo {
  /** 主键 */
  var id = -1L

  /** 模板id */
  var templateId = -1L

  /** 服务提供商编码 */
  var providerCode = ""

  /** 服务提供商短信模板 */
  var providerTemplate = ""

  /** 短信内容 */
  var content = ""

  companion object {
    private const val ENTITY_NAME = "vx_sms_template_provider"

    fun create(
      templateDo: TemplateDo,
      providerCode: String,
      providerTemplate: String?,
      content: String
    ): ProviderTemplateDo {
      val generate = DatabaseUtils.generateId()
      val providerTemplateDo = ProviderTemplateDo()
      providerTemplateDo.id = generate
      providerTemplateDo.templateId = templateDo.id
      providerTemplateDo.providerCode = providerCode
      providerTemplateDo.providerTemplate = providerTemplate ?: ""
      providerTemplateDo.content = content
      return providerTemplateDo
    }

    suspend fun findAll(): List<ProviderTemplateDo> {
      val sql = "select * from $ENTITY_NAME"
      val params: Map<String, Any> = emptyMap()
      val rowSet = select(sql, params)
      return rowSet.map { it }
    }

    private suspend fun select(sql: String, params: Map<String, Any>): RowSet<ProviderTemplateDo> {
      return SqlTemplate.forQuery(DatabaseUtils.pool, sql)
        .mapTo { row ->
          val templateDo = ProviderTemplateDo()
          templateDo.id = row.getLong("id")
          templateDo.templateId = row.getLong("template_id")
          templateDo.providerCode = row.getString("provider_code")
          templateDo.providerTemplate = row.getString("provider_template")
          templateDo.content = row.getString("content")
          templateDo
        }
        .execute(params)
        .await()
    }
  }

  suspend fun persist(): ProviderTemplateDo {
    val sql = """
      insert into $ENTITY_NAME
      (id, content, provider_code, provider_template, template_id)
      values
      (#{id}, #{content}, #{provider_code}, #{provider_template}, #{template_id})
    """.trimIndent()
    val params: Map<String, Any> = toSqlParams()
    SqlTemplate.forUpdate(DatabaseUtils.pool, sql).execute(params).await()
    return this
  }

  suspend fun update(): ProviderTemplateDo {
    val sql = """
      update $ENTITY_NAME
      set content = #{content}
      set provider_code = #{provider_code}
      set provider_template = #{provider_template}
      set template_id = #{template_id}
      where id = #{id}
    """.trimIndent()
    val params: Map<String, Any> = toSqlParams()
    SqlTemplate.forUpdate(DatabaseUtils.pool, sql).execute(params).await()
    return this
  }

  private fun toSqlParams(): Map<String, Any> {
    return mapOf<String, Any>(
      "id" to id,
      "content" to content,
      "provider_code" to providerCode,
      "provider_template" to providerTemplate,
      "template_id" to templateId
    )
  }
}
