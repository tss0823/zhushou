/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.appHost) {
        YT.deploy.appHost = {};
    }

    $.extend(YT.deploy,{
        route_callback : function (d,data) {
            console.log("appHost list after render call");
            ////debugger;
            //组件初始化之后
            var appName = YT.deploy.appHost.appName;

            //注册事件,控制台打印消息
            YT.deploy.eventProcess.addListener("deploy_script",function(msgObj){
                YT.deploy.appHost.writeMsg(msgObj.data+"\r\n", true);
            });
            
            //end

            //刷新分支
            $("#btnRefreshBranch").click(function () {
                $(this).html("刷新中..");
                $(this).attr("disabled",true);
                // document.getElementById("btnRefreshBranch").disabled = true;
                YT.deploy.appHost.refreshBranch(true);
                // $(this).html("刷新分支");
                // $(this).attr("disabled",false);
                YT.deploy.routeStackProcess.refresh();
            });

            //热点分支置顶
            $("#branch").change(function () {
               YT.deploy.appHost.changeBranchSort();
            });

            //注册事件
            $("#btncompile").click(function () {
                YT.deploy.appHost.compile();
            });
            $("#btnCompileAndDeploy").click(function () {
                YT.deploy.appHost.compileAndDeploy();
            });
            $("#btnSingleCompileAndDeploy").click(function () {
                YT.deploy.appHost.singleCompileAndDeploy();
            });

            $("#btnBtachDeploy").click(function () {
                YT.deploy.appHost.batchDeploy();
            });

            $("#btnBatchRestart").click(function () {
                YT.deploy.appHost.batchRestart();
            });

            $("#btnBatchRollback").click(function () {
                YT.deploy.appHost.batchRollback();
            });

            //单个操作
//			$(document).on("each","button[id='btnSingleDeploy']",function(index,item){  //这样写不行，TODO
            $("button[id='btnSingleDeploy']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    YT.deploy.appHost.deploy(ip);
                });
            });
            $("button[id='btnSingleDeployStatic']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    YT.deploy.appHost.deployStatic(ip);
                });
            });
            //
            $("button[id='btnSingleRestart']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    YT.deploy.appHost.restart(ip);
                });
            });

            $("button[id='btnSingleDebug']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    YT.deploy.appHost.debug(ip);
                });
            });
            $("button[id='btnSingleStartAndStop']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    var state = $(item).attr("state");
                    if(state == 0){  //当前下线状态
                        YT.deploy.appHost.start(ip,state);
                    }else if(state == 1){ //当前上线状态
                        YT.deploy.appHost.stop(ip,state);
                    }else{  //禁止点击
                        alert("当前状态不可用");
                        return;
                    }
                });
            });
            //
            $("button[id='btnSingleRollback']").each(function(index,item){
                $(this).click(function() {
                    var ip = $(item).attr("data");
                    YT.deploy.appHost.rollback(ip);
                });
            });

            //end

            YT.deploy.appHost.refreshBranch(false);

            //host table row server check status
            // var statAppObj = appData.serverStatus[appName];
            // $("#tbContentHost").find("tr").each(function (index, item) {
            //     if(!statAppObj){
            //         return false;
            //     }
            //     var $tdServerStatusText = $(item).find("td[name='serverStatusText']");
            //     var hostName = $tdServerStatusText.attr("data");
            //     var statHostObj = statAppObj[hostName];
            //     if(!statHostObj){
            //         return;
            //     }
            //
            //     //回显ui
            //     $tdServerStatusText.css("color",statHostObj.color);
            //     $tdServerStatusText.html(statHostObj.text);
            //     $tdServerStatusText.attr("title",statHostObj.error);
            //
            //     //上线，下线button 显示
            //     var state = 0;
            //     var btnText = "上线";
            //     if(statHostObj.text == "OK"){
            //         state = 1;
            //         btnText = "下线";
            //     }
            //     // $("#btnSingleStartAndStop").attr("disabled",false);
            //     $("#btnSingleStartAndStop").html(btnText);
            //     $("#btnSingleStartAndStop").attr("state",state);
            //
            //
            // });
            //end
        },
    });


    YT.deploy.appHost = {

        model : $("#model").val(),
        appName : $("#appName").val(),

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


        printMsg: function () {
            YT.deploy.util.reqPost("/deploy/getExecMsg", {}, function (d) {
                if (d.success) {
                    var data = d.data;
                    if (data.indexOf("exec-0") == -1) {
                        YT.deploy.appHost.writeMsg(data, true);
                    } else {
                        YT.deploy.appHost.writeMsg(data, true);
                        window.clearInterval(interval);
                        YT.deploy.appHost.writeMsg("执行结束\r\n", true);
                    }
                } else {
                    console.error(d.message);
                }
            }, "json");
        },

        refreshBranch: function (syncRemote) {
            var appName = YT.deploy.appHost.appName;
            var branchList;
            if(syncRemote){
                $.ajax({
                    // type:"POST",
                    url: "/deploy/branchList",
                    data: {"appName": appName},
                    async: false,  //同步
                    success: function (d) {
                        branchList = d.data;
                        // branchData = JSON.stringify(d.data);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(textStatus);
                    }
                });

                // var branchMap = {};
                // var itemValue = localStorage.getItem(YT.deploy.constant.BRANCH_MAP);
                // if(itemValue != null) {
                //     branchMap = JSON.parse(itemValue);
                // }
                var branchMap = YT.deploy.Cache.get(YT.deploy.constant.BRANCH_MAP) || {};
                branchMap[appName] = branchList;

                YT.deploy.Cache.set(YT.deploy.constant.BRANCH_MAP,branchMap);

                // //set to localStorage
                // localStorage.setItem(YT.deploy.constant.BRANCH_MAP,JSON.stringify(branchMap));
                // //set to local cache
                // $.extend(YT.deploy.data, {branchMap:branchMap});

            }else{
                //初始化控件
                var branchMap = YT.deploy.Cache.get(YT.deploy.constant.BRANCH_MAP) || {};
                // var branchMap = YT.deploy.data.branchMap || {};
                // if(branchMap[appName] == null || branchMap[appName].length == 0){
                //     //get from localStorage
                //     var itemValue = localStorage.getItem(YT.deploy.constant.BRANCH_MAP);
                //     if(itemValue != null){
                //         branchMap = JSON.parse(itemValue);
                //
                //         //set to local cache
                //         $.extend(YT.deploy.data, {branchMap:branchMap});
                //     }
                //     // var branchData = branchMap[appName];
                //     // if(typeof(branchData) != "undefined" && branchData){
                //     //     branchList = JSON.parse(branchData);
                //     // }
                // }
                branchList = branchMap[appName];
            }
            if(branchList){
                var model = YT.deploy.appHost.model;
                var branchValArray = [];
                if(model == "prod"){
                    var branchVal = "master";
                    branchValArray.push("<option value='"+branchVal+"'>");
                    branchValArray.push(branchVal);
                    branchValArray.push("</option>");
                }
                for(var i = 0; i < branchList.length; i++){
                    var branchVal = branchList[i];
                    if(branchVal == "master" && model == "prod"){
                        continue;
                    }
                    branchValArray.push("<option value='"+branchVal+"'>");
                    branchValArray.push(branchVal);
                    branchValArray.push("</option>");
                }
                $("#branch").html(branchValArray.join(""));
            }
            $('#branch').chosen();
        },

        changeBranchSort: function () {
            var appName = YT.deploy.appHost.appName;
            var brVal = $("#branch").val();
            // var branchMap = {};
            // var itemValue = localStorage.getItem(YT.deploy.constant.BRANCH_MAP);
            // if(itemValue != null) {
            //     branchMap = JSON.parse(itemValue);
            // }
            var branchMap = YT.deploy.data.branchMap || {};
            var branchList = branchMap[appName] || [];
            var newBranchList = [brVal];
            for(var i = 0; i < branchList.length; i++){
                var branchVal = branchList[i];
                if(branchVal != brVal){
                    newBranchList.push(branchVal)
                }
            } 
            branchMap[appName] = newBranchList;

            YT.deploy.Cache.set(YT.deploy.constant.BRANCH_MAP,branchMap);

            // //set to localStorage
            // localStorage.setItem(YT.deploy.constant.BRANCH_MAP,JSON.stringify(branchMap));
            // //set to local cache
            // $.extend(YT.deploy.data, {branchMap:branchMap});

        },

        compile: function () {
            var model = YT.deploy.appHost.model;
            var appName = YT.deploy.appHost.appName;
            var branch = $("#branch").val();
            if(!branch){
                $("#branch").focus();
                alert("请输入分支名称");
                return;
            }
            var param = {appName:appName,branch:branch,model:model};
            YT.deploy.appHost.writeMsg("执行编译开始,请稍等...\r\n",true);
            YT.deploy.util.reqPost("/deploy/compile",param,function(d){
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });
        },

        compileAndDeploy: function () {
            var model = YT.deploy.appHost.model;
            var appName = YT.deploy.appHost.appName;
            var branch = $("#branch").val();
            if(!branch){
                $("#branch").focus();
                alert("请输入分支名称");
                return;
            }
            var param = {appName:appName,branch:branch,model:model};
            YT.deploy.appHost.writeMsg("执行编译并发布[所有应用]开始,请稍等...\r\n",true);
            YT.deploy.util.reqPost("/deploy/compileAndDeploy",param,function(d){
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });
        },

        singleCompileAndDeploy: function () {
            var model = YT.deploy.appHost.model;
            var appName = YT.deploy.appHost.appName;
            var branch = $("#branch").val();
            if(!branch){
                $("#branch").focus();
                alert("请输入分支名称");
                return;
            }
            var param = {appName:appName,branch:branch,model:model};
            YT.deploy.appHost.writeMsg("执行编译并发布["+appName+"]开始,请稍等...\r\n",true);
            YT.deploy.util.reqPost("/deploy/singleCompileAndDeploy",param,function(d){
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });
        },

        batchDeploy: function () {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if(!confirm("您确认要批量发布【 "+appName+" 】 > "+model+" 吗？")){
                return;
            }
            var ipArray = [];
            $(":checkbox[id='chkForm']").each(function(index,item){
                if($(item).prop("checked")){
                    ipArray.push($(item).val());
                }
            });
//			alert(ipArray.join(","));
            var param = {appName:appName,model:model,ipList:ipArray};
            YT.deploy.appHost.writeMsg("执行批量发布开始,请稍等...\r\n",true);
            YT.deploy.util.reqPost("/deploy/deploy",param,function(d){
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });
        },

        batchRestart: function () {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if(!confirm("您确认要批量重启【 "+appName+" 】 > "+model+" 吗？")){
                return;
            }
            var ipArray = [];
            $(":checkbox[id='chkForm']").each(function(index,item){
                if($(item).prop("checked")){
                    ipArray.push($(item).val());
                }
            });
//			alert(ipArray.join(","));
            var param = {appName:appName,model:model,ipList:ipArray};
            YT.deploy.appHost.writeMsg("执行批量重启开始,请稍等...\r\n",true);
            YT.deploy.util.reqPost("/deploy/restart",param,function(d){
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });
        },
        
        batchRollback: function () {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if(!confirm("您确认要批量回滚【 "+appName+" 】 > "+model+" 吗？")){
                return;
            }
            var ipArray = [];
            $(":checkbox[id='chkForm']").each(function(index,item){
                if($(item).prop("checked")){
                    ipArray.push($(item).val());
                }
            });

            YT.deploy.util.reqGet("/host/getLastBackVer",{appName:appName,ip:ipArray[0]},function(d){
                var deployLogList = d.data;
                if(deployLogList.length == 0){
                    alert("没有可以回滚的版本");
                    return;
                }
                var backVerArray = [];
                for(var i = 0 ; i < deployLogList.length; i++){
                    var deployLog = deployLogList[i];
                    var backVerStr = deployLog.backVer;
                    var backVer = backVerStr.split(",")[0];
                    var realBackVer = "ROOT_"+backVer+".BAK";
                    backVerArray.push("<option value='"+realBackVer+"'>");
                    var contentStr = backVer+"("+deployLog.content+")"
                    backVerArray.push(contentStr);
                    backVerArray.push("</option>");
                }
                var html = "<span class='bigger-110'><select id='backVerId'>"+backVerArray.join("")+"</select></span>";
                bootbox.dialog({
                    title : "请选择您要回滚的版本,请仔细核对好是否为批量发布的？",
                    message: html,
                    width:"800px",
                    buttons:
                    {
                        "success" :
                        {
                            "label" : "<i class='icon-ok'></i> 确认",
                            "className" : "btn-sm btn-success",
                            "callback": function() {
                                var backVer = $("#backVerId").val();
                                var param = {appName: appName, model: model, ipList: ipArray,backVer:backVer};
                                YT.deploy.appHost.writeMsg("执行回滚开始,请稍等...\r\n", true);
                                YT.deploy.util.reqPost("/deploy/rollback", param, function (d) {
                                    //打印消息
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

            });

           
        },

        deploy: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要发布【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行发布开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/deploy", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });

        },
        
        deployStatic: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要静态发布【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行静态发布开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/deployStatic", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
            });

        },

        restart: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要重启【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行重启开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/restart", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg, 300);
            });

        },
        debug: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要调式重启【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行调式重启开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/debug", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg, 300);
            });

        },
        start: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要上线【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行上线开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/start", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg, 300);
                YT.deploy.routeStackProcess.refresh();

            });
        },

        stop: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要下线【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }
            var param = {appName: appName, model: model, ipList: [ip]};
            YT.deploy.appHost.writeMsg("执行下线开始,请稍等...\r\n", true);
            YT.deploy.util.reqPost("/deploy/stop", param, function (d) {
                //打印消息
                // interval = window.setInterval(YT.deploy.appHost.printMsg, 300);
                YT.deploy.routeStackProcess.refresh();
            });

        },
        rollback: function (ip) {
            var appName = YT.deploy.appHost.appName;
            var model = YT.deploy.appHost.model;
            if (!confirm("您确认要回滚【 " + appName + " 】 > " + model + " 吗？")) {
                return;
            }

            YT.deploy.util.reqGet("/host/getLastBackVer",{appName:appName,ip:ip},function(d){
                var deployLogList = d.data;
                if(deployLogList.length == 0){
                    alert("没有可以回滚的版本");
                    return;
                }
                var backVerArray = [];
                for(var i = 0 ; i < deployLogList.length; i++){
                    var deployLog = deployLogList[i];
                    var backVerStr = deployLog.backVer;
                    var backVer = backVerStr.split(",")[0];
                    var realBackVer = "ROOT_"+backVer+".BAK";
                    backVerArray.push("<option value='"+realBackVer+"'>");
                    var contentStr = backVer+"("+deployLog.content+")"
                    backVerArray.push(contentStr);
                    backVerArray.push("</option>");
                }
                var html = "<span class='bigger-110'><select id='backVerId'>"+backVerArray.join("")+"</select></span>";
                bootbox.dialog({
                    title : "请选择您要回滚的版本",
                    message: html,
                    width:"800px",
                    buttons:
                    {
                        "success" :
                        {
                            "label" : "<i class='icon-ok'></i> 确认",
                            "className" : "btn-sm btn-success",
                            "callback": function() {
                                var backVer = $("#backVerId").val();
                                var param = {appName: appName, model: model, ipList: [ip],backVer:backVer};
                                YT.deploy.appHost.writeMsg("执行回滚开始,请稍等...\r\n", true);
                                YT.deploy.util.reqPost("/deploy/rollback", param, function (d) {
                                    //打印消息
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

            });


        },

    };


})(window.YunTao);
