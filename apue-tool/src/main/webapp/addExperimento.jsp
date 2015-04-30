<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<form:form method="post" action="${action}" modelAttribute="experimento">
	<h3>Cadastrar Experimento</h3>
	<br>
	<table summary="Cadastrar um experimento" class="itemSummary" border="0">
  	<tr>
 		<th><form:label path="nome" >Nome do Experimento:</form:label></th>
   		<td><form:input path="nome" ></form:input></td>
  		</tr>
	<tr>
 		<th><form:label path="descricao">Descrição</form:label></th>
   		<td><form:textarea path="descricao" rows="8" cols="25"></form:textarea></td>
  		</tr>
  		<tr>
  			<th><form:label path="urlStreaming">URL da WebCam</form:label></th>
   		<td><form:input path="urlStreaming" size="50"></form:input></td>
  		</tr>
	<tr>
		<th><form:label path="porta">Porta USB</form:label></th>
   		<td><form:select path="porta" items="${portasUsb}"></form:select></td>
  		</tr>
	</table>
	
	<form:input type="hidden" path="idExperimento"></form:input>
	<form:input type="hidden" path="status"></form:input>
<div class="act">
	<input type="submit" value="Salvar e Continuar"></input>
	<input type="button" id="voltar" class="active" value="Voltar" onclick="location= '../experimentos'"></input>
</div>
</form:form>



<jsp:directive.include file="/templates/footer.jsp" />