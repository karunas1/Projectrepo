<#function maskCardNo_fr_cb_ecom maskedCardNum>

  <#assign l = maskedCardNum?length>
  <#assign ms = maskedCardNum?substring(l-4,l)>
  <#assign mcn = ms?left_pad(l, "#")>
  
  <#return mcn>
</#function>

<#function dateTime_fr_cb_ecom dateTime>

  <#assign yyyy = dateTime[0..3]>
  <#assign mm = dateTime[5..6]>
  <#assign dd = dateTime[8..9]>
  <#assign time = dateTime[10..]>
  
  
  <#assign dateTime_ddmmyyyy = dd + "-" + mm + "-" + yyyy + time>
  <#return dateTime_ddmmyyyy>
</#function>