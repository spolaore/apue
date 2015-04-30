package org.sakaiproject.apue.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;


public final class HibernateAwareObjectMapper extends ObjectMapper {
	
	private static final long serialVersionUID = 1L;

	public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
    }
	
}
