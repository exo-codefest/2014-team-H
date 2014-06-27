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
import org.exoplatform.service.taskManagement.entities.Task;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Date;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */
public class TaskService
{
   private NodeHierarchyCreator nodeCreator;
   private String taskRootNode;
    private RepositoryService repositoryService;
    private ProjectService projectService;

    public TaskService(RepositoryService repositoryService, ProjectService projectService){
        this.repositoryService = repositoryService;
        this.projectService = projectService;
    }

   public Task addTask(String projectName, String name , String affected, String desc, Date dueDate){
       Task task = null;
      try {
          Node pRoot = projectService.getProjectByName(projectName);
          long projectId = pRoot.getProperty("idProject").getLong();
         long newId = System.currentTimeMillis();
         Node pNode = pRoot.addNode(newId + "", "exo:task");
         pNode.setProperty("idTask", newId);
         pNode.setProperty("nameTask", name);
         pNode.setProperty("description", desc);
         pNode.setProperty("affected", affected);
         pNode.setProperty("dueDate", dueDate.toString());
         pRoot.save();
          task = new Task((int) projectId, name, dueDate, desc);
      } catch (Exception e) {

      }
       return task;
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
    protected Node getTaskRootNode() throws RepositoryException {
        SessionProvider sessionProvider = null;
        Node node;
        try {
            sessionProvider = SessionProvider.createSystemProvider();
            ManageableRepository currentRepo = repositoryService.getCurrentRepository();
            Session session = sessionProvider.getSession(currentRepo.getConfiguration().getDefaultWorkspaceName(), currentRepo);
            Node rootNode = session.getRootNode();
            if (!rootNode.hasNode(taskRootNode)) {
                node = rootNode.addNode(taskRootNode);
                rootNode.save();
            }
            else
            {
                node=rootNode.getNode(taskRootNode);
            }

        } finally {
        }
        return node;
    }

}
