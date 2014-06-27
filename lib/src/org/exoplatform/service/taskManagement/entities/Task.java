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
package org.exoplatform.service.taskManagement.entities;

import java.util.Date;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class Task {

  private int idTask;
  private int projectId;
  private String nameTask;
  private String description;
   private Date dueDate;
  private  String affected;
    
  public int getId() {
    return idTask;
  }
  public void setId(int idTask) {
    this.idTask = idTask;
  }
  public int getProjectId() {
    return projectId;
  }
  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
  public String getName() {
    return nameTask;
  }
  public void setName(String nameTask) {
    this.nameTask = nameTask;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
   public Date getDueDate() {
      return dueDate;
   }
   public void setDueDate(Date dueDate) {
      this.dueDate = dueDate;
   }
  public String getMembers() {
    return affected;
  }
  public void setMembers(String affected) {
    this.affected = affected;
  }

}
