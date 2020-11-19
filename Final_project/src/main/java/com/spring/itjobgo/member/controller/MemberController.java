package com.spring.itjobgo.member.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.spring.itjobgo.member.model.service.MemberService;
import com.spring.itjobgo.member.model.vo.Member;
import com.spring.itjobgo.security.service.SecurityService;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService service;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private Logger logger;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void memberRegister(@RequestBody Member member) {
		
		String encodePw = encoder.encode(member.getMemberPwd());
		member.setMemberPwd(encodePw);
		int result = 0;
		System.out.println(member);
		try {
			result = service.insertMember(member);
					
		}catch(RuntimeException e) {
			e.printStackTrace();
		}
		

	}
	//인포 업데이트
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public String memberUpdate(@RequestBody Map param) {
		
		Member login = service.selectOneMember(param);
		
		if (login != null) {// id값이 존재하는 경우

			if (encoder.matches((String) param.get("memberPwd"), login.getMemberPwd())) {// 비밀번호 비교
				logger.debug("비밀번호 맞음");
				//업데이트
			}else {//비번 틀림
				return null;
			}
			}
//		try {
//			result = service.insertMember(member);
//					
//		}catch(RuntimeException e) {
//			e.printStackTrace();
//		}
		
		return null;
	}

	// 이메일 중복검사
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public Member checkEmail(@RequestBody Map param) throws IOException {
		System.out.println(param);
		Member m = service.selectOneMember(param);
		return m;
	}
	
	//회원 정보 가져오기
	@RequestMapping(value = "/getMember", method = RequestMethod.GET)
	public Member getMember(@RequestParam Map param) throws IOException {
		System.out.println("멤버호출: " + param);
		Member m = service.selectOneMember(param);
		return m;
	}

	// 전화번호 중복검사
	@RequestMapping(value = "/checkPhone", method = RequestMethod.POST)
	public Member checkPhone(@RequestBody Map param) throws IOException {

		Member m = service.selectPhoneNum(param);
		return m;
	}


	// 전화번호 이메일 비교 : 비밀번호 업데이트
	@RequestMapping(value = "/compareEmailPhone", method = RequestMethod.POST)
	public Member compareEmailPhone(@RequestBody Map param) throws IOException {
		System.out.println("controller: " + param);
		Member m = service.selectEmailPhone(param);
		return m;
	}

	// 전화번호 이메일 비교 : 비밀번호 업데이트
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public int UpdatePwd(@RequestBody Map param) throws IOException {
		System.out.println("update controller: " + param);
		Member m = service.selectOneMember(param);

		
		
		
		String encodePw = encoder.encode((String) param.get("memberPwd"));// 업데이트된 비번 암호화
		m.setMemberPwd(encodePw);
		System.out.println(m);
		int result = 0;
		
		result = service.updatePwd(m);
		
		if(result>0) {
			return result;
		}else {
			return result;
		}
		

	}
	
	//이메일찾기
	@RequestMapping(value = "/selectPhone", method = RequestMethod.POST)
	public Member selectPhone(@RequestBody Map param) throws IOException {
		Member m = service.selectPhone(param);		
		return m;
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map loginMember(@RequestBody Map param) throws IOException {

		logger.debug("param:" + param);

		Member login = service.selectOneMember(param);

		logger.debug("login:" + login);

		if (login != null) {// id값이 존재하는 경우

			if (encoder.matches((String) param.get("memberPwd"), login.getMemberPwd())) {// 비밀번호 비교
				// 비밀번호 매치o
				// 토큰값 생성해야됨
				String token = securityService.createToken((String) param.get("memberEmail"), (2 * 1000 * 60));
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				// 토큰값!!!
				map.put("token", token);
				logger.debug("map: " + map);
				return map;
			} else {// 비밀번호 매치x
				return null;
			}
		} else {
			return null;
		}

	}


	 @RequestMapping(value = "/naverLogin", method = RequestMethod.GET)
	 public String naverLogin( @RequestParam(value = "code") String code,
	            @RequestParam(value = "state") String state)throws Exception {
	        String clientId = "fsL4vVVgvsoOwkPoWPO4";//애플리케이션 클라이언트 아이디값";
	        String naverClientSecret = "voZaFcwXXi";//시크릿값
	        String apiURL;
	        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
	        apiURL += "client_id=" + clientId;
	        apiURL += "&client_secret=" + naverClientSecret;
	        apiURL += "&code=" + code;
	        apiURL += "&state=" + state;
	        String access_token = "";
	        String refresh_token = "";
	        
	        try {
	            URL url = new URL(apiURL);
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            int responseCode = con.getResponseCode();
	            BufferedReader br;

	            if(responseCode==200) { // 정상 호출
	                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            } else {  // 에러 발생
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	            }
	            String inputLine;
	            StringBuffer res = new StringBuffer();
	            while ((inputLine = br.readLine()) != null) {
	                res.append(inputLine);
	            }
	            br.close();
	            
//	            if(responseCode==200) { // 성공적으로 토큰을 가져온다면
//	                int id;
//	                String nickName, email, tmp;
//	                JsonParser parser = new JsonParser();
//	                JsonElement accessElement = parser.parse(res.toString());
//	                access_token = accessElement.getAsJsonObject().get("access_token").getAsString();
//
//	                tmp = getUserInfo(access_token);
//	                JsonElement userInfoElement = parser.parse(tmp);
//	                id = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("id").getAsInt();
//	                nickName = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("nickname").getAsString();
//	                email = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();
//
//	                access_token = createJWTToken(id, nickName, email);
//	            }
//	        } catch (Exception e) {
//	            System.out.println(e);
//	        }
	            if(responseCode==200) {
	                System.out.println(res.toString());
	              }
	            } catch (Exception e) {
	              System.out.println(e);
	            }
	        return "redirect:http://localhost:8080/agreement?token=" + access_token;
	    }

}
