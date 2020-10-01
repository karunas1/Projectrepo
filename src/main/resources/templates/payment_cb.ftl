<!DOCTYPE html>
<html>
   <head>
      <meta name="defaultSubject" content="Votre ticket de paiement">
      <meta name="transactionType" content="SALE,VOID,REFUND,CARD_VERIFICATION">
      <link href='https://fonts.googleapis.com/css?family=Courier%20New' rel='stylesheet'>
      <style>
         body {
         font-size: 12px;
         font-style: normal;
         /*font-family: "Times New Roman", Times, serif;*/
         font-family: "Courier New", Courier, monospace;
         box-shadow: 0 0 1in -0.25in rgba(0, 0, 0, 0.5);
         padding:2mm;
         margin: 0 0 0 0;
         width: 56mm;
         marginheight: 0;
         }
         hr.striped  {
         border: 1px dashed #000;
         width: 70%;
         margin: auto;
         margin-top: 2%;
         margin-bottom: 2%;
         }
      </style>
   </head>
   <body>
      <div style="margin:0 auto;padding:0;background-color:#eeeeee;background-image:url('')">
         <table width="20%" style="padding: 1px 0px 0px 0px;; cellpadding="0" ;cellspacing="0" align="center">
         <tr>
            <td bgcolor="#ffffff" style="padding: 10px 20px">
               <table cellpadding="0" cellspacing="0">
                  <tr>
                     <td width="100%" >
                        <!-- Header Section -->
                        <center>
                           <table width="300px" cellpadding="0" cellspacing="0">
                              <tr >
                                 <td align="center"><b>CARTE BANCAIRE</td>
                              </tr>
                              <tr>
                                 <td colspan="2"><br/><br/></td>
                              </tr>
                              <tr >
                                 <td align="center">${.data_model["transaction.cardBrand"]}</td>
                              </tr>
                              <tr >
                                 <td align="center">USAGE: ${.data_model["transaction.fundingSource"]}</td>
                              </tr>
                              <tr>
                                 <td align="center">${.data_model["metadata.headerText"]}</td>
                              </tr>
                              <tr >
                                 <td align="center">${.data_model["merchant.name"]}</td>
                              </tr>
                              <tr >
                                 <td align="center">${.data_model["merchant.URL"]}</td>
                              </tr>
                           </table>
                        </center>
                        <!-- Transaction Section -->
                        <table width="300px" cellpadding="0" cellspacing="0">
						   <tr>
                                  <td colspan="2"><br/><br/></td>
                           </tr>
                           <#if .data_model["transaction.createdDateTime"] != "FALSE">
                           <tr >
							  <#import "macro_utils.ftl" as e/>
							  <#assign dateTime = .data_model["transaction.createdDateTime"]>
                              <td align="left"><b>Date</td>
                              <td align="right"><b>${e.dateTime_fr_cb_ecom(dateTime)}</td>
                           </tr>
                           </#if>
                           <tr >
                              <td align="left"><br>N° de Contrat</td>
                              <td align="right"><br>${.data_model["merchant.merchantId"]}</td>
                           </tr>
                           <tr >
							  <#import "macro_utils.ftl" as e/>
							  <#assign card = .data_model["transaction.maskedCardNumber"]>
                              <td align="left">N° de Carte</td>
                              <td align="right">${e.maskCardNo_fr_cb_ecom(card)}</td>
                           </tr>
                           <tr >
                              <td align="left">N° de Rang</td>
                              <td align="right">${.data_model["merchant.poiId"]}</td>
                           </tr>
                            <tr >
                               <td align="left">N° de transaction</td>
                               <td align="right">${.data_model["transaction.initiatorTraceId"]}</td>
                            </tr>
                            <tr >
                           <#if .data_model["transaction.transactionType"] == "CARD_VERIFICATION">
                           	<#if .data_model["transaction.responseCode"] == "0000">
                           	<tr >
                           	   <td align="left"><b>DEMANDE DE RENSEIGNEMENT</td>
                           	   <td align="right"><b>ACCORD</td>
                           	</tr>
                           	<#else>
                           	<tr >
                           	   <td align="left"><b>DEMANDE DE RENSEIGNEMENT</td>
                           	   <td align="right"><b>REFUS</td>
                           	</tr>
                           	</#if>
                           <#else>
                           	<#if .data_model["transaction.responseCode"] == "0000">
                           		<tr >
                           		   <#if .data_model["transaction.transactionType"] == "SALE">
                           			   <td align="left"><b>TRANSACTION DE PAIEMENT</td>
                           			   <#if .data_model["transaction.threeDSecure"] == "TRUE">
                           				<td align="right">VADS DEBIT @</td>
                           			   <#else>
                           				<td align="right">VAD DEBIT @</td>
                           			   </#if>
                           		   </#if>
                           		   <#if .data_model["transaction.transactionType"] == "VOID">
                           			   <td align="left"><b>TRANSACTION D’ANNULATION</td>
                           			   <#if .data_model["transaction.threeDSecure"] == "TRUE">
                           				<td align="right">VADS @</td>
                           			   <#else>
                           				<td align="right">VAD @</td>
                           			   </#if>
                           		   </#if>
                           		   <#if .data_model["transaction.transactionType"] == "REFUND">
                           			   <td align="left"><b>TRANSACTION DE CREDIT</td>
                           			   <#if .data_model["transaction.threeDSecure"] == "TRUE">
                           				<td align="right">VADS @</td>
                           			   <#else>
                           				<td align="right">VAD @</td>
                           			   </#if>
                           		   </#if>
                           		</tr>
                           	<#else>
                           		<tr >
                           		   <td align="left"><b>ABANDON</td>
                           		   <#if .data_model["transaction.threeDSecure"] == "TRUE">
                           			   <#if .data_model["transaction.transactionType"] == "SALE">
                           				<td align="right">VADS DEBIT</td>
                           			   </#if>
                           			   <#if (.data_model["transaction.transactionType"] == "VOID") || (.data_model["transaction.transactionType"] == "REFUND") >
                           				<td align="right">VADS</td>
                           			   </#if>
                           		   <#else>
                           			   <#if .data_model["transaction.transactionType"] == "SALE">
                           				<td align="right">VAD DEBIT</td>
                           			   </#if>
                           			   <#if (.data_model["transaction.transactionType"] == "VOID") || (.data_model["transaction.transactionType"] == "REFUND")>
                           				<td align="right">VAD</td>
                           			   </#if>
                           		   </#if>
                           		</tr>
                           	</#if>
                           </#if>
                           <tr >
                              <td align="left">N° AUTO</td>
                              <td align="right">${.data_model["transaction.authorisationCode"]}</td>
                           </tr>
                           <tr>
                              <td align="left"><br><b>MONTANT</b></td>
                              <td align="right"><br><b>${.data_model["transaction.amount"]} ${.data_model["transaction.currencyCode"]}</b></td>
                           </tr>
                           <tr>
                              <td align="left"><br>N° de Référence<br></td>
                              <td align="right"><br>${.data_model["transaction.merchantReference"]}</td>
                           </tr>
                           <tr>
                              <td colspan="2"><br/><br/></td>
                           </tr>
                        </table>
                        <!-- Advertisement Section -->
                        <center>
                           <table width="300px" cellpadding="0" cellspacing="0">
                              <tr >
                                 <td align="center"><b>TICKET CLIENT A CONSERVER</td>
                              </tr>
                              <tr >
                                 <td align="center">${.data_model["metadata.footerText"]}</td>
                              </tr>
                           </table>
                        </center>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
         </table>
      </div>
   </body>
</html>