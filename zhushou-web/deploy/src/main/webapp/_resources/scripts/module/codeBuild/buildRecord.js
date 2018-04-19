/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.buildRecord) {
        YT.deploy.buildRecord = {};
    }

    var common = {

        formId: "buildRecordForm",

        route_callback: function (d,data) {
            console.log("template list after render call");
            //组件初始化之后

            this.formId = $("form:first").attr("id");

            YT.deploy.util.reqGet("/data/projectList",{},function(d){
                var projectList = d.data;
                YT.deploy.util.initSelect(projectList,"id","name","projectId",data.projectId);
            });


            $("a[name='btnDel']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.buildRecord.del(dataId);
            });

            $("a[name='btnDownload']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.buildRecord.download(dataId);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.buildRecord.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.buildRecord.openNewWin();
            });



            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.buildRecord.save();
            });


            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.buildRecord.openEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.buildRecord.query);


        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.buildRecord = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "buildRecordForm";
            var params = YT.deploy.util.getFormParams("#buildRecordForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"构建记录列表"});
            YT.deploy.route("/buildRecord/list", params, "/codeBuild/buildRecordList.html", ext_data);
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
                YT.deploy.buildRecord.genEndTime($(this).val());
            });
            $("#startTime").keyup(function () {
                // console.log($(this).val());
                YT.deploy.buildRecord.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function () {
                // console.log($(this).val());
                YT.deploy.buildRecord.genEndTime($("#startTime").val());
            });

            ;


        },

        openNewWin: function () {
            var param = {tp_title: "构建记录创建", dataList: null};
            $.get("/project/buildRecordNew.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.buildRecord.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "buildRecordNewForm";
                YT.deploy.buildRecord.initNewEdit(param);
            });
        },

        openEditWin: function (id) {
            var param = {tp_title: "构建记录修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/buildRecord/detail", params, function (d) {
                // //debugger;
                param["domain"] = d.data;
                $.get("/project/buildRecordEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.buildRecord.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "buildRecordNewForm";
                    YT.deploy.buildRecord.initNewEdit(d.data);
                });
            });
        },

        

        save: function () {
            var params = YT.deploy.util.getFormParams("#buildRecordNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/buildRecord/update", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.buildRecord.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/buildRecord/save", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.buildRecord.query(1);
                });
            }
        },


        del: function (id) {
            if (!confirm("您确认需要删除吗？")) {
                return;
            }
            YT.deploy.util.reqPost("/buildRecord/deleteById", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.buildRecord.query(1);
            });
        },
        download: function (id) {
            jQuery.download = function(url, id){
                $('<form action="'+url+'" method="post">' +  // action请求路径及推送方法
                    '<input type="text" name="id" id="id" value="'+id+'"/>' + //
                    '</form>')
                    .appendTo('body').submit().remove();
            };
            $.download("/attachment/download",id);
        },

    }

})(window.YunTao);
