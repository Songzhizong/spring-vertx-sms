package com.zzsong.demo.sms.domain.model.template

import cn.idealframework.transmission.exception.ResourceNotFoundException
import cn.idealframework.util.NumberSystemConverter
import com.zzsong.demo.sms.infrastructure.DatabaseUtils
import com.zzsong.demo.sms.infrastructure.toJsonString
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.templates.SqlTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author 宋志宗 on 2021/9/15
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class TemplateDo {
  /** 主键 */
  var id = -1L

  /** 模板编码 */
  var code = ""

  /** 模板名称 */
  var name = ""

  /** 参数信息 */
  var params = ""

  /** 描述 */
  var description = ""

  companion object {
    private val log: Logger = LoggerFactory.getLogger(TemplateDo::class.java)
    const val ENTITY_NAME = "vx_sms_template"

    /** 创建短信模板对象 */
    fun create(
      name: String,
      code: String?,
      description: String?,
      params: Collection<TemplateParam>?
    ): TemplateDo {
      val generate = DatabaseUtils.generateId()
      val templateDo = TemplateDo()
      templateDo.id = generate
      templateDo.code = if (code?.isNotBlank() == true) {
        code
      } else {
        "SMS_" + NumberSystemConverter.tenSystemTo(generate, 36).uppercase()
      }
      templateDo.name = name
      templateDo.description = description ?: ""
      templateDo.params = params?.toJsonString() ?: ""
      return templateDo
    }

    /** 获取所有短信模板 */
    suspend fun findAll(): List<TemplateDo> {
      val sql = "select * from $ENTITY_NAME"
      val params: Map<String, Any> = emptyMap()
      val rowSet = select(sql, params)
      return rowSet.map { it }
    }

    /** 通过id获取短信模板, 不存在则抛出异常 */
    suspend fun findRequiredById(id: Long): TemplateDo {
      return findById(id).orElseThrow {
        log.info("短信模板: {} 不存在", id)
        ResourceNotFoundException("模板不存在")
      }
    }

    /** 通过id获取短信模板 */
    suspend fun findById(id: Long): Optional<TemplateDo> {
      val sql = "select * from $ENTITY_NAME where id = #{id}"
      val params: Map<String, Any> = mapOf("id" to id)
      val rowSet = select(sql, params)
      val templateDo = rowSet.firstOrNull()
      return Optional.ofNullable(templateDo)
    }

    /** 通过id删除模板 */
    suspend fun delete(id: Long) {
      val sql = "delete from $ENTITY_NAME where id = #{id}"
      val params = mapOf("id" to id)
      SqlTemplate.forUpdate(DatabaseUtils.pool, sql).execute(params).await()
    }

    /** 执行查询操作 */
    private suspend fun select(sql: String, params: Map<String, Any>): RowSet<TemplateDo> {
      return SqlTemplate.forQuery(DatabaseUtils.pool, sql)
        .mapTo { row ->
          val templateDo = TemplateDo()
          templateDo.id = row.getLong("id")
          templateDo.code = row.getString("code")
          templateDo.description = row.getString("description")
          templateDo.params = row.getString("params")
          templateDo
        }
        .execute(params)
        .await()
    }
  }

  /** 持久化对象 */
  suspend fun persist(): TemplateDo {
    val sql = """
      insert into $ENTITY_NAME (id, code, description, name, params)
      values (#{id}, #{code}, #{description}, #{name}, #{params})
    """.trimIndent()
    val params: Map<String, Any> = toSqlParams()
    SqlTemplate.forUpdate(DatabaseUtils.pool, sql).execute(params).await()
    return this
  }

  /** 更新模板信息 */
  suspend fun update() {
    val sql = """
      update $ENTITY_NAME
      set code = #{code}
      set description = #{description}
      set name = #{name}
      set params = #{params}
      where id = #{id}
    """.trimIndent()
    val params: Map<String, Any> = toSqlParams()
    SqlTemplate.forUpdate(DatabaseUtils.pool, sql).execute(params).await()
  }

  /** 删除模板 */
  suspend fun delete() {
    if (id < 1) {
      return
    }
    delete(id)
  }

  private fun toSqlParams(): Map<String, Any> {
    return mapOf<String, Any>(
      "id" to id,
      "code" to code,
      "description" to description,
      "name" to name,
      "params" to this.params
    )
  }
}
