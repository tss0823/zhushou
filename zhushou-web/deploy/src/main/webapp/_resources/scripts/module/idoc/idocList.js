/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.idocList) {
        YT.deploy.idocList = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("app list after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var docList = YT.deploy.data.docList;

            //doc select init
            var branchValArray = [];
            for (var i = 0; i < docList.length; i++) {
                var doc = docList[i];
                branchValArray.push("<option value='" + doc.url + "'>");
                branchValArray.push(doc.name);
                branchValArray.push("\n");
                branchValArray.push(doc.url);
                branchValArray.push("</option>");
            }
            $("#selName").append(branchValArray.join(""));
            $('#selName').chosen({
                search_contains: true,
                // disable_search_threshold: 10
            });
            $('#selName').change(function () {
                $("#urlLike").val($(this).val());
                // $("#btnQuery").trigger("click");
            });
            //end

            var enums = YT.deploy.data.enums;

            //moduleType
            YT.deploy.util.initEnumSelect(enums.moduleType, "module", data.module);


            // if($("#resNewForm").length == 1) {  //资源文档
            //     YT.deploy.idoc.resDocMDE = new SimpleMDE({
            //         element: document.getElementById("resDoc"),
            //         insertTexts: {
            //             horizontalRule: ["", "\n\n-----\n\n"],
            //             image: ["![](http://", ")"],
            //             link: ["[<a href='", "' target='_blank'></a>](http://)"],
            //             table: ["", "\n\n| Column 1 | Column 2 | Column 3 |\n| -------- | -------- | -------- |\n| Text     | Text      | Text     |\n\n"],
            //         },
            //     });
            // }
            //
            // if($("#resViewForm").length == 1) {  //资源文档
            //     debugger;
            //     YT.deploy.idoc.resDocMDE = new SimpleMDE({
            //         element: document.getElementById("resDoc"),
            //     });
            //     var resDocHtmls = YT.deploy.idoc.resDocMDE.options.previewRender(data.resultData);
            //     $("#resDocHtml").html(resDocHtmls);
            // }


            //tab
            $( "div[id='tabs']" ).tabs();

            //select change to query
            $("#appName,#module,#selName").change(function(){
                $("#btnQuery").trigger("click");
            });

            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                $(this).html("查询中...");
                $(this).attr("disabled", "true");
                YT.deploy.idocList.query(pageNum);
            });


            $("#btnQueryEnums").click(function () {
                //debugger;
                var pageNum = 1;
                YT.deploy.idocList.queryEnums(pageNum);
            });

            $("#btnQueryRes").click(function () {
                //debugger;
                var pageNum = 1; 
                YT.deploy.idocList.queryRes(pageNum);
            });

            $("#btnListAll").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                // $(this).html("查询中...");
                // $(this).attr("disabled", "true");
                YT.deploy.idocList.queryExport(pageNum);


            });

            //json
            $("div[id='tabs-2']").each(function(){
                // $parent = $(this);
                var $jsonResData = $(this).find("#jsonResData");
                try{
                    var json = $jsonResData.text();
                    $jsonResData.JSONView(json);
                    // with options
                    $jsonResData.JSONView(json, { collapsed: true });

                    $(this).find("#btnExpand").click(function(){
                        $jsonResData.JSONView('expand');
                    });
                    $(this).find("#btnCollapse").click(function(){
                        $jsonResData.JSONView('collapse');
                    });
                    $(this).find("#btnToggle").click(function(){
                        $jsonResData.JSONView('toggle');
                    });
                }catch (e){
                }
            });
            // $("div[id='jsonResData']").each(function(){
            // });
            //end



            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.idocList.query);

            //创建接口
            $("#btnNewTemplate").click(function () {
                YT.deploy.routeTpl("/idoc/bind.html",{title:"接口新建"});
            });
            //创建资源文档
            $("#btnNewRes").click(function () {
                YT.deploy.routeTpl("/idoc/newRes.html",{title:"资源文档新建"});
            });

            //提交接口
            $("#btnSubmitDoc").click(function () {
                YT.deploy.routeTpl("/idoc/subDoc.html",{title:"接口提交"});
            });

            //提交枚举
            $("#btnSubmitEnum").click(function () {
                YT.deploy.routeTpl("/idoc/subEnum.html",{title:"枚举提交"});
            });

            //UED 上传
            $("#btnUploadUED").click(function () {



            });

            //FRD 上传
            $("#btnUploadFRD").click(function () {

            });

            //tab
            $( "div[id='tabs']" ).tabs();

            //调出详情
            $("a[name='enterView']").click(function () {
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

            //跳转到日志
            $("a[name='enterLogQuery']").click(function () {
                var data = $(this).attr("data");
                var authRes = appData.authMap["enterAppLog"];
                //set userData with queryParams
                YT.deploy.userDataProcess.setValueMap(authRes.tplUrl+"_queryData",{url:data,showAll:true,model:'prod'});
                $(".nav-list").find("#enterAppLog").trigger("click");
                // YT.deploy.route("/idocUrl/getIdocUrlVoById",{id:id},"/idoc/bind.html",{title:"日志"});

            });

            $("a[name='btnEnterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.route("/idocUrl/getIdocUrlVoById",{id:id},"/idoc/bind.html",{title:"修改文档接口"});
            });

            $("a[name='btnHttpReq']").click(function () {
                var id = $(this).attr("data");

                var params = {id:id};
                YT.deploy.util.reqGet("/idocUrl/getIdocUrlVoById", params, function (d) {
                    var data = d.data;
                    data = data || {};

                    //渲染左侧栏
                    // debugger;
                    var paramList = data.paramList;
                    var paramObj = {};
                    if(paramList){
                        for(var key in paramList){
                            var param = paramList[key];
                            var key = param.code;
                            var value = param.memo;
                            paramObj[""+key] = value;
                        }
                    }
                    var reqHeaderObj = {Cookie:document.cookie};
                    var newData = {url:data.url,appName:data.appName,model:"test",reqHeader:JSON.stringify(reqHeaderObj),
                        reqData:JSON.stringify(paramObj),
                        resHeader:null,resData:data.resultData};
                    $.extend(YT.deploy.data,{reqContentInitData:newData});

                    $(".nav-list").find("li > a[id='enterReqContent']").first().trigger("click");

                    // YT.deploy.reqContent.initLeftPanel(newData);
                });

                // YT.deploy.route("/idocUrl/getIdocUrlVoById",{id:id},"/idoc/bind.html",{title:"修改文档接口"});
            });

            $("a[name='btnDel']").click(function () {
                if (!confirm("您确认需要删除吗？")) {
                    return;
                }

                var id = $(this).attr("data");
                YT.deploy.util.reqPost("/idocUrl/deleteById", {id:id}, function (d) {
                    if (d.success) {
                        alert("删除成功");
                        YT.deploy.idocList.queryEnums(1);
                    } else {
                        alert("删除失败,err=" + d.message);
                    }
                });
            });

            $("a[name='btnCopy']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.util.reqPost("/idocUrl/copyDoc", {id:id}, function (d) {
                    if (d.success) {
                        alert("复制成功");
                        YT.deploy.idocList.query(1);
                    } else {
                        alert("复制失败,err=" + d.message);
                    }
                });
            });

            //枚举同步创建
            $("#btnSyncNew").click(function () {
                if (!confirm("您确认需要同步创建吗？")) {
                    return;
                }
                YT.deploy.util.reqPbtnSyncUpdateost("/idocUrl/syncNew", {appName:"member"}, function (d) {
                    if (d.success) {
                        alert("同步创建成功");
                    } else {
                        alert("同步创建失败,err=" + d.message);
                    }
                });
            });

            //枚举同步更新
            $("a[name='btnSyncUpdate']").each(function(index,item){
                $(item).click(function(){
                    if (!confirm("您确认需要同步更新吗？")) {
                        return;
                    }
                    var id = $(this).attr("data");
                    YT.deploy.util.reqPost("/idocUrl/syncUpdate", {appName:"member",id:id}, function (d) {
                        if (d.success) {
                            alert("同步修改成功");
                        } else {
                            alert("同步修改失败,err=" + d.message);
                        }
                    });
                });
            });

            //查看资源文档
            $("a[name='btnViewResDoc']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.route("/idocUrl/viewResDoc",{id:id},"/idoc/resDetail.html",{title:"资源文档详情"});
            });

            //修改资源文档
            $("a[name='btnEditResDoc']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.route("/idocUrl/viewResDoc",{id:id},"/idoc/editRes.html",{title:"资源文档修改"});
            });

            //删除资源文档
            $("a[name='btnDelResDoc']").click(function () {
                if (!confirm("您确认需要删除吗？")) {
                    return;
                }

                var id = $(this).attr("data");
                YT.deploy.util.reqPost("/idocUrl/deleteById", {id:id}, function (d) {
                    if (d.success) {
                        alert("删除成功");
                        YT.deploy.idocList.queryRes(1);
                    } else {
                        alert("删除失败,err=" + d.message);
                    }
                });
            });

        },
    }
    
    $.extend(YT.deploy, common);


    YT.deploy.idocList = {
        query: function (pageNum) {
            var id = $("form:first").prop("id");
            if(id == "resForm"){
                //
                YT.deploy.idocList.queryRes(pageNum);
            }else if(id == "enumsForm"){
                YT.deploy.idocList.queryEnums(pageNum);
            }else{
                var params = YT.deploy.util.getFormParams("#idocListForm");
                params["pageNum"] = pageNum;
                var pageSize = $("#pageSize").val();
                var ext_data = $.extend(params, {title: "接口文档"});
                YT.deploy.route("/idocUrl/list", params, "/idoc/list.html", ext_data);
            }
        },

        docPrint: function () {
            $("#printDiv").find("#btnPrint").remove();
            var printData = $("#printDiv").html();
            $(window.document.body).html(printData);
            // window.document.body.innerHTML = printData;   //把 html 里的数据 复制给 body 的 html 数据 ，相当于重置了 整个页面的 内容
            window.print(); // 开始打印
        },

        queryEnums: function (pageNum) {
            var params = YT.deploy.util.getFormParams("#idocListForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["type"] = 1;
            var ext_data = $.extend(params, {title: "枚举接口文档"});
            YT.deploy.route("/idocUrl/list", params, "/idoc/enums.html", ext_data);
        },
        queryRes: function (pageNum) {
            var params = YT.deploy.util.getFormParams("#idocListForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["type"] = 2;
            var ext_data = $.extend(params, {title: "资源文档"});
            YT.deploy.route("/idocUrl/list", params, "/idoc/res.html", ext_data);
        },

        queryExport:function(){
            var params = YT.deploy.util.getFormParams("#idocListForm");
            params["pageNum"] = 1;
            params["pageSize"] = 10000; //无穷大
            // var ext_data = $.extend(params, {title: "接口文档"});
            YT.deploy.util.reqGet("/idocUrl/list", params, function (d) {
                // debugger;
                // var jsonObj = JSON.parse(d.data.parameters);
                // var dataList = [];
                // for(var key in jsonObj){
                //     var val = jsonObj[key];
                //     dataList.push({key:key,value:val});
                // }
                var param = {title:"接口文档",dataList:d.data.dataList.reverse()};
                $.get("/idoc/listAll.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    bootbox.dialog({
                        message: html,
                        width:"800px",
                    });
                    $(".modal-dialog").prop("style","width:100%;height:100%")

                    $("#btnPrint").unbind("click");
                    $("#btnPrint").click(function () {
                        YT.deploy.idocList.docPrint();
                    });
                });
            });

        },

        uploadRes:function(type,title){

            //get html
            $.get("/idoc/uploadRes.html", function (source) {
                var render = template.compile(source);
                var html = render(d);
                YT.deploy.idocList.initQn();
                bootbox.dialog({
                    title : title,
                    message: html,
                    width:"800px",
                    buttons:
                        {
                            "success" :
                                {
                                    "label" : "<i class='icon-ok'></i> 保存",
                                    "className" : "btn-sm btn-success",
                                    "callback": function() {


                                        var version = $("#appVersion").val();
                                        if(!version){
                                            alert("请输入版本号");
                                            return;
                                        }
                                        var name = $("#name").val();
                                        if(!name){
                                            alert("请输入名称");
                                            return;
                                        }
                                        var fileRes = $("#fileRes").val();
                                        if(!fileRes){
                                            alert("请上传文件");
                                            return;
                                        }
                                        var param = {type:type,appVersion: version, namne: name, fileRes:fileRes};
                                        YT.deploy.util.reqPost("/idoc/saveUploadRes", param, function (d) {
                                            // loadTipsObj.close();
                                            // interval = window.setInterval(YT.deploy.appHost.printMsg,300);
                                        });
                                    }
                                },
                            "button" :
                                {
                                    "label" : "放弃",
                                    "className" : "btn-sm"
                                }
                        }

                });
            });


        },

        initQn: function () {

            //引入Plupload 、qiniu.js后
            var uploader = Qiniu.uploader({
                runtimes: 'html5,flash,html4',    //上传模式,依次退化
                browse_button: 'btnUploadImg',       //上传选择的点选按钮，**必需**
                uptoken_url: '/fileRes/token',            //Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
                // uptoken : '', //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
                unique_names: true, // 默认 false，key为文件名。若开启该选项，SDK为自动生成上传成功后的key（文件名）。
                // save_key: true,   // 默认 false。若在服务端生成uptoken的上传策略中指定了 `sava_key`，则开启，SDK会忽略对key的处理
                domain: 'http://oimf6ds63.bkt.clouddn.com/',   //bucket 域名，下载资源时用到，**必需**
                get_new_uptoken: false,  //设置上传文件的时候是否每次都重新获取新的token
                container: 'videoContainer',           //上传区域DOM ID，默认是browser_button的父元素，
                max_file_size: '100mb',           //最大文件体积限制
                flash_swf_url: '/_resources/scripts/qiniu/js/plupload/Moxie.swf',  //引入flash,相对路径
                max_retries: 3,                   //上传失败最大重试次数
                dragdrop: true,                   //开启可拖曳上传
                drop_element: 'videoContainer',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
                chunk_size: '4mb',                //分块上传时，每片的体积
                auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
                init: {
                    'FilesAdded': function (up, files) {
                        plupload.each(files, function (file) {
                            // 文件添加进队列后,处理相关的事情
                            // debugger;
                        });
                    },
                    'BeforeUpload': function (up, file) {
                        // 每个文件上传前,处理相关的事情
                        $("#tbUploadProcess").show();
                    },
                    'UploadProgress': function (up, file) {
                        // 每个文件上传时,处理相关的事情
                        var progress = new FileProgress(file, 'fsUploadProgress');
                        var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                        progress.setProgress(file.percent + "%", file.speed, chunk_size);
                    },
                    'FileUploaded': function (up, file, info) {
                        // debugger;
                        // 每个文件上传成功后,处理相关的事情
                        // 其中 info 是文件上传成功后，服务端返回的json，形式如
                        // {
                        //    "hash": "Fh8xVqod2MQ1mocfI4S4KpRL6D98",
                        //    "key": "gogopher.jpg"
                        //  }
                        // 参考http://developer.qiniu.com/docs/v6/api/overview/up/response/simple-response.html

                        var domain = up.getOption('domain');
                        var res = JSON.parse(info);
                        var sourceLink = domain + res.key; // 获取上传成功后的文件的Url
                        debugger;
                        $("#fileRes").val(sourceLink);
                        // $("#imgView").show().attr("src",sourceLink+"?imageView2/1/w/200/h/200");

                        $("#tbUploadProcess").hide();
                        $("#uploadResult").css("color", "green").html("上传成功");
                    },
                    'Error': function (up, err, errTip) {
                        //上传出错时,处理相关的事情
                        $("#uploadResult").css("color:red").html("上传失败");
                        alert(errTip);
                    },
                    'UploadComplete': function () {
                        //队列文件处理完毕后,处理相关的事情
                        debugger;
                    },
                    'Key': function (up, file) {
                        // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                        // 该配置必须要在 unique_names: false , save_key: false 时才生效

                        debugger;
                        var key = "";
                        // do something with key here
                        return key
                    }
                }
            });

            // domain 为七牛空间（bucket)对应的域名，选择某个空间后，可通过"空间设置->基本设置->域名设置"查看获取
            // uploader 为一个plupload对象，继承了所有plupload的方法，参考http://plupload.com/docs
        },

    }

})(window.YunTao);
