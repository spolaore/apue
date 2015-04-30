<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<h3>Dados de entrada do experimento:</h3>
<div class="act">
	<input type="submit" value="Adicionar novo" onclick="location = 'addDadoEntrada/${idExperimento}'"></input>
</div>
<table id="dadosEntrada" class="listHier lines" cellspacing="0">
   	<thead>
    	<tr class="listHier lines ">
       		<th>Chave</th>
       		<th>Descrição</th>
			<th>Rotulo</th>
			<th>Unidade</th>
			<th>Visibilidade</th>
			<th>Valor Padrão</th>
			<th></th>
		</tr>
		<c:forEach var="dadoEntrada" items="${listaMetadadoEntrada}">
		<tr>
			<td><c:out value="${dadoEntrada.metadado.chave}" /></td>
			<td><c:out value="${dadoEntrada.metadado.descricao}" /></td>
			<td><c:out value="${dadoEntrada.metadado.rotulo}" /></td>
			<td><c:out value="${dadoEntrada.metadado.unidade}" /></td>
			<td><c:out value="${dadoEntrada.visivel}" /></td>
			<td><c:out value="${dadoEntrada.valorPadrao}" /></td>
			<td class="itemAction">
				<a href="dadoEntrada/${dadoEntrada.metadado.idMetadado}">editar</a> | 
				<a href="dadoEntrada/remover/${dadoEntrada.metadado.idMetadado}">remover</a>
			</td>
		</tr>
  		</c:forEach>
	</thead>
</table>

<h3>Dados de saida do experimento:</h3>
<div class="act">
	<input type="submit"  value="Adicionar novo" onclick="location = 'addDadoSaida/${idExperimento}'"></input>
</div>
<table id="dadosSaida" class="listHier lines" cellspacing="0">
	<thead>
    	<tr class="listHier lines ">
	    	<th>Chave</th>
	        <th>Descrição</th>
			<th>Rotulo</th>
			<th>Unidade</th>
			<th></th>
      	</tr>
      		<c:forEach var="dadoSaida" items="${listaMetadadoSaida}">
			<tr>
				<td><c:out value="${dadoSaida.metadado.chave}" /></td>
				<td><c:out value="${dadoSaida.metadado.descricao}" /></td>
				<td><c:out value="${dadoSaida.metadado.rotulo}" /></td>
				<td><c:out value="${dadoSaida.metadado.unidade}" /></td>
				<td class="itemAction">
					<a href="dadoSaida/${dadoSaida.metadado.idMetadado}">Editar</a> | 
					<a href="dadoSaida/remover/${dadoSaida.metadado.idMetadado}">Remover</a>
				</td>
			</tr>
			</c:forEach>
	</thead>
</table>
<div class="act">
	<input type="button" class="active" value="Continuar" onclick="location = '../graficos/${idExperimento}'"></input>
  	<input type="button" class="active" value="Voltar" onclick="location = '../experimentos/${idExperimento}'"></input>
  	<input type="button" class="active" value="Cancelar" onclick="location = '../index.htm'"></input>
</div>
  
  
<jsp:directive.include file="/templates/footer.jsp" />
