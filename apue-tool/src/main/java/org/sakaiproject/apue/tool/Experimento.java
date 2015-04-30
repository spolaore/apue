package org.sakaiproject.apue.tool;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Configurable
@Entity
@Table(name = "experimento")
public class Experimento {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Experimento().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countExperimentoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Experimento o", Long.class).getSingleResult();
    }

	public static List<Experimento> findAllExperimentoes() {
        return entityManager().createQuery("SELECT o FROM Experimento o", Experimento.class).getResultList();
    }

	public static Experimento findExperimento(Integer idExperimento) {
        if (idExperimento == null) return null;
        return entityManager().find(Experimento.class, idExperimento);
    }

	public static List<Experimento> findExperimentoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Experimento o", Experimento.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Experimento attached = Experimento.findExperimento(this.idExperimento);
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
    public Experimento merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Experimento merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(fetch=FetchType.EAGER, mappedBy = "idExperimento")
    private Set<Grafico> graficoes;

	@JsonIgnore
	@OneToMany(mappedBy = "idExperimento")
    private Set<Metadado> metadadoes;

	@JsonIgnore
	@OneToMany(mappedBy = "idExperimento")
    private Set<Sessao> sessaos;

	@Column(name = "nome", length = 100)
    private String nome;

	@Column(name = "porta", length = 50)
    private String porta;

	@Column(name = "descricao", length = 500)
    private String descricao;

	@Column(name = "status", length = 1)
    private String status;

	@Column(name = "url_streaming", length = 100)
    private String urlStreaming;

	public Set<Grafico> getGraficoes() {
        return graficoes;
    }

	public void setGraficoes(Set<Grafico> graficoes) {
        this.graficoes = graficoes;
    }
	
	public Set<Metadado> getMetadadoes() {
        return metadadoes;
    }

	public void setMetadadoes(Set<Metadado> metadadoes) {
        this.metadadoes = metadadoes;
    }

	public Set<Sessao> getSessaos() {
        return sessaos;
    }

	public void setSessaos(Set<Sessao> sessaos) {
        this.sessaos = sessaos;
    }

	public String getNome() {
        return nome;
    }

	public void setNome(String nome) {
        this.nome = nome;
    }

	public String getPorta() {
        return porta;
    }

	public void setPorta(String porta) {
        this.porta = porta;
    }

	public String getDescricao() {
        return descricao;
    }

	public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	public String getStatus() {
        return status;
    }

	public void setStatus(String status) {
        this.status = status;
    }

	public String getUrlStreaming() {
        return urlStreaming;
    }

	public void setUrlStreaming(String urlStreaming) {
        this.urlStreaming = urlStreaming;
    }

	public String toString() {
        return this.nome;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_experimento")
    private Integer idExperimento;

	public Integer getIdExperimento() {
        return this.idExperimento;
    }

	public void setIdExperimento(Integer id) {
        this.idExperimento = id;
    }
}
