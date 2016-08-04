/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.activeSelect) {
        YT.deploy.activeSelect = {};
    }

    var common = {

        route_callback: function (d,data) {
            console.log("active select after render call");
            
            //组件初始化之后
            $("#startTime").datetimepicker({
                format:"Y-m-d H:i:s",
                lang:"zh"
            });

            $("#endTime").datetimepicker({
                format:"Y-m-d H:i:s",
                lang:"zh"
            });


            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled","true");
                YT.deploy.activeSelect.query(pageNum, pageSize);
            });

            $("#btnSelect").click(function () {
                YT.deploy.activeSelect.selectItem();
                $(".bootbox-close-button").trigger("click");
            });
            

            //parameters
            $("button[id='btnParameter']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.activeSelect.openParameterDialog(id);
            });
            
            //btnResponse
            $("button[id='btnResponse']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.activeSelect.openResponseDialog(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.activeSelect.query);

        },
    }
    $.extend(YT.deploy, common);



    YT.deploy.activeSelect = {
        query: function (pageNum, pageSize) {
            var params = YT.deploy.util.getFormParams("#activeSelectForm");
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            // var ext_data = $.extend(params, {title: "活动选择列表"});
            YT.deploy.templateNew.selActive(params);
            // YT.deploy.route("/appLog/list", params, "/at/activeSelect.html", ext_data);
        },

        selectItem: function () {
            $("#chkRow").each(function(index,item){
                if ($(item).find(":checkbox").prop("checked")) {
                    
                }
            });
        },

        openParameterDialog:function(id){
            var params = {stackId:id,month:$("#month").val(),model:$("#model").val()};
            YT.deploy.util.reqGet("/activeSelect/findMasterByStackId", params, function (d) {
                debugger;
                var jsonObj = JSON.parse(d.data.parameters);
                var dataList = [];
                for(var key in jsonObj){
                    var val = jsonObj[key];
                    dataList.push({key:key,value:val});
                }
                var param = {title:"请求参数列表",dataList:dataList};
                $.get("/log/msgKeyVal.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width:"800px",
                    });
                    $(".modal-dialog").prop("style","width:70%;height:85%")

                });
            });

        },

        openResponseDialog:function(id){
            var params = {stackId:id,month:$("#month").val(),model:$("#model").val()};
            YT.deploy.util.reqGet("/activeSelect/findMasterByStackId", params, function (d) {
                var param = {title:"返回结果",logText:d.data.resFormatMsg};
                $.get("/log/msg.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width:"800px",
                    });
                    $(".modal-dialog").prop("style","width:70%;height:85%")

                });
            });
        },

    }

})(window.YunTao);
