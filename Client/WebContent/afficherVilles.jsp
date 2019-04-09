<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Ville de France</title>
</head>
<body>
Les villes : <br/>

			<c:forEach var="ville" items="${villes}" varStatus="loop">
				<tr>
					<td>
						<c:out value="${ville.getNomCommune()}" />
						&nbsp <c:out value="${ville.getCodeCommune()}" />
						&nbsp <c:out value="${ville.getCodePostal()}" />
						&nbsp <c:out value="${ville.getLibelleAcheminement()}" />
						&nbsp <c:out value="${ville.getLigne5()}" />
						&nbsp <c:out value="${ville.getLatitude()}" />
						&nbsp <c:out value="${ville.getLongitude()}" />
						 </br>
					</td>
				</tr>
			</c:forEach>

</body>
</html>