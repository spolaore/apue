<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

<!-- menu -->
<c:if test="${isLabAdmin}">
	<ul class="navIntraTool actionToolBar">
		<li class="firstToolBarItem"><span><a href="#" title="Agendamento" onclick="location = 'agendamentos'">Agendamento</a></span></li>
		<li><span><a href="#" title="Admin Experimento" onclick="location = 'experimentos' ">Admin Experimento</a></span></li>
	</ul>
</c:if>
<!-- menu -->

<!-- conteudo -->

<c:choose>
	<c:when test="${empty sessaoGrupo}">
		<h3>Não há sessão agendada para o seu grupo no momento</h3>
	</c:when>
	<c:when test="${sessaoIniciada == false}">
		<h3>Aguarde, Próxima Sessão: <fmt:formatDate type="both" dateStyle="SHORT" value="${sessaoGrupo.idSessao.inicio.time}"/></h3>
	</c:when>
	<c:when test="${sessaoIniciada}">
		<jsp:directive.include file="/templates/apue.jsp" />
	</c:when>
	<c:otherwise>undefined</c:otherwise>
</c:choose>
<!-- conteudo -->

<jsp:directive.include file="/templates/footer.jsp" />

<div title="Descricao do Experimento" id="descricaoExperimento" style="width: 200px"><span>${experimento.descricao}</span></div>
<div class="modal"></div>