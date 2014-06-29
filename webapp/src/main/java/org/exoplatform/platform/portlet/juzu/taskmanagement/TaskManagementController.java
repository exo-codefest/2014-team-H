package org.exoplatform.platform.portlet.juzu.taskmanagement;

import juzu.Path;
import juzu.*;
import juzu.View;
import juzu.template.Template;
import java.util.*;
import javax.inject.Inject;
import org.exoplatform.commons.juzu.ajax.Ajax;
/**
 * @author <a href="rtouzi@exoplatform.com">rtouzi</a>
 */
public class TaskManagementController {

    @Inject
    @Path("task.gtmpl")
    Template listtask;

    @Inject
    @Path("newproject.gtmpl")
    Template newproject;

    @Inject
    @Path("project.gtmpl")
    Template listproject;

    @Inject
    @Path("newtask.gtmpl")
    Template newtask;

    @View
    public void index() {
        listproject.render();
    }

    @Ajax
    @Resource
    public void addproject() throws Exception {
        newproject.render();
    }

    @Ajax
    @Resource
    public void addtask() throws Exception {
        newtask.render();
    }

    @Ajax
    @Resource
    public void clickproject(String projectID) throws Exception {
        listtask.render();
    }


}
