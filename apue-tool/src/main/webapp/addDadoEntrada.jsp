<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<form:form method="post" action="${action}" modelAttribute="metadadoEntrada">
<!--metadado-->
	<p class="shorttext required">
		<span class="reqStar">*</span>  	
	  	<label for="chave">Chave</label>
	  	<form:input id="chave" path="metadado.chave"></form:input>
  	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="rotulo">Rotulo</label>
   		<form:input id="rotulo" path="metadado.rotulo"></form:input>	 	
	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="descricao">Descrição</label>
   		<form:input id="descricao" path="metadado.descricao"></form:input> 	
	</p>
   	<p class="shorttext required">
		<span class="reqStar">*</span>
	 	<label for="unidade">Unidade</label>
    	<form:input id="unidade" path="metadado.unidade" ></form:input>
	</p>
  			
   	<p class="shorttext required">
		<span class="reqStar">*</span>
	 	
	</p>
<!--metadadoEntrada-->
   	<p class="radiobutton required">
		<span class="reqStar">*</span>
 		<label for="visivel">Visibilidade:</label>
   		<form:radiobutton id="visivel" path="visivel" value="t"></form:radiobutton>
   		<label for="visivel">Visível</label>
   		<form:radiobutton id="invisivel" path="visivel" value="f"></form:radiobutton>
		<label for="invisivel">Invisível</label>
	</p>
	<p class="shorttext">
		<label for="valorPadrao">Valor Padrão</label>
		<form:input id="valorPadrao" path="valorPadrao"></form:input> 	
	</p>
  	<p class="shorttext required">
		<span class="reqStar">*</span>
	 	<label for="tipoEntrada">Tipo do elemento:</label>
    	<form:select id="tipoEntrada" path="tipoEntrada" items="${tipo}" itemLabel="label" itemValue="value"></form:select>
	</p>
	
<div class="act">
	<input type="submit" class="active" value="Salvar Dado"></input>
	<input type="button" class="active" value="Voltar" onclick="location = '../../dadosExperimento/${idExperimento}'"></input>
</div>
<!--metadadoEntrada-->

	<form:input type="hidden" path="metadado.idExperimento.idExperimento" ></form:input>
	<form:input type="hidden" path="metadado.idMetadado" ></form:input>

</form:form>

 <div id="opcoesDiv">
	<table class="listHier lines" cellspacing="0">
	<tr>
	  	<th>Opção</th>
       	<th>Valor</th>
       	<th></th>
    </tr>	
		<c:forEach var="opcaoMetadado" items="${opcoesMetadado}">
           <tr>
       		   <td><c:out value="${opcaoMetadado.rotulo}"/></td>
               <td><c:out value="${opcaoMetadado.valor}"/></td>
               <td class="itemAction">
               	<a href="../../opcaoDadoEntrada/editar/${opcaoMetadado.idOpcao}">Editar</a> 
               	| <a href="../../opcaoDadoEntrada/remover/${opcaoMetadado.idOpcao}">Remover</a>
               <td>
           </tr>
       </c:forEach>
	</table>	
	<div class="act">
		<input type="button" <c:if test="${action eq 'criar'}">disabled="true"</c:if> class="active" value="Adicionar Opção" onclick="location = '../../opcaoDadoEntrada/criar/${metadadoEntrada.metadado.idMetadado}'"></input>
	</div>
  </div> 

<script>

var entradaComOpcao = [3, 4, 6];
$(function() {
	$("#tipoEntrada").change();
});

$("#tipoEntrada").change(function() {
	var tipoEntrada = parseInt($("#tipoEntrada").val());
	if(entradaComOpcao.indexOf(tipoEntrada) < 0) {
		$("#opcoesDiv").hide("fast");
	} else { 
		$("#opcoesDiv").show("fast");
	}
});
</script>

<jsp:directive.include file="/templates/footer.jsp" />