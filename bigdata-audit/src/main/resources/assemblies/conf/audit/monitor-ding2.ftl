日期：${fireDate}

数据源：${rule.appId}


- 达标 指标 数值
<#list items as map>
1. <#if !map['result']>**</#if>${map['result']?string('√', '×')} ${map['name']}   ${map['data']}<#if !map['result']>**</#if>
</#list>
