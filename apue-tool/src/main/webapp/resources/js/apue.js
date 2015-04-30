fezPrimeiroUpdate = false;
temControle = false;
isLabAdmin = false;
ultimoUsuarioControle = "";

$(function() {
	$("body").addClass("loading");
	initApue();
});

function updateSession() {
	var idSessao = $("#idSessao").val();
	$.get("dados/ultimos/" + idSessao, function(data) {
		var dados = data.dados;
		dados.forEach(function(dado) {
			if(!temControle || dado.tipoDado === "S" || !fezPrimeiroUpdate) {
				$("#"+dado.idMetadadoJson).val(dado.valor);
			}
		});
		atualizarControleUsuario(data.usuarioControle, false);
		fezPrimeiroUpdate = true;
	});
}

function initDelegarControls(usuarioControle) {
	$("#delegarButton").click(delegarUsuario);
	usuarioLogado = $("#usuarioLogado").val();
	atualizarControleUsuario(usuarioControle, true);
}

function atualizarControleUsuario(usuarioControle, init) {
	var usuarioSelect = $("#usuarioControle");

	if(init == false && ultimoUsuarioControle === usuarioControle) {
		return;
	}

	var info = $("#info");
	info.html("O usuario " + usuarioControle + " esta com controle do experimento!");
	info.delay(100).fadeTo(600, 1).delay(3000).fadeTo(600, 0);

	usuarioSelect.val(usuarioControle);
	ultimoUsuarioControle = usuarioControle;

	if(usuarioControle !== usuarioLogado) {
		removerControleUsuario();
	} else {
		habilitarControleUsuario();
	}
}

function habilitarControleUsuario() {
	$("#delegarButton").removeAttr("disabled");
	$("#usuarioControle").removeAttr("disabled");
	$("#enviar").removeAttr("disabled");
	$("#dadosEntradaDiv :input").removeAttr("disabled");
	temControle = true;
}

function removerControleUsuario() {
	if(isLabAdmin == false) {
		$("#delegarButton").attr("disabled", true);
		$("#usuarioControle").attr("disabled", true);
	}
	$("#enviar").attr("disabled", true);
	$("#dadosEntradaDiv :input").attr("disabled", true);
	temControle = false;
}

function initApue() {
	$("#enviar").click(enviarDados);
	$("#descricaoExperimento").dialog({
		autoOpen: false, minWidth: 300, minHeight: 300});
	$("#infoExperimento").click(function() {
		$("#descricaoExperimento").dialog("open");
	});
	var idSessao = $("#idSessao").val();	
	isLabAdmin = $("#labAdmin").val() === "true";
	$.get("sessao/" + idSessao, function(data) {
		var sessao = data;
		initDelegarControls(sessao.usuarioControle);
		initStreaming(sessao.idExperimento.urlStreaming);
		initCharts(sessao.idExperimento.graficoes);
		$( "#graficosDiv" ).tabs({
			activate: function( event, ui ) {
				ui.newPanel.highcharts().reflow();
				ui.newPanel.highcharts().redraw();
			}
		});
		$("body").removeClass("loading");
	});

	setInterval(updateSession, 2000);
}

function enviarDados() {
	var dados = [];
	var form = $("#dadosEntradaForm").serializeArray();
	var idSessao = parseInt($("#idSessao").val());
	form.forEach(function(input) {
		var dado = {
			idMetadado: { idMetadado: parseInt(input.name) },
			idSessao: { idSessao: idSessao },
			valor: input.value
		};
		dados.push(dado);
	});
	$.ajax({ 
	    url: "dados/enviar", 
	    type: 'POST',
	    data: JSON.stringify(dados), 
	    contentType: 'application/json'
	});
}

function delegarUsuario() {
	var idSessao = $("#idSessao").val();
	var usuarioControle = $("#usuarioControle").val();
	$.get("sessao/" + idSessao + "/controle", {usuarioControle: usuarioControle}, function() {
		removerControleUsuario();
	});
}

function initStreaming(url) {
		var canvas = document.getElementById('videoCanvas');
		var ctx = canvas.getContext('2d');
		ctx.font = "14px Helvetica Neue";
		ctx.fillStyle = '#444';
		ctx.fillText('Carregando...', canvas.width/2-30, canvas.height/2);
		
		var client = new WebSocket(url);
		var player = new jsmpeg(client, {canvas:canvas});
}

function extractDadosValues(dados) {
	var data = [];
	dados.forEach(function(dado) {
		var y = parseFloat(dado.valor);
		data.push(y);
	});
	return data;
}

function initCharts(graficos) {
	graficos.forEach(function(grafico) {
		Highcharts.setOptions({
			global : {
				useUTC : false
			}
		});
	
		var dados = grafico.idMetadadoY.dadoes;
		var data = extractDadosValues(dados);
		var ultimoDado = dados.length === 0 ? 0 : dados[dados.length-1];
		// Create the chart
		$('#grafico' + grafico.idGrafico).highcharts(
				'StockChart',
				{
					chart : {
							events : {
								load : function() {
									var series = this.series[0];
									//workaround, força redraw logo que carregar, para nao causar bug com jquery tabs
									series.addPoint(0, true, false);
									setInterval(function() {
										$.get("dados/"+grafico.idMetadadoY.idMetadado, {idUltimoDado: ultimoDado.idDado}, function(data) {
											var values = extractDadosValues(data);
											if(data.length === 0) {
												series.addPoint(parseFloat(ultimoDado.valor), false, false);
											} else {
												ultimoDado = data[data.length-1];
												values.forEach(function(point) {
													series.addPoint(point, false, false);
												});
											}
										});
										if(series.chart.container.parentElement.style.display !== "none") {
											series.chart.redraw();
										}
									}, 2000);
								}
							}
						},
		
					rangeSelector : {
						buttons : [ {
							count : 1,
							type : 'minute',
							text : '1M'
						}, {
							count : 5,
							type : 'minute',
							text : '5M'
						}, {
							type : 'all',
							text : 'All'
						} ],
						inputEnabled : false,
						selected : 0
					},
		
					title : {
						text : grafico.nome
					},
		
					exporting : {
						enabled : true
					},
		
					series : [ {
						id : "apueSeries",
						name : grafico.idMetadadoY.rotulo,
						data : data
						} ]
				});
		});
}