package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.apue.tool.Experimento;
import org.sakaiproject.apue.tool.Metadado;
import org.sakaiproject.apue.tool.MetadadoEntrada;
import org.sakaiproject.apue.tool.MetadadoSaida;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/dadosExperimento")
@Controller
public class MetadadoEntradaController {
	

	@RequestMapping(value = "/{idExperimento}", method = RequestMethod.GET)
    public String listaDados(@PathVariable("idExperimento") Integer idExperimento, Model uiModel) {
        List<MetadadoEntrada> listaMetadadoEntrada = MetadadoEntrada.findMetadadoEntradaByExperimento(idExperimento);
        List<MetadadoSaida> listaMetadadoSaida = MetadadoSaida.findMetadadoSaidaByExperimento(idExperimento);
        uiModel.addAttribute("listaMetadadoEntrada", listaMetadadoEntrada);
        uiModel.addAttribute("listaMetadadoSaida", listaMetadadoSaida);
        uiModel.addAttribute("idExperimento", idExperimento);
        return "dadosExperimento";
    }

	@RequestMapping(value = "/addDadoEntrada/{idExperimento}")
    public String adicionarValorEntrada(@PathVariable("idExperimento") Integer idExperimento, Model uiModel) {
		Metadado metadado = new Metadado();
		Experimento experimento = new Experimento();
		experimento.setIdExperimento(idExperimento);
		metadado.setIdExperimento(experimento);
		MetadadoEntrada metadadoEntrada = new MetadadoEntrada();
		metadadoEntrada.setMetadado(metadado);
		metadado.setMetadadoEntrada(metadadoEntrada);
		
		uiModel.addAttribute("action", "criar");
		uiModel.addAttribute("idExperimento", idExperimento);
		uiModel.addAttribute("tipo", MetadadoEntrada.TipoEntrada.values());
		uiModel.addAttribute("metadadoEntrada", metadadoEntrada);
		return "addDadoEntrada";
    }
	
	@RequestMapping(value = "/addDadoEntrada/criar", method = RequestMethod.POST, produces = "text/html")
    public String criar(MetadadoEntrada metadadoEntrada, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	uiModel.addAttribute("metadadoEntrada", metadadoEntrada);
            return "addDadoEntrada";
        }
        uiModel.asMap().clear();
        Metadado metadado = metadadoEntrada.getMetadado();
        metadado.setMetadadoEntrada(metadadoEntrada);
        metadado.persist();
        return "redirect:/dadosExperimento/"+encodeUrlPathSegment(metadadoEntrada.getMetadado().getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/dadoEntrada/editar", method = RequestMethod.POST, produces = "text/html")
    public String editar(MetadadoEntrada metadadoEntrada, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        Metadado metadado = metadadoEntrada.getMetadado();
        metadado.merge();
        return "redirect:/dadosExperimento/"+encodeUrlPathSegment(metadado.getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/dadoEntrada/remover/{idMetadado}", produces = "text/html")
    public String remover(@PathVariable("idMetadado") Integer idMetadado, Model uiModel,HttpServletRequest httpServletRequest) {
		MetadadoEntrada metadadoEntrada = MetadadoEntrada.findMetadadoEntrada(idMetadado);
		metadadoEntrada.remove();
        Metadado metadado = Metadado.findMetadado(idMetadado);
        metadado.remove();
        
        uiModel.asMap().clear();
        return "redirect:/dadosExperimento/"+encodeUrlPathSegment(metadadoEntrada.getMetadado().getIdExperimento().getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/dadoEntrada/{idMetadado}", produces = "text/html")
    public String mostrar(@PathVariable("idMetadado") Integer idMetadado, Model uiModel) {
		MetadadoEntrada metadado = MetadadoEntrada.findMetadadoEntrada(idMetadado);
		uiModel.addAttribute("metadadoEntrada", metadado);
		uiModel.addAttribute("idExperimento", metadado.getMetadado().getIdExperimento().getIdExperimento());
		uiModel.addAttribute("tipo", MetadadoEntrada.TipoEntrada.values());
		metadado.getOpcaoMetadadoEntradas().size();
		uiModel.addAttribute("opcoesMetadado", metadado.getOpcaoMetadadoEntradas());
        uiModel.addAttribute("action", "editar");
        return "addDadoEntrada";
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
