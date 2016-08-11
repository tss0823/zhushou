/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.app) {
        YT.deploy.app = {};
    }

    var common = {

        route_callback: function () {
            console.log("app list after render call");
            //组件初始化之后
            //注册事件
            $("a[id='enterDeploy']").each(function (index, item) {
                $(this).click(function () {
                    var appId = $(item).attr("appId");
                    var model = $(item).attr("model");
                    var appName = $(item).attr("appName");
                    var data = {
                        title: "发布节点",
                        titleColor: $(item).attr("color"),
                        msTitle: " 【 " + appName + " 】 > " + model
                    };
                    data["model"] = model;
                    data["appName"] = appName;
                    YT.deploy.route("/host/getListByAppAndModel", {appId: appId, model: model}, "appHost.html", data);
                });
            });


            //app table row  server status check
            $("#tbContentApp").find("tr").each(function (index, item) {
                var appName = $(item).find("td:first").attr("appName");
                var serverAppStatus = appData.serverStatus[appName];
                if(!serverAppStatus){
                    return false;
                }
                var statAppObj = serverAppStatus["total"];
                if(!statAppObj){
                    return false;
                }

                //回显ui
                var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
                $tdServerStatusText.css("color",statAppObj.color);
                $tdServerStatusText.html(statAppObj.text);
                $tdServerStatusText.attr("title",statAppObj.text);

            });
            //end


        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.app = {}

})(window.YunTao);
