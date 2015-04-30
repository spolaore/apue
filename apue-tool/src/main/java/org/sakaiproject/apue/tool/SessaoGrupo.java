package org.sakaiproject.apue.tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "sessao_grupo")
@Configurable
public class SessaoGrupo {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new SessaoGrupo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSessaoGrupoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SessaoGrupo o", Long.class).getSingleResult();
    }

	public static List<SessaoGrupo> findAllSessaoGrupoes() {
        return entityManager().createQuery("SELECT o FROM SessaoGrupo o", SessaoGrupo.class).getResultList();
    }

	public static SessaoGrupo findSessaoGrupo(SessaoGrupoPK id) {
        if (id == null) return null;
        return entityManager().find(SessaoGrupo.class, id);
    }

	public static List<SessaoGrupo> findSessaoGrupoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SessaoGrupo o", SessaoGrupo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static List<SessaoGrupo> findSessaoAbertaByGrupos(
			Set<String> gruposUsuario) {
			if(gruposUsuario.size() == 0) {
				return new ArrayList<SessaoGrupo>();
			}
			
	        TypedQuery<SessaoGrupo> query = entityManager().createQuery("select sessaoGrupo from SessaoGrupo as sessaoGrupo " +
	        		" inner join fetch sessaoGrupo.idSessao as sessao " +
	        		" where sessao.fim > :agora and sessaoGrupo.id.idGrupo IN :gruposUsuario order by sessao.inicio", SessaoGrupo.class);
	        		query.setParameter("agora", Calendar.getInstance());
	        		query.setParameter("gruposUsuario", gruposUsuario);
	        		return query.getResultList();
	}
	
	public static Set<String> findGruposByIdSessao(Integer idSessao) {
		TypedQuery<SessaoGrupo> query = entityManager().createQuery("select sessaoGrupo from SessaoGrupo as sessaoGrupo where sessaoGrupo.id.idSessao = :idSessao", SessaoGrupo.class);
        		query.setParameter("idSessao", idSessao);
        		
        		 List<SessaoGrupo> sessaoGrupos = query.getResultList();
        		 List<String> gruposId = new ArrayList<String>();
        		 for(SessaoGrupo sessaoGrupo : sessaoGrupos) {
        			 gruposId.add(sessaoGrupo.getId().getIdGrupo());
        		 }
        		 
        		 return new HashSet<String>(gruposId);
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
            SessaoGrupo attached = SessaoGrupo.findSessaoGrupo(this.id);
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
    public SessaoGrupo merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SessaoGrupo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@EmbeddedId
    private SessaoGrupoPK id;

	public SessaoGrupoPK getId() {
        return this.id;
    }

	public void setId(SessaoGrupoPK id) {
        this.id = id;
    }

	@ManyToOne
    @JoinColumn(name = "id_sessao", referencedColumnName = "id_sessao", nullable = false, insertable = false, updatable = false)
    private Sessao idSessao;

	public Sessao getIdSessao() {
        return idSessao;
    }

	public void setIdSessao(Sessao idSessao) {
        this.idSessao = idSessao;
    }

	public String toString() {
        return this.getId().getIdGrupo();
    }

}
