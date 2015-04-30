package org.sakaiproject.apue.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

/**
 * Implementation of {@link SakaiProxy}
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class SakaiProxyImpl implements SakaiProxy {

	private static final Logger log = Logger.getLogger(SakaiProxyImpl.class);

	/**
 	* {@inheritDoc}
 	*/
	public String getCurrentSiteId(){
		return toolManager.getCurrentPlacement().getContext();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getCurrentUserId() {
		return sessionManager.getCurrentSessionUserId();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getCurrentUserDisplayName() {
	   return userDirectoryService.getCurrentUser().getDisplayName();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public boolean isSuperUser() {
		return securityService.isSuperUser();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public void postEvent(String event,String reference,boolean modify) {
		eventTrackingService.post(eventTrackingService.newEvent(event,reference,modify));
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getSkinRepoProperty(){
		return serverConfigurationService.getString("skin.repo");
		
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getToolSkinCSS(String skinRepo){
		
		String skin = siteService.findTool(sessionManager.getCurrentToolSession().getPlacementId()).getSkin();			
		
		if(skin == null) {
			skin = serverConfigurationService.getString("skin.default");
		}
		
		return skinRepo + "/" + skin + "/tool.css";
	}
	
	public boolean hasPermission(String action) {
		String reference = this.siteService.siteReference(getCurrentSiteId());
		return this.securityService.unlock(getCurrentUserId(), action, reference);
	}
	
	public void registerFunction(String function) {
		functionManager.registerFunction(function);
	}
	
	public Set<String> getAllGroups() {
		Collection<Group> groups;
		try {
			Site site = siteService.getSite(getCurrentSiteId());
			groups = site.getGroups();
		} catch (IdUnusedException e) {
			groups = new ArrayList<Group>();
			e.printStackTrace();
		}
		Set<String> groupsTitle = groupsToString(groups);
		
		return groupsTitle;
	}
	
	public Set<String> getUserGroups() {
		Collection<Group> groups;
		try {
			Site site = siteService.getSite(getCurrentSiteId());
			groups = site.getGroupsWithMember(getCurrentUserId());
		} catch (IdUnusedException e) {
			groups = new ArrayList<Group>();
			e.printStackTrace();
		}
		
		Set<String> groupsTitle = groupsToString(groups);
		
		return groupsTitle;
	}
	
	public Collection<String> getUsersInGroups(Set<String> groupIds) {
		Collection<String> users = new HashSet<String>();
		
		try {
			Site site = siteService.getSite(getCurrentSiteId());
			Collection<Group> groups = site.getGroups();
			for (Group group : groups) {
				if(groupIds.contains(group.getTitle())) {
					List<User> groupUsers = this.userDirectoryService.getUsers(group.getUsers());
					for(User user : groupUsers) {
						users.add(user.getDisplayName());
					}
				}
			}
		} catch (IdUnusedException e) {
			users = new HashSet<String>();
			e.printStackTrace();
		}
		
		return users;
	}

	private Set<String> groupsToString(Collection<Group> groups) {
		Set<String> groupsTitle = new HashSet<String>(groups.size());
		for (Group group : groups) {
			groupsTitle.add(group.getTitle());
		}
		
		return groupsTitle;
	}
	
	
	/**
	 * init - perform any actions required here for when this bean starts up
	 */
	public void init() {
		registerFunction(APUE_LAB_ADMIN);
		log.info("init");
	}
	
	@Getter @Setter
	private ToolManager toolManager;
	
	@Getter @Setter
	private SessionManager sessionManager;
	
	@Getter @Setter
	private UserDirectoryService userDirectoryService;
	
	@Getter @Setter
	private SecurityService securityService;
	
	@Getter @Setter
	private EventTrackingService eventTrackingService;
	
	@Getter @Setter
	private ServerConfigurationService serverConfigurationService;
	
	@Getter @Setter
	private SiteService siteService;
	
	@Getter @Setter
	private FunctionManager functionManager;
	
	@Getter @Setter
	private AuthzGroupService groupService;
	
}
