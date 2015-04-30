package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.apue.tool.Dado;
import org.sakaiproject.apue.tool.Experimento;
import org.sakaiproject.apue.tool.Grafico;
import org.sakaiproject.apue.tool.Metadado;
import org.sakaiproject.apue.tool.MetadadoSaida;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;



@RequestMapping("/dadosExperimento")
@Controller
public class MetadadoSaidaController 
{

	@RequestMapping(value = "/addDadoSaida/{idExperimento}")
    public String adicionarValorSaida(@PathVariable("idExperimento") Integer idExperimento, Model uiModel) {
		Metadado metadado = new Metadado();
		metadado.setIdExperimento(Experimento.findExperimento(idExperimento));
		MetadadoSaida metadadoSaida = new MetadadoSaida();
		metadadoSaida.setMetadado(metadado);
		
		populateEditForm(uiModel, metadadoSaida);
		uiModel.addAttribute("action", "criar");
		uiModel.addAttribute("metadadoSaida", metadadoSaida);		
        return "addDadoSaida";
    }
	
	@RequestMapping(value = "/addDadoSaida/criar", method = RequestMethod.POST, produces = "text/html")
    public String criar(MetadadoSaida metadadoSaida, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, metadadoSaida);
            return "addDadoSaida";
        }
        uiModel.asMap().clear();
        Metadado metadado = metadadoSaida.getMetadado();
        metadado.setMetadadoSaida(metadadoSaida);
        metadado.persist();
        return "redirect:/dadosExperimento/"+encodeUrlPathSegment(metadadoSaida.getMetadado().getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value ="/voltar/{idExperimento}", method = RequestMethod.GET)
	public String voltar(@PathVariable("idExperimento") Integer idExperimento, HttpServletRequest httpServletRequest) throws Exception {
		return "redirect:/dadosExperimento/"+encodeUrlPathSegment(idExperimento.toString(), httpServletRequest);
	}
	
	@RequestMapping(value = "/dadoSaida/remover/{idMetadado}", produces = "text/html")
    public String remover(@PathVariable("idMetadado") Integer idMetadado, Model uiModel,HttpServletRequest httpServletRequest) {
        MetadadoSaida metadadoSaida = MetadadoSaida.findMetadadoSaida(idMetadado);
        metadadoSaida.getMetadado().remove();
        uiModel.asMap().clear();
        return "redirect:/dadosExperimento/"+encodeUrlPathSegment(metadadoSaida.getMetadado().getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/dadoSaida/{idMetadado}", produces = "text/html")
    public String mostrar(@PathVariable("idMetadado") Integer idMetadado, Model uiModel) {
        uiModel.addAttribute("metadadoSaida", MetadadoSaida.findMetadadoSaida(idMetadado));
        uiModel.addAttribute("action", "modificar");
        return "addDadoSaida";
    }
	
	@RequestMapping(value="/dadoSaida/modificar", method = RequestMethod.POST, produces = "text/html")
    public String atualizar(@ModelAttribute MetadadoSaida metadadoSaida, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, metadadoSaida);
            return "addExperimento";
        }
        uiModel.asMap().clear();
        metadadoSaida.getMetadado().merge();
        return "redirect:/dadosExperimento/" + encodeUrlPathSegment(metadadoSaida.getMetadado().getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	void populateEditForm(Model uiModel, MetadadoSaida metadadoSaida) {
        uiModel.addAttribute("metadadoSaida", metadadoSaida);
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
