/**
 *
 * Created by shan on 2016/4/7.
 */
(function(YT){
    if (!YT.deploy.template) {
        YT.deploy.template = {};
    }

    var common = {

        route_callback : function (d) {
            console.log("template list after render call");
            //组件初始化之后
            //注册事件
            $("a[id='enterView']").each(function(index,item){
                $(this).click(function(){
                    // var appId = $(item).attr("appId");
                    // var model = $(item).attr("model");
                    // var appName = $(item).attr("appName");
                    // var data = {title:"发布节点",titleColor:$(item).attr("color"),msTitle:" 【 "+appName+" 】 > "+model};
                    // data["model"] = model;
                    // data["appName"] = appName;
                    // YT.deploy.route("/host/getListByAppAndModel",{appId:appId,model:model},"appHost.html",data);
                });
            });

            $("#btnQuery").click(function(){
                var pageNum = 1;
                var pageSize = $("#pageSize").val();
                YT.deploy.template.query(pageNum,pageSize);
            });

            $("#btnNewTemplate").click(function(){
                YT.deploy.template.newTemplate();
            });

            //enterView
            $("a[id='enterView']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.template.openDetailDialog(id);
            });

            //分页信息init
            YT.deploy.util.paginationInit(d.data,YT.deploy.template.query);

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.template = {

        query:function(pageNum,pageSize){
            var params = YT.deploy.util.getFormParams("#templateForm");
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {title:"模板列表"});
            YT.deploy.route("/atTemplate/list",params,"/template.html",ext_data);
        },

        newTemplate:function(){
            YT.deploy.route("/atTemplate/list",{},"/at/templateNew.html",{title:"创建模板"});
        },

        openDetailDialog:function(id){
            var params = {id:id};
            YT.deploy.util.reqGet("/template/getDetail", params, function (d) {
                var data = d.data;
                //debugger;
                $.get("/viewDetail.html", function (source) {
                    var render = template.compile(source);
                    var html = render(data);
                    bootbox.dialog({
                        message: html,
                        width:"800px",
                    });
                    $(".modal-dialog").prop("style","width:90%")

                });
            });

        },

    }

})(window.YunTao);
