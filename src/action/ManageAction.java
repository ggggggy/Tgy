package action;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Dianping;
import model.Gonggao;
import model.Hotel;
import model.Jingdian;
import model.Manager;
import model.Raiders;
import model.Reserve;
import model.Tours;
import model.User;

import org.apache.struts2.ServletActionContext;
import util.Pager;
import util.Util;

import com.opensymphony.xwork2.ActionSupport;


import dao.DianpingDao;
import dao.GonggaoDao;
import dao.HotelDao;
import dao.JingdianDao;
import dao.ManagerDao;
import dao.RaidersDao;
import dao.ReserveDao;
import dao.ToursDao;
import dao.UserDao;


public class ManageAction extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	
	
	private String url="./";
	private File uploadfile;
	
	
	 public File getUploadfile() {
			return uploadfile;
		}

		
	 public void setUploadfile(File uploadfile) {
			this.uploadfile = uploadfile;
		}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	private UserDao userDao;
	private ManagerDao managerDao;
	private GonggaoDao gonggaoDao;
	private ToursDao toursDao;
	private HotelDao hotelDao;
	private JingdianDao jingdianDao;
	private RaidersDao raidersDao;
	private ReserveDao reserveDao;
	private DianpingDao dianpingDao;
	
	

	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public ManagerDao getManagerDao() {
		return managerDao;
	}


	public void setManagerDao(ManagerDao managerDao) {
		this.managerDao = managerDao;
	}

	

	public GonggaoDao getGonggaoDao() {
		return gonggaoDao;
	}


	public void setGonggaoDao(GonggaoDao gonggaoDao) {
		this.gonggaoDao = gonggaoDao;
	}
	public ToursDao getToursDao() {
		return toursDao;
	}


	public void setToursDao(ToursDao toursDao) {
		this.toursDao = toursDao;
	}
	


	public HotelDao getHotelDao() {
		return hotelDao;
	}


	public void setHotelDao(HotelDao hotelDao) {
		this.hotelDao = hotelDao;
	}


	public JingdianDao getJingdianDao() {
		return jingdianDao;
	}


	public void setJingdianDao(JingdianDao jingdianDao) {
		this.jingdianDao = jingdianDao;
	}


	public RaidersDao getRaidersDao() {
		return raidersDao;
	}


	public void setRaidersDao(RaidersDao raidersDao) {
		this.raidersDao = raidersDao;
	}
	
	public ReserveDao getReserveDao() {
		return reserveDao;
	}


	public void setReserveDao(ReserveDao reserveDao) {
		this.reserveDao = reserveDao;
	}
	
	public DianpingDao getDianpingDao() {
		return dianpingDao;
	}


	public void setDianpingDao(DianpingDao dianpingDao) {
		this.dianpingDao = dianpingDao;
	}


	/***************后台管理**************************/
	
	//用户登陆操作
	public void login() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Manager manager = managerDao.selectBean(" where username='"+username+"' and password='"+password+"' and  managerlock=0 ");
		if(manager!=null){
			HttpSession session = request.getSession();
			session.setAttribute("manager", manager);
			response.setCharacterEncoding("gbk");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('登陆成功');window.location.href='index.jsp'; </script>");
		}else{
			response.setCharacterEncoding("gbk");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('用户名或者密码错误');window.location.href='login.jsp'; </script>");
		}
	}
	
	//用户退出操作
	public void loginout() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("gbk");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('退出成功');window.location.href='login.jsp'; </script>");
	}
	
	
	//跳转到修改密码页面
	public String passwordupdate(){
		this.setUrl("manager/passwordupdate.jsp");
		return SUCCESS;
	}
	
	
	//修改密码操作
	public void passwordupdate2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		HttpSession session = request.getSession();
		Manager manager = (Manager)session.getAttribute("manager");
		Manager bean = managerDao.selectBean(" where username='"+manager.getUsername()+"' and password='"+password1+"' ");
		if(bean!=null){
			bean.setPassword(password2);
			managerDao.updateBean(bean);
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('操作成功'); </script>");
		}else{
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('原密码错误,修改失败!!');window.location.href='method!passwordupdate'; </script>");
		}		
	}

	
     //管理员信息页面	
	  public String managerlist(){
		HttpServletRequest request = ServletActionContext.getRequest();
		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		String where =" where managerlock=0  order by id desc ";
		long total = managerDao.selectBeanCount(where);
		List<Manager> list = managerDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!managerlist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("manager/managerlist.jsp");
		return SUCCESS;
	}
	
	  
	  
	//跳转到添加管理员页面
		public String manageradd(){
			this.setUrl("manager/manageradd.jsp");
			return SUCCESS;
		}
		
		//添加管理员操作
		public void manageradd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf8");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Manager bean = managerDao.selectBean(" where username='"+username+"' ");
			if(bean==null){
				bean = new Manager();
				bean.setUsername(username);
				bean.setPassword(password);
				bean.setCreatetime(new Date());
				managerDao.insertBean(bean);
				PrintWriter writer = response.getWriter();
				writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!managerlist'; </script>");
			}else{
				PrintWriter writer = response.getWriter();
				writer.print("<script  language='javascript'>alert('提交失败，该管理员已经存在');window.location.href='method!managerlist'; </script>");
			}
			
		}
	
	  
		//跳转到管理员个人信息页面
		public String managerupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			Manager bean = (Manager)session.getAttribute("manager");	
			request.setAttribute("bean", bean);
			this.setUrl("manager/managerupdate.jsp");
			return SUCCESS;
		}
		
		//跳转到更新管理员页面
		public String managerupdate2(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Manager bean =managerDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("manager/managerupdate2.jsp");
			return SUCCESS;
		}
		
		
		//更新管理员操作
		public void managerupdate3() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String id = request.getParameter("id");
			Manager bean =managerDao.selectBean(" where id= "+id);
			bean.setUsername(username);
			bean.setPassword(password);
			managerDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!managerlist'; </script>");
			
		}
		
	/***************用户***************/  
	//用户页面
	public String userlist(){
	   HttpServletRequest request = ServletActionContext.getRequest();
	       String truename = request.getParameter("truename");
	       StringBuffer sb = new StringBuffer();
	       sb.append(" where ");

	       if(truename !=null &&!"".equals(truename)){
		     sb.append(" truename like '"+truename+"' ");
		     sb.append(" and ");
		     request.setAttribute("truename", truename);
	       }
	    sb.append(" userlock=0  order by id desc ");
		String where = sb.toString();
		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		long total = userDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<User> list = userDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!userlist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("user/userlist.jsp");
		return SUCCESS;
	}
	
	
	
	//删除用户操作
	public void userdelete() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String id = request.getParameter("id");
		User bean =userDao.selectBean(" where id= "+id);
		bean.setUserlock(1);
		userDao.insertBean(bean);
		response.setCharacterEncoding("utf8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!userlist'; </script>");
		
	}
	
		
		/**************************公告***************************/
		//公告列表
		public String gonggaolist(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String biaoti = request.getParameter("biaoti");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");
			if(biaoti !=null &&!"".equals(biaoti)){
				sb.append(" biaoti like '%"+biaoti+"%' ");
				sb.append(" and ");
				request.setAttribute("biaoti", biaoti);
			}	
			sb.append(" gonggaolock=0 order by id desc ");
			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 10;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}	
			long total = gonggaoDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Gonggao> list = gonggaoDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!gonggaolist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("gonggao/gonggaolist.jsp");
			return SUCCESS;
		}
		
		
		//跳转到添加公告页面
		public String gonggaoadd(){
			this.setUrl("gonggao/gonggaoadd.jsp");
			return SUCCESS;
		}
		
		
		//添加公告操作
		public void gonggaoadd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String biaoti = request.getParameter("biaoti");
			String content = request.getParameter("content");
			Gonggao bean = new Gonggao();
			bean.setBiaoti(biaoti);
			bean.setContent(content);
			bean.setCreatetime(new Date());
			gonggaoDao.insertBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!gonggaolist'; </script>");
			
		}
		
		
		
		//删除公告操作
		public void gonggaodelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Gonggao bean =gonggaoDao.selectBean(" where id= "+id);
			bean.setGonggaolock(1);
			gonggaoDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!gonggaolist'; </script>");
			
		}
		
		//跳转到更新公告页面
		public String gonggaoupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Gonggao bean =gonggaoDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("gonggao/gonggaoupdate.jsp");
			return SUCCESS;
		}
		
		
		//更新公告操作
		public void gonggaoupdate2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String biaoti = request.getParameter("biaoti");
			String content = request.getParameter("content");
			String id = request.getParameter("id");
			Gonggao bean =gonggaoDao.selectBean(" where id= "+id);
			bean.setBiaoti(biaoti);
			bean.setContent(content);
			gonggaoDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!gonggaolist'; </script>");
			
		}
		
		
		/**************************旅游(游行)***************************/
		//旅游(游行)列表
		public String tourslist(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String category = request.getParameter("category");
			String biaoti = request.getParameter("biaoti");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");

			if(category !=null &&!"".equals(category)){
				sb.append(" category like '"+category+"' ");
				sb.append(" and ");

				request.setAttribute("category", category);
			}
			if(biaoti !=null &&!"".equals(biaoti)){
				sb.append(" biaoti like '%"+biaoti+"%' ");
				sb.append(" and ");

				request.setAttribute("biaoti", biaoti);
			}
			sb.append(" tourslock=0 order by id desc ");

			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 10;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}
			long total = toursDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Tours> list = toursDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!tourslist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("tours/tourslist.jsp");
			return SUCCESS;
		}
		
		
		//跳转到添加旅游(游行)页面
		public String toursadd(){
			this.setUrl("tours/toursadd.jsp");
			return SUCCESS;
		}
		
		
		//添加旅游(游行)操作
		public void toursadd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String category = request.getParameter("category");
			String biaoti = request.getParameter("biaoti");
			String content = request.getParameter("content");
			String xingcheng = request.getParameter("xingcheng");
			String xuzhi = request.getParameter("xuzhi");
			Integer price = Integer.parseInt(request.getParameter("price"));
			
			//上传图片
			String savapath = "C:/temp/";
			String time = Util.getTime2();
			String imgpath = time+".jpg";
			File file = new File(savapath+imgpath);
			Util.copyFile(uploadfile, file);
			
			Tours bean = new Tours();
			bean.setCategory(category);
			bean.setBiaoti(biaoti);
			bean.setContent(content);
			bean.setXingcheng(xingcheng);
			bean.setXuzhi(xuzhi);
			bean.setPrice(price);
			bean.setImgpath(imgpath);
			bean.setCreatetime(new Date());
			toursDao.insertBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!tourslist'; </script>");
			
		}
		
		
		//删除旅游(游行)操作
		public void toursdelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Tours bean =toursDao.selectBean(" where id= "+id);
			bean.setTourslock(1);
			toursDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!tourslist'; </script>");
			
		}
		
		//跳转到更新旅游(游行)页面
		public String toursupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Tours bean =toursDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("tours/toursupdate.jsp");
			return SUCCESS;
		}
		
		
		//更新旅游(游行)操作
		public void toursupdate2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String biaoti = request.getParameter("biaoti");
			String content = request.getParameter("content");
			String xingcheng = request.getParameter("xingcheng");
			String xuzhi = request.getParameter("xuzhi");
			Integer price = Integer.parseInt(request.getParameter("price"));

			String id = request.getParameter("id");
			Tours bean =toursDao.selectBean(" where id= "+id);
			bean.setBiaoti(biaoti);
			bean.setContent(content);
			bean.setXingcheng(xingcheng);
			bean.setXuzhi(xuzhi);
			bean.setPrice(price);
			bean.setCreatetime(new Date());
			toursDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!tourslist'; </script>");
			
		}
		

		
		/**********************酒店*******************************/
		//酒店列表
		public String hotellist(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String name = request.getParameter("name");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");
			if(name !=null &&!"".equals(name)){
				sb.append(" name like '%"+name+"%' ");
				sb.append(" and ");

				request.setAttribute("name", name);
			}
			sb.append(" hotellock=0 order by id desc ");
			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 10;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}
			long total = hotelDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Hotel> list = hotelDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!hotellist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("hotel/hotellist.jsp");
			return SUCCESS;
		}
		
		
		//跳转到添加酒店页面
		public String hoteladd(){
			this.setUrl("hotel/hoteladd.jsp");
			return SUCCESS;
		}
		
		
		//添加酒店操作
		public void hoteladd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String price = request.getParameter("price");
			String tel = request.getParameter("tel");
			String address = request.getParameter("address");
			String content = request.getParameter("content");
			//上传图片
			String savapath = "C:/temp/";
			String time = Util.getTime2();
			String imgpath = time+".jpg";
			File file = new File(savapath+imgpath);
			Util.copyFile(uploadfile, file);
			Hotel bean = new Hotel();
			bean.setName(name);
			bean.setPrice(Integer.parseInt(price));
			bean.setTel(tel);
			bean.setAddress(address);
			bean.setContent(content);
			bean.setImgpath(imgpath);
			bean.setCreatetime(new Date());
			hotelDao.insertBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!hotellist'; </script>");
			
		}
		
		
		
		//删除酒店操作
		public void hoteldelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Hotel bean =hotelDao.selectBean(" where id= "+id);
			bean.setHotellock(1);
			hotelDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!hotellist'; </script>");
			
		}
		
		//跳转到更新酒店页面
		public String hotelupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Hotel bean =hotelDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("hotel/hotelupdate.jsp");
			return SUCCESS;
		}
		
		
		//更新酒店操作
		public void hotelupdate2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String price = request.getParameter("price");
			String tel = request.getParameter("tel");
			String address = request.getParameter("address");
			String content = request.getParameter("content");
			
			String id = request.getParameter("id");
			Hotel bean =hotelDao.selectBean(" where id= "+id);
			bean.setName(name);
			bean.setPrice(Integer.parseInt(price));
			bean.setTel(tel);
			bean.setAddress(address);
			bean.setContent(content);
			hotelDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!hotellist'; </script>");
			
		}

		
		/******************景点***********************/
		//景点列表
		public String jingdianlist(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String name = request.getParameter("name");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");
			if(name !=null &&!"".equals(name)){
				sb.append(" name like '%"+name+"%' ");
				sb.append(" and ");

				request.setAttribute("name", name);
			}
			sb.append(" jingdianlock=0 order by id desc ");
			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 10;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}
			
			long total = jingdianDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Jingdian> list = jingdianDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!jingdianlist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("jingdian/jingdianlist.jsp");
			return SUCCESS;
		}
		
		
		//跳转到添加景点页面
		public String jingdianadd(){
			this.setUrl("jingdian/jingdianadd.jsp");
			return SUCCESS;
		}
		
		
		//添加景点操作
		public void jingdianadd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String content = request.getParameter("content");
			Integer price = Integer.parseInt(request.getParameter("price"));
			
			//上传图片
			String savapath = "C:/temp/";
			String time = Util.getTime2();
			String imgpath = time+".jpg";
			File file = new File(savapath+imgpath);
			Util.copyFile(uploadfile, file);
			
			Jingdian bean = new Jingdian();
			bean.setName(name);
			bean.setAddress(address);
			bean.setContent(content);
			bean.setImgpath(imgpath);
			bean.setPrice(price);
			bean.setCreatetime(new Date());
			jingdianDao.insertBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!jingdianlist'; </script>");
			
		}
		
		
		//删除景点操作
		public void jingdiandelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Jingdian bean =jingdianDao.selectBean(" where id= "+id);
			bean.setJingdianlock(1);
			jingdianDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!jingdianlist'; </script>");
			
		}
		
		//跳转到更新景点页面
		public String jingdianupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Jingdian bean =jingdianDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("jingdian/jingdianupdate.jsp");
			return SUCCESS;
		}
		
		
		//更新景点操作
		public void jingdianupdate2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String content = request.getParameter("content");
			Integer price = Integer.parseInt(request.getParameter("price"));
			
			String id = request.getParameter("id");
			Jingdian bean =jingdianDao.selectBean(" where id= "+id);
			bean.setName(name);
			bean.setAddress(address);
			bean.setContent(content);
			bean.setPrice(price);
			jingdianDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!jingdianlist'; </script>");
			
		}
		
		
		
		/******************旅游攻略***********************/
		//旅游攻略列表
		public String raiderslist(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String name = request.getParameter("name");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");
			if(name !=null &&!"".equals(name)){
				sb.append(" name like '%"+name+"%' ");
				sb.append(" and ");

				request.setAttribute("name", name);
			}
			sb.append(" raiderslock=0 order by id desc ");
			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 10;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}
			
			long total = raidersDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Raiders> list = raidersDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!raiderslist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("raiders/raiderslist.jsp");
			return SUCCESS;
		}
		
		
		//跳转到添加旅游攻略页面
		public String raidersadd(){
			this.setUrl("raiders/raidersadd.jsp");
			return SUCCESS;
		}
		
		
		//添加旅游攻略操作
		public void raidersadd2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String content = request.getParameter("content");
			
			//上传图片
			String savapath = "C:/temp/";
			String time = Util.getTime2();
			String imgpath = time+".jpg";
			File file = new File(savapath+imgpath);
			Util.copyFile(uploadfile, file);
			
			Raiders bean = new Raiders();
			bean.setName(name);
			bean.setContent(content);
			bean.setImgpath(imgpath);
			bean.setCreatetime(new Date());
			raidersDao.insertBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!raiderslist'; </script>");
			
		}
		
		
		
		//删除旅游攻略操作
		public void raidersdelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Raiders bean =raidersDao.selectBean(" where id= "+id);
			bean.setRaiderslock(1);
			raidersDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!raiderslist'; </script>");
			
		}
		
		//跳转到更新旅游攻略页面
		public String raidersupdate(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			Raiders bean =raidersDao.selectBean(" where id= "+id);
			request.setAttribute("bean", bean);
			this.setUrl("raiders/raidersupdate.jsp");
			return SUCCESS;
		}
		
		
		//更新旅游攻略操作
		public void raidersupdate2() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String name = request.getParameter("name");
			String content = request.getParameter("content");

			String id = request.getParameter("id");
			Raiders bean =raidersDao.selectBean(" where id= "+id);
			bean.setName(name);
			bean.setContent(content);
			raidersDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!raiderslist'; </script>");
			
		}
		

		
		
		/***************************预定后台管理************************/
	//酒店预定(后台)
		public String reshotellist() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			String username = request.getParameter("username");
			StringBuffer sb = new StringBuffer();
			sb.append(" where ");
			if(username !=null &&!"".equals(username)){
				sb.append(" user.username like '%"+username+"%' ");
				sb.append(" and ");
				request.setAttribute("username", username);
			}
			sb.append(" reservelock=0 and hotel!=null and hotel.hotellock=0 order by id desc ");
			String where = sb.toString();
			int currentpage = 1;
			int pagesize = 9;
			if(request.getParameter("pagenum") != null){
				currentpage = Integer.parseInt(request.getParameter("pagenum"));
			}
			long total = reserveDao.selectBeanCount(where.replaceAll("order by id desc", ""));
			List<Reserve> list = reserveDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
			request.setAttribute("list", list);
			String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!reshotellist", "共有"+total+"条记录");
			request.setAttribute("pagerinfo", pagerinfo);
			this.setUrl("reserve/reshotellist.jsp");
			return SUCCESS;
		}
		
		//删除预定酒店操作
		public void reshoteldelete() throws IOException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String id = request.getParameter("id");
			Reserve bean =reserveDao.selectBean(" where id= "+id);
			bean.setReservelock(1);
			reserveDao.updateBean(bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!reshotellist'; </script>");
			
		}
		
		
		//景点预定(后台)
			public String resjingdianlist() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				String name = request.getParameter("name");
				StringBuffer sb = new StringBuffer();
				sb.append(" where ");
				if(username !=null &&!"".equals(username)){
					sb.append(" user.username like '%"+username+"%' ");
					sb.append(" and ");

					request.setAttribute("username", username);
				}
				if(name !=null &&!"".equals(name)){
					sb.append(" jingdian.name like '%"+name+"%' ");
					sb.append(" and ");

					request.setAttribute("name", name);
				}
				sb.append(" reservelock=0 and jingdian!=null and  jingdian.jingdianlock=0 order by id desc ");
				String where = sb.toString();
				int currentpage = 1;
				int pagesize = 9;
				if(request.getParameter("pagenum") != null){
					currentpage = Integer.parseInt(request.getParameter("pagenum"));
				}
				long total = reserveDao.selectBeanCount(where.replaceAll("order by id desc", ""));
				List<Reserve> list = reserveDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
				request.setAttribute("list", list);
				String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!resjingdianlist", "共有"+total+"条记录");
				request.setAttribute("pagerinfo", pagerinfo);
				this.setUrl("reserve/resjingdianlist.jsp");
				return SUCCESS;
			}
			
			//删除预定景点操作
			public void resjingdiandelete() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String id = request.getParameter("id");
				Reserve bean =reserveDao.selectBean(" where id= "+id);
				bean.setReservelock(1);
				reserveDao.updateBean(bean);
				response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!resjingdianlist'; </script>");
				
			}
			
			
			
			//旅游预定(后台)
			public String restourslist() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				String category = request.getParameter("category");
				StringBuffer sb = new StringBuffer();
				sb.append(" where ");
				if(username !=null &&!"".equals(username)){
					sb.append(" user.username like '%"+username+"%' ");
					sb.append(" and ");

					request.setAttribute("username", username);
				}
				if(category !=null &&!"".equals(category)){
					sb.append(" tours.category like '%"+category+"%' ");
					sb.append(" and ");
					request.setAttribute("category", category);
				}
				sb.append(" reservelock=0 and tours!=null and  tours.tourslock=0 order by id desc ");
				String where = sb.toString();
				int currentpage = 1;
				int pagesize = 9;
				if(request.getParameter("pagenum") != null){
					currentpage = Integer.parseInt(request.getParameter("pagenum"));
				}
				long total = reserveDao.selectBeanCount(where.replaceAll("order by id desc", ""));
				List<Reserve> list = reserveDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
				request.setAttribute("list", list);
				String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!restourslist", "共有"+total+"条记录");
				request.setAttribute("pagerinfo", pagerinfo);
				this.setUrl("reserve/restourslist.jsp");
				return SUCCESS;
			}
			
			//删除预定旅游操作
			public void restoursdelete() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String id = request.getParameter("id");
				Reserve bean =reserveDao.selectBean(" where id= "+id);
				bean.setReservelock(1);
				reserveDao.updateBean(bean);
				response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!restourslist'; </script>");
				
			}
			
			
			//预定处理操作
			public void reserveupdate() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String id = request.getParameter("id");
				Reserve bean =reserveDao.selectBean(" where id= "+id);
				bean.setStauts("已使用");
				reserveDao.updateBean(bean);
				response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
				PrintWriter writer = response.getWriter();
				if(bean.getJingdian()!=null){
					writer.print("<script  language='javascript'>alert('处理成功');window.location.href='method!resjingdianlist'; </script>");
				}
				if(bean.getHotel()!=null){
					writer.print("<script  language='javascript'>alert('处理成功');window.location.href='method!reshotellist'; </script>");
				}
				if(bean.getTours()!=null){
					writer.print("<script  language='javascript'>alert('处理成功');window.location.href='method!restourslist'; </script>");
				}	
				
			}
			


			//旅游点评(后台)
			public String tours_dianping() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				String category = request.getParameter("category");
				StringBuffer sb = new StringBuffer();
				sb.append(" where ");
				if(username !=null &&!"".equals(username)){
					sb.append(" user.username like '%"+username+"%' ");
					sb.append(" and ");

					request.setAttribute("username", username);
				}
				if(category !=null &&!"".equals(category)){
					sb.append(" tours.category like '%"+category+"%' ");
					sb.append(" and ");
					request.setAttribute("category", category);
				}
				sb.append(" deletestatus=0 and tours!=null and  tours.tourslock=0 order by id desc ");
				String where = sb.toString();
				int currentpage = 1;
				int pagesize = 9;
				if(request.getParameter("pagenum") != null){
					currentpage = Integer.parseInt(request.getParameter("pagenum"));
				}
				long total = dianpingDao.selectBeanCount(where.replaceAll("order by id desc", ""));
				List<Dianping> list = dianpingDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
				request.setAttribute("list", list);
				String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!tours_dianping", "共有"+total+"条记录");
				request.setAttribute("pagerinfo", pagerinfo);
				this.setUrl("dianping/tours_dianping.jsp");
				return SUCCESS;
			}
			
			//度假酒店点评(后台)
			public String hotel_dianping() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				String name = request.getParameter("name");
				StringBuffer sb = new StringBuffer();
				sb.append(" where ");
				if(username !=null &&!"".equals(username)){
					sb.append(" user.username like '%"+username+"%' ");
					sb.append(" and ");

					request.setAttribute("username", username);
				}
				if(name !=null &&!"".equals(name)){
					sb.append(" hotel.name like '%"+name+"%' ");
					sb.append(" and ");
					request.setAttribute("name", name);
				}
				sb.append(" deletestatus=0 and hotel!=null and  hotel.hotellock=0 order by id desc ");
				String where = sb.toString();
				int currentpage = 1;
				int pagesize = 9;
				if(request.getParameter("pagenum") != null){
					currentpage = Integer.parseInt(request.getParameter("pagenum"));
				}
				long total = dianpingDao.selectBeanCount(where.replaceAll("order by id desc", ""));
				List<Dianping> list = dianpingDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
				request.setAttribute("list", list);
				String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!hotel_dianping", "共有"+total+"条记录");
				request.setAttribute("pagerinfo", pagerinfo);
				this.setUrl("dianping/hotel_dianping.jsp");
				return SUCCESS;
			}
			
			
			//景点点评(后台)
			public String jingdian_dianping() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				String name = request.getParameter("name");
				StringBuffer sb = new StringBuffer();
				sb.append(" where ");
				if(username !=null &&!"".equals(username)){
					sb.append(" user.username like '%"+username+"%' ");
					sb.append(" and ");

					request.setAttribute("username", username);
				}
				if(name !=null &&!"".equals(name)){
					sb.append(" jingdian.name like '%"+name+"%' ");
					sb.append(" and ");
					request.setAttribute("name", name);
				}
				sb.append(" deletestatus=0 and jingdian!=null and  jingdian.jingdianlock=0 order by id desc ");
				String where = sb.toString();
				int currentpage = 1;
				int pagesize = 9;
				if(request.getParameter("pagenum") != null){
					currentpage = Integer.parseInt(request.getParameter("pagenum"));
				}
				long total = dianpingDao.selectBeanCount(where.replaceAll("order by id desc", ""));
				List<Dianping> list = dianpingDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
				request.setAttribute("list", list);
				String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!jingdian_dianping", "共有"+total+"条记录");
				request.setAttribute("pagerinfo", pagerinfo);
				this.setUrl("dianping/jingdian_dianping.jsp");
				return SUCCESS;
			}
			
			//点评删除操作（管理员）
			public void dianpingdelete() throws IOException{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String id = request.getParameter("id");
				Dianping bean =dianpingDao.selectBean(" where id= "+id);
				bean.setDeletestatus(1);
				dianpingDao.updateBean(bean);
				response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
				PrintWriter writer = response.getWriter();
				if(bean.getJingdian()!=null){
					writer.print("<script  language='javascript'>alert('删除成功');window.location.href='method!jingdian_dianping'; </script>");
				}
				if(bean.getHotel()!=null){
					writer.print("<script  language='javascript'>alert('删除成功');window.location.href='method!hotel_dianping'; </script>");
				}
				if(bean.getTours()!=null){
					writer.print("<script  language='javascript'>alert('删除成功');window.location.href='method!tours_dianping'; </script>");
				}	
				
			}
		
}
