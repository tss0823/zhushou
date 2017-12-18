/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.taskLog) {
        YT.deploy.taskLog = {};
    }

    var common = {

        formId: "taskLogForm",

        route_callback: function (d, data) {

            // YT.deploy.taskLog.currData = data;

            YT.deploy.formId = "taskLogForm";

            console.log("task log list after render call");
            //组件初始化之后
            var appList = appData.appList;
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

            $("#taskLogForm #startTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            });
            $("#taskLogForm #endTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            });
            //end

            //授权资源
            var authRes = data.authRes;

            var enums = YT.deploy.data.enums;

            //logStatus
            // var logStatus = enums.logStatus;
            YT.deploy.util.initEnumSelect("logStatus", "success", data.success);

            //batchNoType
            // var logQueryType = enums.logQueryType;
            YT.deploy.util.initEnumSelect("logQueryType", "batchNoType", data.batchNoType);

            //messageType
            YT.deploy.util.initEnumSelect("logQueryType", "messageType", data.messageType);

            //descType
            YT.deploy.util.initEnumSelect("logQueryType", "descType", data.descType);

            //select change to query
            $("#appName,#month,#startTime,#endTime").change(function () {
                $("#btnQuery").trigger("click");
            });

            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled", "true");
                YT.deploy.taskLog.query(pageNum, pageSize);
            });


            $("a[id='enterDetail']").each(function (index, item) {
                $(this).click(function () {
                    var batchNo = $(item).attr("batchNo");
                    var message = $(item).attr("message");
                    // var checked = $("#model").attr("checked");
                    // var model = checked ? "prod" : "test";
                    // var params = {batchNo:batchNo,month:$("#month").val(),model:model};
                    var appName = $(item).attr("appName");
                    // var data = {
                    //     title: "【"+batchNo+"】task日志详细",
                    //     msTitle: " 【 " + appName + " 】 > " + model
                    // };
                    // data["model"] = model;
                    // data["appName"] = appName;
                    // YT.deploy.route("/taskLog/selectListByBatchNo", params, "/taskLog/detailList.html", data);

                    YT.deploy.taskLog.openDetailWin(batchNo, appName, message);
                });
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
                YT.deploy.taskLog.query(1, $("#pageSize").val());
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.taskLog.query);

        },
    }
    $.extend(YT.deploy, common);

    YT.deploy.taskLog.detailParam = null,
        YT.deploy.taskLog.detailExtData = null,


        YT.deploy.taskLog = {
            query: function (pageNum, pageSize) {
                var checked = $("#model").attr("checked");
                var checkState = checked ? "prod" : "test";
                YT.deploy.goSearchPage("taskLogForm", pageNum, pageSize, {model: checkState});
            },

            queryDetail: function (pageNum) {
                var params = YT.deploy.util.getFormParams("#taskLogDetailListForm");
                params["pageNum"] = pageNum;
                params = $.extend(params,YT.deploy.taskLog.detailParam);

                // YT.deploy.taskLog.openDetailWin(params["batchNo"],params["appName"],"");
                // var ext_data = $.extend(params, {
                //     title: YT.deploy.taskLog.currData.title,
                //     msTitle: YT.deploy.taskLog.currData.msTitle
                // });
                YT.deploy.routeOpenWin("/taskLog/selectListByBatchNo", params, "/taskLog/detailList.html", YT.deploy.taskLog.detailExtData,function(data){
                    YT.deploy.taskLog.openDetailInit(data);
                });

            },

            openDetailInit: function (data) {

                YT.deploy.formId = "taskLogDetailListForm";

                //messageType
                YT.deploy.util.initEnumSelect("logQueryType","messageType",data.messageType);

                //descType
                YT.deploy.util.initEnumSelect("logQueryType","descType",data.descType);

                $("#taskLogDetailListForm #startTime").datetimepicker({
                    showSecond: true,
                    // showMillisec: true,
                    // timeFormat: 'hh:mm:ss:l'
                });
                $("#taskLogDetailListForm #endTime").datetimepicker({
                    showSecond: true,
                    // showMillisec: true,
                    // timeFormat: 'hh:mm:ss:l'
                });

                //注册事件
                $("#btnQueryDetail").click(function () {
                    var pageNum = 1;  //查询触发从第一页开始
                    // var pageSize = $("#pageSize").val();
                    $(this).html("查询中...");
                    $(this).attr("disabled", "true");
                    YT.deploy.taskLog.queryDetail(pageNum);
                });

                $("button[id='btnSql']").click(function () {
                    var dataId = $(this).attr("data");
                    console.info("dataId=" + dataId);
                    var item = $("tr[name='trChildSqlItem_" + dataId + "']").first();
                    if ($(item).css("display") == "none") {
                        $(item).show();
                    } else {
                        $(item).hide();
                    }
                });
                $("button[id='btnMore']").click(function () {
                    var dataId = $(this).attr("data");
                    var item = $("tr[name='trChildDataItem_" + dataId + "']").first();
                    if ($(item).css("display") == "none") {
                        $(item).show();
                    } else {
                        $(item).hide();
                    }
                });

                $("div[id='dataMsg'][type='1']").each(function (index, item) {
                    var jsonData = $(item).text();
                    $(item).JSONView(jsonData, {collapsed: true});
                });
                $("div[id='dataMsg'][type='2']").each(function (index, item) {
                    var jsonData = $(item).text();
                    $(item).JSONView(jsonData, {collapsed: true});

                });
            },

            openDetailWin: function (batchNo, appName, message) {
                var checked = $("#model").attr("checked");
                var model = checked ? "prod" : "test";
                var params = {batchNo: batchNo, month: $("#month").val(), model: model};
                YT.deploy.taskLog.detailParam = params;
                YT.deploy.util.reqGet("/taskLog/selectListByBatchNo", params, function (d) {
                    // var jsonLog = d.data.response;
                    // var jsonObj = JSON.parse(jsonLog);
                    // var formatJsonLog = JSON.stringify(jsonObj, null, 4);

                    var extData = {tp_title: "【" + message + "】task日志详细列表", msTitle: " 【 " + appName + " 】 > " + model};
                    YT.deploy.taskLog.detailExtData = extData;
                    var param = $.extend(d.data, extData);
                    // //debugger;
                    $.get("/taskLog/detailList.html", function (source) {
                        var render = template.compile(source);
                        var html = render(param);
                        bootbox.dialog({
                            message: html,
                            width: "800px",
                        });
                        $(".modal-dialog").prop("style", "width:70%;height:85%")

                        YT.deploy.taskLog.openDetailInit({});


                    });
                });
            },

        }

})(window.YunTao);
