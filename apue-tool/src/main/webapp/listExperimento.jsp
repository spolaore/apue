<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<ul class="navIntraTool actionToolBar">
	<li class="firstToolBarItem">
		<span class=""><a href="#" title="Cadastrar Experimento" onclick="location = 'experimentos/cadastrar'">Cadastrar Experimento</a></span>
	</li>
</ul>

<table id="listaExperimentos" class="listHier lines" cellspacing="0">
	<tr>
		<th>Nome</th>
		<th>Porta</th>
		<th>Descrição</th>
		<th>Status</th>
		<th>URL Streaming</th>
		<th></th>
	</tr>
	<c:forEach var="experimento" items="${experimentos}">
		<tr>
			<td><c:out value="${experimento.nome}" /></td>
			<td><c:out value="${experimento.porta}" /></td>
			<td><c:out value="${experimento.descricao}" /></td>
			<td><c:out value="${experimento.status}" /></td>
			<td><c:out value="${experimento.urlStreaming}" /></td>
			<td class="itemAction">
				<a href="experimentos/${experimento.idExperimento}">Editar</a>|<a href="experimentos/remover/${experimento.idExperimento}">Remover</a>
			</td>
		</tr>
	</c:forEach>
</table>
<div class="act">
	<input type="button" id="voltar" class="active" value="Voltar" onclick="location = 'index.htm'"></input>
</div>
<jsp:directive.include file="/templates/footer.jsp" />

