package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.apue.tool.MetadadoEntrada;
import org.sakaiproject.apue.tool.OpcaoMetadadoEntrada;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/opcaoDadoEntrada")
@Controller
public class OpcaoMetadadoEntradaController {

	@RequestMapping(value="/criar/{idMetadadoEntrada}", method = RequestMethod.GET)
    public String createForm(@PathVariable("idMetadadoEntrada")Integer idMetadado, Model uiModel) {
		OpcaoMetadadoEntrada opcaoMetadadoEntrada = new OpcaoMetadadoEntrada();
        MetadadoEntrada metadadoEntrada = new MetadadoEntrada();
        metadadoEntrada.setIdMetadado(idMetadado);
        opcaoMetadadoEntrada.setIdMetadado(metadadoEntrada);
        uiModel.addAttribute("opcaoMetadadoEntrada", opcaoMetadadoEntrada);
        uiModel.addAttribute("action", "criar");
        return "addOpcaoDadoEntrada";
    }

	@RequestMapping(value = "/criar", method = RequestMethod.POST)
    public String criar(OpcaoMetadadoEntrada opcaoMetadado, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		System.out.println("IdOpcao:"+opcaoMetadado.getIdOpcao());
		System.out.println("IdMetadado:"+opcaoMetadado.getIdMetadado().getIdMetadado());
		opcaoMetadado.persist();
        return "redirect:/dadosExperimento/dadoEntrada/" + encodeUrlPathSegment(opcaoMetadado.getIdMetadado().getIdMetadado().toString(), httpServletRequest);
    }
	
	@RequestMapping(value="/editar/{idOpcao}", method = RequestMethod.GET)
    public String show(@PathVariable("idOpcao")Integer idOpcao, Model uiModel, HttpServletRequest httpServletRequest) {
		OpcaoMetadadoEntrada opcao = OpcaoMetadadoEntrada.findOpcaoMetadadoEntrada(idOpcao);
        uiModel.addAttribute("opcaoMetadadoEntrada", opcao);
        uiModel.addAttribute("action", "editar");
        return "addOpcaoDadoEntrada";
    }
	
	@RequestMapping(value="/editar", method = RequestMethod.POST)
    public String editar(OpcaoMetadadoEntrada opcaoMetadado, Model uiModel, HttpServletRequest httpServletRequest) {
		opcaoMetadado.merge();
        return "redirect:/dadosExperimento/dadoEntrada/" + encodeUrlPathSegment(opcaoMetadado.getIdMetadado().getIdMetadado().toString(), httpServletRequest);
    }
	
	@RequestMapping(value="/remover/{idOpcao}", method = RequestMethod.GET)
    public String remover(@PathVariable("idOpcao")Integer idOpcao, Model uiModel, HttpServletRequest httpServletRequest) {
		OpcaoMetadadoEntrada opcao = OpcaoMetadadoEntrada.findOpcaoMetadadoEntrada(idOpcao);
		opcao.getIdMetadado().getOpcaoMetadadoEntradas().remove(opcao);
		opcao.getIdMetadado().merge();
		return "redirect:/dadosExperimento/dadoEntrada/" + encodeUrlPathSegment(opcao.getIdMetadado().getIdMetadado().toString(), httpServletRequest);
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
