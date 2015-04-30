package org.sakaiproject.apue.tool;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.beans.factory.annotation.Configurable;

@Embeddable
@Configurable
public final class SessaoGrupoPK implements Serializable {

	@Column(name = "id_sessao", nullable = false)
    private Integer idSessao;

	@Column(name = "id_grupo", nullable = false, length = 100)
    private String idGrupo;

	public SessaoGrupoPK(Integer idSessao, String idGrupo) {
        super();
        this.idSessao = idSessao;
        this.idGrupo = idGrupo;
    }

	private SessaoGrupoPK() {
        super();
    }

	public Integer getIdSessao() {
        return idSessao;
    }

	public String getIdGrupo() {
        return idGrupo;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idGrupo == null) ? 0 : idGrupo.hashCode());
		result = prime * result
				+ ((idSessao == null) ? 0 : idSessao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessaoGrupoPK other = (SessaoGrupoPK) obj;
		if (idGrupo == null) {
			if (other.idGrupo != null)
				return false;
		} else if (!idGrupo.equals(other.idGrupo))
			return false;
		if (idSessao == null) {
			if (other.idSessao != null)
				return false;
		} else if (!idSessao.equals(other.idSessao))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static SessaoGrupoPK fromJsonToSessaoGrupoPK(String json) {
        return new JSONDeserializer<SessaoGrupoPK>().use(null, SessaoGrupoPK.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SessaoGrupoPK> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<SessaoGrupoPK> fromJsonArrayToSessaoGrupoPKs(String json) {
        return new JSONDeserializer<List<SessaoGrupoPK>>().use(null, ArrayList.class).use("values", SessaoGrupoPK.class).deserialize(json);
    }
	
	
}
