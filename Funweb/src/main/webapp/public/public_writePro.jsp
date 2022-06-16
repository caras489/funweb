<%@page import="publicboard.PublicBoardBean"%>
<%@page import="publicboard.PublicBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// POST 방식 한글 처리
request.setCharacterEncoding("UTF-8");

// public_write.jsp 페이지로부터 전달받은 파라미터를 BoardBean 객체에 저장
PublicBoardBean pbb = new PublicBoardBean();

pbb.setName(request.getParameter("name"));
pbb.setPass(request.getParameter("pass"));
pbb.setSubject(request.getParameter("subject"));
pbb.setContent(request.getParameter("content"));

// PublicBoardDAO 객체의 insertBoard() 메서드를 호출하여 게시물 등록 작업 요청
// => 파라미터 : PublicBoardBean 객체   리턴타입 : int(insertCount)
PublicBoardDAO pbd = new PublicBoardDAO();
int insertCount = pbd.insertBoard(pbb);

// 게시물 등록 작업 요청 결과 처리
// => 성공 시 public.jsp 페이지로 포워딩
//    실패 시 자바스크립트 사용하여 "글쓰기 실패!" 출력 후 이전페이지로 돌아가기
if(insertCount > 0){
	response.sendRedirect("public.jsp");
} else {%>
	<script>
		alert("글쓰기 실패");
		history.back();
	</script>
<% }%>