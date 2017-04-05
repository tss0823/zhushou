/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.appFront) {
        YT.deploy.appFront = {};
    }

    var common = {

        route_callback: function () {
            console.log("appFront list after render call");
            //组件初始化之后

            //注册事件,控制台打印消息
            YT.deploy.eventProcess.addListener("deploy_script",function(msgObj){
                YT.deploy.appFront.writeMsg(msgObj.data+"\r\n", true);
            });
            //end

            //注册事件
            $("button[id='btnDeploy']").each(function (index, item) {
                $(this).click(function () {
                    var appName = $(item).attr("appName");
                    var model = $(item).attr("model");
                    var deployType = $(item).attr("deployType");
                    var deployVersion = $(item).attr("deployVersion");
                    var env = model == "test" ? "测试": "<strong style='color:red'>线上</strong>";

                    var html = "<span class='bigger-110'>发布环境："+env+"<br/><br/>发布版本号：<input id='txtVersion' value='"+deployVersion+"' /><br/><br/>是否强制更新： <input id='chkForceUpdate'  type='checkbox' class='ace ace-switch ace-switch-5'/><span class='lbl'></span>";
                    var logInputHtml = "<br/><br/>更新日志：<textarea id='txtaUpdateLog' style='width:460px;height:120px'></textarea>"
                    html += logInputHtml;
                    html += "<span>";
                    bootbox.dialog({
                        title : "发布有风险！！！请仔细核对好数据，然后进行发布！",
                        message: html,
                        width:"800px",
                        buttons:
                            {
                                "success" :
                                    {
                                        "label" : "<i class='icon-ok'></i> 确认",
                                        "className" : "btn-sm btn-success",
                                        "callback": function() {
                                            if(!confirm("您确认要发布【 "+appName+" 】 > "+model+" 吗？")){
                                                return;
                                            }
                                            YT.deploy.appFront.writeMsg("执行发布开始,请稍等...\r\n",true);
                                            var version = $("#txtVersion").val();
                                            var checked = $("#chkForceUpdate").prop("checked");
                                            var forceUpdate = checked ? 1 : 0;
                                            var updateLog = $("#txtaUpdateLog").val();
                                            if(!updateLog){
                                                alert("请输入更新日志");
                                                return;
                                            }
                                            var param = {appName: appName, model: model, version: version,type:deployType,forceUpdate:forceUpdate,type:deployType,updateLog:updateLog};
                                            YT.deploy.util.reqPost("/deploy/deployFront", param, function (d) {
                                                //打印消息
                                                // loadTipsObj.close();
                                                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
                                            });
                                        }
                                    },
                                "button" :
                                    {
                                        "label" : "放弃",
                                        "className" : "btn-sm"
                                    }
                            }

                    });
                    // var data = {
                    //     title: "发布节点",
                    //     titleColor: $(item).attr("color"),
                    //     msTitle: " 【 " + appName + " 】 > " + model
                    // };
                    // data["model"] = model;
                    // data["appName"] = appName;
                    // YT.deploy.route("/host/getListByappFrontAndModel", {appId: appId, model: model}, "appHost.html", data);
                });
            });


            //app table row  server status check
            // $("#tbContentApp").find("tr").each(function (index, item) {
            //     var appName = $(item).find("td:first").attr("appName");
            //     var serverAppStatus = appData.serverStatus[appName];
            //     if(!serverAppStatus){
            //         return false;
            //     }
            //     var statAppObj = serverAppStatus["total"];
            //     if(!statAppObj){
            //         return false;
            //     }
            //
            //     //回显ui
            //     var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
            //     $tdServerStatusText.css("color",statAppObj.color);
            //     $tdServerStatusText.html(statAppObj.text);
            //     $tdServerStatusText.attr("title",statAppObj.text);
            //
            // });
            //end


        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.appFront = {
        writeMsg: function (msg, flag) {
            if (!msg) {
                return;
            }
            if (typeof(flag) != "undefind" && flag) {
                msg = $("#result").val() + msg;
            }
//				msg+="\r\n";
            $("#result").val(msg);
            if(typeof(result) != "undefined"){
                result.scrollTop = result.scrollHeight;
            }
        },
    }

})(window.YunTao);
