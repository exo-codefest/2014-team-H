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

import org.exoplatform.service.taskManagement.entities.Task;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import java.util.Date;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

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
   private static final Log LOG = ExoLogger.getLogger("taskManagement.service.TaskService");

   public TaskService(RepositoryService repositoryService, ProjectService projectService)
   {
      this.repositoryService = repositoryService;
      this.projectService = projectService;
   }

   public Task addTask(long projectId, String name, String desc)
   {
      return addTask(projectId, name, null, desc, null);
   }

   public Task addTask(long projectId, String name, String affected, String desc, Date dueDate)
   {
      Task task = null;
      try
      {
         Node pRoot = projectService.getProjectNodeById(projectId);
         Node tasks = null;
         if (pRoot.hasNode("tasksList"))
         {
            tasks=pRoot.getNode("tasksList");
         }
         else
         {
            tasks=pRoot.addNode("tasksList", "exo:tasksList");
            pRoot.save();
         }
         long newId = System.currentTimeMillis();
         Node pNode = tasks.addNode("Task" + newId + "", "exo:task");
         pNode.setProperty("idTask", newId);
         pNode.setProperty("nameTask", name);
         pNode.setProperty("description", desc);
         pNode.setProperty("affected", affected);
         //pNode.setProperty("dueDate", dueDate.toString());
         pRoot.save();
         task = new Task(projectId, newId ,name, dueDate, desc);
      }
      catch (Exception e)
      {
         LOG.error(e);
      }
      return task;
   }

   public Task getTaskById(int taskId)
   {
      Node tNode = getTaskNodeById(taskId);

      Task task = new Task();
      try
      {
         Node project = tNode.getParent().getParent();
         task.setProjectId(project.getProperty("idProject").getLong());
         task.setId(tNode.getProperty("idTask").getLong());
         task.setDescription(tNode.getProperty("description").getString());
         task.setName(tNode.getProperty("nameTask").getString());
         //task.setDueDate(n.getProperty("idTask").getDate());
         task.setAffected(tNode.getProperty("affected").getString());
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }
      return task;
   }

   public boolean removeTask(int idTask)
   {
      try
      {
         Node pNode = projectService.getTaskRootNode();
         Node tNode = this.getTaskNodeById(idTask);
         tNode.remove();
         pNode.save();
      }
      catch (Exception e)
      {
         LOG.error(e);
         return false;
      }
      return true;
   }

   private Node getTaskNodeById(long taskId)
   {

      Node pRoot = null;
      try
      {
         pRoot = projectService.getTaskRootNode();
         String statement = "SELECT * FROM exo:task WHERE jcr:path LIKE '" +
            pRoot.getPath() + "/%' AND idTask ='" + taskId + "'";

         Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
         for (NodeIterator iter = query.execute().getNodes(); iter.hasNext(); )
         {
            Node t = iter.nextNode();
            return t;
         }
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }

      return null;

   }
}
