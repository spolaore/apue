package org.sakaiproject.apue.tool.controller;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.apue.logic.SakaiProxy;
import org.sakaiproject.apue.tool.Experimento;
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

@RequestMapping("/experimentos")
@Controller
public class ExperimentoController {
	
	@Autowired
	private SakaiProxy sakaiProxy = null;
	
	@Autowired
	private ArduinoController serial = null;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) throws Exception {
		List<Experimento> experimentos = Experimento.findAllExperimentoes();
		model.addAttribute("experimentos", experimentos);
		return "listExperimento";
	}

	@RequestMapping(value = "/cadastrar")
	public String cadastrar(Model uiModel) {
		populateEditForm(uiModel, new Experimento());
		uiModel.addAttribute("action", "criar");
		uiModel.addAttribute("portasUsb", getPortasUsb());
		return "addExperimento";
	}
	
	@RequestMapping(value = "/criar", method = RequestMethod.POST, produces = "text/html")
    public String criar(Experimento experimento, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimento);
            return "addExperimento";
        }
        uiModel.asMap().clear();
        experimento.setStatus("a");
        experimento.persist();
        return "redirect:/dadosExperimento/" + encodeUrlPathSegment(experimento.getIdExperimento().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/remover/{idExperimento}", produces = "text/html")
    public String remover(@PathVariable("idExperimento") Integer idExperimento, Model uiModel) {
        Experimento experimento = Experimento.findExperimento(idExperimento);
        experimento.remove();
        uiModel.asMap().clear();
        return "redirect:/experimentos";
    }
	
	@RequestMapping(value = "/{idExperimento}", produces = "text/html")
    public String mostrar(@PathVariable("idExperimento") Integer idExperimento, Model uiModel) {
        uiModel.addAttribute("experimento", Experimento.findExperimento(idExperimento));
        uiModel.addAttribute("itemId", idExperimento);
        uiModel.addAttribute("portasUsb", getPortasUsb());
        uiModel.addAttribute("action", "editar");
        return "addExperimento";
    }

	private Collection<String> getPortasUsb() {
		return serial.getPorts();
	}
	
	@RequestMapping(value="/editar", method = RequestMethod.POST, produces = "text/html")
    public String atualizar(@ModelAttribute Experimento experimento, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimento);
            return "addExperimento";
        }
        System.out.println(experimento.getNome());
        uiModel.asMap().clear();
        experimento.merge();
        return "redirect:/dadosExperimento/" + encodeUrlPathSegment(experimento.getIdExperimento().toString(), httpServletRequest);
    }
	
	void populateEditForm(Model uiModel, Experimento experimento) {
        uiModel.addAttribute("experimento", experimento);
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
