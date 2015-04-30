<script src="resources/js/highstock.js"></script>
<script src="resources/js/exporting.js"></script>
<script src="resources/js/jsmpg.js"></script>
<script src="resources/js/apue.js"></script>

<h3>${experimento.nome}</h3><img id="infoExperimento" src="/library/skin/neo-default/images/help.gif" alt="Informações sobre o experimento ${experimento.nome}" border="0">
	<div id="info" class="information" style="opacity: 0;">&nbsp</div>
	<p class="shorttext">
		<label for="usuarioControle">Controlador</label>
		<select id="usuarioControle">
			<c:forEach var="participante" items="${participantes}">
				<c:choose>
					<c:when test="${participante == sessaoGrupo.idSessao.usuarioControle}">
		       			<option selected="selected" value="${participante}">${participante}</option>
		    		</c:when>
		    		<c:otherwise>
						<option value="${participante}">${participante}</option>
		    		</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<input id="delegarButton" type="button" value="Delegar"></input>
	</p>

	<div id="videoDiv">
		<h3>Apue Streaming</h3>
		<canvas id="videoCanvas" width="320" height="240">
			<p>
				Please use a browser that supports the Canvas Element, like
				<a href="http://www.google.com/chrome">Chrome</a>,
				<a href="http://www.mozilla.com/firefox/">Firefox</a>,
				<a href="http://www.apple.com/safari/">Safari</a> or Internet Explorer 10
			</p>
		</canvas>
	</div>
	
	<div id="graficosDiv">
		<ul>
			<c:forEach var="grafico" items="${experimento.graficoes}">
				<li><a href="#grafico${grafico.idGrafico}">${grafico.nome}</a></li>
			</c:forEach>
		</ul>
		<c:forEach var="grafico" items="${experimento.graficoes}">
			<div id="grafico${grafico.idGrafico}" class="grafico"></div>
		</c:forEach>
	</div>
	
	<div id="dadosEntradaDiv">
		<h3>Dados de Entrada</h3>
		<form id="dadosEntradaForm">
			<c:forEach var="metadadoEntrada" items="${metadadosEntrada}">
				<p class="shorttext">
			  		<label for="${metadadoEntrada.metadado.chave}">${metadadoEntrada.metadado.rotulo}</label>
			  		<c:choose>
					    <c:when test="${metadadoEntrada.tipoEntrada == 1}">
					       <input type="text" id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}"></input>
					    </c:when>
					    <c:when test="${metadadoEntrada.tipoEntrada == 2}">
					       <input type="range" min="0" max="100" id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}"></input>
					    </c:when>
					    <c:when test="${metadadoEntrada.tipoEntrada == 3}">
					    	<select id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}">
					    		<c:forEach var="opcao" items="${metadadoEntrada.opcaoMetadadoEntradas}">
						     		<option value="${opcao.valor}">${opcao.rotulo}</option>
					    		</c:forEach>
					    	</select>
					    </c:when>
					    <c:when test="${metadadoEntrada.tipoEntrada == 4}">
					    	<c:forEach var="opcao" items="${metadadoEntrada.opcaoMetadadoEntradas}">
						     		<input type="checkbox" id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}" value="${opcao.valor}">${opcao.rotulo}</input>
					    	</c:forEach>
					    </c:when>
					    <c:when test="${metadadoEntrada.tipoEntrada == 5}">
					       <input type="number" id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}"></input>
					    </c:when>
					    <c:when test="${metadadoEntrada.tipoEntrada == 6}">
					       <c:forEach var="opcao" items="${metadadoEntrada.opcaoMetadadoEntradas}">
						     		<input type="radio" id="${metadadoEntrada.metadado.idMetadado}" name="${metadadoEntrada.metadado.idMetadado}" value="${opcao.valor}">${opcao.rotulo}</input>
					    	</c:forEach>
					    </c:when>
					    <c:otherwise>
					        Tipo Entrada Inválido!
					    </c:otherwise>
					</c:choose>
			  	</p>
			</c:forEach>
		</form>
		<div class="act">
			<input id="enviar" type="button" value="Enviar Dados"></input>
		</div>
	</div>
	
	<div id="dadosSaidaDiv">
		<h3>Dados de Saída</h3>
		<c:forEach var="metadadoSaida" items="${metadadosSaida}">
			<p class="shorttext">
		  		<label for="${metadadoSaida.metadado.chave}">${metadadoSaida.metadado.rotulo}</label>
		  		<input id="${metadadoSaida.metadado.idMetadado}"></input>
		  		${metadadoSaida.metadado.unidade}
		  	</p>
		</c:forEach>
	</div>
	
	<input type="hidden" id="idSessao" value="${sessaoGrupo.idSessao.idSessao}"/>
	<input type="hidden" id="usuarioLogado" value="${usuarioLogado}"/>
	<input type="hidden" id="labAdmin" value="${isLabAdmin}"/>