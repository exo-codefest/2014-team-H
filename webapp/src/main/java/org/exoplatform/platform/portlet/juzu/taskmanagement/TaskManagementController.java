package org.exoplatform.platform.portlet.juzu.taskmanagement;

import juzu.Path;
import juzu.View;
import juzu.template.Template;

import javax.inject.Inject;

/**
 * @author <a href="rtouzi@exoplatform.com">rtouzi</a>
 */
public class TaskManagementController {
    @Inject
    @Path("task.gtmpl")
    Template listtask;

    @View
    public void index() {
        listtask.render();
    }
}
