package org.sakaiproject.apue.tool.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.sakaiproject.apue.logic.SakaiProxy;
import org.sakaiproject.apue.tool.Experimento;
import org.sakaiproject.apue.tool.MetadadoEntrada;
import org.sakaiproject.apue.tool.MetadadoSaida;
import org.sakaiproject.apue.tool.SessaoGrupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ApueController {
	
	@Setter
	@Getter
	@Autowired
	private SakaiProxy sakaiProxy = null;
	
	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
	public String index(Model model) throws Exception {
		boolean isLabAdmin = sakaiProxy.hasPermission(SakaiProxy.APUE_LAB_ADMIN);
		model.addAttribute("isLabAdmin", isLabAdmin);
		
		Set<String> gruposUsuario = sakaiProxy.getUserGroups();
		//Buscao sessoes nao terminadas para os grupos desse usuario, ordenadas por data
		List<SessaoGrupo> sessaoGrupos = SessaoGrupo.findSessaoAbertaByGrupos(gruposUsuario);
	
		//Se existir uma sessao aberta para o usuario
		if(sessaoGrupos.size() > 0) {
			SessaoGrupo sessaoGrupo = sessaoGrupos.get(0);
			
			if(sessaoGrupo.getIdSessao().getInicio().after(Calendar.getInstance())) {
				model.addAttribute("sessaoIniciada", Boolean.FALSE);
			} else {
				model.addAttribute("sessaoIniciada", Boolean.TRUE);
			}
			
			//Buscando grupos que fazem parte dessa sessao
			Set<String> gruposId = SessaoGrupo.findGruposByIdSessao(sessaoGrupo.getIdSessao().getIdSessao());
			
			//Buscando usuarios dos grupos dessa sessao
			Collection<String> participantes = sakaiProxy.getUsersInGroups(gruposId);
			
			//Verficando se esse é o primeiro usuário a entrar na sessão
			String usuarioControle = sessaoGrupo.getIdSessao().getUsuarioControle();
			if(usuarioControle == null || usuarioControle.isEmpty()) {
				sessaoGrupo.getIdSessao().setUsuarioControle(sakaiProxy.getCurrentUserDisplayName());
				sessaoGrupo.getIdSessao().merge();
			}
			
			Experimento experimento = sessaoGrupo.getIdSessao().getIdExperimento();
			
			model.addAttribute("sessaoGrupo", sessaoGrupo);
			model.addAttribute("participantes", participantes);
			model.addAttribute("experimento", experimento);
			model.addAttribute("metadadosEntrada", MetadadoEntrada.findMetadadoEntradaByExperimento(experimento.getIdExperimento()));
			model.addAttribute("metadadosSaida", MetadadoSaida.findMetadadoSaidaByExperimento(experimento.getIdExperimento()));
			model.addAttribute("usuarioLogado", sakaiProxy.getCurrentUserDisplayName());
		}
		
		return "index";
	}
	
}
