#### ${date}日报: ${rule.name}

- 达标 指标 数值 明细
<#list items as map>
1. <#if !map['result']>**</#if>${map['result']?string('√', '×')} ${map['name']} ${map['data']} ${map['detail']}<#if !map['result']>**</#if>
</#list>
