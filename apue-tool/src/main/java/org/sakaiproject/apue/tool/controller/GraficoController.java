package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.sakaiproject.apue.logic.SakaiProxy;
import org.sakaiproject.apue.tool.Experimento;
import org.sakaiproject.apue.tool.Grafico;
import org.sakaiproject.apue.tool.Metadado;
import org.sakaiproject.apue.tool.MetadadoSaida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/graficos")
@Controller
public class GraficoController 
{
	@Setter
	@Getter
	@Autowired
	private SakaiProxy sakaiProxy = null;
	
	@RequestMapping(value = "/{idExperimento}", method = RequestMethod.GET)
	public String listGrafico(@PathVariable("idExperimento") Integer idExperimento, Model model) throws Exception {
		Set<Grafico> graficos = Experimento.findExperimento(idExperimento).getGraficoes();
		graficos.size();
		model.addAttribute("graficos", graficos);
		return "graficosExperimento";
	}
	
	@RequestMapping(value = "/addGrafico/{idExperimento}", method = RequestMethod.GET)
	public String createForm(@PathVariable("idExperimento") Integer idExperimento,  Model model) throws Exception {
		Grafico grafico = new Grafico();
		Experimento experimento = new Experimento();
		experimento.setIdExperimento(idExperimento);
		grafico.setIdExperimento(experimento);
		
		populateEditForm(model, grafico);
		List<Metadado> metadados = Metadado.findMetadadoByExperimento(idExperimento);
		model.addAttribute("action", "criar");
		model.addAttribute("metadados", metadados);
		return "addGrafico";
	}
	
	@RequestMapping(value = "addGrafico/criar", method = RequestMethod.POST, produces = "text/html")
    public String criar(Grafico grafico, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(model, grafico);
            return "addGrafico";
        }
        model.asMap().clear();
        grafico.persist();
        return "redirect:/graficos/"+encodeUrlPathSegment(grafico.getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/remover/{idGrafico}", produces = "text/html")
    public String remover(@PathVariable("idGrafico") Integer idGrafico, Model uiModel, HttpServletRequest httpServletRequest) {
        Grafico grafico = Grafico.findGrafico(idGrafico);
        grafico.remove();
        uiModel.asMap().clear();
        return "redirect:/graficos/"+encodeUrlPathSegment(grafico.getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	
	@RequestMapping(value = "/editar/{idGrafico}", produces = "text/html")
    public String show(@PathVariable("idGrafico") Integer idGrafico, Model uiModel) {
		uiModel.addAttribute("action", "modificar");
		uiModel.addAttribute("grafico", Grafico.findGrafico(idGrafico));
		Grafico grafico = Grafico.findGrafico(idGrafico);
		List<Metadado> metadados = Metadado.findMetadadoByExperimento(grafico.getIdExperimento().getIdExperimento());
		uiModel.addAttribute("metadados", metadados);
		uiModel.addAttribute("idExperimento", grafico.getIdExperimento().getIdExperimento());
		return "addGrafico";
    }
	
	@RequestMapping(value = "/editar/modificar",produces = "text/html")
    public String modificar(@ModelAttribute Grafico grafico, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, grafico);
            return "uiBuilder";
        }
        uiModel.asMap().clear();
        grafico.merge();
        return "redirect:/graficos/"+encodeUrlPathSegment(grafico.getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	

	void populateEditForm(Model uiModel, Grafico grafico) {
        uiModel.addAttribute("grafico", grafico);
        uiModel.addAttribute("experimentoes", Experimento.findAllExperimentoes());
        uiModel.addAttribute("metadados", Metadado.findAllMetadadoes());
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
