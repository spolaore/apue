package org.sakaiproject.apue.tool;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Configurable
@Entity
@Table(name = "dado")
public class Dado {
	
	public enum TipoDado {
		ENTRADA("E"), SAIDA("S");

		private String value;

		private TipoDado(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public Dado() {

	}

	public String toString() {
		return this.valor;
	}
	
	@Transient
	@JsonInclude(Include.NON_NULL)
	private Integer idMetadadoJson;

	public Integer getIdMetadadoJson() {
		return idMetadadoJson;
	}

	public void setIdMetadadoJson(Integer idMetadadoJson) {
		this.idMetadadoJson = idMetadadoJson;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_metadado", referencedColumnName = "id_metadado", nullable = false)
	private Metadado idMetadado;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sessao", referencedColumnName = "id_sessao", nullable = false)
	private Sessao idSessao;

	@Column(name = "tipo_dado", length = 1)
	private String tipoDado;

	@Column(name = "data_insercao")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dataInsercao;

	@Column(name = "valor", length = 100)
	private String valor;

	@JsonIgnore
	public Metadado getIdMetadado() {
		return idMetadado;
	}

	@JsonProperty
	public void setIdMetadado(Metadado idMetadado) {
		this.idMetadado = idMetadado;
	}

	@JsonIgnore
	public Sessao getIdSessao() {
		return idSessao;
	}
	
	@JsonProperty
	public void setIdSessao(Sessao idSessao) {
		this.idSessao = idSessao;
	}

	public String getTipoDado() {
		return tipoDado;
	}

	public void setTipoDado(String tipoDado) {
		this.tipoDado = tipoDado;
	}

	public Calendar getDataInsercao() {
		return dataInsercao;
	}

	public void setDataInsercao(Calendar dataInsercao) {
		this.dataInsercao = dataInsercao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_dado")
	private Integer idDado;

	public Integer getIdDado() {
		return this.idDado;
	}

	public void setIdDado(Integer id) {
		this.idDado = id;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Dado().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countDadoes() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Dado o",
				Long.class).getSingleResult();
	}

	public static List<Dado> findAllDadoes() {
		return entityManager().createQuery("SELECT o FROM Dado o", Dado.class)
				.getResultList();
	}

	public static Dado findDado(Integer idDado) {
		if (idDado == null)
			return null;
		return entityManager().find(Dado.class, idDado);
	}

	public static List<Dado> findDadoEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Dado o", Dado.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Dado attached = Dado.findDado(this.idDado);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Dado merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Dado merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static List<Dado> findNovosDados(Integer idMetadado, Integer idUltimoDado) {
		TypedQuery<Dado> query = entityManager()
				.createQuery(
						"select dado from Dado as dado where dado.idDado > :idUltimoDado and dado.idMetadado.idMetadado = :idMetadado",
						Dado.class);
		query.setParameter("idUltimoDado", idUltimoDado);
		query.setParameter("idMetadado", idMetadado);
		return query.getResultList();
	}

	public static List<Dado> findUltimosDados(Integer idSessao) {
		Query query = entityManager()
				.createNativeQuery("select * from apue.dado inner join " +
						"(select max(dado.id_dado) as id_dado from apue.dado where dado.id_sessao = ? " +
						"group by dado.id_sessao, dado.id_metadado) max_dado ON dado.id_dado = max_dado.id_dado", Dado.class);
		query.setParameter(1, idSessao);
		return query.getResultList();
	}
}
