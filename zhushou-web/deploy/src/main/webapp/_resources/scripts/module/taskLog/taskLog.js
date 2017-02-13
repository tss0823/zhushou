/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.taskLog) {
        YT.deploy.taskLog = {};
    }

    var common = {

        route_callback: function (d,data) {

            YT.deploy.taskLog.currData = data;

            console.log("app list after render call");
            //组件初始化之后
            var appList = appData.appList;
            YT.deploy.util.initSelect(appList,"name","name","appName",data.appName);

            //month select init
            var nowDate = new Date();
            var year = nowDate.getFullYear();
            var month = nowDate.getMonth() + 1;
            var monthArray = [];
            var monthStr = month < 10 ? "0"+month : month;
            monthArray.push({value:year+"."+monthStr,text:year+"年"+monthStr+"月"});
            var monthCount = 6;
            for(var i = 0; i < monthCount; i++){
                month--;
                if(month == 0){
                    month = 12;
                    year--;
                }
                monthStr = month < 10 ? "0"+month : month;
                monthArray.push({value:year+"."+monthStr,text:year+"年"+monthStr+"月"});
            }
            YT.deploy.util.initSelect(monthArray,"value","text","month",data.month);
            
            $("#startTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            }) ;
            $("#endTime").datetimepicker({
                showSecond: true,
                // showMillisec: true,
                // timeFormat: 'hh:mm:ss:l'
            }) ;
            //end

            //授权资源
            var authRes = data.authRes;

            var enums = YT.deploy.data.enums;

            //logStatus
            var logStatus = enums.logStatus;
            YT.deploy.util.initEnumSelect(logStatus,"success",data.success);

            //batchNoType
            var logQueryType = enums.logQueryType;
            YT.deploy.util.initEnumSelect(logQueryType,"batchNoType",data.batchNoType);

            //messageType
            YT.deploy.util.initEnumSelect(logQueryType,"messageType",data.messageType);

            //descType
            YT.deploy.util.initEnumSelect(logQueryType,"descType",data.descType);


            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled","true");
                YT.deploy.taskLog.query(pageNum, pageSize);
            });


            $("a[id='enterDetail']").each(function (index, item) {
                $(this).click(function () {
                    var batchNo = $(item).attr("batchNo");
                    var checked = $("#model").attr("checked");
                    var model = checked ? "prod" : "test";
                    var params = {batchNo:batchNo,month:$("#month").val(),model:model};
                    var appName = $(item).attr("appName");
                    var data = {
                        title: "【"+batchNo+"】task日志详细",
                        msTitle: " 【 " + appName + " 】 > " + model
                    };
                    data["model"] = model;
                    data["appName"] = appName;
                    YT.deploy.route("/taskLog/selectListByBatchNo", params, "/taskLog/detailList.html", data);
                });
            });

            //model
            $("input[id='model']").click(function () {
                var checked = $(this).attr("checked");
                // var checkState = checked ? true : false;
                $(this).attr("checked",!checked);

                // var nameSpace = authRes.tplUrl;
                // YT.deploy.userDataProcess.setValue(nameSpace,"model",checkState,7);
                //
                // $(".nav-list").find("li").first().find("a[id='"+authRes.actionId+"']").trigger("click");
                YT.deploy.taskLog.query(1,$("#pageSize").val());
            });
            
            $("button[id='btnMore']").click(function(){
                var dataId = $(this).attr("data");
                var item = $("tr[name='trChildItem_" + dataId + "']").first();
                if ($(item).css("display") == "none") {
                    $(item).show();
                }else{
                    $(item).hide();
                }
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.taskLog.query);

        },
    }
    $.extend(YT.deploy, common);

    YT.deploy.taskLog.currData = null,


    YT.deploy.taskLog = {
        query: function (pageNum, pageSize) {
            var checked = $("#model").attr("checked");
            var checkState = checked ? "prod" : "test";
            debugger;
            if($("form:first").attr("id") == "taskLogDetailListForm"){
               YT.deploy.queryDetail(pageNum,pageSize);
            }else{
                YT.deploy.goSearchPage("taskLogForm",pageNum,pageSize,{model:checkState});
            }
        },

    }

})(window.YunTao);
