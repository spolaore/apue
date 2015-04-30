package org.sakaiproject.apue.tool;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "metadado")
@Configurable
public class Metadado {

	public String toString() {
        return this.getRotulo();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_metadado")
    private Integer idMetadado;

	@OrderBy
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "idMetadado", cascade = CascadeType.REMOVE)
    private Set<Dado> dadoes;
	
	public Set<Dado> getDadoes() {
        return dadoes;
    }

	public void setDadoes(Set<Dado> dadoes) {
        this.dadoes = dadoes;
    }
	
	public Integer getIdMetadado() {
        return this.idMetadado;
    }

	public void setIdMetadado(Integer id) {
        this.idMetadado = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Metadado().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMetadadoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Metadado o", Long.class).getSingleResult();
    }

	public static List<Metadado> findAllMetadadoes() {
        return entityManager().createQuery("SELECT o FROM Metadado o", Metadado.class).getResultList();
    }

	public static Metadado findMetadado(Integer idMetadado) {
        if (idMetadado == null) return null;
        return entityManager().find(Metadado.class, idMetadado);
    }

	public static List<Metadado> findMetadadoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Metadado o", Metadado.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static List<Metadado> findMetadadoByExperimento(Integer idExperimento) {
        if (idExperimento == null) return null;
        TypedQuery<Metadado> query = entityManager().createQuery("select metadado from Metadado as metadado inner join fetch metadado.idExperimento as experimento where experimento.idExperimento = :idExperimento ", Metadado.class);
        		query.setParameter("idExperimento", idExperimento);
        		return query.getResultList();
    }
	
	public static String findMetadadoChave(Integer idMetadado) {
        TypedQuery<String> query = entityManager().createQuery("select metadado.chave from Metadado as metadado  where metadado.idMetadado= :idMetadado ", String.class);
        		query.setParameter("idMetadado", idMetadado);
        		return query.getSingleResult();
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
            Metadado attached = Metadado.findMetadado(this.idMetadado);
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
    public Metadado merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Metadado merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@JsonIgnore
	@OneToOne(mappedBy = "metadado", cascade=CascadeType.ALL)
    private MetadadoEntrada metadadoEntrada;

	@JsonIgnore
	@OneToOne(mappedBy = "metadado", cascade=CascadeType.ALL)
    private MetadadoSaida metadadoSaida;

	@ManyToOne
	@JsonIgnore
    @JoinColumn(name = "id_experimento", referencedColumnName = "id_experimento", nullable = false)
    private Experimento idExperimento;
	
	@JsonIgnore
	@OneToMany(mappedBy = "idMetadadoX")
    private Set<Grafico> graficoes;

	@JsonIgnore
	@OneToMany(mappedBy = "idMetadadoY")
    private Set<Grafico> graficoes1;
	
	@Column(name = "chave", length = 100, unique = true)
    private String chave;

	@Column(name = "descricao", length = 500)
    
    private String descricao;

	@Column(name = "rotulo", length = 50)
    
    private String rotulo;

	@Column(name = "unidade", length = 20)
    
    private String unidade;

	public MetadadoEntrada getMetadadoEntrada() {
        return metadadoEntrada;
    }

	public void setMetadadoEntrada(MetadadoEntrada metadadoEntrada) {
        this.metadadoEntrada = metadadoEntrada;
    }

	public MetadadoSaida getMetadadoSaida() {
        return metadadoSaida;
    }

	public void setMetadadoSaida(MetadadoSaida metadadoSaida) {
        this.metadadoSaida = metadadoSaida;
    }

	public Experimento getIdExperimento() {
        return idExperimento;
    }

	public void setIdExperimento(Experimento idExperimento) {
        this.idExperimento = idExperimento;
    }

	public String getChave() {
        return chave;
    }

	public void setChave(String chave) {
        this.chave = chave;
    }

	public String getDescricao() {
        return descricao;
    }

	public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	public String getRotulo() {
        return rotulo;
    }

	public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

	public String getUnidade() {
        return unidade;
    }

	public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
	
	public Set<Grafico> getGraficoes() {
        return graficoes;
    }

	public void setGraficoes(Set<Grafico> graficoes) {
        this.graficoes = graficoes;
    }

	public Set<Grafico> getGraficoes1() {
        return graficoes1;
    }

	public void setGraficoes1(Set<Grafico> graficoes1) {
        this.graficoes1 = graficoes1;
    }

	public static Integer findIdMetadadoByChave(String chave) {
		TypedQuery<Integer> query = entityManager().createQuery("select metadado.idMetadado from Metadado as metadado  where metadado.chave =:chave ", Integer.class);
 		query.setParameter("chave", chave);
 		return query.getSingleResult();
	}
}
