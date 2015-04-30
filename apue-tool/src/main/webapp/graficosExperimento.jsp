<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<ul class="navIntraTool actionToolBar">
	<li><span class=""><a href="#" title="Novo Gr�fico" onclick="location = 'addGrafico/${idExperimento}'">Novo Gr�fico</a></span></li>
</ul>
<h3>Gr�ficos</h3>
<table id="listaGraficos" class="listHier lines" cellspacing="0">
	<tr class="listHier lines">
		<th>Nome do Gr�fico</th>
		<th>Eixo X</th>
		<th>Eixo Y</th>
		<th></th>
	</tr>
	<c:forEach var="grafico" items="${graficos}">
		<tr>
			<td><c:out value="${grafico.nome}" /></td>
			<td><c:out value="${grafico.idMetadadoX.chave}" /></td>
			<td><c:out value="${grafico.idMetadadoY.chave}" /></td>
			<td class="itemAction">
				<a href="editar/${grafico.idGrafico}">Editar</a> | 
				<a href="remover/${grafico.idGrafico}">Remover</a>
			</td>
		</tr>
	</c:forEach>
</table>
<div class="act">
	<input type="button" class="active" value="Continuar" onclick="location = '../index.htm'"></input>
	<input type="button" class="active" value="Voltar" onclick="location = '../dadosExperimento/${idExperimento}'"></input>
  	<input type="submit" id="cancelar" class="active" value="Cancelar" onclick="location = '../index.htm'"></input>
</div>
<jsp:directive.include file="/templates/footer.jsp" />