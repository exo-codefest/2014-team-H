(function ($) {

    var addproject;
    var remove ;
    var edit;
    var addtask;
    $(".bundle").each(function() {
        addproject = $(this).data("addproject");
        remove = $(this).data("remove");
        edit = $(this).data("edit");
        addtask = $(this).data("addtask");
    });

    return {
        initTaskManagement: function() {
            $.getJSON("taskmanagmentservice", function(list){

                if (list.items.length <0){
                    $(".projectList").html("<div>No projects</div>");

                }else{










                    $.each(list.items, function(i, item){

                        var link = "";


                        link += "<li class='project clearfix' id='"+item.projectId+"'>"

                        link += "<div style='display:none;' class='projectAction' ><a class='ignore' href='#' onclick='return false'><i class='uiIconClose'></i></a></div>";

                        link += "</div></li>";

                        $("#projects").append(link);

                        $("#"+item.projectId).mouseover(function(){
                            var $item = $(this);
                            $item.find(".projectName").addClass("actionAppears");
                            $item.find(".projectAction").show();
                        });
                        $("#"+item.projectId).mouseout(function(){
                            var $item = $(this);
                            $item.find(".projectName").removeClass("actionAppears");
                            $item.find(".projectAction").hide();
                        });



                        $("#"+item.projectId+" a.ignore").live("click", function(){
                            //$.getJSON("/rest/homepage/intranet/people/contacts/ignore/"+item.suggestionId, null);
                            if($("#projects").children().length == 1) {
                                $("#project").fadeOut(500, function () {
                                    $("#"+item.relationId).remove();
                                    $("#project").hide();



                                });
                            }
                            else {
                                $("#"+item.projectId).fadeOut(500, function () {
                                    $("#"+item.projectId).remove();
                                    $('#projects li:hidden:first').fadeIn(500, function() {});

                                });
                            }
                        });

                    });
                }





            });



            $('.jz').on("click", '.addproject', function(){
                $('#taskManagement').jzLoad(
                    "TaskManagementController.addproject()");
            });

            $('.jz').on("click", '.project', function(){
                $('#taskManagement').jzLoad(
                    "TaskManagementController.clickproject()");
            });

            $('.jz').on("click", '.addtask', function(){
                $('#taskManagement').jzLoad(
                    "TaskManagementController.addtask()");
            });

            $('.jz').on("click", '.CancelButton', function(){
                $('#taskManagement').jzLoad(
                    "TaskManagementController.index()");
            });


        }
    };
})($);
