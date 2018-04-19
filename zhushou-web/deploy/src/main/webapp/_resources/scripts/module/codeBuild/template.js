/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.template) {
        YT.deploy.template = {};
    }

    var common = {

        formId: "templateForm",

        route_callback: function (d,data) {
            console.log("template list after render call");
            //组件初始化之后

            this.formId = $("form:first").attr("id");

            //cacheType
            YT.deploy.util.initEnumSelect("templateType", "type", data.type);


            $("a[name='btnDel']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.template.del(dataId);
            });
            $("a[name='btnDownload']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.template.download(dataId);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.template.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.template.openNewWin();
            });



            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.template.save();
            });


            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.template.openEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.template.query);


        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.template = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "templateForm";
            var params = YT.deploy.util.getFormParams("#templateForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"模版列表"});
            YT.deploy.route("/template/list", params, "/codeBuild/templateList.html", ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);

            YT.deploy.util.initEnumSelect("templateType", "type", data.type);

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
                YT.deploy.template.genEndTime($(this).val());
            });
            $("#startTime").keyup(function () {
                // console.log($(this).val());
                YT.deploy.template.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function () {
                // console.log($(this).val());
                YT.deploy.template.genEndTime($("#startTime").val());
            });

            // debugger; 不能放在此处加载，模版没有完成
//             $('#fileupload').fileupload({
//                 url:'/attachment/save',
//                 dataType: 'json',
//                 add: function (e, data) {
// //                debugger;
//
//                     $("<p/>").text(data.files[0].name).append("#files");
//                 },
//
//                 done: function (e, data) {
//                   debugger;
//                     $("#attachmentId").val(data.result.data);
//                 },
//
//                 progressall: function (e, data) {
//                     var progress = parseInt(data.loaded / data.total * 100, 10);
//                     $('#progress .progress-bar').css(
//                         'width',
//                         progress + '%'
//                     );
//                 }
//             });



        },

        openNewWin: function () {
            var param = {tp_title: "模版创建", dataList: null};
            $.get("/codeBuild/templateNew.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.template.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "templateNewForm";
                YT.deploy.template.initNewEdit(param);
            });
        },

        openEditWin: function (id) {
            var param = {tp_title: "模版修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/template/detail", params, function (d) {
                // //debugger;
                param["domain"] = d.data;
                $.get("/codeBuild/templateEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.template.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "templateNewForm";
                    YT.deploy.template.initNewEdit(d.data);
                });
            });
        },

        

        save: function () {
            var params = YT.deploy.util.getFormParams("#templateNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/template/update", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.template.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/template/save", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.template.query(1);
                });
            }
        },


        del: function (id) {
            if (!confirm("您确认需要删除吗？")) {
                return;
            }
            YT.deploy.util.reqPost("/template/deleteById", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.template.query(1);
            });
        },

        download: function (id) {
            // $.ajax({
            //     url: "/attachment/download",
            //     type: 'post',
            //     data: {'id': id},
            //     success: function (data, status, xhr) {
            //         console.log("Download file DONE!");
            //     }
            // });
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
