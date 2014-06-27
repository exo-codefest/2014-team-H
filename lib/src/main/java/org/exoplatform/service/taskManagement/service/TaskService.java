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
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import java.util.Date;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */
public class TaskService
{
   private NodeHierarchyCreator nodeCreator;
   private String taskRootNode;

   public void addTask(String name , String affected, String desc, String dueDate){
      Node pRoot = getProjectRootNode();
      try {
         long newId = System.currentTimeMillis();
         Node pNode = pRoot.addNode(newId + "", "exo:task");
         pNode.setProperty("idTask", newId);
         pNode.setProperty("nameTask", name);
         pNode.setProperty("description", desc);
         pNode.setProperty("affected", affected);
         pNode.setProperty("dueDate", dueDate.toString());
         pRoot.save();
      } catch (Exception e) {

      }
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
