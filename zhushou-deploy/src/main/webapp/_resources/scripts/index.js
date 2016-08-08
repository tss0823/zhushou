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
                    var valueMap = YT.deploy.userDataProcess.getValueMap(authRes.tplUrl);
                    valueMap = valueMap || {};
                    //end
                    debugger;
                    var ext_data = {title: authRes.name,authRes:authRes};
                    $.extend(ext_data,valueMap);
                    YT.deploy.route(authRes.url, valueMap, authRes.tplUrl,ext_data);
                    $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
                    $(this).parent("li").addClass("active");
                    YT.deploy.index.activeMenu = this;
                });
            }
            

            // $("#enterDeployLog").click(function () {
            //     YT.deploy.route("/deployLog/list", {}, "/deployLog.html", {title: "发布日志"});
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });
            //
            // $("#enterAppLog").click(function () {
            //     var showAll = $.cookie("app_log_show_all");
            //     var dataDisplay = showAll && showAll == "1" ? "table_row" : "none";
            //     var ext_data = {title: "应用日志",showAll:showAll,dataDisplay:dataDisplay};
            //     YT.deploy.route("/appLog/list", {}, "/log/appLog.html", ext_data);
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });
            //
            // $("#enterTaskLog").click(function () {
            //     var ext_data = {title: "Task日志"};
            //     YT.deploy.route("/taskLog/list", {}, "/taskLog/list.html", ext_data);
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });
            //
            // $("#enterIdoc").click(function () {
            //     YT.deploy.route("/idocUrl/list", {}, "/idoc/list.html", {title: "接口列表"});
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });
            //
            // $("#enterAutoTest").click(function () {
            //     YT.deploy.route("/atTemplate/list", {}, "/at/template.html", {title: "模板列表"});
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });
            //
            // $("#enterSettings").click(function () {
            //     console.log("enter settings nothing")
            //     $(YT.deploy.index.activeMenu).parent("li").removeClass("active");
            //     $(this).parent("li").addClass("active");
            //     YT.deploy.index.activeMenu = this;
            // });

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

            //事件触发
            var preKeyCode = null;
            $(document).keydown(function (e) {
                console.log("keyCode=" + e.keyCode);
                if (preKeyCode == 18 && e.keyCode == 13) {
                    $("button[enter='true']").trigger("click");
                    return false;
                }

                if (preKeyCode == 17 && e.keyCode == 82) { //ctrl + r
                    console.log("presskey ctrl + r");
                    YT.deploy.routeStackProcess.refresh();
                    return false;  //屏蔽系统event
                }
                if (preKeyCode == 18 && e.keyCode == 72) { //alt + h
                    console.log("presskey alt + h");
                    YT.deploy.routeStackProcess.home();
                    return false;  //屏蔽系统event
                }
                if (preKeyCode == 18 && e.keyCode == 37) { //alt + left
                    console.log("presskey alt+left");
                    YT.deploy.routeStackProcess.prevRoute();
                    return false;
                }
                if (preKeyCode == 18 && e.keyCode == 39) { //alt + right
                    console.log("presskey alt + right");
                    YT.deploy.routeStackProcess.nextRoute();
                    return false;
                }
                if (e.keyCode == 27) { //Esc
                    console.log("presskey Esc");
                    $(".bootbox-close-button").trigger("click");
                    return false;
                }
                preKeyCode = e.keyCode;

            });



            //webSocket
            var hostname = location.hostname;
            var webSocket = new WebSocket('ws://'+hostname+':9003/indexWebSocket');
            
            // webSocket.send("发送消息ok");

            webSocket.onerror = function(event) {
                console.log("error,data="+event.data);
            };

            webSocket.onopen = function(event) {
                console.log("open,data="+event.data);
            };

            webSocket.onmessage = function(event) {
                YT.deploy.eventProcess.notifyEvent(event.data);
                console.log("onmessage data="+event.data);
            };
            //end
            
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

        },


    }

})(window.YunTao);
