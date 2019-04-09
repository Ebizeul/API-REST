<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	Accueil : <br />

	<form method="get" action="AppelClientM">
		<td>
		<button type="submit" class="btn btn-primary center-block" name="submit" value="villeDeFrance" id="submit">
			<span class="glyphicon glyphicon-search"></span> Afficher liste des villes de France
		</button>
		</td>
		<td>
		<button type="submit" class="btn btn-primary center-block" name="submit" value="distance" id="submit">
			<span class="glyphicon glyphicon-search"></span> Distance entre deux villes
		</button>
		</td>
	</form>

</body>
</html>