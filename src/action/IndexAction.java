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
import model.Raiders;
import model.Reserve;
import model.Tours;
import model.User;

import org.apache.struts2.ServletActionContext;
import util.Pager;

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


public class IndexAction extends ActionSupport{


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



	/********首页用户操作******/

	// 用户登录操作
	public void ulogin() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User bean = userDao.selectBean("  where  username='"+ username + "' and password='" + password + "' and userlock=0  ");
		if (bean != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", bean);
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('登录成功！');window.location.href='index.jsp'; </script>");
		} else {
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('用户名或者密码错误！登录失败');window.location.href='index.jsp'; </script>");
		}

	}

	// 用户退出操作
	public void uloginout() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('退出成功！');window.location.href='index.jsp'; </script>");

	}


	//用户注册操作
	public void register() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String truename = request.getParameter("truename");
		String telephone = request.getParameter("telephone");
		String jiguan = request.getParameter("jiguan");
		String address = request.getParameter("address");
		String xingbie = request.getParameter("xingbie");
		String age = request.getParameter("age");
		String email = request.getParameter("email");
		User bean = userDao.selectBean(" where username='"+username+"'  and userlock=0  ");
		if(bean==null){
			bean = new User();
			bean.setUsername(username);
			bean.setPassword(password);
			bean.setTruename(truename);
			bean.setTelephone(telephone);
			bean.setJiguan(jiguan);
			bean.setAddress(address);
			bean.setXingbie(xingbie);
			bean.setAge(age);
			bean.setEmail(email);
			bean.setCreatetime(new Date());
			userDao.insertBean(bean);
			response.setCharacterEncoding("gbk");
			response.getWriter().write("注册成功！您的用户名"+bean.getUsername()+"");
		}else{
			response.setCharacterEncoding("gbk");
			response.getWriter().write("提交失败，该用户已经注册过，请重新注册");
		}
		
	}

	//跳转到更新用户页面
	public String userupdate(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		User bean =userDao.selectBean(" where id= "+user.getId());
		request.setAttribute("bean", bean);
		this.setUrl("userupdate.jsp");
		return SUCCESS;
	}


	//更新用户操作
	public void userupdate2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String password = request.getParameter("password");
		String truename = request.getParameter("truename");
		String telephone = request.getParameter("telephone");
		String jiguan = request.getParameter("jiguan");
		String address = request.getParameter("address");
		String xingbie = request.getParameter("xingbie");
		String age = request.getParameter("age");
		String email = request.getParameter("email");
		String id = request.getParameter("id");
		User bean =userDao.selectBean(" where id= "+id);
		bean.setPassword(password);
		bean.setTruename(truename);
		bean.setTelephone(telephone);
		bean.setJiguan(jiguan);
		bean.setAddress(address);
		bean.setXingbie(xingbie);
		bean.setAge(age);
		bean.setEmail(email);
		bean.setCreatetime(new Date());
		userDao.updateBean(bean);
		response.setCharacterEncoding("gbk");
		response.getWriter().write("个人信息修改成功");
	}




	//公告列表(首页)
	public String sy_gonggaolist(){
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
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_gonggaolist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("sy_gonggaolist.jsp");
		return SUCCESS;
	}


	//跳转到查看公告页面(首页)
	public String xq_gonggao(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Gonggao bean =gonggaoDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		this.setUrl("xq_gonggao.jsp");
		return SUCCESS;
	}




	//首页-旅游(游行)列表
	public String sy_tours() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String biaoti = request.getParameter("biaoti");
		String category = request.getParameter("category");
		category=new String(category.getBytes("iso8859-1"),"UTF-8");
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		if(biaoti !=null &&!"".equals(biaoti)){
			sb.append(" biaoti like '%"+biaoti+"%' ");
			sb.append(" and ");
			request.setAttribute("biaoti", biaoti);
		}
		if(category !=null &&!"".equals(category)){
			sb.append(" category like '"+category+"' ");
			sb.append(" and ");
			request.setAttribute("category", category);
		}
		sb.append(" tourslock=0  order by id desc ");
		String where = sb.toString();
		int currentpage = 1;
		int pagesize = 5;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		long total = toursDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Tours> list = toursDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		if(category !=null &&"自由行".equals(category)){
			String pagerinfo1 = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_tours?category=自由行", "共有"+total+"条记录");
			request.setAttribute("pagerinfo1", pagerinfo1);
		}
		if(category !=null &&"跟团游".equals(category)){
			String pagerinfo2 = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_tours?category=跟团游", "共有"+total+"条记录");
			request.setAttribute("pagerinfo2", pagerinfo2);
		}
		if(category !=null &&"国内游".equals(category)){
			String pagerinfo3 = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_tours?category=国内游", "共有"+total+"条记录");
			request.setAttribute("pagerinfo3", pagerinfo3);
		}
		if(category !=null &&"国外游".equals(category)){
			String pagerinfo4 = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_tours?category=国外游", "共有"+total+"条记录");
			request.setAttribute("pagerinfo4", pagerinfo4);
		}
		this.setUrl("sy_tours.jsp");
		return SUCCESS;
	}

	//旅游(游行)详情页面（首页）
	public String xq_tours(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Tours bean =toursDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> tourslist = dianpingDao.selectBeanList(0, 99, " where tours="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("tourslist", tourslist);
		this.setUrl("xq_tours.jsp");
		return SUCCESS;
	}

	//旅游(游行)详情页面（点评页面）
	public String xq_tours2(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Tours bean =toursDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> tourslist = dianpingDao.selectBeanList(0, 99, " where tours="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("tourslist", tourslist);
		this.setUrl("xq_tours2.jsp");
		return SUCCESS;
	}




	//首页-酒店列表
	public String sy_hotel() throws IOException{
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
		int pagesize = 9;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		long total = hotelDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Hotel> list = hotelDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_hotel", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("sy_hotel.jsp");
		return SUCCESS;
	}

	//酒店详情页面（首页）
	public String xq_hotel(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Hotel bean =hotelDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> hotellist = dianpingDao.selectBeanList(0, 99, " where hotel="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("hotellist", hotellist);
		this.setUrl("xq_hotel.jsp");
		return SUCCESS;
	}

	//酒店详情页面（点评）
	public String xq_hotel2(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Hotel bean =hotelDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> hotellist = dianpingDao.selectBeanList(0, 99, " where hotel="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("hotellist", hotellist);
		this.setUrl("xq_hotel2.jsp");
		return SUCCESS;
	}




	//首页-景点列表
	public String sy_jingdian() throws IOException{
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
		int pagesize = 9;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		long total = jingdianDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Jingdian> list = jingdianDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_jingdian", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("sy_jingdian.jsp");
		return SUCCESS;
	}

	//景点详情页面（首页）
	public String xq_jingdian(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Jingdian bean =jingdianDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> jingdianlist = dianpingDao.selectBeanList(0, 99, " where jingdianid="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("jingdianlist", jingdianlist);
		this.setUrl("xq_jingdian.jsp");
		return SUCCESS;
	}

	//景点详情页面（点评）
	public String xq_jingdian2(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Jingdian bean =jingdianDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		List<Dianping> jingdianlist = dianpingDao.selectBeanList(0, 99, " where jingdianid="+bean.getId()+" and deletestatus=0 ");
		request.setAttribute("jingdianlist", jingdianlist);
		this.setUrl("xq_jingdian2.jsp");
		return SUCCESS;
	}



	//首页-旅游攻略列表
	public String sy_raiders() throws IOException{
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
		int pagesize = 9;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		long total = raidersDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Raiders> list = raidersDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "indexmethod!sy_raiders", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		this.setUrl("sy_raiders.jsp");
		return SUCCESS;
	}

	//旅游攻略详情页面（首页）
	public String xq_raiders(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Raiders bean =raidersDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		this.setUrl("xq_raiders.jsp");
		return SUCCESS;
	}




	/*****************************预定*********************/
	//跳转到添加酒店预定页面
	public String sy_hoteladd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String id = request.getParameter("id");
		Hotel bean =hotelDao.selectBean(" where id= "+id);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		if (user == null) {
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('请先登录');window.location.href='indexmethod!xq_hotel?id="+id+"'; </script>");
			return null ;
		}

		request.setAttribute("bean", bean);
		this.setUrl("sy_hoteladd.jsp");
		return SUCCESS;
	}


	//添加酒店预定操作
	public void sy_hoteladd2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		Integer number = Integer.parseInt(request.getParameter("number"));//房间数
		Integer tianshu = Integer.parseInt(request.getParameter("tianshu"));//天数
		String times = request.getParameter("times");
		String id = request.getParameter("id");
		Hotel b =hotelDao.selectBean(" where id= "+id);
		Reserve bean=new Reserve();
		bean.setHotel(b);
		bean.setUser(user);
		bean.setNumber(number);
		bean.setTimes(times);
		bean.setTianshu(tianshu);
		bean.setStauts("已预订");
		bean.setHeji(bean.getNumber()*b.getPrice()*bean.getTianshu());
		bean.setCreatetime(new Date());
		reserveDao.insertBean(bean);
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('预定成功');window.location.href='indexmethod!sy_hotel'; </script>");

	}

	//跳转到添加景点预定页面
	public String sy_jingdianadd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String id = request.getParameter("id");
		Jingdian bean =jingdianDao.selectBean(" where id= "+id);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		if (user == null) {
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('请先登录');window.location.href='indexmethod!xq_jingdian?id="+id+"'; </script>");
			return null ;
		}
		request.setAttribute("bean", bean);
		this.setUrl("sy_jingdianadd.jsp");
		return SUCCESS;
	}


	//添加景点预定操作
	public void sy_jingdianadd2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		Integer number = Integer.parseInt(request.getParameter("number"));//预定票数
		String times = request.getParameter("times");
		String id = request.getParameter("id");
		Jingdian b =jingdianDao.selectBean(" where id= "+id);
		Reserve bean=new Reserve();
		bean.setJingdian(b);
		bean.setUser(user);
		bean.setNumber(number);
		bean.setTimes(times);
		bean.setStauts("已预订");
		bean.setHeji(bean.getNumber()*b.getPrice());
		bean.setCreatetime(new Date());
		reserveDao.insertBean(bean);
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('预定成功');window.location.href='indexmethod!sy_jingdian'; </script>");

	}


	//跳转到添加旅游预定页面
	public String sy_toursadd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String id = request.getParameter("id");
		Tours bean =toursDao.selectBean(" where id= "+id);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		if (user == null) {
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('请先登录');window.location.href='indexmethod!xq_tours?id="+id+"'; </script>");
			return null ;
		}
		request.setAttribute("bean", bean);
		this.setUrl("sy_toursadd.jsp");
		return SUCCESS;
	}


	//添加旅游预定操作
	public void sy_toursadd2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		Integer number = Integer.parseInt(request.getParameter("number"));//预定票数
		String times = request.getParameter("times");
		String id = request.getParameter("id");
		Tours b =toursDao.selectBean(" where id= "+id);
		Reserve bean=new Reserve();
		bean.setTours(b);
		bean.setUser(user);
		bean.setNumber(number);
		bean.setTimes(times);
		bean.setStauts("已预订");
		bean.setHeji(bean.getNumber()*b.getPrice());
		bean.setCreatetime(new Date());
		reserveDao.insertBean(bean);
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('预定成功');window.location.href='indexmethod!sy_tours?category="+bean.getTours().getCategory()+"'; </script>");

	}

	//退订操作(所有)
	public void sy_delete() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String id = request.getParameter("id");
		Reserve bean =reserveDao.selectBean(" where id= "+id);
		bean.setReservelock(1);
		reserveDao.updateBean(bean);
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print("<script  language='javascript'>alert('退订成功');window.location.href='indexmethod!sy_reserve'; </script>");

	}

	//我的预定列表(景点,酒店,旅游)
	public String sy_reserve() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String category = request.getParameter("category");
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");	
		if(user==null){
			response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print("<script  language='javascript'>alert('请先登录');" +"window.location.href='index.jsp'; </script>");
			return null;
		}

		List<Reserve> hotellist = reserveDao.selectBeanList(0, 99, "  where reservelock=0 and hotel.hotellock=0 and  hotel!=null and user="+user.getId()+" order by id desc ");
		request.setAttribute("hotellist", hotellist);
		List<Reserve> jingdianlist = reserveDao.selectBeanList(0, 99, "  where reservelock=0 and jingdian.jingdianlock=0 and jingdian!=null and user="+user.getId()+" order by id desc ");
		request.setAttribute("jingdianlist", jingdianlist);

		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		if(category !=null &&!"".equals(category)){
			sb.append(" tours.category like '%"+category+"%' ");
			sb.append(" and ");
			request.setAttribute("category", category);
		}
		sb.append(" reservelock=0 and tours.tourslock=0  and tours!=null and user="+user.getId()+" order by id desc  ");
		String where = sb.toString();

		List<Reserve> tourslist = reserveDao.selectBeanList(0, 99, where );
		request.setAttribute("tourslist", tourslist);
		this.setUrl("sy_reserve.jsp");
		return SUCCESS;
	}







	//添加旅游点评操作
	public void tours_dianpingadd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		Tours t =toursDao.selectBean(" where id= "+id);

		Dianping bean = new Dianping();
		bean.setTours(t);
		bean.setUser(user);
		bean.setContent(content);
		bean.setCreatetime(new Date());
		dianpingDao.insertBean(bean); 
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='indexmethod!xq_tours2?id="+id+"'; </script>");

	}


	//添加酒店点评操作
	public void hotel_dianpingadd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		Hotel t =hotelDao.selectBean(" where id= "+id);

		Dianping bean = new Dianping();
		bean.setHotel(t);
		bean.setUser(user);
		bean.setContent(content);
		bean.setCreatetime(new Date());
		dianpingDao.insertBean(bean); 
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='indexmethod!xq_hotel2?id="+id+"'; </script>");

	}


	//添加景点点评操作
	public void jingdian_dianpingadd() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		Jingdian t =jingdianDao.selectBean(" where id= "+id);

		Dianping bean = new Dianping();
		bean.setJingdian(t);
		bean.setUser(user);
		bean.setContent(content);
		bean.setCreatetime(new Date());
		dianpingDao.insertBean(bean); 
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='indexmethod!xq_jingdian2?id="+id+"'; </script>");

	}





}
