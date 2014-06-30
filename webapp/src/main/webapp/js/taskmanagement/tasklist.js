

function createTaskTable(projectId){
   $.getJSON('/rest/taskmanagement/task/getalltask?projectid='+projectId, function(list) {
          if (list.tasks.length <1){
                             $(".taskList").html("<div style='text-align: center;'>** No tasks **</div>");

                         }else{

                             $.each(list.tasks, function(i, task){

                                 var link = "";


                                 link += "<li class='task clearfix'  id='"+task.idTask+"'>"

                                 link += "<div class='detail' > <div class='name'> <a style='margin-left=10%;' href='#' onclick='return false'>"+task.nameTasks+"</a> </div>";

                                 link += "<div class='peopleConnection' style='margin-left=10%;'>Description : "+task.description+"</div></li>";

                                 $("#tasks").append(link);
 });
                }





            });
             $('.jz').on("click", '.addtask', function(){
                                        $('#content2').jzLoad(
                                            "TaskManagementController.addtask()");
                                    });
             $('.jz').on("click", '.CancelButton', function(){
                                       
                                            alert("not yet implemented");
                                                            });
            }



$(document).ready(function () {
$("#content2").show();
$("#taskManagment").show();
store.get('saved_data', function(ok, val) {
  if (ok)
    createTaskTable(val);
});


});

var re = /^(.*)\(\)$/;
$.fn.jz = function() {
    return this.closest(".jz");
};
$.fn.jzURL = function(mid) {
    return this.
        jz().
        children().
        filter(function() { return $(this).data("method-id") == mid; }).
        map(function() { return $(this).data("url"); })[0];
};
$.fn.jzLoad = function(url, data, complete) {
    var match = re.exec(url);
    if (match != null) {
        var repl = this.jzURL(match[1]);
        url = repl || url;
    }
    if (typeof data === "function") {
        complete = data;
        data = null;
    }
    return this.load(url, data, complete);
};
