<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />
<form:form  method="post" action="../${action}" modelAttribute="opcaoMetadadoEntrada">
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="rotulo">Rótulo</label>
   		<form:input id="rotulo" path="rotulo"></form:input>	 	
	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>  	
	  	<label for="valor">Valor</label>
	  	<form:input id="valor" path="valor"></form:input>
  	</p>
  	<form:input type="hidden" path="idMetadado.idMetadado"></form:input>
  	<form:input type="hidden" path="idOpcao"></form:input>
<div class="act">
	<input type="submit" class="active" value="Salvar"></input>
	<input type="button" class="active" value="Voltar" onclick="location = '../../dadosExperimento/dadoEntrada/${opcaoMetadadoEntrada.idMetadado.idMetadado}'"></input>
</div>
</form:form>


<jsp:directive.include file="/templates/footer.jsp" />