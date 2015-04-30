<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />

	<h3>Agendar Experimento</h3>
	<br>
	<form:form action="${action}" modelAttribute="sessao">
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="experimento">Experimento:</label>
		<form:select id="experimento" path="idExperimento" items="${experimentos}" itemValue="idExperimento"></form:select>
	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="grupo">Grupo:</label>
		<form:select multiple="true" path="gruposId" items="${gruposId}"></form:select>
	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="dataIni">Início (dd/mm/aaaa hh:mm:ss)</label>
		<form:input id="dataIni" path="inicio" size="23"></form:input>
	</p>
	<p class="shorttext required">
		<span class="reqStar">*</span>
		<label for="dataFim">Fim: (dd/mm/aaaa hh:mm:ss)</label>
		<form:input id="dataFim" path="fim" size="23"></form:input>
	</p>
	<form:input type="hidden" path="idSessao"></form:input>
	<form:input type="hidden" path="usuarioCriador"></form:input>
	<form:input type="hidden" path="usuarioControle"></form:input>
	<div class="act">
		<input type="submit" value="Salvar" /> 
		<input type="button" value="Voltar" class="active" onclick="location = '../agendamentos'" />
	</div>
	</form:form>
<jsp:directive.include file="/templates/footer.jsp" />