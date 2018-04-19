/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.project) {
        YT.deploy.project = {};
    }

    var common = {

        formId: "projectForm",

        route_callback: function (d) {
            console.log("template list after render call");
            //组件初始化之后

            var data = d.data;

            this.formId = $("form:first").attr("id");

            //
            YT.deploy.util.reqGet("/data/templateList",{},function(d){
                YT.deploy.util.initSelect(d.data,"id","name","templateId",data.templateId);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.project.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.project.openNewWin();
            });

            // $("#btnBuildSql").click(function () {
            //     $(this).prop("disabled",true);
            //     YT.deploy.project.openMsgWin(this,"生成SQL","/project/buildSql");
            // });
            //
            // $("#btnBuildApp").click(function () {
            //     $(this).prop("disabled",true);
            //     YT.deploy.project.openMsgWin(this,"生成代码","/project/buildApp");
            // });

            $("#btnHostQuery").click(function () {
                YT.deploy.project.queryHostList();
            });

            $("#btnAppQuery").click(function () {
                YT.deploy.project.queryAppList();
            });


            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.project.save();
            });

            //保存属性
            $(document).off("click", "#btnSaveProperty");
            $(document).on("click", "#btnSaveProperty", function () {
                $(this).prop("disabled",true);
                YT.deploy.project.propertySave(this);
            });

            // //保存属性列表
            // $(document).off("click", "#btnSaveActiveCollect");
            // $(document).on("click", "#btnSaveActiveCollect", function () {
            //     YT.deploy.project.saveActiveCollect();
            // });

            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.project.openEditWin(id);
            });

            $("a[name='btnCopy']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.project.copyEntity(id);
            });

            $("a[name='btnDel']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.project.del(id);
            });

            //enter property edit
            $("a[name='enterProjectConfigEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.project.openProjectConfigEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.project.query);

            //配置项编辑操作
            if (YT.deploy.formId == "projectConfigForm") {

                var $form = $("#" + YT.deploy.formId);
                // $form.find("select[name='dataType']").each(function (index, item) {
                //     var id = $(this).prop("id");
                //     var value = $(this).attr("data");
                //     YT.deploy.util.initEnumSelect("javaDataType", id, value);
                // });


            }

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.project = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "projectForm";
            var params = YT.deploy.util.getFormParams("#projectForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"项目列表"});
            YT.deploy.route("/project/list", params, "/project/list.html", ext_data);
        },

        queryHostList: function () {
            var params = {};
            var ext_data = $.extend(params, {tp_title: "主机列表"});
            YT.deploy.route("/host/list", params, "/project/hostList.html", ext_data);
            YT.deploy.formId = "hostForm";
            // YT.deploy.atTemplate.initNewEdit(ext_data);
        },

        queryAppList: function () {
            var params = {};
            var ext_data = $.extend(params, {tp_title: "应用列表"});
            YT.deploy.route("/app/list", params, "/project/appList.html", ext_data);
            YT.deploy.formId = "appForm";
            // YT.deploy.atTemplate.initNewEdit(ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);

            YT.deploy.util.reqGet("/data/templateList",{},function(d){
                YT.deploy.util.initSelect(d.data,"id","name","templateId",data.templateId);
            });


            if (YT.deploy.formId == "projectForm") {

            }
            if (YT.deploy.formId == "projectConfigForm") {
                var $form = $("#" + YT.deploy.formId);
                $form.find("select[name='dataType']").each(function (index, item) {
                    var id = $(this).prop("id");
                    var value = $(this).attr("data");
                    YT.deploy.util.initEnumSelect("javaDataType", id, value);
                });
                $form.find("input[name='isNull']").each(function (index, item) {
                    var id = $(this).prop("id");
                    var value = $(this).attr("data");
                    $("#"+id).prop("checked",value == "1" ? true : false);
                    // YT.deploy.util.initEnumSelect("yesNoIntType", id, value);
                });
                $form.find("input[name='primaryKey']").each(function (index, item) {
                    var id = $(this).prop("id");
                    var value = $(this).attr("data");
                    $("#"+id).prop("checked",value == "1" ? true : false);
                    // YT.deploy.util.initEnumSelect("yesNoIntType", id, value);
                });

                //添加固定项
                $(document).off("click", "button[id='btnAddFixItem']");
                $(document).on("click", "button[id='btnAddFixItem']", function () {
                    // //debugger;
                    var $table = $(document).find("#tbPropertyContent");
                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $tr.find("input[id='name']").val("jdbc.driverClassName");
                    $tr.find("input[id='value']").val("com.mysql.jdbc.Driver");
                    $table.append($tr);

                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $tr.find("input[id='name']").val("jdbc.url");
                    $tr.find("input[id='value']").val("jdbc:mysql://localhost:3306/zhushou");
                    $table.append($tr);

                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $tr.find("input[id='name']").val("jdbc.username");
                    $tr.find("input[id='value']").val("root");
                    $table.append($tr);

                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $tr.find("input[id='name']").val("jdbc.password");
                    $tr.find("input[id='value']").val("123456");
                    $table.append($tr);

                });

                //添加
                $(document).off("click", "button[id='btnAddItem']");
                $(document).on("click", "button[id='btnAddItem']", function () {
                    // //debugger;
                    // debugger;
                    var $table = $(document).find("#tbPropertyContent");
                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $table.append($tr);
                });

                //上移
                $(document).off("click", "a[name='itemArrowUp']");
                $(document).on("click", "a[name='itemArrowUp']", function () {
                    var $thisTr = $(this).parents("tr[name='dataItem']");
                    var $prevTr = $thisTr.prev();
                    $thisTr.after($prevTr);
                    // $prevTr.before($thisTr);
                });

                //下移
                $(document).off("click", "a[name='itemArrowDown']");
                $(document).on("click", "a[name='itemArrowDown']", function () {
                    var $thisTr = $(this).parents("tr[name='dataItem']");
                    var $nextTr = $thisTr.next();
                    $nextTr.after($thisTr);
                });


                //删除
                $(document).off("click", "a[name='itemRemove']");
                $(document).on("click", "a[name='itemRemove']", function () {
                    if (!confirm("您确要删除吗？")) {
                        return false;
                    }
                    $(this).parents("tr[name='dataItem']").remove();
                });

                //复制
                $(document).off("click", "a[name='itemCopy']");
                $(document).on("click", "a[name='itemCopy']", function () {
                    var $tr = $(this).parents("tr[name='dataItem']").clone();
                    var $table = $(document).find("#tbPropertyContent");
                    $tr.show();
                    $table.append($tr);
                });
            }
            //组件初始化之后

        },

        openNewWin: function () {
            var param = {tp_title: "实体创建", dataList: null};
            $.get("/project/new.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.project.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "projectNewForm";
                // debugger;
                YT.deploy.project.initNewEdit(param);
            });
        },



        

        openEditWin: function (id) {
            var param = {tp_title: "实体修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/project/detail", params, function (d) {
                // //debugger;
                param["domain"] = d.data;
                $.get("/project/edit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.project.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "projectNewForm";
                    YT.deploy.project.initNewEdit(d.data);
                });
            });
        },

        copyEntity:function(id){
            if (!confirm("您确认需要复制吗？")) {
                return;
            }

            YT.deploy.util.reqPost("/project/entityCopy", {id:id}, function (d) {
                alert("复制成功");
                YT.deploy.project.query(1);
            });
        },
        del:function(id){
            if (!confirm("您确认需要删除吗？")) {
                return;
            }

            YT.deploy.util.reqPost("/project/deleteById", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.project.query(1);
            });
        },

        openProjectConfigEditWin: function (id) {
            var param = {tp_title: "配置列表编辑", projectId: id};
            YT.deploy.util.reqGet("/project/projectConfigList", param, function (d) {
                // //debugger;
                param["configList"] = d.data;
                $.get("/project/projectConfigEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.project.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:80%;height:85%")
                    YT.deploy.formId = "projectConfigForm";
                    YT.deploy.project.initNewEdit(d.data);
                });
            });
        },
       

        save: function () {
            var params = YT.deploy.util.getFormParams("#projectNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/project/update", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.project.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/project/save", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.project.query(1);
                });
            }
        },

        propertySave: function (parent) {
            var $table = $(document).find("#tbPropertyContent");
            var projectId = $("#projectId").val();
            var params = {projectId:projectId};
            var arrIndex = 0;
            $table.find("tr[name='dataItem']").each(function (index, item) {
                var code = $(item).find("input[id='name']").val();
                if (!code) {
                    return true;
                }
                params["configList[" + arrIndex + "].name"] = code;
                params["configList[" + arrIndex + "].orderBy"] = arrIndex+1;
                params["configList[" + arrIndex + "].value"] = $(item).find("input[id='value']").val();
                arrIndex++;
            });

            YT.deploy.util.reqPost("/project/saveConfig", params, function (d) {
                alert("保存成功");
                $(parent).prop("disabled",false);
            });
        },


    }

})(window.YunTao);
