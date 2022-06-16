<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>company/welcome.jsp</title>
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
		<div id="sub_img"></div>
		<!-- 왼쪽 메뉴 -->
		<nav id="sub_menu">
			<ul>
				<li><a href="welcome.jsp">Welcome</a></li>
				<li><a href="../solutions/solutions.jsp">Solutions</a></li>
				<li><a href="#">Newsroom</a></li>
				<li><a href="#">Public Policy</a></li>
			</ul>
		</nav>
		<!-- 본문 내용 -->
		<article>
			<h1>Welcome</h1>
			<figure class="ceo">
				<img src="../images/company/lhj.jpg" width=196; height=226;>
				<figcaption>XLNT LEEHJ</figcaption>
			</figure>
			<h3>부산 IT WILL 개인 프로젝트 게시판 생성</h3>
			<h3>개발자 : 이현진</h3>
			<h3>제작기간 : 2022.04.01 ~ 2022.04.10</h3>
			<hr>
			<h3>프로젝트 개발환경</h3>
			<ul>
				<li>개발도구 : Eclipse, Apache Tomcat v8.5, Java Compiler 1.8, MySQL</li>
				<li>SDK : JDK 1.8</li>
				<li>API : 다음주소 API, 구글지도 임베드 코드</li>
				<li>개발언어 : JAVA, JSP, Javascript, CSS, MySQL</li>
			</ul>
			<h3>구현기능</h3>
			<ul>
				<li>회원가입, 로그인</li>
				<li>게시판 글 작성, 수정, 삭제</li>
				<li>최근 게시글 5개 조회</li>
				<li>지도</li>
				<li>주소찾기</li>
			</ul>
		</article>


		<div class="clear"></div>
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp" />
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>


