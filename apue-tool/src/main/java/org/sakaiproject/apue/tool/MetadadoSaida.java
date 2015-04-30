package org.sakaiproject.apue.tool;

import java.util.List;
import java.util.Set;

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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "metadado_saida")
@Configurable
public class MetadadoSaida {

	@Id
    @Column(name = "id_metadado")
    private Integer idMetadado;

	public Integer getIdMetadado() {
        return this.idMetadado;
    }

	public void setIdMetadado(Integer id) {
        this.idMetadado = id;
    }

	@MapsId
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metadado", nullable = false, insertable = false, updatable = false)
    private Metadado metadado;


	public Metadado getMetadado() {
        return metadado;
    }

	public void setMetadado(Metadado metadado) {
        this.metadado = metadado;
    }

	public String toString() {
		return this.getMetadado().getRotulo();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new MetadadoSaida().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMetadadoSaidas() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MetadadoSaida o", Long.class).getSingleResult();
    }

	public static List<MetadadoSaida> findAllMetadadoSaidas() {
        return entityManager().createQuery("SELECT o FROM MetadadoSaida o", MetadadoSaida.class).getResultList();
    }

	public static MetadadoSaida findMetadadoSaida(Integer idMetadado) {
        if (idMetadado == null) return null;
        return entityManager().find(MetadadoSaida.class, idMetadado);
    }

	public static List<MetadadoSaida> findMetadadoSaidaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MetadadoSaida o", MetadadoSaida.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static List<MetadadoSaida> findMetadadoSaidaByExperimento(Integer idExperimento) {
        if (idExperimento == null) return null;
        TypedQuery<MetadadoSaida> query = entityManager().createQuery("select saida from MetadadoSaida as saida inner join fetch saida.metadado as metadado inner join fetch metadado.idExperimento as experimento where experimento.idExperimento = :idExperimento ", MetadadoSaida.class);
        		query.setParameter("idExperimento", idExperimento);
        		return query.getResultList();
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
            MetadadoSaida attached = MetadadoSaida.findMetadadoSaida(this.idMetadado);
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
    public MetadadoSaida merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MetadadoSaida merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
