package org.sakaiproject.apue.tool;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Configurable
@Entity
@Table(name = "grafico")
public class Grafico {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_grafico")
    private Integer idGrafico;

	public Integer getIdGrafico() {
        return this.idGrafico;
    }

	public void setIdGrafico(Integer id) {
        this.idGrafico = id;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "id_experimento", referencedColumnName = "id_experimento", nullable = false)
    private Experimento idExperimento;

	@ManyToOne
    @JoinColumn(name = "id_metadado_x", referencedColumnName = "id_metadado", nullable = false)
    private Metadado idMetadadoX;

	@ManyToOne
    @JoinColumn(name = "id_metadado_y", referencedColumnName = "id_metadado", nullable = false)
    private Metadado idMetadadoY;

	@Column(name = "nome", length = 50)
    
    private String nome;

	@Column(name = "descricao", length = 500)
    
    private String descricao;

	public Experimento getIdExperimento() {
        return idExperimento;
    }

	public void setIdExperimento(Experimento idExperimento) {
        this.idExperimento = idExperimento;
    }

	public Metadado getIdMetadadoX() {
        return idMetadadoX;
    }

	public void setIdMetadadoX(Metadado idMetadadoX) {
        this.idMetadadoX = idMetadadoX;
    }

	public Metadado getIdMetadadoY() {
        return idMetadadoY;
    }

	public void setIdMetadadoY(Metadado idMetadadoY) {
        this.idMetadadoY = idMetadadoY;
    }

	public String getNome() {
        return nome;
    }

	public void setNome(String nome) {
        this.nome = nome;
    }

	public String getDescricao() {
        return descricao;
    }

	public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Grafico().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countGraficoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Grafico o", Long.class).getSingleResult();
    }

	public static List<Grafico> findAllGraficoes() {
        return entityManager().createQuery("SELECT o FROM Grafico o", Grafico.class).getResultList();
    }

	public static Grafico findGrafico(Integer idGrafico) {
        if (idGrafico == null) return null;
        return entityManager().find(Grafico.class, idGrafico);
    }

	public static List<Grafico> findGraficoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Grafico o", Grafico.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Grafico attached = Grafico.findGrafico(this.idGrafico);
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
    public Grafico merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Grafico merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
