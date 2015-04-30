package org.sakaiproject.apue.tool;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sessao")
@Configurable
public class Sessao {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Sessao().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSessaos() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Sessao o", Long.class).getSingleResult();
    }

	public static List<Sessao> findAllSessaos() {
        return entityManager().createQuery("SELECT o FROM Sessao o", Sessao.class).getResultList();
    }

	public static Sessao findSessao(Integer idSessao) {
        if (idSessao == null) return null;
        return entityManager().find(Sessao.class, idSessao);
    }
	
	public static Sessao findSessaoByUser(Integer idSessao) {
        if (idSessao == null) return null;
        return entityManager().find(Sessao.class, idSessao);
    }

	public static List<Sessao> findSessaoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Sessao o", Sessao.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Sessao attached = Sessao.findSessao(this.idSessao);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Sessao merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Sessao merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_sessao")
    private Integer idSessao;

	public Integer getIdSessao() {
        return this.idSessao;
    }

	public void setIdSessao(Integer id) {
        this.idSessao = id;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@JsonIgnore
	@OneToMany(mappedBy = "idSessao")
    private Set<Dado> dadoes;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "idSessao", orphanRemoval = true)
    private Set<SessaoGrupo> sessaoGrupoes;

	@ManyToOne
    @JoinColumn(name = "id_experimento", referencedColumnName = "id_experimento", nullable = false)
    private Experimento idExperimento;

	@Column(name = "usuario_criador", length = 150)
    
    private String usuarioCriador;

	@Column(name = "inicio")
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar inicio;

	@Column(name = "fim")
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar fim;

	@Column(name = "usuario_controle", length = 150)
    
    private String usuarioControle;

	public Set<Dado> getDadoes() {
        return dadoes;
    }

	public void setDadoes(Set<Dado> dadoes) {
        this.dadoes = dadoes;
    }

	public Set<SessaoGrupo> getSessaoGrupoes() {
        return sessaoGrupoes;
    }

	public void setSessaoGrupoes(Set<SessaoGrupo> sessaoGrupoes) {
        this.sessaoGrupoes = sessaoGrupoes;
    }

	public Experimento getIdExperimento() {
        return idExperimento;
    }

	public void setIdExperimento(Experimento idExperimento) {
        this.idExperimento = idExperimento;
    }

	public String getUsuarioCriador() {
        return usuarioCriador;
    }

	public void setUsuarioCriador(String usuarioCriador) {
        this.usuarioCriador = usuarioCriador;
    }

	public Calendar getInicio() {
        return inicio;
    }

	public void setInicio(Calendar inicio) {
        this.inicio = inicio;
    }

	public Calendar getFim() {
        return fim;
    }

	public void setFim(Calendar fim) {
        this.fim = fim;
    }

	public String getUsuarioControle() {
        return usuarioControle;
    }

	public void setUsuarioControle(String usuarioControle) {
        this.usuarioControle = usuarioControle;
    }
	
	public List<String> getGruposId() {
		return gruposId;
	}

	public void setGruposId(List<String> gruposId) {
		this.gruposId = gruposId;
	}

	@JsonIgnore
	@Transient
	private List<String> gruposId;

	public static String findUsuarioControle(Integer idSessao) {
		TypedQuery<String> query = entityManager().createQuery("select sessao.usuarioControle from Sessao as sessao  where sessao.idSessao =:idSessao ", String.class);
 		query.setParameter("idSessao", idSessao);
 		return query.getSingleResult();
	}

	public static Integer findSessaoByPortaExperimento(String porta) {
		TypedQuery<Integer> query = entityManager().createQuery("select sessao.idSessao from Sessao as sessao where sessao.idExperimento.porta =:porta and sessao.fim > :agora ", Integer.class);
 		query.setParameter("porta", porta);
 		query.setParameter("agora", Calendar.getInstance());
 		return query.getSingleResult();
	}
}
