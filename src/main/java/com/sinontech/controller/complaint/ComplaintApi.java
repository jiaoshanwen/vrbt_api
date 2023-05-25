package com.sinontech.controller.complaint;

import com.sinontech.service.ComplaintService;
import com.sinontech.tools.common.PageData;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api")
public class ComplaintApi {

    @Resource(name = "complaintService")
    private ComplaintService complaintService;

    @RequestMapping(value = "/queryOrder")
    @ResponseBody
    public PageData queryOrder(HttpServletRequest request, HttpServletResponse response) {
        PageData data = (PageData) request.getAttribute("data");
        return complaintService.queryOrder(data);
    }

//    @RequestMapping(value = "/saveComplaint")
//    @ResponseBody
//    public PageData saveComplaint(HttpServletRequest request, HttpServletResponse response) {
//        PageData data = (PageData) request.getAttribute("data");
//        return complaintService.saveComplaint(data);
//    }

    @RequestMapping(value = "/saveComplaintRefund")
    @ResponseBody
    public PageData saveComplaintRefund(HttpServletRequest request, HttpServletResponse response) {
        PageData data = (PageData) request.getAttribute("data");
        //退费的
        if(null!=data&&"1".equals(data.getString("CANCEL_FEE"))){
            try{
                complaintService.saveComplaintRefund(data);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //投诉的
        return complaintService.saveComplaint(data);
    }


}
