package publicboard;

import static DB.JdbcUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import board.*;
public class PublicBoardDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	// => 파라미터 : PublicBoardBean 객체   리턴타입 : int(insertCount)
	public int insertBoard(PublicBoardBean pbb) { // 게시물 등록 작업
		int insertCount = 0; 
		int num = 1; // 작성한 글의 글번호 변수
		
		try {
			con = getConnection(); // 1단계 연결
			
			String sql = "SELECT MAX(num) FROM publicboard"; // 2단계 구문 작성
			// publicboard 테이블에서 num의 최댓값이 몇인지 조회하라
			pstmt = con.prepareStatement(sql); // 3단계 구문 전달
			rs = pstmt.executeQuery(); // 구문 실행
			
			if (rs.next()) { // rs.next가 존재할때 실행한다 
				num = rs.getInt(1)+1; // 조회한 결과가 하나라도 있을때 +1 한다
			}
			
			sql = "INSERT INTO publicboard VALUES (?,?,?,?,?,now(),0)";
			// 글쓰기 작업을 수행할 구문 작성 date는 현재시각 now(), 조회수(readCount)는 0으로 시작
			// publicboard 테이블에 (?,?,?,?,?,now(),0)을 입력하라
			pstmt = con.prepareStatement(sql); // 구문전달
			
			pstmt.setInt(1, num);
			pstmt.setString(2, pbb.getName());
			pstmt.setString(3, pbb.getPass());
			pstmt.setString(4, pbb.getSubject());
			pstmt.setString(5, pbb.getPass());
			
			insertCount = pstmt.executeUpdate(); // 구문실행
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
			// 자원반환
		}
		return insertCount;
	}
