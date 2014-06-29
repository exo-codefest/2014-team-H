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
package org.exoplatform.service.taskManagement.rest;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */

import org.exoplatform.service.taskManagement.entities.Task;
import org.exoplatform.service.taskManagement.service.ProjectService;
import org.exoplatform.service.taskManagement.service.TaskService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/taskmanagement/task")
public class TaskRESTService implements ResourceContainer
{
   private TaskService taskService;
   private ProjectService projectService;
   private static final Log LOG = ExoLogger.getLogger("taskManagement.service.TaskRESTService");


   public TaskRESTService(TaskService taskService, ProjectService projectService)
   {
      this.taskService = taskService;
      this.projectService = projectService;
   }

   @GET
   @Path("/addtask")
   @Produces(MediaType.TEXT_HTML)
   @RolesAllowed("users")
   public Response createTask(@QueryParam("projectId") long projectId, @QueryParam("taskName") String taskName
      , @QueryParam("desc") String desc)
   {

      Task t = taskService.addTask(projectId, taskName, desc);
      return Response.ok(t.toString()).build();
   }

   @GET
   @Path("/getalltask")
   @Produces("application/json")
   @RolesAllowed("users")
   public Response getAllTask(@QueryParam("projectid") long projectId)
   {

      JSONArray jsonArray = new JSONArray();
      JSONObject jsonGlobal = new JSONObject();

      try
      {
         for (Task task : projectService.getTasks(projectId))
         {
            JSONObject json = new JSONObject();
            json.put("idProject", task.getProjectId());
            json.put("idTask", task.getId());
            json.put("nameTasks", task.getName());
            json.put("description", task.getDescription());
            json.put("affected", task.getAffected());
            json.put("dueDate", task.getDueDate());
            jsonArray.put(json);
         }
         jsonGlobal.put("totalNbTasks", projectService.getTasks(projectId).size());
         jsonGlobal.put("tasks", jsonArray);
      }
      catch (JSONException e)
      {
         LOG.error(e);
      }

      return Response.ok(jsonGlobal.toString(), MediaType.APPLICATION_JSON).build();

   }

}
