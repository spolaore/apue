package org.sakaiproject.apue.tool;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Configurable
@Entity
@Table(name = "metadado_entrada")
public class MetadadoEntrada {

	public enum TipoEntrada {
		TEXT("Campo Texto", 1), RANGE("Slider", 2), SELECT("Combo Box", 3), CHECKBOX(
				"Checkbox", 4), NUMBER("Number", 5), RADIO("Radio", 6);
		
		private int value;
		private String label;
		
		private TipoEntrada(String label, int value) {
			this.value = value;
			this.label = label;
		}
		
		public int getValue() { return this.value; }
		public String getLabel() { return this.label; }
	}
	
	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("metadado").toString();
    }

	@JsonIgnore
	@MapsId
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metadado", nullable = false, insertable = false, updatable = false)
    private Metadado metadado;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "idMetadado", orphanRemoval = true)
    private Set<OpcaoMetadadoEntrada> opcaoMetadadoEntradas;

	@Column(name = "valor_padrao", length = 100)
    @JsonIgnore
    private String valorPadrao;

	@Column(name = "tipo_entrada")
    @JsonIgnore
    private Integer tipoEntrada;

	@Column(name = "visivel", length = 1)
    @JsonIgnore
    private String visivel;

	public Metadado getMetadado() {
        return metadado;
    }

	public void setMetadado(Metadado metadado) {
        this.metadado = metadado;
    }

	public Set<OpcaoMetadadoEntrada> getOpcaoMetadadoEntradas() {
        return opcaoMetadadoEntradas;
    }

	public void setOpcaoMetadadoEntradas(Set<OpcaoMetadadoEntrada> opcaoMetadadoEntradas) {
        this.opcaoMetadadoEntradas = opcaoMetadadoEntradas;
    }

	public String getValorPadrao() {
        return valorPadrao;
    }

	public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

	public Integer getTipoEntrada() {
        return tipoEntrada;
    }

	public void setTipoEntrada(Integer tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

	public String getVisivel() {
        return visivel;
    }

	public void setVisivel(String visivel) {
        this.visivel = visivel;
    }

	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_metadado")
    private Integer idMetadado;

	public Integer getIdMetadado() {
        return this.idMetadado;
    }

	public void setIdMetadado(Integer id) {
        this.idMetadado = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new MetadadoEntrada().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMetadadoEntradas() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MetadadoEntrada o", Long.class).getSingleResult();
    }

	public static List<MetadadoEntrada> findAllMetadadoEntradas() {
        return entityManager().createQuery("SELECT o FROM MetadadoEntrada o", MetadadoEntrada.class).getResultList();
    }

	public static MetadadoEntrada findMetadadoEntrada(Integer idMetadado) {
        if (idMetadado == null) return null;
        return entityManager().find(MetadadoEntrada.class, idMetadado);
    }
	
	public static List<MetadadoEntrada> findMetadadoEntradaByExperimento(Integer idExperimento) {
        if (idExperimento == null) return null;
        TypedQuery<MetadadoEntrada> query = entityManager().createQuery("select entrada from MetadadoEntrada as entrada inner join fetch entrada.metadado as metadado inner join fetch metadado.idExperimento as experimento where experimento.idExperimento = :idExperimento ", MetadadoEntrada.class);
        		query.setParameter("idExperimento", idExperimento);
        		return query.getResultList();
    }

	public static List<MetadadoEntrada> findMetadadoEntradaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MetadadoEntrada o", MetadadoEntrada.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MetadadoEntrada attached = MetadadoEntrada.findMetadadoEntrada(this.idMetadado);
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
    public MetadadoEntrada merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MetadadoEntrada merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
