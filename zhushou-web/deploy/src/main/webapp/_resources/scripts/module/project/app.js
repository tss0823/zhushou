/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.app) {
        YT.deploy.app = {};
    }

    var common = {

        formId: "appForm",

        route_callback: function (d,data) {
            console.log("template list after render call");
            //组件初始化之后

            this.formId = $("form:first").attr("id");

            // YT.deploy.util.initEnumSelect("logModel", "model", data.model);

            YT.deploy.util.reqGet("/data/projectList",{},function(d){
                var projectList = d.data;
                YT.deploy.util.initSelect(projectList,"id","name","projectId",data.projectId);
            });

            YT.deploy.util.reqGet("/data/hostList",{},function(d){
                YT.deploy.app.hostList = d.data;
            });

            $("a[name='btnDel']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.app.del(dataId);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.app.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.app.openNewWin();
            });



            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.app.save();
            });


            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.app.openEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.app.query);


        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.app = {

        openWinObj: null,

        hostList:null,

        query: function (pageNum) {
            YT.deploy.formId = "appForm";
            var params = YT.deploy.util.getFormParams("#appForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"应用列表"});
            YT.deploy.route("/app/list", params, "/project/appList.html", ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);


            //组件初始化之后
            // var appList = YT.deploy.data.appList;
            // var docList = YT.deploy.data.docList;
            // YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            YT.deploy.util.reqGet("/data/projectList",{},function(d){
                var projectList = d.data;
                YT.deploy.util.initSelect(projectList,"id","name","projectId",data.projectId);
            });




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
                YT.deploy.app.genEndTime($(this).val());
            });
            $("#startTime").keyup(function () {
                // console.log($(this).val());
                YT.deploy.app.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function () {
                // console.log($(this).val());
                YT.deploy.app.genEndTime($("#startTime").val());
            });

            ;


        },

        openNewWin: function () {
            var param = {tp_title: "应用创建", dataList: null};
            $.get("/project/appNew.html", function (source) {
                var render = template.compile(source);
                param["hostList"] = YT.deploy.app.hostList;
                var html = render(param);
                YT.deploy.app.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "appNewForm";
                YT.deploy.app.initNewEdit(param);
            });
        },

        openEditWin: function (id) {
            var param = {tp_title: "应用修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/app/detail", params, function (d) {
                // //debugger;
                param["domain"] = d.data;
                param["hostList"] = YT.deploy.app.hostList;
                $.get("/project/appEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.app.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    }).on('shown.bs.modal', function (e) {
                        $(".modal-dialog").prop("style", "width:70%;height:85%")
                        YT.deploy.formId = "appNewForm";
                        YT.deploy.app.initNewEdit(d.data);
                        $("input[id='chkTestHost']").each(function(index,item){
                            for(var key in d.data.testHostList){
                                var dbData = d.data.testHostList[key];
                                if(dbData.id == $(item).val()){
                                    $(item).prop("checked",true);
                                }
                            }
                        });
                        $("input[id='chkProdHost']").each(function(index,item){
                            for(var key in d.data.prodHostList){
                                var dbData = d.data.prodHostList[key];
                                if(dbData.id == $(item).val()){
                                    $(item).prop("checked",true);
                                }
                            }
                        });
                    });
                });
            });
        },

        

        save: function () {
            var params = YT.deploy.util.getFormParams("#appNewForm");
            //get hostIds
            var testHostIds = [];
            var prodHostIds = [];
            $("input[id='chkTestHost']").each(function(index,item){
                if($(item).prop("checked")){
                    testHostIds.push($(item).val());
                }
            });
            $("input[id='chkProdHost']").each(function(index,item){
                if($(item).prop("checked")){
                    prodHostIds.push($(item).val());
                }
            });
            params["testHostIds"] = testHostIds.join(",");
            params["prodHostIds"] = prodHostIds.join(",");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/app/update", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.app.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/app/save", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.app.query(1);
                });
            }
        },


        del: function (id) {
            if (!confirm("您确认需要删除吗？")) {
                return;
            }
            YT.deploy.util.reqPost("/app/deleteById", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.app.query(1);
            });
        },



    }

})(window.YunTao);
