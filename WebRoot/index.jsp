<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="util.Util"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="css/main.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/lanrenzhijia.css" media="screen" />
<script type="text/javascript" src="js/lanrenzhijia.js"></script>
<script src="js/jquery.superslide.2.1.1.js" type="text/javascript"></script>
<%
org.springframework.web.context.WebApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
dao.ToursDao toursDao = (dao.ToursDao)app.getBean("toursDao");
List<model.Tours> list1 = toursDao.selectBeanList(0,5," where tourslock=0 and category='自由行' order by createtime desc ");
List<model.Tours> list2 = toursDao.selectBeanList(0,5," where tourslock=0 and category='跟团游' order by createtime desc ");
List<model.Tours> list3 = toursDao.selectBeanList(0,5," where tourslock=0 and category='国内游' order by createtime desc ");
List<model.Tours> list4 = toursDao.selectBeanList(0,5," where tourslock=0 and category='国外游' order by createtime desc ");

dao.HotelDao hotelDao = (dao.HotelDao)app.getBean("hotelDao");
List<model.Hotel> hotellist = hotelDao.selectBeanList(0,99," where  hotellock=0 ");
dao.JingdianDao jingdianDao = (dao.JingdianDao)app.getBean("jingdianDao");
List<model.Jingdian> jingdianlist = jingdianDao.selectBeanList(0,99," where  jingdianlock=0 ");


%>
</head>
<body>
<!--[if IE 6]>
    <script type="text/javascript" src="js/DD_belatedPNG.js"></script>
    <script type="text/javascript">
    DD_belatedPNG.fix('div.menu ul li,.more');
    </script>
<![endif]-->
<jsp:include page="top.jsp"></jsp:include>

<div class="clear"></div>


<div class="wt1002 fwmain">
	  <jsp:include page="left.jsp"></jsp:include>

	<div class="fwmain_left l ">

        <div class="index_pro mTop10 l label">
        	<div class="label_head">	
            	<div class="label_title"><a href="indexmethod!sy_tours?category=自由行" class="more">更多</a>自由行</div>
            </div>
            <ul class="label_pad">
             <%
        for(model.Tours bean2:list1){
        %>
                <li><span><fmt:formatDate value="<%=bean2.getCreatetime() %>" pattern="yyyy-MM-dd" /></span><a href="indexmethod!xq_tours?id=<%=bean2.getId() %>"><%=bean2.getBiaoti() %></a></li>
                  <% 
        }
        %>
            </ul>
        </div>
        <div class="index_pro mTop10 mLeft10 l label">
        	<div class="label_head">	
            	<div class="label_title"><a href="indexmethod!sy_tours?category=跟团游" class="more">更多</a>跟团游</div>
            </div>
             <ul class="label_pad">
              <%
        for(model.Tours bean2:list2){
        %>
                <li><span><fmt:formatDate value="<%=bean2.getCreatetime() %>" pattern="yyyy-MM-dd" /></span><a href="indexmethod!xq_tours?id=<%=bean2.getId() %>"><%=bean2.getBiaoti() %></a></li>
               
                   <% 
        }
        %>
            </ul>
        </div>
         <div class="index_pro mTop10 l label">
        	<div class="label_head">	
            	<div class="label_title"><a href="indexmethod!sy_tours?category=国内游" class="more">更多</a>国内游</div>
            </div>
            <ul class="label_pad">
                <%
        for(model.Tours bean2:list3){
        %>
                <li><span><fmt:formatDate value="<%=bean2.getCreatetime() %>" pattern="yyyy-MM-dd" /></span><a href="indexmethod!xq_tours?id=<%=bean2.getId() %>"><%=bean2.getBiaoti() %></a></li>
               
                   <% 
        }
        %>
            </ul>
        </div>
        <div class="index_pro mTop10 mLeft10 l label">
        	<div class="label_head">	
            	<div class="label_title"><a href="indexmethod!sy_tours?category=国外游" class="more">更多</a>国外游</div>
            </div>
             <ul class="label_pad">
                 <%
        for(model.Tours bean2:list4){
        %>
                <li><span><fmt:formatDate value="<%=bean2.getCreatetime() %>" pattern="yyyy-MM-dd" /></span><a href="indexmethod!xq_tours?id=<%=bean2.getId() %>"><%=bean2.getBiaoti() %></a></li>
               
                   <% 
        }
        %>
            </ul>
        </div>
        
        
        <div class="index_img mTop10 l label">
        	<div class="label_head">
            	<div class="label_title"><a href="indexmethod!sy_jingdian" class="more">更多</a>景点</div>
            </div>
            <div id="demox">
                <div id="demoxList">
                    <div id="demo1x"> 
                       <%
        for(model.Jingdian bean2:jingdianlist){
        %>
            <a href="indexmethod!xq_jingdian?id=<%=bean2.getId() %>"><img src='<%=basePath %>/temp/<%=bean2.getImgpath() %>' /><br/><%=bean2.getName() %></a>
              <% 
        }
        %>
                       </div>	
                       <div id="demo2x"></div>
                       <script type="text/javascript" src="js/rolling.js"></script>
                   </div>
             </div>
             
             <div class="label_head">
            	<div class="label_title"><a href="indexmethod!sy_hotel" class="more">更多</a>度假酒店</div>
            </div>
            <div id="demox">
                <div id="demoxList">
                    <div id="demo1x"> 
            <%
        for(model.Hotel bean2:hotellist){
        %>
            <a href="indexmethod!xq_hotel?id=<%=bean2.getId() %>"><img src='<%=basePath %>/temp/<%=bean2.getImgpath() %>' /><br/><%=bean2.getName() %></a>
              <% 
        }
        %>
                       </div>	
                       <div id="demo2x"></div>
                       <script type="text/javascript" src="js/rolling.js"></script>
                   </div>
             </div>
        </div>
        
         
		<div class="clear"></div>
    </div><!--left--> 
    <div class="clear"></div>
</div>
<jsp:include page="floot.jsp"></jsp:include>
</body>
</html>