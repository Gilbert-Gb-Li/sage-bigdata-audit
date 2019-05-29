${vo.curDate?string('yyyy-MM-dd')} ${vo.name} 是否达标：${vo.ok?string('yes', 'no') }; 差值：${vo.bound};
    ${vo.preDate?string('yyyy-MM-dd')}方差：${vo.preVariance}; 数据：<#list vo.preData as item>${item.docCount!''}; </#list>
    ${vo.curDate?string('yyyy-MM-dd')}方差：${vo.curVariance}; 数据：<#list vo.curData as item>${item.docCount!''}; </#list>
