package getInfoFromAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class AppelClientM
 */
@WebServlet("/AppelClientM")
public class AppelClientM extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ATT_VILLES = "villes";

    /**
     * Default constructor. 
     */
    public AppelClientM() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String submit = request.getParameter("submit");
		
		HttpSession session = request.getSession();
		
		URL url = new URL("http://localhost:8184/test?value=villesFrance");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//String builder
		StringBuilder res = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));	
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	res.append(inputLine);
        }
		
		//Gson
        Gson gson = new Gson();
        Type founderListVille = new TypeToken<ArrayList<Ville>>() {}.getType();
        List<Ville> villes = gson.fromJson(res.toString(), founderListVille);
        
        session.setAttribute(ATT_VILLES, villes);
        System.out.println("Ville : " + villes);
        RequestDispatcher dispat = request.getRequestDispatcher("afficherVilles.jsp");
        
        if(submit.equals("distance")){
        	dispat = request.getRequestDispatcher("calculDistance.jsp");
        }
        
        
        dispat.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
