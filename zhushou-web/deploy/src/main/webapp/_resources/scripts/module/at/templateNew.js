/**
 *
 * Created by shan on 2016/4/7.
 */
(function(YT){
    if (!YT.deploy.templateNew) {
        YT.deploy.templateNew = {};
    }

    var common = {

        route_callback : function (d,data) {
            console.log("templateNew list after render call");
            //组件初始化之后
            var enums = YT.deploy.data.enums;

            //logModel
            var logModel = enums.logModel;
            YT.deploy.util.initEnumSelect(logModel,"model",data.model);

            //ruleType
            var ruleType = enums.paramRuleType;
            YT.deploy.util.initEnumSelect(ruleType,"ruleType");
            
            //dataType
            var dataType = enums.paramDataType;
            YT.deploy.util.initEnumSelect(dataType,"dataType");

            //注册事件
            $("#btnQuery").click(function(){
                var pageNum = 1;
                var pageSize = $("#pageSize").val();
                YT.deploy.templateNew.query(pageNum,pageSize);
            });


            $("#btnSelActive").click(function(){
                YT.deploy.templateNew.selActive();
            });
            
            $(document,"#activeList").on("click",function(e){
                var eventId = $(e.target).prop("id");
                if(typeof(eventId) == "undefined"){
                    eventId = "notDefinedId";
                }
                $(this).find("li[id='activeItem']").each(function(index,item){
                    if ($(item).find("div[id='" + eventId + "']").length > 0 || $(e.target).parent("#activeItemText").length > 0 ||
                    $(e.target).parent("#activeItemIcon").length > 0) {
                        if ($(item).find("#activeChildItem").css("display") == "none") {
                            $(item).find("#activeChildItem").show();
                        }else{
                            $(item).find("#activeChildItem").hide();
                        }
                    }
                    // debugger;
                    if(eventId == "itemRemove" || $(e.target).parents("#itemRemove").length > 0 ){
                        console.info("item remove call") ;
                    }
                    if(eventId == "itemChildRemove" || $(e.target).parents("#itemChildRemove").length > 0 ){
                        console.info("child item remove call") ;
                    }
                });


            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data,YT.deploy.templateNew.query);

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.templateNew = {

        query:function(pageNum,pageSize){
            var params = YT.deploy.util.getFormParams("#templateNewForm");
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {title:"模板列表"});
            YT.deploy.route("/atTemplate/list",params,"/template.html",ext_data);
        },


        selActive:function(params){
            var params = params || {};
            params["pageSize"] = 50;
            params["model"] = $("#model").val();
            YT.deploy.util.reqGet("/appLog/list", params, function (d) {
                var data = d.data;
                var param = $.extend(data,{title:"活动选择列表",model:$("#model").val()});
                param = $.extend(param,params);
                $.get("/at/activeSelect.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    if($(".bootbox-body").length > 0){
                       $(".bootbox-body").html(html);
                    }else{
                        bootbox.dialog({
                            message: html,
                            width:"800px",
                        });
                        $(".modal-dialog").prop("style","width:90%;height:600px")
                    }
                    YT.deploy.route_callback(d,data);
                });
            });
        },

    }

})(window.YunTao);
