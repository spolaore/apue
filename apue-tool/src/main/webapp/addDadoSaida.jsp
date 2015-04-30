<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<form:form  action="${action}" modelAttribute="metadadoSaida">
  <table summary="Cadastrar um dado de entrada do experimento" class="itemSummary" border="0" >
  	<tr>
		<th><form:label path="metadado.chave">Chave</</form:label></th>
    	<td><form:input path="metadado.chave"></form:input></td>
	</tr>
   	<tr>
  		<th><form:label path="metadado.rotulo">Rotulo</form:label></th>
   		<td><form:input path="metadado.rotulo"></form:input></td>
	</tr>
   	<tr>
   		<th><form:label path="metadado.descricao">Descrição</form:label></th>
    	<td><form:input path="metadado.descricao"></form:input></td>
   	</tr>
   	<tr>
		<th><form:label path="metadado.unidade">Unidade</form:label></th>
		<td><form:input path="metadado.unidade"></form:input><br></td>
   	</tr>
   	<tr>
		<form:input type="hidden" path="metadado.idMetadado" ></form:input>
		<form:input type="hidden" path="metadado.idExperimento.idExperimento" ></form:input>
	</tr>
	<tr>
	<td>
		<div class="act">
		<input type="submit" value="Salvar"></input>
		<input type="button" id="voltar" class="active" value="Voltar" onclick="location = '../${metadadoSaida.metadado.idExperimento.idExperimento}'"></input>
		</div>
	</td>	
   	</tr>
  </table>
</form:form>
<jsp:directive.include file="/templates/footer.jsp" />