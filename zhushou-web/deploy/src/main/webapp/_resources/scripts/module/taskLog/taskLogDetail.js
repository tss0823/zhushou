/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.taskLogDetail) {
        YT.deploy.taskLogDetail = {};
    }

    var common = {

        formId: "taskLogDetailListForm",

        route_callback: function (d,data) {
            console.log("app list after render call");
            //组件初始化之后
            YT.deploy.taskLogDetail.currData = data;

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
            // var logStatus = enums.logStatus;
            YT.deploy.util.initEnumSelect("logStatus","success",data.success);

            //batchNoType
            // var logQueryType = enums.logQueryType;
            YT.deploy.util.initEnumSelect("logQueryType","batchNoType",data.batchNoType);

            //messageType
            YT.deploy.util.initEnumSelect("logQueryType","messageType",data.messageType);

            //descType
            YT.deploy.util.initEnumSelect("logQueryType","descType",data.descType);


            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled","true");
                YT.deploy.taskLogDetail.query(pageNum, pageSize);
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
                YT.deploy.taskLogDetail.query(1,$("#pageSize").val());
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
            YT.deploy.util.paginationInit(d.data, YT.deploy.taskLogDetail.query);

        },
    }
    $.extend(YT.deploy, common);

    YT.deploy.taskLogDetail.currData = null,
    YT.deploy.taskLogDetail = {
        query: function (pageNum, pageSize) {
            var checked = $("#model").attr("checked");
            // var checkState = checked ? "prod" : "test";
            // debugger;
            var params = YT.deploy.util.getFormParams("#taskLogDetailListForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["month"] = YT.deploy.taskLogDetail.currData.month;
            params["model"] = YT.deploy.taskLogDetail.currData.model;
            params["batchNo"] = YT.deploy.taskLogDetail.currData.batchNo;

            var ext_data = $.extend(params, {
                title: YT.deploy.taskLogDetail.currData.title,
                msTitle: YT.deploy.taskLogDetail.currData.msTitle
            });
            YT.deploy.route("/taskLog/selectListByBatchNo", params, "/taskLog/detailList.html", ext_data);
        },

    }

})(window.YunTao);
