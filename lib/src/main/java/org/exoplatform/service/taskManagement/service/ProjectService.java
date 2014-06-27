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

import org.exoplatform.service.taskManagement.entities.Project;
import org.exoplatform.service.taskManagement.entities.Task;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */
public class ProjectService
{
   private String taskRootNode = "TaskManagement";
   private RepositoryService repositoryService;
   private static final Log LOG = ExoLogger.getLogger("taskManagement.service.ProjectService");

   public ProjectService(RepositoryService repositoryService)
   {
      this.repositoryService = repositoryService;
   }


   public Project addProject(String pName, String teamLead)
   {
      Project pr = null;
      Node root = null;
      try
      {
         root = getTaskRootNode();
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }
      try
      {
         long newId = System.currentTimeMillis();
         Node node = root.addNode("Project" + newId, "exo:project");
         node.setProperty("idProject", newId);
         node.setProperty("teamLead", teamLead);
         node.setProperty("nameProject", pName);
         root.save();
         pr = new Project((int)newId, pName, teamLead);
      }
      catch (Exception e)
      {
         LOG.error(e);
      }
      return pr;
   }

   public List<Project> getAllProject()
   {
      List<Project> list = new ArrayList<Project>();
      Node root = null;
      try
      {
         root = getTaskRootNode();
      }
      catch (RepositoryException e)
      {
         e.printStackTrace();
      }
      String statement = null;
      try
      {
         statement = "SELECT * FROM  exo:project WHERE jcr:path LIKE '" + root.getPath() + "/%'";
         Query query = root.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
         for (NodeIterator iter = query.execute().getNodes(); iter.hasNext(); )
         {
            Node n = iter.nextNode();
            int id = (int)n.getProperty("idProject").getLong();
            String name = n.getProperty("nameProject").getString();
            String lead = n.getProperty("teamLead").getString();
            Project pr = new Project(id, name, lead);
            list.add(pr);
         }
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }

      return list;
   }

   public List<Task> getTasks(Project project)
   {

      List<Task> list = new ArrayList<Task>();
      Node pNode = getProjectNodeById(project.getId());
      try
      {
         for (NodeIterator iter = pNode.getNode("tasksList").getNodes(); iter.hasNext(); )
         {
            Task task = new Task();
            Node n = iter.nextNode();
            task.setProjectId((int)pNode.getProperty("idProject").getLong());
            task.setId((int)n.getProperty("idTask").getLong());
            task.setDescription(n.getProperty("idTask").getString());
            task.setName(n.getProperty("idTask").getString());
            //task.setDueDate(n.getProperty("idTask").getDate());
            task.setAffected(n.getProperty("idTask").getString());
            list.add(task);
         }
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }
      return list;

   }

   public Node getProjectByName(String projectName) throws RepositoryException
   {

      Node root = getTaskRootNode();
      try
      {
         Node node = root.getNode(projectName);
         return node;
      }
      catch (Exception e)
      {
         LOG.error(e);
      }
      return null;
   }

   public boolean removeProject(int id)
   {
      try
      {
         Node pRoot = getTaskRootNode();
         Node pNode = getProjectNodeById(id);
         pNode.remove();
         pRoot.save();
      }
      catch (Exception e)
      {
         LOG.error(e);
         return false;
      }
      return true;
   }

   protected Node getTaskRootNode() throws RepositoryException
   {
      SessionProvider sessionProvider = null;
      Node node = null;
      try
      {
         sessionProvider = SessionProvider.createSystemProvider();
         ManageableRepository currentRepo = repositoryService.getCurrentRepository();
         Session session = sessionProvider.getSession(currentRepo.getConfiguration().getDefaultWorkspaceName(), currentRepo);
         Node rootNode = session.getRootNode();
         if (!rootNode.hasNode(taskRootNode))
         {
            node = rootNode.addNode(taskRootNode);
            rootNode.save();
         }
         else
         {
            node = rootNode.getNode(taskRootNode);
         }

      }
      catch (Exception e)
      {
         LOG.error(e);
      }
      return node;
   }

   public Node getProjectNodeById(int id)
   {
      Node pRoot = null;
      try
      {
         pRoot = getTaskRootNode();
         String statement = "SELECT * FROM exo:project WHERE jcr:path LIKE '" +
            pRoot.getPath() + "/%' AND idProject='" + id + "'";

         Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
         for (NodeIterator iter = query.execute().getNodes(); iter.hasNext(); )
         {
            Node p = iter.nextNode();
            return p;
         }
      }
      catch (RepositoryException e)
      {
         LOG.error(e);
      }

      return null;
   }

}
