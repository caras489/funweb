<%@page import="publicboard.PublicBoardBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="publicboard.PublicBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
// 검색타입과 검색어 가져와서 변수에 저장
String search = request.getParameter("search");
String searchType = request.getParameter("searchType");

// ------------------------------------ 페이징 처리 --------------------------------------
int pageNum = 1; // 현재 페이지 번호를 저장할 변수 선언. 기본값 1 설정

// 현재 페이지 번호가 저장된 page 파라미터에서 값을 가져와서 pageNum 변수에 저장
// => 단, page 파라미터가 존재할 경우에만 가져오기
if(request.getParameter("page") != null) {
	pageNum = Integer.parseInt(request.getParameter("page")); // String -> int 형변환 필요
}

int listLimit = 10; // 한 페이지 당 표시할 목록(게시물) 갯수
int pageLimit = 10; // 한 페이지 당 표시할 페이지 갯수


// ----------------------------------------------------------------------------------------
// PublicBoardDAO 객체의 selectSearchListCount() 메서드를 호출하여 
// 검색어에 해당하는 게시물 전체 목록 갯수 조회 작업 요청
// => 파라미터 : 검색어(search), 검색타입(searchType)   리턴타입 : int(listCount)
PublicBoardDAO pbd = new PublicBoardDAO();
int listCount = pbd.selectSearchListCount(search, searchType);

// 페이징 처리를 위한 계산 작업
int maxPage = (int)Math.ceil((double)listCount / listLimit);
int startPage = ((int)((double)pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
int endPage = startPage + pageLimit - 1;
if(endPage > maxPage) {
	endPage = maxPage;
}

// PublicBoardDAO 객체의 selectSearchBoardList() 메서드를 호출하여 
// 검색어에 해당하는 게시물 목록 조회 작업 요청
// => 파라미터 : 현재 페이지 번호(pageNum), 표시할 목록 갯수(listLimit), 검색어(search), 검색타입(searchType)
// 리턴타입 : java.util.ArrayList<BoardBean>(boardList)
ArrayList<PublicBoardBean> boardList = pbd.selectSearchBoardList(pageNum, listLimit, search, searchType);
%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>public/public_search.jsp</title>
<link href="../css/default.css" rel="stylesheet" type="text/css">
<link href="../css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrap">
		<!-- 헤더 들어가는곳 -->
		<jsp:include page="../inc/top.jsp" />
		<!-- 헤더 들어가는곳 -->

		<!-- 본문들어가는 곳 -->
		<!-- 본문 메인 이미지 -->
		<div id="sub_img_center"></div>
		<!-- 왼쪽 메뉴 -->
		<nav id="sub_menu">
			<ul>
				<li><a href="../center/notice.jsp">Notice</a></li>
				<li><a href="public.jsp">Public News</a></li>
				<li><a href="#">Driver Download</a></li>
				<li><a href="#">Service Policy</a></li>
			</ul>
		</nav>
		<!-- 본문 내용 -->
		<article>
			<h1>Public News</h1>
			<table id="notice">
				<tr>
					<th class="tno">No.</th>
					<th class="ttitle">Title</th>
					<th class="twrite">Write</th>
					<th class="tdate">Date</th>
					<th class="tread">Read</th>
				</tr>
				<!-- 반복문을 사용하여 ArrayList 객체만큼 반복하면서 -->
				<!-- ArrayList 객체 내의 PublicBoardBean 객체를 꺼내서 저장한 후 -->
				<!-- 각 레코드를 tr 태그의 td 태그에 출력하기 -->
				<%
				// 2) 향상된 for문을 사용하여 반복 과정에서 즉시 PublicBoardBean 꺼내기
				for(PublicBoardBean pbb:boardList) {%>
					<!-- 게시물 행 클릭 시 public_content.jsp 페이지로 이동 -->
					<!-- URL 파라미터로 글번호와 페이지번호 전달 -->
					<tr onclick="location.href='public_content.jsp?num=<%=pbb.getNum() %>&page=<%=pageNum %>'">
						<td><%=pbb.getNum() %></td>
						<td class="left"><%=pbb.getSubject() %></td>
						<td><%=pbb.getName() %></td>
						<td><%=pbb.getDate() %></td>
						<td><%=pbb.getReadCount() %></td>
					</tr>
				<%}%>
			</table>
			<div id="table_search">
				<form action="public_search.jsp" method="get">
					<select name="searchType">
						<!-- searchType 이 name 일 경우 작성자 항목을 선택 -->
						<option value="subject">제목</option>
						<option value="name" <%if(searchType.equals("name")) {%> selected="selected" <%} %>>작성자</option>
						<option value="num" <%if(searchType.equals("num")) {%> selected="selected" <%} %>>글번호</option>
						<option value="content" <%if(searchType.equals("content")) {%> selected="selected" <%} %>>내용</option>
					</select>
					<input type="text" name="search" class="input_box" value="<%=search%>">
					<input type="submit" value="Search" class="btn">
					<input type="button" value="글쓰기" class="btn" onclick="location.href='public_write.jsp'">
				</form>
			</div>
			<!-- 페이징 처리 -->
			<div class="clear"></div>
			<div id="page_control">
				<!-- 페이징 처리 시 링크에 pubiic.jsp 대신 public_search.jsp 로 이동(검색어도 전달) -->
				<!-- 
				현재 페이지 번호(pageNum)가 1보다 클 경우에만 Prev 링크 동작
				=> 클릭 시 public.jsp 로 이동하면서 
				   현재 페이지 번호(pageNum) - 1 값을 page 파라미터로 전달
				-->
				<%if(pageNum > 1) { // 이전페이지가 존재할 경우 %>
					<a href="public_search.jsp?page=<%=pageNum - 1%>&search=<%=search%>&searchType=<%=searchType%>">Prev</a>
				<%} else { // 이전페이지가 존재하지 않을 경우 %>
					Prev&nbsp;
				<%} %>
				<!-- 페이지 번호 목록은 시작 페이지(startPage)부터 끝 페이지(endPage) 까지 표시 -->
				<%for(int i = startPage; i <= endPage; i++) { %>
					<!-- 단, 현재 페이지 번호는 링크 없이 표시 -->
					<%if(pageNum == i) { %>
						&nbsp;&nbsp;<%=i %>&nbsp;&nbsp;
					<%} else { %>
						<a href="public_search.jsp?page=<%=i%>&search=<%=search%>&searchType=<%=searchType%>"><%=i %></a>
					<%} %>
				<%} %>
				<!-- 
				현재 페이지 번호(pageNum)가 총 페이지 수보다 작을 때만 Next 링크 동작
				=> 클릭 시 public.jsp 로 이동하면서 
				   현재 페이지 번호(pageNum) + 1 값을 page 파라미터로 전달
				-->
				<%if(pageNum < maxPage) { // 다음페이지가 존재할 경우 %>
					<a href="public_search.jsp?page=<%=pageNum + 1%>&search=<%=search%>&searchType=<%=searchType%>">Next</a>
				<%} else { // 다음페이지가 존재하지 않을 경우 %>
					&nbsp;Next
				<%} %>
			</div>
		</article>

		<div class="clear"></div>
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp" />
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>