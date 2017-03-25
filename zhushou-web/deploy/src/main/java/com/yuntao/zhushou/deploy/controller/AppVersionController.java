package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.AppDownloadRecords;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.service.inter.AppDownloadRecordsService;
import com.yuntao.zhushou.service.inter.AppVersionService;
import com.yuntao.zhushou.service.inter.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("appVersion")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private AppDownloadRecordsService appDownloadRecordsService;

    @Autowired
    private CompanyService companyService;


    @RequestMapping("checkUpdate")
    @ResponseBody
    public ResponseObject checkUpdate(@RequestParam String cpKey,@RequestParam String appName,@RequestParam String model,@RequestParam String version) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        //
        Company company = companyService.findByKey(cpKey);

        //根据 appName and model 获取最新的
//        AppVersionQuery appVersionQuery = new AppVersionQuery();
//        appVersionQuery.setCompanyId(company.getId());
//        appVersionQuery.setAppName(appName);
//        appVersionQuery.setModel(model);
        AppVersion lastVersion = appVersionService.getLastVersion(company.getId(), appName, model);
//        String version = (String) lastVersion1
//        AppVersion lastVersion = (AppVersion) version
        if(lastVersion != null){
            String lastVersionStr = lastVersion.getVersion();
            String lastVersionInt = lastVersionStr.replaceAll("\\.", "");
            String versionInt  = version.replaceAll("\\.","");
            if(Integer.valueOf(lastVersionInt) > Integer.valueOf(versionInt)){  //有最新版本,返回更新链接
                responseObject.setData(lastVersion);
            }
        }
        return responseObject;
    }

    @RequestMapping(value = "downloadApp",method = RequestMethod.GET)
    public void downloadApp(@RequestParam Long appVersionId,  @RequestParam(required = false) Long userId, HttpServletRequest request,HttpServletResponse response) {

        String deviceId = request.getHeader("deviceid");
        String clientIp = null;
        if (request.getHeader("x-forwarded-for") == null) {
            clientIp = request.getRemoteAddr();
        }else {
            clientIp =  request.getHeader("x-forwarded-for");
        }
        //根据 appName and model 获取最新的
        AppVersion appVersion = appVersionService.findById(appVersionId);
        String appUrl = appVersion.getAppUrl();
        response.setStatus(302);
        response.setHeader("Location",appUrl);
        //下载记录保存
        AppDownloadRecords appDownloadRecords = new AppDownloadRecords();
        appDownloadRecords.setAppVersionId(appVersionId);
        appDownloadRecords.setIp(clientIp);
        appDownloadRecords.setDeviceId(deviceId);
        appDownloadRecords.setUserId(userId);
        appDownloadRecordsService.insert(appDownloadRecords);
        //end
    }

}
