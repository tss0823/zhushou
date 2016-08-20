/**
 * Created by shan on 2016/4/7.
 * 结构 YT 公司名
 * deploy 工程名
 * app    模块名
 */
(function (YT) {
    if (!YT) {
        window.YunTao = YT = {};
    }
    if (!YT.deploy) {
        YT.deploy = {};
    }

    if (!YT.deploy.constant) {
        YT.deploy.constant = {};
    }
    if (!YT.deploy.util) {
        YT.deploy.util = {};
    }

    //定义常量
    var constant = {
        NOT_LOGIN: "01",  //未登录
        
        C_USER_DATA : "c_user_data",   //用户数据cookie key
    }


    $.extend(YT.deploy.constant, constant);


    //工具类
    var util = {

        reqGet: function (url, param, callback) {
            $.get(url, param, function (d) {
                if (d.success) {
                    callback(d);
                    return true;
                } else {
                    alert(d.message);
                    if (d.code == YT.deploy.constant.NOT_LOGIN) {
                        YT.deploy.goLoginPage();
                    }
                    return false;
                }
            });
        },

        reqPost: function (url, param, callback) {
            $.post(url, param, function (d) {
                if (d.success) {
                    callback(d);
                } else {
                    alert(d.message);
                    if (d.code == YT.deploy.constant.NOT_LOGIN) {
                        YT.deploy.goLoginPage();
                    }
                }
            });
        },
        getReqUrlParam: function () {
            var url = location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
                }
            }
            return theRequest;
        },
        //获得提交表单数据
        getFormParams: function (id) {
            var sel = id || "form";
            var paramsArray = $(sel).serializeArray();
            var params = {};
            while (paramsArray.length > 0) {
                var data = paramsArray.pop();
                var oldValue = params[data.name];
                if (typeof(oldValue) != "undefined") {  //存在数据
                    if (typeof(oldValue) == "object") {
                        oldValue.push(data.value); //add
                    } else {
                        var arr = [];
                        arr.push(oldValue);
                        arr.push(data.value);
                        oldValue = arr;
                    }
                    params[data.name] = oldValue;
                } else {
                    params[data.name] = data.value;
                }

            }
            return params;
        },

        paramToString : function(param){
            if(!param){
                return "";
            }
            var strArray = [];
            for(var p in param){
                strArray.push("&"+p+"="+param[p]);
            }
            if(strArray.length == 0){
                return "";
            }
            return strArray.join("");

        },

        initSelect:function(dataList,key,val,selectId,initValue){
            var dataArray = [];
            for (var i = 0; i < dataList.length; i++) {
                var data = dataList[i];
                dataArray.push("<option value='" + data[key] + "'");
                if(typeof(initValue) != "undefined" && initValue == data[key]){
                    dataArray.push(" selected='true' ");
                }
                dataArray.push(">");
                dataArray.push(data[val]);
                dataArray.push("</option>");
            }
            $("#"+selectId).append(dataArray.join(""));
        },
        
        initEnumSelect:function(dataList,selectId,initValue){
            var dataArray = [];
            for (var i = 0; i < dataList.length; i++) {
                var data = dataList[i];
                dataArray.push("<option value='" + data.code + "'");
                if(typeof(initValue) != "undefined" && initValue == data.code){
                    dataArray.push(" selected='true' ");
                }
                dataArray.push(">");
                dataArray.push(data.description);
                dataArray.push("</option>");
            }
            $("#"+selectId).append(dataArray.join(""));
        },
        
        paginationInit: function (data, queryFn) {
            if ($(".totalCount").length == 0 || !data) {
                return;
            }

            $(".totalCount").html(data.totalCount);
            $(".pageCount").html(data.pageCount);
            $(".pageNum").html(data.pageNum);
            $(".startRow").html(data.startRow + 1);
            $(".endRow").html(data.endRow);

            var pageArray = [];
            pageArray.push('<input type="hidden" id="pageNum" value="' + data.pageNum + '" />');
            pageArray.push('<input type="hidden" id="pageSize" value="' + data.pageSize + '" />');
            pageArray.push('<li class="prev');
            if (data.pageNum == 1) {
                pageArray.push(' disabled');
            }
            pageArray.push('"><a href="javascript:void(0)" id="pageLeft"><i class="icon-double-angle-left"></i></a></li>');

            var pageIndex = data.pageNum;
            var maxShowCount = data.pageCount >= 3 ? 3 : data.pageCount;
            var startShowIndex = pageIndex - 1;
            if (startShowIndex < 1) {
                startShowIndex = 1;
            }
            var endShowIndex = startShowIndex + maxShowCount;
            if (endShowIndex > (data.pageCount + 1)) {
                endShowIndex = data.pageCount + 1;
            }
            var pageShowNumArray = [];
            while (startShowIndex < endShowIndex) {
                pageArray.push("<li");
                if (startShowIndex == data.pageNum) {
                    pageArray.push(' class="active" ');
                }
                pageArray.push(">");
                pageArray.push('<a href="javascript:void(0)" id="pageNum_' + startShowIndex + '">' + startShowIndex + '</a></li>');
                pageShowNumArray.push(startShowIndex);
                startShowIndex++;
            }
            pageArray.push('<li class="next');
            if (data.pageNum == data.pageCount) {
                pageArray.push(' disabled');
            }
            pageArray.push('"><a href="javascript:void(0)" id="pageRight"><i class="icon-double-angle-right"></i></a></li>');

            //append to dom
            $(".pagination").html(pageArray.join(""));

            //bind event
            for (var i = 0; i < pageShowNumArray.length; i++) {
                var pageIndex = pageShowNumArray[i];
                $("#pageNum_" + pageIndex).click(function () {
                    var id = $(this).attr("id");
                    var pageIndex = id.split("_")[1];
                    queryFn(pageIndex, data.pageSize);
                });
            }

            $("#pageLeft").click(function () {
                if (data.pageNum != 1) {
                    queryFn(1, data.pageSize);
                }
            });

            $("#pageRight").click(function () {
                if (data.pageNum != data.pageCount) {
                    queryFn(data.pageCount, data.pageSize);
                }
            });

        }
    };

    $.extend(YT.deploy.util, util);

    //实现路由程序
    YT.deploy.routeStackProcess = {
        currentIndex: -1,

        routeStack: {
            url: null,
            param: null,
            tlp_url: null,
            ext_data: null, //
        },

        rsArray: [],  //路由桟数组

        doRoute: function (url, param, tpl_url, ext_data) {
            var rsArray = YT.deploy.routeStackProcess.rsArray;
            var key = url + YT.deploy.util.paramToString(param);
            key += tpl_url + YT.deploy.util.paramToString(ext_data);
            console.log("key="+key);
            var routeMethod = ext_data["method"];
            if(routeMethod){
                if(routeMethod == "prevRoute" || routeMethod == "nextRoute"){
                    for(var i = 0; i < rsArray.length; i++){
                        var routeStatck = rsArray[i];
                        var compareKey = routeStatck.url + YT.deploy.util.paramToString(param);
                        compareKey += routeStatck.tpl_url + YT.deploy.util.paramToString(ext_data);
                        if(key == compareKey){  //exists return
                            YT.deploy.routeStackProcess.currentIndex = i;
                            return;
                        }
                    }

                }else if(routeMethod == "refresh"){
                    return;
                }


            }
            //other route
            var routeStatck = {url: url, param: param, tpl_url: tpl_url, ext_data: ext_data};
            rsArray.length = YT.deploy.routeStackProcess.currentIndex + 1;
            rsArray.push(routeStatck);
            if(rsArray.length > 50){
                //remove 10 element
                var index = 10;
                while(index < rsArray.length){
                    rsArray[index-10] = rsArray[index]
                    index++;
                }
                rsArray.length = rsArray.length-10;
            }
            YT.deploy.routeStackProcess.currentIndex = rsArray.length-1;


        },

        //刷新
        refresh: function () {
            var currentIndex = YT.deploy.routeStackProcess.currentIndex;
            var rsArray = YT.deploy.routeStackProcess.rsArray;
            if(rsArray.length == 0){
                return;
            }
            var routeStack = rsArray[currentIndex];
            routeStack.ext_data["method"] = "refresh";
            YT.deploy.route(routeStack.url,routeStack.param,routeStack.tpl_url,routeStack.ext_data);

        },

        //主页
        home: function () {
            var rsArray = YT.deploy.routeStackProcess.rsArray;
            if(rsArray.length == 0){
                return;
            }
            var currentIndex = 0
            var routeStack = rsArray[currentIndex];
            routeStack.ext_data["method"] = "home";
            YT.deploy.route(routeStack.url,routeStack.param,routeStack.tpl_url,routeStack.ext_data);

        },

        //上一个路由
        prevRoute: function () {
            var currentIndex = YT.deploy.routeStackProcess.currentIndex;
            var rsArray = YT.deploy.routeStackProcess.rsArray;
            if(rsArray.length == 0){
                return;
            }
            currentIndex--;
            var routeStack = rsArray[currentIndex];
            routeStack.ext_data["method"] = "prevRoute";
            YT.deploy.route(routeStack.url,routeStack.param,routeStack.tpl_url,routeStack.ext_data);
        },

        //下一个路由
        nextRoute: function () {
            //debugger;
            var currentIndex = YT.deploy.routeStackProcess.currentIndex;
            var rsArray = YT.deploy.routeStackProcess.rsArray;
            if(rsArray.length == 0){
                return;
            }
            if(currentIndex == (rsArray.length-1)){
                return;
            }
            currentIndex++;
            var routeStack = rsArray[currentIndex];
            routeStack.ext_data["method"] = "nextRoute";
            YT.deploy.route(routeStack.url,routeStack.param,routeStack.tpl_url,routeStack.ext_data);
        },
        
        


    };


    //用户数据处理程序
    YT.deploy.userDataProcess = {

        userDataMap : {},   //用户数据
        
        setValue : function(nameSpace,key,value,expire){
            var moduleData = this.userDataMap[nameSpace];  //this == userDataProcess 作用域
            moduleData = moduleData || {};
            moduleData[key] = value;
            this.userDataMap[nameSpace] = moduleData;
            var userDataString = JSON.stringify(this.userDataMap);
            $.cookie(YT.deploy.constant.C_USER_DATA,userDataString,{ expires: expire, path: "/" });
        },
        
        getValue : function(nameSpace,key){
            
        },

        setValueMap : function(nameSpace,dataMap,expire){
            if($.isEmptyObject(dataMap)){
                return;
            }
            var moduleData = this.userDataMap[nameSpace];  //this == userDataProcess 作用域
            moduleData = moduleData || {};
            // debugger;
            for(var key in dataMap){
               moduleData[key] = dataMap[key]; 
            }
            this.userDataMap[nameSpace] = moduleData;
            var userDataString = JSON.stringify(this.userDataMap);
            $.cookie(YT.deploy.constant.C_USER_DATA,userDataString,{ expires: expire, path: "/" });
        },
        
        getValueMap : function(nameSpace){
            var userDataString = $.cookie(YT.deploy.constant.C_USER_DATA);
            // debugger;
            if(!userDataString){
                return {};
            }
            var userData = JSON.parse(userDataString);
            return userData[nameSpace];
        },

        clear : function(nameSpace){
            this.userDataMap[nameSpace] = {};
            var userDataString = JSON.stringify(this.userDataMap);
            $.cookie(YT.deploy.constant.C_USER_DATA,userDataString,{ expires: 7, path: "/" });
        },
        
    }



    YT.deploy.eventProcess = {
        listener : [],  //监听器集合
        event : function(_type,_fun){
            this.type = _type;
            this.fun = _fun;
        },
        //使用端添加
        addListener : function(type,fun){
            var listenerList = YT.deploy.eventProcess.listener;
            for(var i = 0; i < listenerList.length; i++){
                var myEvent = listenerList[i];
                if(myEvent.type == type){
                    return;
                }
            }
            var myEvent = new YT.deploy.eventProcess.event(type,fun);
            YT.deploy.eventProcess.listener.push(myEvent);
        },
        //使用端删除
        removeListener : function(type){
            var listenerList = YT.deploy.eventProcess.listener;
            for(var i = 0; i < listenerList.length; i++){
                var myEvent = listenerList[i];
                if(myEvent.type == type){
                    //TODO
                    // YT.deploy.eventProcess.listener
                }
            }
        },
        
        //推送端触发
        notifyEvent : function(message){
            var msgObj = JSON.parse(message);
            var type = msgObj.type;
            var listenerList = YT.deploy.eventProcess.listener;
            for(var i = 0; i < listenerList.length; i++){
                var myEvent = listenerList[i];
                var executeFun = myEvent.fun;
                if(myEvent.type != type){
                    continue;
                }
                executeFun(msgObj);
            }
        }
    };

    var common = {
        checkLogin: function () {
            var isLogin = false;
            //获取用户信息
            $.ajax({
                url: "/user/getLoginUser",
                async: false,
                success: function (d) {
                    if (d.success) {
                        appData["user"] = d.data.user;
                        appData["authResList"] = d.data.authResList;
                        isLogin = true;
                    } else {
                        alert(d.message);
                        if (d.code == "01") {
                            YT.deploy.goLoginPage();
                        }
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
            return isLogin;
        },



        route: function (url, param, tpl_url, ext_data) {

            YT.deploy.util.reqGet(url, param, function (d) {
                var data = d.data;
                data = data || {};
                if (ext_data) {
                    for (var key in ext_data) {
                        data[key] = ext_data[key];
                    }
                }
                $.extend(data,appData);
                
                $.get(tpl_url, function (source) {
                    var render = template.compile(source);
                    var html = render(data);
                    $(".page-content").html(html);
                    YT.deploy.route_callback(d,data);
                    YT.deploy.routeStackProcess.doRoute(url, param, tpl_url, ext_data);

                });
            });
        },
        
        //只跳转模板，不请求数据
        routeTpl: function (tpl_url, data) {
            // $this = this;
            data = data || {};
            $.extend(data,appData);
            $.get(tpl_url, function (source) {
                var render = template.compile(source);
                var html = render(data);
                $(".page-content").html(html);
                YT.deploy.route_callback(data,data);
                // YT.deploy.routeStackProcess.doRoute(url, param, tpl_url, data);
            });
            
        },

        //请求 index.html 进行路由 
        routeIndex: function (tpl_url, ext_data) {
            $.get(tpl_url, function (source) {
                var render = template.compile(source);
                var html = render(ext_data);
                $("#indexBlock").html(html);
            });
        },

        //跳到登录
        goLoginPage: function () {
            $(document.body).addClass("login-layout");
            YunTao.deploy.routeIndex("/login.html", appData);
        },

        //跳到首页
        goIndexPage: function () {
            $(document.body).removeClass("login-layout");
            YT.deploy.routeIndex("/list.html", appData);
        },
        
        //搜索跳转
        goSearchPage: function (formId,pageNum,pageSize,ext_data) {
            var params = YT.deploy.util.getFormParams("#"+formId);
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            if (ext_data) {
                for (var key in ext_data) {
                    params[key] = ext_data[key];
                }
            }
            // debugger;
            var actionId = $("#"+formId).attr("actionId");
            var authRes = appData.authMap[actionId];
            var ext_data = $.extend(params, {title: authRes.name});
            ext_data = $.extend(ext_data,{authRes:authRes});
            YT.deploy.route(authRes.url, params, authRes.tplUrl, ext_data);

            //set userData
            YT.deploy.userDataProcess.setValueMap(authRes.tplUrl,params);
            //end
        },

        route_callback: function () {

        },

    };


    $.extend(YT.deploy, common);
    
    //local data
    YT.deploy.data = {
        
    }


    //事件触发
    var preKeyCode = null;
    $(document).keydown(function (e) {
        console.log("keyCode=" + e.keyCode);
        if (preKeyCode == 18 && e.keyCode == 13) {
            $("button[enter='true']").trigger("click");
            return false;
        }

        if (preKeyCode == 17 && e.keyCode == 82) { //ctrl + r
            console.log("presskey ctrl + r");
            YT.deploy.routeStackProcess.refresh();
            return false;  //屏蔽系统event
        }
        if (preKeyCode == 18 && e.keyCode == 72) { //alt + h
            console.log("presskey alt + h");
            YT.deploy.routeStackProcess.home();
            return false;  //屏蔽系统event
        }
        if (preKeyCode == 18 && e.keyCode == 37) { //alt + left
            console.log("presskey alt+left");
            YT.deploy.routeStackProcess.prevRoute();
            return false;
        }
        if (preKeyCode == 18 && e.keyCode == 39) { //alt + right
            console.log("presskey alt + right");
            YT.deploy.routeStackProcess.nextRoute();
            return false;
        }
        if (e.keyCode == 27) { //Esc
            console.log("presskey Esc");
            $(".bootbox-close-button").trigger("click");
            return false;
        }
        preKeyCode = e.keyCode;

    });
    //end


    //webSocket
    YT.deploy.WebSocket = {

        init : function(){
            var hostname = location.hostname;
            var webSocket = new WebSocket('ws://'+hostname+':9003/indexWebSocket');
            // webSocket.send("发送消息ok");

            webSocket.onerror = function(event) {
                console.error("error,data="+event.data);
            };

            webSocket.onopen = function(event) {
                console.log("open,data="+event.data);
            };

            webSocket.onmessage = function(event) {
                YT.deploy.eventProcess.notifyEvent(event.data);
                console.log("onmessage data="+event.data);
            };
            webSocket.onclose = function(event) {
                console.error("onclose data="+event.data);
                //重新连接
                setTimeout(this.init(), 1000);
            };
        },
    };
    //end

    YT.deploy.WebSocket.init();   //初始化socket
    
    // window.confirm = function(message){
    //     bootbox.confirm(message, function(result) {
    //         return result;
    //         if(result) {
    //             //
    //             alert(result);
    //         }
    //     });
    // }


})(window.YunTao);