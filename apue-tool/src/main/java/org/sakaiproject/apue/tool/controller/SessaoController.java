package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.sakaiproject.apue.logic.SakaiProxy;
import org.sakaiproject.apue.tool.Experimento;
import org.sakaiproject.apue.tool.Sessao;
import org.sakaiproject.apue.tool.SessaoGrupo;
import org.sakaiproject.apue.tool.SessaoGrupoPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class SessaoController {
	
	@Setter
	@Getter
	@Autowired
	private SakaiProxy sakaiProxy = null;
	
	@RequestMapping(value = "/agendamentos", method = RequestMethod.GET)
	public String listAgendamento(Model model) throws Exception {
		List<Sessao> sessoes = Sessao.findAllSessaos();
		model.addAttribute("sessoes", sessoes);
		return "listAgendamento";
	}
	
	@RequestMapping(value = "/agendamentos/form", method = RequestMethod.GET)
	public String createForm( Model model) throws Exception {
		model.addAttribute("action", "create");
		Sessao sessao = new Sessao();
		
		List<Experimento> experimentos = Experimento.findAllExperimentoes();
		model.addAttribute("experimentos", experimentos);
		model.addAttribute("sessao", sessao);
		model.addAttribute("gruposId", sakaiProxy.getAllGroups());
		return "addAgendar";
	}
	
	@RequestMapping(value = "/agendamentos/create", method = RequestMethod.POST, produces = "text/html")
    public String create(Sessao sessao, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
		//TODO VALIDAR COLISAO DE AGENDAMENTO
		sessao.setUsuarioCriador(sakaiProxy.getCurrentUserId());
		sessao.persist();
		persistSessaoGrupos(sessao);
        return "redirect:/agendamentos";
    }

	private void persistSessaoGrupos(Sessao sessao) {
		for(String grupoId : sessao.getGruposId()) {
			SessaoGrupo sessaoGrupo = buildSessaoGrupo(sessao, grupoId);
			sessaoGrupo.persist();
		}
	}

	private SessaoGrupo buildSessaoGrupo(Sessao sessao, String grupoId) {
		SessaoGrupo sessaoGrupo = new SessaoGrupo();
		sessaoGrupo.setIdSessao(sessao);
		SessaoGrupoPK sessaoGrupoPK = new SessaoGrupoPK(sessao.getIdSessao(), grupoId);
		sessaoGrupo.setId(sessaoGrupoPK);
		return sessaoGrupo;
	}
	
	@RequestMapping(value = "/agendamentos/remover/{idSessao}", produces = "text/html")
    public String delete(@PathVariable("idSessao") Integer idSessao, Model uiModel) {
        Sessao sessao = Sessao.findSessao(idSessao);
        sessao.remove();
        uiModel.asMap().clear();
        return "redirect:/agendamentos";
    }
	
	@RequestMapping(value = "/agendamentos/{idSessao}", produces = "text/html")
    public String show(@PathVariable("idSessao") Integer idSessao, Model uiModel) {
		uiModel.addAttribute("action", "modificar");
		
		Sessao sessao = Sessao.findSessao(idSessao);
		
		sessao.setGruposId(new ArrayList<String>());
		for(SessaoGrupo sessaoGrupo : sessao.getSessaoGrupoes()) {
			sessao.getGruposId().add(sessaoGrupo.getId().getIdGrupo());
		}
		
		uiModel.addAttribute("sessao", sessao);
        List<Experimento> experimentos = Experimento.findAllExperimentoes();
		uiModel.addAttribute("experimentos", experimentos);
		uiModel.addAttribute("gruposId", sakaiProxy.getAllGroups());
        return "addAgendar";
    }
	
	@Transactional
	@RequestMapping(value = "/sessao/{idSessao}", produces="application/json")
    public @ResponseBody String getSessao(@PathVariable("idSessao") Integer idSessao) throws JsonProcessingException {
		Sessao sessao = Sessao.findSessao(idSessao);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(sessao);
    }
	
	@Transactional
	@RequestMapping(value = "/sessao/{idSessao}/controle")
	@ResponseStatus(value = HttpStatus.OK)
    public void delegarControle(@PathVariable("idSessao") Integer idSessao, String usuarioControle) {
		Sessao sessao = Sessao.findSessao(idSessao);
		sessao.setUsuarioControle(usuarioControle);
		sessao.merge();
    }
	
	@RequestMapping(value = "/agendamentos/modificar",produces = "text/html")
    public String modificar(@ModelAttribute Sessao sessao, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        uiModel.asMap().clear();
        
        Set<SessaoGrupo> sessaoGrupos = new HashSet<SessaoGrupo>();
        for(String grupoId : sessao.getGruposId()) {
			SessaoGrupo sessaoGrupo = buildSessaoGrupo(sessao, grupoId);
			sessaoGrupos.add(sessaoGrupo);
		}
        sessao.setSessaoGrupoes(sessaoGrupos);
        
        sessao.merge();
        return "redirect:/agendamentos";
    }
	
	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
	
	

