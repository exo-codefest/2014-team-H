/*
 * Copyright (C) 2014 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.service.taskManagement.service;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.service.taskManagement.entities.Project;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;


/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */
public class ProjectService
{
private NodeHierarchyCreator nodeCreator;
private String taskRootNode="TaskManagement";

   public Project addProject(String pName, String teamLead){
      Project pr=null;
      Node root = getProjectRootNode();
      try {
         long newId = System.currentTimeMillis();
         Node node = root.addNode(newId + "", "exo:project");
         node.setProperty("idProject", newId);
         node.setProperty("teamLead", teamLead);
         node.setProperty("nameProject", pName);
         root.save();
         pr = new Project((int)newId,pName,teamLead);
      } catch (Exception e) {

      }
      return pr;
   }

   public List<Project> getAllProject()
   {
      List<Project> list = new ArrayList<Project>();
      Node root = getProjectRootNode();
      String statement = null;
      try
      {
         statement = "SELECT * FROM  exo:project WHERE jcr:path LIKE '" +  root.getPath() + "/%";
         Query query = root.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
         for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
            Node n = iter.nextNode();
            int id=(int)n.getProperty("idProject").getLong();
            String name=n.getProperty("nameProject").getString();
            String lead=n.getProperty("teamLead").getString();
            Project pr = new Project(id,name,lead);
            list.add(pr);
         }
      }
      catch (RepositoryException e)
      {
         e.printStackTrace();
      }

         return list;
   }

   private Node getProjectRootNode() {
      SessionProvider sessionProvider = getSystemSessionProvider();
      try {
         return nodeCreator.getPublicApplicationNode(sessionProvider).getNode(taskRootNode);
      } catch (PathNotFoundException e) {
         try {
            Node appNode = nodeCreator.getPublicApplicationNode(sessionProvider);
            Node ret = appNode.addNode(taskRootNode);
            appNode.save();
            return ret;
         } catch(Exception ex) {
            return null;
         }
      } catch (Exception e) {
         return null;
      }
   }

   public static SessionProvider getSystemSessionProvider() {
      String containerName = PortalContainer.getCurrentPortalContainerName();
      ExoContainer container = RootContainer.getInstance().getPortalContainer(containerName);
      SessionProviderService sessionProviderService = (SessionProviderService)container.getComponentInstanceOfType(SessionProviderService.class);
      return sessionProviderService.getSystemSessionProvider(null);
   }

}
