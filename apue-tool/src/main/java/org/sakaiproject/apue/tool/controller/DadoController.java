package org.sakaiproject.apue.tool.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.sakaiproject.apue.tool.Dado;
import org.sakaiproject.apue.tool.Metadado;
import org.sakaiproject.apue.tool.Sessao;
import org.sakaiproject.apue.tool.UpdateSessao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/dados/")
@Controller
public class DadoController {
	
	@Autowired
	ArduinoController arduino;
	
	@PostConstruct
	public void initSerial() {
		arduino.initialize();
	}
	
	@RequestMapping(value="{idMetadado}", method = RequestMethod.GET)
	public @ResponseBody List<Dado> getNovosDados(@PathVariable Integer idMetadado, Integer idUltimoDado) {
		return Dado.findNovosDados(idMetadado, idUltimoDado);
	}
	
	@RequestMapping(value="enviar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarDados(@RequestBody ArrayList<Dado> dados) {
		for(Dado dado : dados) {
			dado.getIdMetadado().setChave(Metadado.findMetadadoChave(dado.getIdMetadado().getIdMetadado()));
			dado.setTipoDado(Dado.TipoDado.ENTRADA.getValue());
			dado.persist();
		}
		arduino.sendDados(dados);
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="ultimos/{idSessao}", method = RequestMethod.GET, produces="application/json")
	public String getUltimosDados(@PathVariable Integer idSessao) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<Dado> dados = Dado.findUltimosDados(idSessao);
		String usuarioControle = Sessao.findUsuarioControle(idSessao);
		for (Dado dado : dados) {
			dado.setIdMetadadoJson(dado.getIdMetadado().getIdMetadado());
		}
		UpdateSessao update = new UpdateSessao(dados, usuarioControle);
		String returnString = mapper.writeValueAsString(update);
		return returnString;
	}
}
