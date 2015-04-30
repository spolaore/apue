<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<h3>Adicionar Gráfico</h3>
<br>
<form:form method="post" action="${action}" modelAttribute="grafico">
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="nome">Nome do Gráfico:</label>
		<form:input id="nome" path="nome"></form:input>
    </p>
  	<p class="shorttext required">
		<span class="reqStar">*</span>
	 	<label for="descricao">Descrição do Gráfico:</label>
		<form:input id="descricao" path="descricao"></form:input>
	</p>
  	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="idMetadadoX">Eixo X:</label>
		<form:select disabled="disabled" id="idMetadadoX" path="idMetadadoX" items="${metadados}" itemValue="idMetadado"></form:select>
    </p>
    <p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="idMetadadoY">Eixo Y:</label>
 		<form:select id="idMetadadoY" path="idMetadadoY" items="${metadados}" itemValue="idMetadado"></form:select>
    </p>
    <form:input type="hidden" path="idGrafico" ></form:input>
    <form:input type="hidden" path="idExperimento.idExperimento" ></form:input>
   	<div class="act">
   		<input type="submit" value="Salvar"></input>
   		<input type="button" class="active" value="Voltar" onclick="location = '../../graficos/${idExperimento}'"></input>
   	</div>
</form:form>
<jsp:directive.include file="/templates/footer.jsp" />