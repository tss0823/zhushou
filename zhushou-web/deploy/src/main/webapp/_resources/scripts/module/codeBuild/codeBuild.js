/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.codeBuild) {
        YT.deploy.codeBuild = {};
    }

    var common = {

        formId: null,

        route_callback: function (d) {
            console.log("template list after render call");
            //组件初始化之后

            var data = d.data;

            this.formId = $("form:first").attr("id");

            //

            //tab
            $("div[id='tabs']").tabs();

            //调出http 执行详情
            $("a[name='enterHttpExecView']").click(function () {
                var dataId = $(this).attr("data");
                var item = $("tr[data='trChildItem_" + dataId + "']").first();
                var state = ($(item).css("display") == "none");
                //所有的hide
                $("tr[name='trChildItem']").hide();
                if (state) {
                    $(item).show();
                } else {
                    $(item).hide();
                }
            });
            $("div[name='enterHttpExecView']").click(function () {
                var dataId = $(this).attr("data");
                var item = $("tr[data='trChildItem_" + dataId + "']").first();
                var state = ($(item).css("display") == "none");
                //所有的hide
                $("tr[name='trChildItem']").hide();
                if (state) {
                    $(item).show();
                } else {
                    $(item).hide();
                }
            });
            // $("a[name='btnActiveDel']").click(function () {
            //     var dataId = $(this).attr("data");
            //     YT.deploy.codeBuild.delActive(dataId);
            // });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.codeBuild.query(pageNum);
            });

            $("#btnNew").click(function () {
                YT.deploy.codeBuild.openNewWin();
            });

            $("#btnBuildSql").click(function () {
                YT.deploy.codeBuild.openMsgWin("生成SQL","/codeBuild/buildSql");
            });

            $("#btnBuildApp").click(function () {
                YT.deploy.codeBuild.openMsgWin("生成代码","/codeBuild/buildApp");
            });


            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.codeBuild.save();
            });

            //保存属性
            $(document).off("click", "#btnSaveProperty");
            $(document).on("click", "#btnSaveProperty", function () {
                $(this).prop("disabled",true);
                YT.deploy.codeBuild.propertySave(this);
            });

            // //保存属性列表
            // $(document).off("click", "#btnSaveActiveCollect");
            // $(document).on("click", "#btnSaveActiveCollect", function () {
            //     YT.deploy.codeBuild.saveActiveCollect();
            // });

            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.codeBuild.openEditWin(id);
            });

            $("a[name='btnDel']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.codeBuild.delEntity(id);
            });

            //enter collect active
            $("a[name='enterPropertyEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.codeBuild.openPropertyEditWin(id);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.codeBuild.query);

            //属性编辑操作
            if (YT.deploy.formId == "codeBuildPropertyForm") {

                var $form = $("#" + YT.deploy.formId);
                $form.find("select[name='dataType']").each(function (index, item) {
                    var id = $(this).prop("id");
                    var value = $(this).attr("data");
                    YT.deploy.util.initEnumSelect("javaDataType", id, value);
                });


            }

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.codeBuild = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "codeBuildForm";
            var params = YT.deploy.util.getFormParams("#codeBuildForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"实体列表"});
            YT.deploy.route("/codeBuild/list", params, "/codeBuild/list.html", ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);


            if (YT.deploy.formId == "codeBuildMsgForm") {

            }
            if (YT.deploy.formId == "codeBuildPropertyForm") {
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

                //添加
                $(document).off("click", "button[id='btnAddItem']");
                $(document).on("click", "button[id='btnAddItem']", function () {
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
                $(document).off("click", "a[name='btnCopy']");
                $(document).on("click", "a[name='btnCopy']", function () {
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
            $.get("/codeBuild/new.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.codeBuild.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "codeBuildForm";
                YT.deploy.codeBuild.initNewEdit(param);
            });
        },

        openMsgWin: function (title,url) {
            var idArray = [];
            $(":checkbox[id='chkForm']").each(function(index,item){
                if($(item).prop("checked")){
                    idArray.push($(item).val());
                }
            });
            if(idArray.length == 0){
                alert("请选择您要操作的项");
                return;
            }
            var params = {ids:idArray.join("")};
            YT.deploy.util.reqPost(url, params, function (d) {
                var param = {tp_title: title, msg: d.data};
                $.get("/codeBuild/msg.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.codeBuild.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:60%;height:55%")
                    YT.deploy.formId = "codeBuildMsgForm";
                    YT.deploy.codeBuild.initNewEdit(param);
                });

            });
        },

        

        openEditWin: function (id) {
            var param = {tp_title: "实体修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/codeBuild/entityDetail", params, function (d) {
                // debugger;
                param["domain"] = d.data;
                $.get("/codeBuild/edit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.codeBuild.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "codeBuildForm";
                    YT.deploy.codeBuild.initNewEdit(d.data);
                });
            });
        },

        delEntity:function(id){
            if (!confirm("您确认需要删除吗？")) {
                return;
            }

            YT.deploy.util.reqPost("/codeBuild/entityDelete", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.codeBuild.query(1);
            });
        },

        openPropertyEditWin: function (id) {
            var param = {tp_title: "属性列表编辑", entityId: id};
            YT.deploy.util.reqGet("/codeBuild/propertyList", param, function (d) {
                // debugger;
                param["propertyList"] = d.data;
                $.get("/codeBuild/propertyEdit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.codeBuild.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:80%;height:85%")
                    YT.deploy.formId = "codeBuildPropertyForm";
                    YT.deploy.codeBuild.initNewEdit(d.data);
                });
            });
        },
       

        save: function () {
            var params = YT.deploy.util.getFormParams("#codeBuildNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/codeBuild/entityUpdate", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.codeBuild.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/codeBuild/entitySave", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.codeBuild.query(1);
                });
            }
        },

        propertySave: function (parent) {
            var $table = $(document).find("#tbPropertyContent");
            var entityId = $("#entityId").val();
            var params = {id:entityId};
            var arrIndex = 0;
            $table.find("tr[name='dataItem']").each(function (index, item) {
                var code = $(item).find("input[id='enName']").val();
                if (!code) {
                    return true;
                }
                params["propertyList[" + arrIndex + "].enName"] = code;
                params["propertyList[" + arrIndex + "].cnName"] = $(item).find("input[id='cnName']").val();
                params["propertyList[" + arrIndex + "].dataType"] = $(item).find("select[name='dataType']").val();
                params["propertyList[" + arrIndex + "].length"] = $(item).find("input[id='length']").val();
                params["propertyList[" + arrIndex + "].defaultValue"] = $(item).find("input[id='defaultValue']").val();
                params["propertyList[" + arrIndex + "].isNull"] = $(item).find("input[name='isNull']").prop("checked") ? "1" : 0;
                params["propertyList[" + arrIndex + "].primaryKey"] = $(item).find("input[name='primaryKey']").prop("checked") ? "1" : "0";
                arrIndex++;
            });

            YT.deploy.util.reqPost("/codeBuild/propertySave", params, function (d) {
                alert("保存成功");
                $(parent).prop("disabled",false);
            });
        },


    }

})(window.YunTao);
