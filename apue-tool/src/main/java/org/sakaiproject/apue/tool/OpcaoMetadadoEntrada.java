package org.sakaiproject.apue.tool;

import java.util.List;

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
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "opcao_metadado_entrada")
@Configurable
public class OpcaoMetadadoEntrada {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_opcao")
    private Integer idOpcao;

	public Integer getIdOpcao() {
        return this.idOpcao;
    }

	public void setIdOpcao(Integer id) {
        this.idOpcao = id;
    }

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metadado", referencedColumnName = "id_metadado", nullable = false)
    private MetadadoEntrada idMetadado;

	@Column(name = "rotulo", length = 100)
    
    private String rotulo;

	@Column(name = "valor", length = 100)
    
    private String valor;

	public MetadadoEntrada getIdMetadado() {
        return idMetadado;
    }

	public void setIdMetadado(MetadadoEntrada idMetadado) {
        this.idMetadado = idMetadado;
    }

	public String getRotulo() {
        return rotulo;
    }

	public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

	public String getValor() {
        return valor;
    }

	public void setValor(String valor) {
        this.valor = valor;
    }

	public String toString() {
        return this.rotulo;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new OpcaoMetadadoEntrada().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOpcaoMetadadoEntradas() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OpcaoMetadadoEntrada o", Long.class).getSingleResult();
    }

	public static List<OpcaoMetadadoEntrada> findAllOpcaoMetadadoEntradas() {
        return entityManager().createQuery("SELECT o FROM OpcaoMetadadoEntrada o", OpcaoMetadadoEntrada.class).getResultList();
    }

	public static OpcaoMetadadoEntrada findOpcaoMetadadoEntrada(Integer idOpcao) {
        if (idOpcao == null) return null;
        return entityManager().find(OpcaoMetadadoEntrada.class, idOpcao);
    }

	public static List<OpcaoMetadadoEntrada> findOpcaoMetadadoEntradaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OpcaoMetadadoEntrada o", OpcaoMetadadoEntrada.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            OpcaoMetadadoEntrada attached = OpcaoMetadadoEntrada.findOpcaoMetadadoEntrada(this.idOpcao);
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
    public OpcaoMetadadoEntrada merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OpcaoMetadadoEntrada merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
