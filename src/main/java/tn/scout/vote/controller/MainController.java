package tn.scout.vote.controller;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.scout.vote.dao.CONDIDAT_DAO;
import tn.scout.vote.dao.GLOBAL_DAO;
import tn.scout.vote.dao.USER_DAO;
import tn.scout.vote.dao.VOTERS_DAO;
import tn.scout.vote.model.CONDIDAT;
import tn.scout.vote.model.GLOBAL;
import tn.scout.vote.model.ImportService;
import tn.scout.vote.model.USER;
import tn.scout.vote.model.VOTERS;

@Controller
public class MainController {
	@Autowired
	USER_DAO user_dao;
	@Autowired
	VOTERS_DAO voter_dao;
	@Autowired
	CONDIDAT_DAO condidat_dao;
	@Autowired
	public GLOBAL_DAO global_dao;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	@GetMapping("/")
	public String viewHomePage(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "redirect:login";
		}
		return "redirect:/index";
	}

	@GetMapping("/index")
	public String index(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		model.addAttribute("nbr_total_voter", voter_dao.count());
		model.addAttribute("nbr_con_homme", condidat_dao.CountbyType("H"));
		model.addAttribute("nbr_con_femme", condidat_dao.CountbyType("F"));
		model.addAttribute("nbr_vote", voter_dao.getVoteAll());
		model.addAttribute("nbr_voteok", voter_dao.getVoteOK());
		model.addAttribute("nbr_voteko", voter_dao.getVoteKO());

		return "index";
	}

	@GetMapping("/Allvote")
	public String Allvote(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		model.addAttribute("listes", global_dao.findAll());

		return "Allvote";
	}
	
	@GetMapping("/Edit_Profile")
	public String Edit_Profile(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		USER u = user_dao.getUser(session.getAttribute("id").toString()); 
		model.addAttribute("userr",u);
		model.addAttribute("pp","pp");
		return "Edit_Profile";
	}
		@RequestMapping(value = "/UpdateUser", method = RequestMethod.POST)
		public String UpdateUser(Model model, @RequestParam(value = "login") String login,
				@RequestParam(value = "nom") String nom,@RequestParam(value = "prenom") String prenom,  HttpSession session) {
			
		user_dao.upUser(login, nom, prenom);
		USER u = user_dao.getUser(session.getAttribute("id").toString()); 
		model.addAttribute("userr",u);
		model.addAttribute("ok","ok");
		model.addAttribute("pp","pp");
		return "Edit_Profile";
	}
		@GetMapping("/Edit_ProfilePWD")
		public String Edit_ProfilePWD(Model model, HttpSession session) {

			if (session.getAttribute("id") == null) {
				return "404";
			}
			USER u = user_dao.getUser(session.getAttribute("id").toString()); 
			model.addAttribute("userr",u);
			model.addAttribute("pp","pwd");
			return "Edit_Profile";
		}
			@RequestMapping(value = "/UpdateUserPWD", method = RequestMethod.POST)
			public String UpdateUserPWD(Model model, @RequestParam(value = "login") String login,
					@RequestParam(value = "password") String password, @RequestParam(value = "password1") String password1, HttpSession session) {
				final String secretKey = "secrete";
		        String decryptedString = encrypt(password, secretKey);
				USER u = user_dao.getLogin(login, decryptedString);
				if (u != null) {
			        String encryptedString = encrypt(password1, secretKey);	
			        user_dao.upPassword(login, encryptedString);
					model.addAttribute("userr",u);
					model.addAttribute("ok","ok");
					model.addAttribute("pp","pwd");
					return "Edit_Profile";
				}	
				else
				{		

					model.addAttribute("pp","pwd");
					model.addAttribute("userr",user_dao.getUser(session.getAttribute("id").toString()));
					model.addAttribute("ok","ko");
					return "Edit_Profile";
				}

		}
	@GetMapping("/ResetAll")
	public String ResetAll(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		if (session.getAttribute("role").toString().contentEquals("ADMIN")) {
			global_dao.deleteAll();
			voter_dao.deleteAll();
			condidat_dao.deleteAll();
			return "logout";
		}

		return "404";
	}
	@GetMapping("/deleteallCON")
	public String deleteallCON(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		if (session.getAttribute("role").toString().contentEquals("ADMIN")) {
			condidat_dao.deleteAll();
			return "condidat";
		}

		return "404";
	}
	

	@GetMapping("/ResultatH")
	public String ResultatH(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		model.addAttribute("CAT", "H");
		model.addAttribute("result_homme", global_dao.getResultVote("H"));
		return "Resultat";
	}

	@GetMapping("/ResultatF")
	public String ResultatF(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}
		model.addAttribute("CAT", "F");
		model.addAttribute("result_homme", global_dao.getResultVote("F"));
		return "Resultat";
	}

	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}



	// ------Gestion User Admin
	@GetMapping("/GestionAdmin")
	public String CrudAdmin(Model model, HttpSession session) {


		if (session.getAttribute("role").toString().contentEquals("ADMIN")) {
			model.addAttribute("users", user_dao.findAll());
			model.addAttribute("erorXL", "");
			
			return "GestionAdmin";
		}
		
		return "404";

	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(Model model, @RequestParam(value = "login") String login,
			@RequestParam(value = "nom") String nom, @RequestParam(value = "prenom") String prenom,
			@RequestParam(value = "role") String role, @RequestParam(value = "password") String password) {
		
		final String secretKey = "secrete";
        String encryptedString = encrypt(password, secretKey);

		USER user = new USER(login, encryptedString, role, nom, prenom);
		user_dao.save(user);
		return "redirect:/GestionAdmin";
	}



	@GetMapping("/delete")
	public String delete(String id) {
		user_dao.deleteById(id);
		return "redirect:/GestionAdmin";
	}

	// ----update----
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(USER user) {
		user_dao.save(user);
		return "redirect:/GestionAdmin";
	}

	@GetMapping("/edit")
	@ResponseBody
	public Optional<USER> update(String id) {
		return user_dao.findById(id);
	}

	// --------CRUD VOTERS ------

	@GetMapping("/VOTERS")
	public String CrudVoters(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}

		model.addAttribute("voters", voter_dao.findAll());

		return "VOTERS";
	}
	
	@GetMapping("/Suivie")
	public String SuivieVoters(Model model, HttpSession session) {

		if (session.getAttribute("id") == null) {
			return "404";
		}

		model.addAttribute("voters", voter_dao.findAll());

		return "Suivie";
	}

	@GetMapping("/noVote")
	public String noVote(String id) {
		//user_dao.deleteById(id);
		voter_dao.voteEncours("5",id);
		return "redirect:/Suivie";
	}
	@GetMapping("/recordlock")
	public String recordlock(String id) {
		//user_dao.deleteById(id);
		global_dao.reset(id);
		voter_dao.voteEncours("0",id);
		return "redirect:/Suivie";
	}
	
	@GetMapping("/notshowvotepage")
	public String notshowvotepage(String id) {
		user_dao.upshow(id,"show");
		return "redirect:/GestionAdmin";
	}
	@GetMapping("/showvotepage")
	public String showvotepage(String id) {
		user_dao.upshow(id,"notshow");
		return "redirect:/GestionAdmin";
	}
	


	@GetMapping("/ResetVoter")
	public String ResetVoter() {
		voter_dao.deleteAll();
		return "redirect:/VOTERS";
	}

	@RequestMapping(value = "/CreateVoter", method = RequestMethod.POST)
	public String CreateVoters(Model model, @RequestParam(value = "nbr") int nbr) {
		voter_dao.deleteAll();
		for (int i = 0; i < nbr; i++) {
			
			String login = getRandomStr2() + getRandomStr(5);
			String password = getRandomStr1(5);
			Boolean ok = true ;
			
			while (ok == true)  {
				if (voter_dao.getLoginEX(login) == 0) {
					VOTERS voter = new VOTERS(login, password, "0");
					voter_dao.save(voter);
					ok =false ; 	
				}else 
					{
					 login = getRandomStr(5);
				//	LOGGER.info("login trouv :"+login);
					}; 
			}; 
		}

		return "redirect:/VOTERS";

	}
	  @Autowired
	    private ImportService importService;

	   @RequestMapping(value="/upload",method=RequestMethod.POST)
	    public   String  uploadExcel(HttpServletRequest request,Model model) throws Exception {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	 
	        InputStream inputStream =null;
	        List<List<Object>> list = null;
	        MultipartFile file = multipartRequest.getFile("filename");
	        if(file.isEmpty()){
	                       return "File cannot be empty";
	        }
	        inputStream = file.getInputStream();
	        list = importService.getBankListByExcel(inputStream,file.getOriginalFilename());
	        inputStream.close();
      	   // LOGGER.info(""+list);
      	  LOGGER.info(""+list);
	        for (int i = 1; i < list.size(); i++) {
	        	List<Object> lo = list.get(i);
		         String image =String.valueOf(lo.get(0));
		         String poss =String.valueOf(lo.get(1)).toString();
		         String date_nais =String.valueOf(lo.get(2)).toString();
	        	 String nom = String.valueOf(lo.get(3));

		        	if ( (nom=="null") || (image=="null") || (image.isEmpty())  || (poss=="null")  || (date_nais=="null") ) {

		        	//	LOGGER.info("champ vide line :"+i);
		        		LOGGER.info("champ vide line :"+i);

	        		model.addAttribute("erorXL","الرجاء التثبت في الملف يوجد جدول فارغ في السطر "+i) ;
	        		return "GestionAdmin" ;
	        	}


//	        	if (  ( ! poss.toUpperCase().contains("H")) || ( ! poss.toUpperCase().contains("F")) ) {
//   	        		model.addAttribute("erorXL", "il faut preciser le sexe H/F  line :"+i) ;
//   		      	    LOGGER.info("il faut preciser le sexe H/F  line :"+i);
//   	        		return "GestionAdmin" ;
//   	        	}
//
//	        	if (! image.toUpperCase().endsWith(".jpg"))  {
//   	        		model.addAttribute("erorXL", "l image doite etre au format jpg  line :"+i) ;
//   		      	    LOGGER.info("l image doite etre au format jpg  line :"+i);
//   		      	model.addAttribute("erorXL", "l image doite etre au format jpg  line:"+i) ;
//   	        		return "GestionAdmin" ;
//   	        	}
//	        	if (! String.valueOf(lo.get(2)).contains("/"))  {
//   	        		model.addAttribute("msgg", "la date doit etre au format jj/mm/aaaa  line :"+i) ;
//   		      	    LOGGER.info("la date doit etre au format jj/mm/aaaa  line:"+i);
//
//   	        		return "condidat" ;
//   	        	}
	        }
	        
	        for (int i = 0; i < list.size(); i++) {
	        	
	            List<Object> lo = list.get(i);
	            
	           BufferedImage bImage = ImageIO.read(new File(String.valueOf(lo.get(0))));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            ImageIO.write(bImage, "jpg", bos );
	            byte [] image = bos.toByteArray();
	            String sDate1=String.valueOf(lo.get(2));  
	            java.util.Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);             
	            java.sql.Date date_nais = new java.sql.Date(date1.getTime());
	            
	            String nom = String.valueOf(lo.get(3));
	            String pos =String.valueOf(lo.get(1));
	           // LOGGER.info(String.valueOf(lo.get(0))+","+String.valueOf(lo.get(1))+","+String.valueOf(lo.get(3)));
	            LOGGER.info(String.valueOf(lo.get(0))+","+String.valueOf(lo.get(1))+","+String.valueOf(lo.get(3)));
				CONDIDAT imageGallery = new CONDIDAT(0L,nom, image,pos ,date_nais);
				condidat_dao.save(imageGallery);
	            
	        }
	                         return "redirect:/condidatH";
	    }

	// ----------------IMAGE------------
	@GetMapping("/image/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<CONDIDAT> imageGallery)
			throws ServletException, IOException {
		imageGallery = condidat_dao.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(imageGallery.get().getImage());
		response.getOutputStream().close();
	}


	@GetMapping("/condidatH")
	String show(Model map, HttpSession session) {
		if (session.getAttribute("id") == null) {
			return "404";
		}
		map.addAttribute("CAT", "H");
		map.addAttribute("condidats", condidat_dao.GetByPost("H"));
		return "condidat";
	}

	@GetMapping("/condidatF")
	String showF(Model map, HttpSession session) {
		if (session.getAttribute("id") == null) {
			return "404";
		}
		map.addAttribute("CAT", "F");
		map.addAttribute("condidats", condidat_dao.GetByPost("F"));
		return "condidat";
	}

	// ----------election---------
	// ------verif login----
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public String verif(Model model, @RequestParam(value = "login") String login,
			@RequestParam(value = "password") String password, HttpSession session) {

		final String secretKey = "secrete";
        String decryptedString = encrypt(password, secretKey);
		USER u = user_dao.getLogin(login, decryptedString);
		if (u != null) {
			session.setAttribute("role", u.getRole());
			session.setAttribute("nom", u.getNom());
			session.setAttribute("prenom", u.getPrenom());
			session.setAttribute("id", u.getLogin());
			if (u.getRole().contentEquals("BUREAU"))
			{
				return "redirect:/Suivie";
				
			}
			return "redirect:/";
		}

		else  {
			String ss = user_dao.getshow("show");
			if (ss.contentEquals("notshow"))
			{
				return "404";	
			}

			else {
			
			VOTERS voter = voter_dao.getLogin(login, password);
			if (voter != null) {
				String e = voter_dao.getEt(login);

				if (e.contentEquals("1")) {
					session.setAttribute("role", "voter");
					session.setAttribute("id_voter", voter.getLogin());
					session.setAttribute("okH", "0"); 
					return "redirect:/listevote";	
				}
				if (e.contentEquals("5")) {
					session.setAttribute("role", "voter");
					session.setAttribute("id_voter", voter.getLogin());
					session.setAttribute("okH", "0"); 
					return "redirect:/listevote";	
				}
				if (e.contentEquals("2")) {
					session.setAttribute("eureur", "هذا المعرف في طور الانتخاب");
					return "redirect:/login";	
				}
				if (e.contentEquals("3")) {
					session.setAttribute("eureur", "هذا المعرف في طور الانتخاب");
					return "redirect:/login";	
				}


				voter_dao.voteEncours("2", login);
				session.setAttribute("role", "voter");
				session.setAttribute("id_voter", voter.getLogin());
				return "redirect:/feuille_Vote";
				
			}
			else {
				session.setAttribute("eureur", "الرجاء التثبت في معطيات الدخول");
				return "redirect:/login";
			}

		}}

	}


	@GetMapping("/listevote")
	public String listevote(Model model, HttpSession session) {
	
		String e = voter_dao.getEt((String) session.getAttribute("id_voter"));


		if (e.contentEquals("2")) {
				return "redirect:/feuille_Vote";
		}
		if (e.contentEquals("3")) {

				return "redirect:/feuille_Vote_f";
		}	
		if (session.getAttribute("okH").equals("1")) {
			model.addAttribute("yes", "no");
			return "listevote";
		} 	

		VOTERS rst = voter_dao.getLogin1((String) session.getAttribute("id_voter"));
		if (rst.getEtat().equals("1")) {
			model.addAttribute("yes", "yes");
			return "listevote";
		}
		if (rst.getEtat().equals("5")) {
			model.addAttribute("yes", "zero");
			return "listevote";
		}

	
		
		else
		return "404";
	}
	@GetMapping("/feuille_Vote")
	public String ELECTION(Model model, HttpSession session) {
		if (session.getAttribute("id_voter") == null) {
			return "404";
		} 
		
		String e = voter_dao.getEt((String) session.getAttribute("id_voter"));

		if (e.contentEquals("1")) {
			model.addAttribute("yes", "yes");
			return "listevote";	
		}
		if (e.contentEquals("2")) {
			  LOGGER.info("login  :" +session.getAttribute("id_voter")+" Debut vote homme");
				model.addAttribute("elections", condidat_dao.GetByPost("H"));
				return "feuille_Vote";
		}
		if (e.contentEquals("3")) {
			return "redirect:/feuille_Vote_f";	
		}
	
		else return "404";

	}

	@RequestMapping(value = "/addELECT", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam List<String> nbr, HttpSession session, Model model) {
		if (session.getAttribute("id_voter") == null) {
			return "404";
		}
		String e = voter_dao.getEt((String) session.getAttribute("id_voter"));

		if (e.contentEquals("1")) {
			model.addAttribute("yes", "yes");
			return "listevote";	
		}
		if (e.contentEquals("3")) {
			return "redirect:/feuille_Vote_f";	
		}
		VOTERS v = voter_dao.getLogin1(session.getAttribute("id_voter").toString());

		if (v.getEtat().equals("2")) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dd = simpleformat.format(cal.getTime());
			if (nbr.size() != 45) {
				 LOGGER.info("login  :" +session.getAttribute("id_voter")+ " mauvaise manip reload page size  :" +nbr.size());
				return "redirect:/feuille_Vote";
			}
			for (String temp : nbr) {
				int number = Integer.parseInt(temp);
				long l = number;
				CONDIDAT c = condidat_dao.getID(l);
				GLOBAL g = new GLOBAL(0L, dd, v, c);
				global_dao.save(g);
			}
			 voter_dao.voteEncours("3", (String) session.getAttribute("id_voter"));
			 LOGGER.info("login  :" +session.getAttribute("id_voter")+" fin vote homme avec success");
			return "redirect:/feuille_Vote_f";

		} 
		else return "404";
	}

	@GetMapping("/feuille_Vote_f")
	public String ELECTION_F(Model model, HttpSession session) {
		
		if (session.getAttribute("id_voter") == null) {
			return "404";
		} 
		
		String e = voter_dao.getEt((String) session.getAttribute("id_voter"));

		if (e.contentEquals("1")) {
			model.addAttribute("yes", "yes");
			return "listevote";	
		}
		if (e.contentEquals("2")) {
				return "redirect:/feuille_Vote";
		}
		if (e.contentEquals("3")) {
			 LOGGER.info("login  :" +session.getAttribute("id_voter")+" Debut vote femme");
				model.addAttribute("elections", condidat_dao.GetByPost("F"));
				return "feuille_Vote_f";
		}
	
		else return "404";
	}

	@RequestMapping(value = "/addELECT_F", method = RequestMethod.POST)
	public String addF(@RequestParam List<String> nbr, HttpSession session, Model model) {

		if (session.getAttribute("id_voter") == null) {
			return "404";
		}
		String e = voter_dao.getEt((String) session.getAttribute("id_voter"));

		if (e.contentEquals("1")) {
			model.addAttribute("yes", "yes");
			return "listevote";	
		}
		if (e.contentEquals("2")) {
			return "redirect:/feuille_Vote";	
		}
		VOTERS v = voter_dao.getLogin1(session.getAttribute("id_voter").toString());

		if (v.getEtat().equals("3")) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dd = simpleformat.format(cal.getTime());
//			// current time
			if (nbr.size() != 30) {
				 LOGGER.info("login  :" +session.getAttribute("id_voter")+ " mauvaise manip reload page size  :" +nbr.size());
				return "redirect:/feuille_Vote_f";
			}
			for (String temp : nbr) {
				int number = Integer.parseInt(temp);
				long l = number;
				CONDIDAT c = condidat_dao.getID(l);
				GLOBAL g = new GLOBAL(0L, dd, v, c);
				global_dao.save(g);

			}
			 voter_dao.voteEncours("1", (String) session.getAttribute("id_voter"));
			 LOGGER.info("login  :" +session.getAttribute("id_voter")+" Fin vote femme avec success");
				session.setAttribute("okH", "1"); 
			return "redirect:/listevote";

		} 
		else return "404";

	}
//-----------------CRYPTAGE -----------------
	
	private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";

    public void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            LOGGER.info("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            LOGGER.info("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
 //-------------------
	public static String getRandomStr(int n) {
		String str = "abcdefghijklmnopqrstuvwxyz";

		StringBuilder s = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (str.length() * Math.random());
			s.append(str.charAt(index));
		}
		return s.toString();
	}
	public static String getRandomStr2() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		StringBuilder s = new StringBuilder(1);
		int index = (int) (str.length() * Math.random());
		s.append(str.charAt(index));
		return s.toString();
	}
	public static String getRandomStr1(int n) {
		String str = "0123456789";

		StringBuilder s = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (str.length() * Math.random());
			s.append(str.charAt(index));
		}
		return s.toString();
	}


}
