<style>
    table {
        border-collapse: collapse;
        width: 100%;
        margin-bottom: 4px;
        margin-top: 4px;

        line-height: 2em;
    }

    th {
        font-size: 14px;
        color: #fff;
        background-color: #555;
        border: 1px solid #555;

        padding: 3px;
        vertical-align: top;
        text-align: left;
    }

    td {
        font-size: 12px;
        min-width: 24px;
        border: 1px solid #d4d4d4;
        padding: 5px;
        padding-top: 7px;
        padding-bottom: 7px;
        vertical-align: top;
    }

    .tr-fail td {
        color: red;
    }

    .fail {
        color: red;
    }
</style>
<h2>日期：${date}</h2>
<h2>数据源：${rule.appId}</h2>

<table>
    <thead>
    <tr>
        <th>指标</th>
        <th>数值</th>
        <th>达标</th>
        <th>明细</th>
    </tr>
    </thead>

    <tbody>
        <#list items as map>
        <tr <#if !map['result']> class="fail"</#if>>
            <td style="min-width: 100px">${map['name']}</td>
            <td>${map['data']}</td>
            <td>${map['result']?string('√', '×')}</td>
            <td><pre>${map['detail']}</pre></td>
        </tr>
        </#list>
    </tbody>
</table>