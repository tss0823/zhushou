/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.appLog) {
        YT.deploy.appLog = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("app list after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            var docList = YT.deploy.data.docList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            //month select init
            var nowDate = new Date();
            var year = nowDate.getFullYear();
            var month = nowDate.getMonth() + 1;
            var monthArray = [];
            var monthStr = month < 10 ? "0" + month : month;
            monthArray.push({value: year + "." + monthStr, text: year + "年" + monthStr + "月"});
            var monthCount = 6;
            for (var i = 0; i < monthCount; i++) {
                month--;
                if (month == 0) {
                    month = 12;
                    year--;
                }
                monthStr = month < 10 ? "0" + month : month;
                monthArray.push({value: year + "." + monthStr, text: year + "年" + monthStr + "月"});
            }
            YT.deploy.util.initSelect(monthArray, "value", "text", "month", data.month);
            //end

            //doc select init
            var branchValArray = [];
            for (var i = 0; i < docList.length; i++) {
                var doc = docList[i];
                branchValArray.push("<option value='" + doc.url + "'>");
                branchValArray.push(doc.name);
                branchValArray.push("\n");
                branchValArray.push(doc.url);
                branchValArray.push("</option>");
            }
            $("#branch").append(branchValArray.join(""));
            $('#branch').chosen({
                search_contains: true,
                // disable_search_threshold: 10
            });
            $('#branch').change(function () {
                $("#url").val($(this).val());
            });
            //end

            //授权资源
            var authRes = data.authRes;

            var enums = YT.deploy.data.enums;

            $("#startTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            });
            $("#endTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            });

            // //logModel
            // var logModel = enums.logModel;
            // YT.deploy.util.initEnumSelect(logModel,"model",data.model);

            //logLevel
            var logLevel = enums.logLevel;
            YT.deploy.util.initEnumSelect(logLevel, "level", data.level);

            //urlType
            var logQueryType = enums.logQueryType;
            YT.deploy.util.initEnumSelect(logQueryType, "urlType", data.urlType);

            //userAgentType
            YT.deploy.util.initEnumSelect(logQueryType, "userAgentType", data.userAgentType);

            //text
            var logQueryTextCat = enums.logQueryTextCat;
            YT.deploy.util.initEnumSelect(logQueryTextCat, "textCat", data.textCat);

            YT.deploy.util.initEnumSelect(logQueryType, "textType", data.textType);


            //ip city show
            YT.deploy.appLog.showIpCity();
            //end

            //init doc name
            YT.deploy.appLog.showDocName(docList);
            //end

            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled", "true");
                YT.deploy.appLog.query(pageNum, pageSize);
            });


            $("button[id='btnMore']").click(function () {
                var dataId = $(this).attr("data");
                var item = $("tr[name='trChildItem_" + dataId + "']").first();
                if ($(item).css("display") == "none") {
                    $(item).show();
                } else {
                    $(item).hide();
                }
            });

            //show all
            $("input[id='showAll']").click(function () {
                var checked = $(this).attr("checked");
                var checkState = checked ? false : true;
                $(this).attr("checked", checkState);
                $("tr[id='trChildItem']").each(function (index, item) {
                    if (checkState) {
                        $(item).show();
                    } else {
                        $(item).hide();
                    }
                });
                console.log("checkState=" + checkState);
                // params["showAll"] = params["showAll"] ? true : false;

                var nameSpace = authRes.tplUrl;
                YT.deploy.userDataProcess.setValue(nameSpace, "showAll", checkState, 7);
            });

            //model
            $("input[id='model']").click(function () {
                var checked = $(this).attr("checked");
                // var checkState = checked ? true : false;
                $(this).attr("checked", !checked);

                // var nameSpace = authRes.tplUrl;
                // YT.deploy.userDataProcess.setValue(nameSpace,"model",checkState,7);
                //
                // $(".nav-list").find("li").first().find("a[id='"+authRes.actionId+"']").trigger("click");
                YT.deploy.appLog.query(1, $("#pageSize").val());
            });

            //parameters
            $("a[id='btnParameter']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.openParameterDialog(id);
            });

            //btnResponse
            $("a[id='btnResponse']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.openResponseDialog(id);
            });

            //msg
            $("a[id='btnMsg']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.openMsgDialog(id);
            });

            //format msg
            $("a[id='btnFormatMsg']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.openFormatMsgDialog(id);
            });

            //req url
            $("a[id='btnReqExecute']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.reqExecute(id);
            });

            //to req url
            $("a[id='btnToReqExecute']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.toReqExecute(id);
            });

            //bind idoc
            $("a[id='btnBindIdoc']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.appLog.bindIdoc(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.appLog.query);

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.appLog = {
        query: function (pageNum, pageSize) {
            var checked = $("#model").attr("checked");
            var checkState = checked ? "prod" : "test";
            // debugger;
            YT.deploy.goSearchPage("appLogForm", pageNum, pageSize, {model: checkState});
            // YT.deploy.goSearchPage("appLogForm",pageNum,pageSize);
            // var params = YT.deploy.util.getFormParams("#appLogForm");
            // //debugger;
            // // var checked = $("#chkShowAll").attr("checked");
            // // checked = checked ? "1" : "0";
            // // params["showAll"] = checked ;
            // params["pageNum"] = pageNum;
            // params["pageSize"] = pageSize;
            // // var dataDisplay = checked == "1" ? "table_row" : "none";
            // var actionId = $("form:first").attr("actionId");
            // var authRes = appData.authMap[actionId];
            // // $(".nav-list").find("li").first().find("a[id='"+actionId+"']").trigger("click");
            // var ext_data = $.extend(params, {title: "应用日志"});
            // ext_data = $.extend(ext_data,{authRes:authRes});
            // YT.deploy.route("/appLog/list", params, "/log/appLog.html", ext_data);
        },

        openParameterDialog: function (id) {
            var checked = $("#model").attr("checked");
            var checkState = checked ? "prod" : "test";
            var params = {stackId: id, month: $("#month").val(), model: checkState};
            YT.deploy.util.reqGet("/appLog/findMasterByStackId", params, function (d) {
                // debugger;
                var jsonObj = JSON.parse(d.data.parameters);
                var dataList = [];
                for (var key in jsonObj) {
                    var val = jsonObj[key];
                    dataList.push({key: key, value: val});
                }
                var param = {title: "请求参数列表", dataList: dataList};
                $.get("/log/msgKeyVal.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")

                });
            });

        },

        openResponseDialog: function (id) {
            var checked = $("#model").attr("checked");
            var checkState = checked ? "prod" : "test";
            var params = {stackId: id, month: $("#month").val(), model: checkState};
            YT.deploy.util.reqGet("/appLog/findMasterByStackId", params, function (d) {
                var jsonLog = d.data.response;
                var jsonObj = JSON.parse(jsonLog);
                var formatJsonLog = JSON.stringify(jsonObj, null, 4);
                var param = {title: "返回结果", logText: jsonLog, formatLogText: formatJsonLog};
                $.get("/log/msg.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")

                    // var json = data.resFormatMsg;
                    // $("#logText").JSONView(jsonLog);
                    // with options
                    $("#logText").JSONView(jsonLog, {collapsed: true});

                    new Clipboard("#btnCopy");
                    // new Clipboard("#btnCopy", {
                    //     // text: function(trigger) {
                    //     //     var logText = $("#formatLogText").text();
                    //     //     alert(logText);
                    //     //     return logText;
                    //     //     // return trigger.getAttribute('aria-label');
                    //     // }
                    // });


                    $(document).find("#btnExpand").click(function () {
                        $("#logText").JSONView('expand');
                    });
                    $(document).find("#btnCollapse").click(function () {
                        $("#logText").JSONView('collapse');
                    });
                    $(document).find("#btnToggle").click(function () {
                        $("#logText").JSONView('toggle');
                    });

                });
            });
        },

        openMsgDialog: function (id) {
            var checked = $("#model").attr("checked");
            var checkState = checked ? "prod" : "test";
            var params = {stackId: id, month: $("#month").val(), model: checkState};
            YT.deploy.util.reqGet("/appLog/selectListByStackId", params, function (d) {
                var data = d.data;
                var param = {title: "消息", logList: data.logList};
                $.get("/log/allMsg.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:90%")


                    new Clipboard("#btnCopySql");

                    //all msg show cache data
                    $("button[id='btnShowCache']").click(function () {
                        // var $showCache = $(this).parent().next();
                        // var key = $showCache.attr("data");
                        //get cache data
                        // YT.deploy.util.reqPost("/cache/getCache", {key:key,type:'byt'}, function (d) {
                        //     if (d.success) {
                        //         $showCache.show();
                        //         $showCache.html(d.data);
                        //     } else {
                        //         alert("查询cache失败,err=" + d.message);
                        //     }
                        // });
                        var state = $(this).html() == "显示结果";
                        $("button[id='btnShowCache']").each(function (index, item) {
                            $(this).parent().next().hide();
                            $(this).html("显示结果");
                        });
                        if (state) {
                            $(this).parent().next().show();
                            $(this).html("隐藏结果");
                        } else {
                            $(this).parent().next().hide();
                            $(this).html("显示结果");
                        }
                    });

                    //all msg show db sql
                    $("button[id='btnShowDbSql']").click(function () {
                        var state = $(this).html() == "显示SQL";
                        $("button[id='btnShowDbSql']").each(function (index, item) {
                            $(this).parent().next().hide();
                            $(this).html("显示SQL");
                        });
                        if (state) {
                            $(this).parent().next().show();
                            $(this).html("隐藏SQL");
                        } else {
                            $(this).parent().next().hide();
                            $(this).html("显示SQL");
                        }
                    });

                    //all msg show db result
                    $("button[id='btnShowDbResult']").click(function () {
                        var state = $(this).html() == "显示结果";
                        $("button[id='btnShowDbResult']").each(function (index, item) {
                            $(this).parent().siblings("pre[name='dbResult']").hide();
                            $(this).html("显示结果");
                        });
                        if (state) {
                            var $dbResult = $(this).parent().siblings("pre[name='dbResult']");
                            $dbResult.show();
                            // var jsonText = "{"+$dbResult.val()+"}";
                            // $dbResult.JSONView(jsonText, { collapsed: true });

                            $(this).html("隐藏结果");
                        } else {
                            $(this).parent().siblings("pre[name='dbResult']").hide();
                            $(this).html("显示结果");
                        }
                    });

                    //格式化基础数据
                    $("button[id='btnNormalMsgFormat']").click(function () {
                        var state = $(this).html() == "格式化";
                        if(state){
                            $(this).html("还原");
                            var jsonText = $(this).parent().attr("data");
                            $(this).parent().find("span").JSONView(jsonText, { collapsed: true });
                        }else{
                            $(this).html("格式化");
                            var jsonText = $(this).parent().attr("data");
                            $(this).parent().find("span").html(jsonText);
                        }
                    });



                });

            });

        },

        reqExecute: function (id) {
            var checked = $("#model").attr("checked");
            var model = checked ? "prod" : "test";
            if (model == "prod") {
                if (!confirm("您确认要执行请求吗？")) {
                    return;
                }
            }
            var params = {stackId: id, month: $("#month").val(), model: model};
            YT.deploy.util.reqPost("/appLog/httpExecute", params, function (d) {
                if (d.success) {
                    alert("执行成功");
                    YT.deploy.routeStackProcess.refresh();
                } else {
                    alert("执行失败,err=" + d.message);
                }
            });

        },

        toReqExecute: function (id) {
            var checked = $("#model").attr("checked");
            var model = checked ? "prod" : "test";
            if (model == "prod") {
                if (!confirm("您确认要跳转执行请求吗？")) {
                    return;
                }
            }

            var params = {stackId: id, month: $("#month").val(), model: model};
            YT.deploy.util.reqGet("/appLog/findMasterByStackId", params, function (d) {
                var data = d.data;
                data = data || {};

                //渲染左侧栏
                var newData = {
                    url: data.url, appName: data.appName, model: data.type, reqHeader: data.reqHeaders,
                    reqData: data.parameters, resHeader: data.resHeaders, resData: data.response
                };
                $.extend(YT.deploy.data, {reqContentInitData: newData});

                $(".nav-list").find("li > a[id='enterReqContent']").first().trigger("click");

                // YT.deploy.reqContent.initLeftPanel(newData);
            });


        },

        bindIdoc: function (id) {
            var checked = $("#model").attr("checked");
            var model = checked ? "prod" : "test";
            var params = {stackId: id, month: $("#month").val(), model: model};
            YT.deploy.route("/idocUrl/bind", params, "/idoc/bind.html", {title: "绑定保存"});
        },

        //
        openFormatMsgDialog: function (id) {
            var checked = $("#model").attr("checked");
            var model = checked ? "prod" : "test";
            var params = {stackId: id, month: $("#month").val(), model: model};
            YT.deploy.util.reqGet("/appLog/findMasterByStackId", params, function (d) {
                var jsonObjReq = JSON.parse(d.data.reqHeaders);
                var reqDataList = [];
                for (var key in jsonObjReq) {
                    var val = jsonObjReq[key];
                    reqDataList.push({key: key, value: val});
                }
                var jsonObjRes = JSON.parse(d.data.resHeaders);
                var resDataList = [];
                for (var key in jsonObjRes) {
                    var val = jsonObjRes[key];
                    resDataList.push({key: key, value: val});
                }
                // debugger;
                var param = {reqTitle: "请求头列表", reqDataList: reqDataList, resTitle: "返回头列表", resDataList: resDataList};
                $.get("/log/msgFormat.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")

                });
            });

        },

        //展示ip城市
        showIpCity: function () {
            var ipCityMap = {};
            $("table[name='tbChildItem']").each(function (index, item) {
                var $tdClientIp = $(item).find("td[name='showClientIp']");
                var ip = $tdClientIp.attr("data");
                if (ipCityMap[ip]) {
                    $tdClientIp.html(ip + " [" + ipCityMap[ip] + "]");
                    return true;
                }
                $.get("/getCityById", {ip: ip}, function (d) {
                    // console.log("city="+d.data);
                    $tdClientIp.html(ip + " [" + d.data + "]");
                }, "json");
            });
        },

        //展示文档名称
        showDocName: function (docList) {
            $("#tbContent").find("tr").each(function (index, item) {
                var url = $(item).find("div[name='url']").text();
                for (var i = 0; i < docList.length; i++) {
                    var doc = docList[i];
                    if (doc.url == url) {
                        $(item).find("div[name='docName']").html(doc.name);
                        break;
                    }
                }
            });
        }


    }

})(window.YunTao);
