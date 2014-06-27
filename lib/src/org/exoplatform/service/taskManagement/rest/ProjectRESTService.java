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

import org.exoplatform.service.taskManagement.entities.Project;
import org.exoplatform.service.taskManagement.service.ProjectService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: Body Header.java 34027 2009-07-15 23:26:43Z aheritier $
 */


/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014
 */
@Path("/taskManagement")
public class ProjectRESTService implements ResourceContainer
{
   private ProjectService projectService;


   public ProjectRESTService(ProjectService projectService) {
      this.projectService = new ProjectService();
   }

   @POST
   @Path("/addproject")
   @Produces(MediaType.TEXT_HTML)
   @RolesAllowed("users")
   public Response createProject(@PathParam("name") String name, @PathParam("lead") String lead) {
      Project proj =projectService.addProject(name,lead);
      return Response.ok(proj.toString()).build();
   }


   @GET
   @Path("/getallprojects")
   @Produces("application/json")
   @RolesAllowed("users")
   public Response getAllProjects() {

      JSONArray jsonArray = new JSONArray();
      JSONObject jsonGlobal = new JSONObject();

      try
      {
         for (Project project : projectService.getAllProject()) {
            JSONObject json = new JSONObject();
            json.put("projectId",project.getId());
            json.put("nameProject",project.getName());
            json.put("teamLead",project.getLead());
            jsonArray.put(json);
         }
         jsonGlobal.put("totalNbProjects",  projectService.getAllProject().size());
         jsonGlobal.put("projects", jsonArray);
      }
      catch (JSONException e)
      {
         e.printStackTrace();
      }

      return Response.ok(jsonGlobal.toString(), MediaType.APPLICATION_JSON).build();

   }




}
