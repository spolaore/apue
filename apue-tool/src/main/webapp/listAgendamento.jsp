<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<ul class="navIntraTool actionToolBar">
	<li class="firstToolBarItem">
		<span class=""><a href="#" title="Agendar" onclick="location = 'agendamentos/form'">Agendar</a></span>
	</li>
</ul>

<table class="listHier lines" cellspacing="0">
		<tr>
			<th>Nome do Experimento</th>
			<th>Usuario Criador</th>
			<th>Inicio</th>
			<th>Fim</th>
			<th></th>
		</tr>
		<c:forEach var="sessao" items="${sessoes}">
			<tr>
				<td><c:out value="${sessao.idExperimento.nome}" /></td>
				<td><c:out value="${sessao.usuarioCriador}" /></td>
				<td><fmt:formatDate type="both" dateStyle="SHORT" value="${sessao.inicio.time}"/></td>
				<td><fmt:formatDate type="both" dateStyle="SHORT" value="${sessao.fim.time}"/></td>
				<td class="itemAction">
					<a href="agendamentos/${sessao.idSessao}">Editar</a>|<a href="agendamentos/remover/${sessao.idSessao}">Remover</a>
			    </td>
			</tr>
		</c:forEach>
</table>
<div class="act">
	<input type="submit" id="voltar" class="active" value="Voltar" onclick="location = 'index.htm'"></input>
</div>
<jsp:directive.include file="/templates/footer.jsp" />