//	파라미터 : 현재 페이지 번호(pageNum), 표시할 목록 갯수(listLimit)
//  리턴타입 : java.util.ArrayList<PublicBoardBean>(boardList)
	public ArrayList<PublicBoardBean> selectBoardList(int pageNum, int listLimit){ // 게시물 목록 조회 작업
		ArrayList<PublicBoardBean> boardList = null; // 리턴할 boardList 초기화
		
		try {
			con = getConnection(); // 1단계 연결
			
			int startNum = (pageNum - 1) * listLimit; // (현재 페이지 번호 - 1) * 페이지에 표시할 글 갯수
			
			String sql = "SELECT * FROM publicboard ORDER BY num DESC LIMIT ?,?"; // sql 구문작성
			// publicboard 테이블에서 num을 기준으로 내림차순 정렬하여 ?번 위치부터 ?개의 모든 컬럼을 조회하라
			pstmt = con.prepareStatement(sql); // 구문전달
			
			pstmt.setInt(1, startNum);
			pstmt.setInt(2, listLimit);
			
			rs = pstmt.executeQuery(); // 구문실행
			
			boardList = new ArrayList<PublicBoardBean>(); // 전체 레코드를 저장할 객체생성
			
			while (rs.next()) { // rs.next() (다음레코드) 가 존재하는 동안 반복한다
				PublicBoardBean pbb = new PublicBoardBean(); // 레코드 정보를 저장할 객체 생성
				// 조회된 레코드를 객체에 모두 저장한다
				pbb.setNum(rs.getInt("num"));
				pbb.setName(rs.getString("name"));
				pbb.setPass(rs.getString("pass"));
				pbb.setSubject(rs.getString("subject"));
				pbb.setContent(rs.getString("content"));
				pbb.setDate(rs.getDate("date"));
				pbb.setReadCount(rs.getInt("readcount"));
				
				boardList.add(pbb); // 전체 레코드를 저장하는 ArrayList 객체에 저장된 pbb 객체를 추가한다
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
			// 자원반환
		}
		return boardList;
	}
	// => 파라미터 : 없음, 리턴타입 : int(listCount)
	public int selectListCount() { //게시물 전체 목록 갯수 조회 작업
		int listCount = 0;
		
		try {
			con = getConnection(); // 1단계
			
			String sql = "SELECT COUNT(num) FROM publicboard"; // sql 구문 작성
			// publicboard 테이블에서 num이 몇개인지 조회한다
			
			pstmt = con.prepareStatement(sql); // 구문 전달
			rs = pstmt.executeQuery(); // 구문 실행
			
			if (rs.next()) { // rs.next() 다음레코드가 존재한다면
				listCount = rs.getInt(1); // 조회한 결과값의 1번을 저장
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
			// 자원 반환
		}
		return listCount;
	}
	// => 파라미터 : 글번호(num), 리턴타입 : BoardBean(board)
	public PublicBoardBean selectBoard(int num) { // 게시물을 클릭했을때 나오는 상세 내용 조회 작업
		PublicBoardBean pbb = null; // pbb 객체 초기화
		
		try {
			con = getConnection(); // 연결
			
			String sql = "SELECT * FROM publicboard WHERE num=?"; // 구문 작성
			// publicboard 테이블에서 num의 값이 ? 인 모든 컬럼을 조회한다
			pstmt = con.prepareStatement(sql); // 구문 전달
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery(); // 구문 실행
			if (rs.next()) { // rs.next() 다음레코드가 존재한다면
				pbb = new PublicBoardBean(); // board 객체 생성 후 조회한 데이터 저장
				
				pbb.setNum(rs.getInt("num"));
				pbb.setName(rs.getString("name"));
				pbb.setSubject(rs.getString("subject"));
				pbb.setContent(rs.getString("content"));
				pbb.setDate(rs.getDate("date"));
				pbb.setReadCount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
			// 자원반환
		}
		return pbb;
	}
	
	// => 파라미터 : 글번호(num), 리턴타입 : void
	public void updateReadCount(int num) {// 게시물 클릭시 조회수 증가 하는 작업
		try {
			con = getConnection(); // 연결
			
			String sql = "UPDATE publicboard SET readcount=readcount+1 WHERE num=?"; // 구문작성
			// publicboard 테이블에서 num의 값이 ?이면 readcount을 1씩 증가시킨다 
			pstmt = con.prepareStatement(sql); // 구문 전달
			
			pstmt.setInt(1, num);
			pstmt.executeUpdate(); //구문 실행
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
			close(con);
			// 자원반환
		}
		
	}
	// => 파라미터 : PublicBoardBean 객체(pbb), 리턴타입 : int(updateCount)
	// => 단, UPDATE 구문 수행 전 SELECT 구문을 사용하여 패스워드 일치 여부 판별 후
//	    패스워드가 일치할 경우에만 UPDATE 구문 실행(UPDATE 대상에서 패스워드는 제외)
	public int updateBoard(PublicBoardBean pbb) { // 게시물 수정 작업
		int updateCount = 0;
		
		try {
			con = getConnection(); // 연결
			
			String sql = "SELECT pass FROM publicboard WHERE num = ?"; // 구문작성
			// publicboard 테이블에서 num의 값이 ? 인 pass 컬럼을 조회하라
			
			pstmt = con.prepareStatement(sql); // 구문전달
			pstmt.setInt(1, pbb.getNum());
			rs = pstmt.executeQuery(); // 구문실행
			
			if(rs.next()) { // 조회한 결과값이 존재할때
				if (rs.getString("pass").equals(pbb.getPass())) {
					// rs.getString(조회한 결과값의 pass가) pbb.getPass(pbb에서 가져온 pass와 동일할때)
					sql = "UPDATE publicboard SET name=?,subject=?,content=? WHERE num=?"; // 구문작성
					// num의 값이 ? 인 publicboard 테이블의 name,subject,content 를 수정한다
					pstmt = con.prepareStatement(sql); // 구문전달
					
					pstmt.setString(1, pbb.getName());
					pstmt.setString(2, pbb.getSubject());
					pstmt.setString(3, pbb.getContent());
					pstmt.setInt(4, pbb.getNum());
					
					updateCount = pstmt.executeUpdate(); // 구문실행
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
			// 자원 반환
		}
		return updateCount;
	}
	
	// => 파라미터 : 글번호(num), 패스워드(pass)    리턴타입 : int(deleteCount)
	// => 패스워드를 조회하여 패스워드가 일치할 경우에만 레코드 삭제
	public int deleteBoard(int num, String pass) { // 게시물 삭제 작업
		int deleteCount = 0;
		
		try {
			con = getConnection(); // 연결
			
			String sql = "SELECT * FROM publicboard WHERE num=? AND pass=?"; // 구문 작성
			// publicboard 테이블에서 pass의 값이 ?이고 num의 값이 ?인 모든 컬럼 조회하라
			pstmt = con.prepareStatement(sql); // 구문 전달
			
			pstmt.setInt(1, num);
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery(); // 구문 실행
			
			if (rs.next()) { // 조회결과값이 존재할 때(pass와 num이 각각 일치할때) 삭제작업실행
				sql = "DELETE FROM publicboard WHERE num=?";
				// publicboard 테이블에서 num의 값이 ? 인 컬럼 삭제
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				deleteCount = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
			close(con);
		}
		return deleteCount;
	}
	
	// => 파라미터 : 검색어(search), 검색타입(searchType)   리턴타입 : int(listCount)
	public int selectSearchListCount(String search, String searchType) { // 검색어에 해당하는 게시물 전체 목록 갯수 조회 작업
		int listCount = 0;
		
		try {
			con = getConnection(); // 연결
			
			String sql = "SELECT COUNT(num) FROM publicboard WHERE " + searchType + " LIKE ?"; // 구문 작성
			// publicboard 테이블에서 searchType(제목or작성자or내용등)에 ?가 들어가는 num의 갯수를 조회한다 
			pstmt = con.prepareStatement(sql); // 구문 전달
			pstmt.setString(1, "%"+search+"%"); // %는 문자제한이 없는 구문
			rs = pstmt.executeQuery(); // 구문 실행
			
			if (rs.next()) {
				listCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return listCount;
	}
	
	// 리턴타입 : java.util.ArrayList<PublicBoardBean>(boardList)
	// => 파라미터 : 현재 페이지 번호(pageNum), 표시할 목록 갯수(listLimit), 검색어(search), 검색타입(searchType)
	public ArrayList<PublicBoardBean> selectSearchBoardList(int pageNum, int listLimit, String search, String searchType){
		// 검색어에 해당하는 게시물 목록 조회 작업
		ArrayList<PublicBoardBean> boardList = null;

		try {
			con = getConnection(); // 연결
			
			// 현재 페이지에서 불러올 목록(레코드)의 첫번째(시작) 행번호 계산
			int startRow = (pageNum - 1) * listLimit;
			
			String sql = "SELECT * FROM publicboard WHERE "+searchType+" LIKE ? ORDER BY num DESC LIMIT ?,?";
			// publicboard테이블에서 searchType에 ?가 들어가는 컬럼을 ?번부터 ?개까지 내림차순으로 정렬하여 조회
			pstmt = con.prepareStatement(sql); // 구문 전달
			pstmt.setString(1, "%"+search+"%"); 
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, listLimit);
			
			rs = pstmt.executeQuery(); // 구문 실행
			
			boardList = new ArrayList<PublicBoardBean>();
			
			while (rs.next()) { // 다음 레코드 조회 결과가 존재하는 동안 pbb 객체 생성하여 저장한다
				PublicBoardBean pbb = new PublicBoardBean();
				
				pbb.setNum(rs.getInt("num"));
				pbb.setName(rs.getString("name"));
				pbb.setPass(rs.getString("pass"));
				pbb.setSubject(rs.getString("subject"));
				pbb.setContent(rs.getString("content"));
				pbb.setDate(rs.getDate("date"));
				pbb.setReadCount(rs.getInt("readcount"));
				
				boardList.add(pbb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return boardList;
	}
	
	// => 파라미터 : 없음    리턴타입 : ArrayList<PublicBoardBean>(boardList)
	public ArrayList<PublicBoardBean> selectRecentBoardList(){ // 최근 게시물 5개 조회 요청 작업
		ArrayList<PublicBoardBean> recentBoardList = null;
		
		try {
			con = getConnection(); // 연결
			
			String sql = "SELECT * FROM publicboard ORDER BY num DESC LIMIT ?"; // 구문 작성
			// publicboard 테이블에서 num을 기준으로 내림차순 정렬하여 모든 컬럼중 최신게시글 5개 조회
			pstmt = con.prepareStatement(sql); // 구문 전달
			pstmt.setInt(1, 5);
			
			rs = pstmt.executeQuery(); // 구문 실행
			
			recentBoardList = new ArrayList<PublicBoardBean>();
			
			while (rs.next()) {
				PublicBoardBean pbb = new PublicBoardBean();
				pbb.setNum(rs.getInt("num"));
				pbb.setName(rs.getString("name"));
				pbb.setSubject(rs.getString("subject"));
				pbb.setContent(rs.getString("content"));
				pbb.setPass(rs.getString("pass"));
				pbb.setDate(rs.getDate("date"));
				pbb.setReadCount(rs.getInt("readcount"));
				
				recentBoardList.add(pbb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return recentBoardList;
	}
}