var store = new Persist.Store('My Data Store');


function createProjectTable(){
   $.getJSON('/rest/taskmanagement/project/getallprojects', function(list) {
          if (list.projects.length <1){
                             $(".projectList").html("<div style='text-align: center;'>No projects</div>");

                         }else{

                             $.each(list.projects, function(i, project){

                                 var link = "";


                                 link += "<li class='project clearfix' style='margin-left=10%;' id='"+project.projectId+"'>"

                                 link += "<div class='detail' > <div class='name'> <a style='margin-left=10%;' href='#' onclick='return false'>"+project.nameProject+"</a> </div>";

                                 link += "<div class='peopleConnection' style='margin-left=10%;'>Project Lead : "+project.teamLead+"  </div></li>";

                                 $("#projects").append(link);
 });
                }





            });
            $('.jz').on("click", '.addproject', function(){
                            $('#content').jzLoad(
                                "TaskManagementController.addproject()");
                        });

                        $('.jz').on("click", '.project', function(){

                            $('#content').jzLoad(
                                "TaskManagementController.clickproject()");
                                
                                store.set('saved_data', this.id);

                        });

                        $('.jz').on("click", '.addtask', function(){
                            $('#content').jzLoad(
                                "TaskManagementController.addtask()");
                        });

                        $('.jz').on("click", '.CancelButton', function(){

                                alert("not yet implemented");
                        });





}
$('.jz').on("click", '.addproj', function(){

                               var projectname = $('#inputEmail5').val();
                                               // make a POST ajax call
                                               $.ajax({
                                                   type: "GET",
                                                   url: "/rest/private/taskmanagement/project/addproject", // set your URL here
                                                   data: {
                                                   name: projectname, // send along this data (can add more data separated by comma)
                                                   lead: "eXo-Manager"
                                               },

                                               }).done(function( response ) {
                                                   alert(projectname);
                                               });
                        });
$(document).ready(function () {
$("#content").show();
$("#taskManagement").show();
createProjectTable();
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
