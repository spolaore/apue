package org.sakaiproject.apue.tool;

import java.util.List;

public class UpdateSessao {

	private List<Dado> dados;
	private String usuarioControle;

	public UpdateSessao() {
		
	}
	
	public UpdateSessao(List<Dado> dados, String usuarioControle) {
		this.dados = dados;
		this.usuarioControle = usuarioControle;
	}
	
	public List<Dado> getDados() {
		return dados;
	}

	public void setDados(List<Dado> dados) {
		this.dados = dados;
	}

	public String getUsuarioControle() {
		return usuarioControle;
	}

	public void setUsuarioControle(String usuarioControle) {
		this.usuarioControle = usuarioControle;
	}

}
