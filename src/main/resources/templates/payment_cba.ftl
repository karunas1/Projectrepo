<!DOCTYPE html>
<html>
   <head>
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
                                      <td align="center">${.data_model["transaction.acquirerName"]}</td>
                                   </tr>
                                   <tr >
                                      <td align="center">${.data_model["merchant.name"]}</td>
                                   </tr>
                                   <tr >
                                      <td align="center">${.data_model["merchant.address"]}</td>
                                   </tr>
                                   <tr >
                                      <td align="center">${.data_model["merchant.postCode"]}</td>
                                   </tr>
                                   <tr >
                                      <td align="center">${.data_model["merchant.city"]}</td>
                                   </tr>
                                   <#if .data_model["merchant.phoneNumbers"] != "">
                                       <tr >
                                          <td align="center">${.data_model["merchant.phoneNumbers"]}</td>
                                       </tr>
                                   </#if>
                                   <#if .data_model["merchant.country"] != "">
                                       <tr >
                                          <td align="center">${.data_model["merchant.country"]}</td>
                                       </tr>
                                   </#if>
                                </table>
                            </center>
                            <!-- Transaction Section -->
                            <table width="300px" cellpadding="0" cellspacing="0">
                               <tr >
                                  <td>${label_terminal_id}</td>
                                  <td align="right">${.data_model["merchant.poiId"]}</td>
                               </tr>
                               <tr >
                                  <td>${label_merchant_id}</td>
                                  <td align="right">${.data_model["merchant.merchantId"]}</td>
                               </tr>
                               <tr >
                                  <td>${label_stan}&nbsp;${.data_model["transaction.initiatorTraceId"]}</td>
                                  <td align="right">${label_auth}&nbsp;${.data_model["transaction.authorisationCode"]}</td>
                               </tr>
                               <tr >
                                  <td>${label_card}&nbsp;${.data_model["transaction.maskedCardNumber"]}</td>
                                  <td align="right">${.data_model["transaction.accountType"]}&nbsp;${.data_model["transaction.entryMode"]}</td>
                               </tr>
                               <tr >
                                  <td>EFTPOS</td>
                                  <td align="right">CSN&nbsp;01</td>
                               </tr>
                               <tr>
                                  <td colspan="2"><br/><br/></td>
                               </tr>
                                  <!-- Blank Line -->
                               <tr >
                                  <td><b>${label_datetime}</b></td>
                                  <td align="right"><b>${.data_model["transaction.transmittedDateTime"]}</b></td>
                               </tr>
                               <tr >
                                  <td>${.data_model["transaction.fundingSource"]}</td>
                                   <#if .data_model["metadata.psnAvailable"]>
                                       <td align="right">PSN &nbsp;${.data_model["transaction.icc.sequenceNumber"]}</td>
                                   </#if>
                               </tr>
                               <tr >
                                  <td>${label_aid}</td>
                                  <td align="right">${.data_model["transaction.dedicatedFileName"]}</td>
                               </tr>
                               <tr>
                                  <td>${label_tvr}&nbsp;${.data_model["transaction.icc.terminalVerificationResults"]}</td>
                                  <td align="right">${label_atc}&nbsp;${.data_model["transaction.icc.expectedApplicationTransactionCounter"]}</td>
                               </tr>
                               <tr >
                                  <td>${.data_model["metadata.cryptoType"]}</td>
                                  <td align="right">${.data_model["transaction.icc.cryptogramInformationData"]}</td>
                               </tr>
                               <tr>
                                  <td colspan="2"><br/><br/></td>
                               </tr>
                               <!-- Blank Line -->
                               <#if .data_model["transaction.amount"] != "-1.0">
	                               <tr>
	                                  <td><b>${label_txn}</b></td>
	                                  <td align="right"><b>${.data_model["transaction.amount"]}</b></td>
	                               </tr>
	                           </#if>
                               <#if .data_model["transaction.cashbackAmount"] != "-1.0">
	                               <tr>
	                                  <td><b>${label_cashout}</b></td>
	                                  <td align="right"><b>${.data_model["transaction.cashbackAmount"]}</b></td>
	                               </tr>
	                           </#if>
	                           <#if .data_model["transaction.gratuityAmount"] != "-1.0">
	                               <tr>
	                                  <td><b>${label_tip}</b></td>
	                                  <td align="right"><b>${.data_model["transaction.gratuityAmount"]}</b></td>
	                               </tr>
	                           </#if>
	                           <#if .data_model["transaction.feeAmount"] != "-1.0">
	                               <tr>
	                                  <td><b>${label_surcharge}</b></td>
	                                  <td align="right"><b>${.data_model["transaction.feeAmount"]}</b></td>
	                               </tr>
	                           </#if>
	                           <#if .data_model["transaction.totalAmount"] != "-1.0">
	                               <tr>
	                                  <td><b>${label_total}</b></td>
	                                  <td align="right"><b>${.data_model["transaction.totalAmount"]}</b></td>
	                               </tr>
	                           </#if>
                               <tr>
                                  <td colspan="2"><br/><br/></td>
                               </tr>
                                  <!-- Blank Line -->
                               <tr>
                                  <td><b>${.data_model["metadata.txnApprovalText"]}</b></td>
                                  <td align="right"><b>${.data_model["transaction.responseCode"]}</b></td>
                               </tr>
                               <tr >
                                  <td align="center" colspan="2">${.data_model["transaction.errorMessage"]}</td>
                               </tr>
                               <tr>
                                  <td colspan="2"><br/><br/></td>
                               </tr>
                                  <!-- Blank Line -->
                               <tr>
                                  <td colspan="2">
                                     <hr  class="striped">
                                  </td>
                               </tr>
                               <tr>
                                  <td colspan="2" align="center">${label_customer_copy}</td>
                               </tr>
                               <tr>
                                  <td colspan="2">
                                     <hr  class="striped">
                                  </td>
                               </tr>
                               <tr>
                                  <td colspan="2" align="center">${label_duplicate_copy}</td>
                               </tr>
                               <tr>
                                  <td colspan="2">
                                     <hr  class="striped">
                                  </td>
                               </tr>
                               <tr>
                                  <td colspan="2" align="center">Thank You!!!!</td>
                               </tr>
                            </table>

                         </td>
                      </tr>
                   </table>
                </td>
             </tr>
          </table>
       </div>
   </body>
</html>