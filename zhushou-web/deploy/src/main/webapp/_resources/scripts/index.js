/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.index) {
        YT.deploy.index = {};
    }

    // var common = {
    //
    // }
    // $.extend(YT.deploy, common);


    YT.deploy.index = {
        initData:function(){
            

            //获取系统枚举值
            $.ajax({
                url: "/data/enums",
                async: false,
                success: function (d) {
                    var enums = {enums: d.data};
                    $.extend(YT.deploy.data, enums);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });



            //获取系统列表值
            // $.ajax({
            //     url: "/data/appDataList",
            //     async: false,
            //     success: function (d) {
            //         var enums = {appData: d.data};
            //         $.extend(YT.deploy.data, enums);
            //     },
            //     error: function (XMLHttpRequest, textStatus, errorThrown) {
            //         alert(textStatus);
            //     }
            // });
            //end
            appData.serverStatus = {};  //服务器监控对象

            //初始化权限资源
            var authResList = appData.authResList;
            var authMap = {};
            for(var i = 0; i < authResList.length; i++){
                var authRes = authResList[i]
                var authResChildList = authRes.childList;
                var child = {};
                if(authResChildList.length > 0){
                    for(var x = 0; x < authResChildList.length; x++){
                        var authResChild = authResChildList[x];
                        child[authResChild.actionId] = authResChild;
                    }
                }
                authRes["child"] = child;
                authMap[authRes.actionId] = authRes;
            }

            // debugger;
            appData.authMap = authMap;

            //end

            $.extend(YT.deploy.data, appData);
            var nickName = YT.deploy.data.user.nickName;
            $("#nickName").html(nickName);



        },

        activeMenu: null,  //激活菜单

        renderInit: function () {
            console.log("index list after render call");
            //组件初始化之后

            //初始化公共数据
            YT.deploy.index.initData();
            //end
            
            YT.deploy.index.activeMenu = $("#enterAppList");
            //注册事件
            
            var authResList = appData.authResList;
            for(var i = 0; i < authResList.length; i++){
                var authRes = authResList[i];
                var actionId = authRes.actionId;
                $("#"+actionId).click(function () {
                    var authRes = appData.authMap[$(this).attr("id")];
                    //cookie 拿 userData
                    var queryParams = YT.deploy.userDataProcess.getValueMap(authRes.tplUrl+"_queryData");
                    queryParams = queryParams || {};
                    //end
                    // debugger;
                    var extData = YT.deploy.userDataProcess.getValueMap(authRes.tplUrl+"_extData");
                    // extData = {tp_title:authRes.name,authRes:authRes};
                    extData = $.extend(extData,{tp_title: authRes.name,authRes:authRes});
                    YT.deploy.route(authRes.url, queryParams, authRes.tplUrl,extData);
                    $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
                    $(this).parent("li").addClass("active");
                    YT.deploy.index.activeMenu = this;

                });
            }
            

            $("#logout").click(function () {
                YT.deploy.util.reqGet("/user/logout", {}, function (d) {
                    $(document.body).addClass("login-layout");
                    YunTao.deploy.routeIndex("/login.html", appData);
                    // window.location = "/login.html";
                });

            });


            $("#btnHome").click(function () {
                YT.deploy.routeStackProcess.home();
            });
            $("#btnRefresh").click(function () {
                YT.deploy.routeStackProcess.refresh();
            });


            
            //注册事件,发布状态
            YT.deploy.eventProcess.addListener("monitor_status",function(msgObj){
                var msg = msgObj.data;
                var color = "red";
                if(msg && msg.length < 4){
                    color = "green";
                }
                $("#deployStatus").css("color",color);
                $("#deployStatus").html(" "+msg);
            });
            //end

            //注册服务状态监控事件
            YT.deploy.eventProcess.addListener("server_status_check", function (msgObj) {
                var dataObj = JSON.parse(msgObj.data);
                //app table row
                $("#tbContentApp").find("tr").each(function (index, item) {
                    var appName = $(item).find("td:first").attr("appName");
                    var hostStatusMsg = dataObj[appName];
                    var errArray = [];
                    var hostSize = 0;
                    // debugger;
                    for(var hostName in hostStatusMsg){
                        var hostStatus = hostStatusMsg[hostName];
                        if(!hostStatus["success"]){
                            errArray.push(hostStatus["message"]);
                        }
                        hostSize++;
                    }
                    var color = "green";
                    var text = "OK";
                    if(typeof(hostStatusMsg) != "undefined" && errArray.length == Object.keys(hostStatusMsg).length){
                        color = "red"; //all failed
                        text = "ERROR";
                    }else if(errArray.length > 0){
                        color = "black";
                        text = "ERROR";
                    }else{
                        text = "OK";
                    }
                    var errMsg = errArray.join("\r\n");
                    //set to appData
                    var statObj = appData.serverStatus[appName] || {};
                    statObj["total"] = {color:color,text:text,error:errMsg};
                    appData.serverStatus[appName] = statObj;
                    //end
                    
                    //回显ui
                    var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
                    $tdServerStatusText.css("color",color);
                    $tdServerStatusText.html("<strong>"+text+"</strong>");
                    $tdServerStatusText.attr("title",errMsg);
                });
                //end

                //host table row
                // debugger;
                var appName = $("#appName").val();
                var hostStatusMsg = dataObj[appName];
                $("#tbContentHost").find("tr").each(function (index, item) {
                    // debugger;
                    var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
                    var hostName = $tdServerStatusText.attr("data");
                    var hostStatus = hostStatusMsg[hostName];
                    var color = "green";
                    var text = "OK";
                    if(!hostStatus["success"]){
                        text = "ERROR";
                        color = "red"
                    }else{
                        text = "OK";
                        color = "green";
                    }
                    var errMsg = hostStatusMsg["message"];
                    //set to appData

                    // var statObj = {};
                    if(!appData.serverStatus[appName] ){
                        appData.serverStatus[appName] = {};
                    }
                    var statObj = {color:color,text:text,error:errMsg};
                    appData.serverStatus[appName][hostName] = statObj;
                    //end
                    
                    //回显ui
                    $tdServerStatusText.css("color",color);
                    $tdServerStatusText.html("<strong>"+text+"</strong>");
                    $tdServerStatusText.attr("title",errMsg);

                    //上线，下线button 显示
                    var state = 0;
                    var btnText = "上线";
                    if(text == "OK"){
                        state = 1;
                        btnText = "下线";
                    }
                    // $("#btnSingleStartAndStop").attr("disabled",false);
                    $("button[id='btnSingleStartAndStop']").html(btnText);
                    $("button[id='btnSingleStartAndStop']").attr("state",state);


                    
                });
                //end
            });
            //end register event
        },


    }

})(window.YunTao);
