/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.appList) {
        YT.deploy.appList = {};
    }

    var common = {

        formId: "appListForm",

        route_callback: function (d,data) {
            console.log("appList list after render call");
            //组件初始化之后

            YT.deploy.util.reqGet("/data/projectList",{},function(d){
                var projectList = d.data;
                YT.deploy.util.initSelect(projectList,"id","name","projectId",data.projectId);
            });

            //注册事件
            $("a[id='enterDeploy']").each(function (index, item) {
                $(this).click(function () {
                    var appId = $(item).attr("appId");
                    var model = $(item).attr("model");
                    var projectId = $("#projectId").val();
                    var appName = $(item).attr("appName");
                    var data = {
                        title: "发布节点",
                        titleColor: $(item).attr("color"),
                        msTitle: " 【 " + appName + " 】 > " + model
                    };
                    data["model"] = model;
                    data["appName"] = appName;
                    data["appId"] = appId;
                    YT.deploy.route("/host/getListByAppAndModel", {appId: appId, model: model,appName:appName,projectId:projectId}, "appHost.html", data);
                });
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.appList.query(pageNum);
            });

            //appList table row  server status check
            // $("#tbContentAppList").find("tr").each(function (index, item) {
            //     var appListName = $(item).find("td:first").attr("appListName");
            //     var serverAppListStatus = appListData.serverStatus[appListName];
            //     if(!serverAppListStatus){
            //         return false;
            //     }
            //     var statAppListObj = serverAppListStatus["total"];
            //     if(!statAppListObj){
            //         return false;
            //     }
            //
            //     //回显ui
            //     var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
            //     $tdServerStatusText.css("color",statAppListObj.color);
            //     $tdServerStatusText.html(statAppListObj.text);
            //     $tdServerStatusText.attr("title",statAppListObj.text);
            //
            // });
            //end


        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.appList = {
        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "appListForm";
            var params = YT.deploy.util.getFormParams("#appListForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"应用列表"});
            YT.deploy.route("/app/list", params, "/appList.html", ext_data);
        },
    }

})(window.YunTao);
