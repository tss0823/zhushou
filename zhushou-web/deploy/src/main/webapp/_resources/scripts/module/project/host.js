/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.host) {
        YT.deploy.host = {};
    }

    var common = {

        formId: "hostForm",

        route_callback: function (d,data) {
            console.log("template list after render call");
            //组件初始化之后

            this.formId = $("form:first").attr("id");

            YT.deploy.util.initEnumSelect("logModel", "model", data.model);


            $("a[name='btnDel']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.host.del(dataId);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.host.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.host.openNewWin();
            });



            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.host.save();
            });


            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.host.openEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.host.query);


        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.host = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "hostForm";
            var params = YT.deploy.util.getFormParams("#hostForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"变量列表"});
            YT.deploy.route("/host/list", params, "/project/hostList.html", ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);

            YT.deploy.util.initEnumSelect("logModel", "model", data.model);

            //组件初始化之后
            // var appList = YT.deploy.data.appList;
            // YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            // YT.deploy.util.reqGet("/data/atTemplateList",{},function(d){
            //     var templateList = d.data;
            //     YT.deploy.util.initSelect(templateList,"id","name","templateId",data.templateId);
            // });


            $("#" + YT.deploy.formId).find("#startTime").datetimepicker({
                lang: 'ch',
                format: 'Y-m-d H:i',
                todayBtn: true,
                autoclose: true,
            });
            $("#" + YT.deploy.formId).find("#endTime").datetimepicker({
                lang: 'ch',
                format: 'Y-m-d H:i',
                todayBtn: true,
                autoclose: true,
            });

            $("#startTime").change(function () {
                // console.log($(this).val());
                YT.deploy.host.genEndTime($(this).val());
            });
            $("#startTime").keyup(function () {
                // console.log($(this).val());
                YT.deploy.host.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function () {
                // console.log($(this).val());
                YT.deploy.host.genEndTime($("#startTime").val());
            });

            ;


        },

        openNewWin: function () {
            var param = {tp_title: "主机创建", dataList: null};
            $.get("/project/hostNew.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.host.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "hostNewForm";
                YT.deploy.host.initNewEdit(param);
            });
        },

        openEditWin: function (id) {
            var param = {tp_title: "主机修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/host/detail", params, function (d) {
                // //debugger;
                param["domain"] = d.data;
                $.get("/project/hostEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.host.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "hostNewForm";
                    YT.deploy.host.initNewEdit(d.data);
                });
            });
        },

        

        save: function () {
            var params = YT.deploy.util.getFormParams("#hostNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/host/update", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.host.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/host/save", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.host.query(1);
                });
            }
        },


        del: function (id) {
            if (!confirm("您确认需要删除吗？")) {
                return;
            }
            YT.deploy.util.reqPost("/host/deleteById", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.host.query(1);
            });
        },



    }

})(window.YunTao);
