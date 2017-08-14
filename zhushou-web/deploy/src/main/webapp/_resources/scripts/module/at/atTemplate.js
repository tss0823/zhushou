/**
 *
 * Created by shan on 2016/4/7.
 */
(function(YT){
    if (!YT.deploy.atTemplate) {
        YT.deploy.atTemplate = {};
    }

    var common = {

        formId : null,

        route_callback : function (d) {
            console.log("template list after render call");
            //组件初始化之后

            var data = d.data;

            this.formId = $("form:first").attr("id");

            YT.deploy.util.initEnumSelect("logModel","model",data.model);

            //
            YT.deploy.atTemplate.init();

            //注册事件
            $("a[id='enterView']").each(function(index,item){
                $(this).click(function(){
                    // var appId = $(item).attr("appId");
                    // var model = $(item).attr("model");
                    // var appName = $(item).attr("appName");
                    // var data = {title:"发布节点",titleColor:$(item).attr("color"),msTitle:" 【 "+appName+" 】 > "+model};
                    // data["model"] = model;
                    // data["appName"] = appName;
                    // YT.deploy.route("/host/getListByAppAndModel",{appId:appId,model:model},"appHost.html",data);
                });
            });

            $("#btnQuery").click(function(){
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.atTemplate.query(pageNum);
            });

            $("#btnNewTemplate").click(function(){
                YT.deploy.atTemplate.openNewWin();
            });

            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.atTemplate.save();
            });

            //保存活动采集
            $(document).off("click", "#btnSaveActiveCollect");
            $(document).on("click", "#btnSaveActiveCollect", function () {
                YT.deploy.atTemplate.saveActiveCollect();
            });

            // //保存活动列表
            // $(document).off("click", "#btnSaveActiveCollect");
            // $(document).on("click", "#btnSaveActiveCollect", function () {
            //     YT.deploy.atTemplate.saveActiveCollect();
            // });

            //活动执行
            $(document).off("click", "#btnActiveStart");
            $(document).on("click", "#btnActiveStart", function () {
                YT.deploy.atTemplate.activeStart();
            });

            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[id='enterEdit']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.atTemplate.openEditWin(id);
            });

            //enter collect active
            $("a[id='enterActiveCollect']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.atTemplate.openActiveCollectWin(id);
            });

            //enter active edit
            $("a[id='enterActiveEdit']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.atTemplate.openActiveEditWin(id);
            });

            //enter collect start
            $("a[id='enterStart']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.atTemplate.enterStartWin(id);
            });

            //enter active execute his
            $("a[id='enterHis']").click(function(){
                var id = $(this).attr("data");
                YT.deploy.atTemplate.enterHisWin(id);
            });

            //分页信息init
            YT.deploy.util.paginationInit(d.data,YT.deploy.atTemplate.query);

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.atTemplate = {

        openWinObj : null,

        query:function(pageNum){
            var params = YT.deploy.util.getFormParams("#templateForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {title:"模板列表"});
            YT.deploy.route("/atTemplate/list",params,"/at/template.html",ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#"+YT.deploy.formId);

            YT.deploy.util.initEnumSelect("logModel","model",data.model);

            //组件初始化之后
            var appList = YT.deploy.data.appList;
            // var docList = YT.deploy.data.docList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            $("#"+YT.deploy.formId).find("#startTime").datetimepicker({
                lang:'ch',
                format:'Y-m-d H:i',
                todayBtn : true,
                autoclose : true,
            }) ;
            $("#"+YT.deploy.formId).find("#endTime").datetimepicker({
                lang:'ch',
                format:'Y-m-d H:i',
                todayBtn : true,
                autoclose : true,
            }) ;

            $("#startTime").change(function(){
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($(this).val());
            });
            $("#startTime").keyup(function(){
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function(){
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($("#startTime").val());
            });

        },

        openNewWin: function () {
            var param = {tp_title: "模版创建", dataList: null};
            $.get("/at/new.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "templateNewForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },

        openEditWin: function (id) {
            var param = {tp_title: "模版修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/atTemplate/detail", params, function (d) {
                // debugger;
                param["domain"] = d.data;
                $.get("/at/edit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "templateNewForm";
                    YT.deploy.atTemplate.initNewEdit(d.data);
                });
            });
        },

        openActiveCollectWin: function (id) {
            var param = {tp_title: "活动采集", templateId: id};
            $.get("/at/activeCollect.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "activeCollectForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },
        openActiveEditWin: function (id) {
            var param = {tp_title: "模版修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/atTemplate/detail", params, function (d) {
                // debugger;
                param["domain"] = d.data;
                $.get("/at/edit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "templateNewForm";
                    YT.deploy.atTemplate.initNewEdit(d.data);
                });
            });
        },

        enterStartWin: function (id) {
            var params = {id: id};
            var ext_data = $.extend(params, {tp_title: "活动开始"});
            YT.deploy.route("/atTemplate/detail", params, "/at/activeStart.html", ext_data);
        },

        enterHisWin: function (id) {
            var param = {tp_title: "活动采集", templateId: id};
            $.get("/at/activeCollect.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "activeCollectForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },

        save: function () {
            var params = YT.deploy.util.getFormParams("#templateNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/atTemplate/updateTemplate", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.atTemplate.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/atTemplate/saveTemplate", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.atTemplate.query(1);
                });
            }
        },

        saveActiveCollect: function () {
            var params = YT.deploy.util.getFormParams("#activeCollectForm");
            YT.deploy.util.reqPost("/atTemplate/collect", params, function (d) {
                alert("保存成功");
                $(".bootbox-close-button").trigger("click");
                YT.deploy.atTemplate.query(1);
            });
        },
        activeStart: function () {
            var params = YT.deploy.util.getFormParams("#activeStartForm");
            YT.deploy.util.reqPost("/atTemplate/start", params, function (d) {
                alert("执行开始");
            });
        },

        /**
         * 生成结束时间
         * @param startTimeVal
         */
        genEndTime: function (startTimeVal) {
            // debugger;
            if(startTimeVal.length != 16){
                return;
            }
            var rangeMinute = $("#rangeMinute").val();
            if(!rangeMinute){
                return;
            }
            // var hour = startTimeVal.substr(11,2);
            // var minute = startTimeVal.substr(14,2);
            var endTimeObj = moment(startTimeVal, "YYYY-MM-DD HH:mm").add(rangeMinute,"m");
            var endTimeVal = endTimeObj.format("YYYY-MM-DD HH:mm");
            $("#endTime").val(endTimeVal);
        },

        init:function(){
            //注册服务状态监控事件
            YT.deploy.eventProcess.addListener("test_active_http_execute", function (msgObj) {
                debugger;
                var dataObj = JSON.parse(msgObj.data);

                //end
            });
        }

    }

})(window.YunTao);
