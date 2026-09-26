<%@ page import="java.util.*" %>
<%
    // shared catalog list stored in application scope
    List<String[]> catalog = (List<String[]>) application.getAttribute("catalog");
    if (catalog == null) {
        catalog = new ArrayList<>();
        application.setAttribute("catalog", catalog);
    }

    String error = "";

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String artist = request.getParameter("artist");
        String song = request.getParameter("song");
        String genre = request.getParameter("genre");

        if (artist == null || artist.trim().isEmpty() ||
            song == null || song.trim().isEmpty() ||
            genre == null || genre.trim().isEmpty()) {
            error = "All fields are required.";
        } else if (!artist.matches("[a-zA-Z0-9 .'-]+") ||
                   !song.matches("[a-zA-Z0-9 .'-]+") ||
                   !genre.matches("[a-zA-Z ]+")) {
            error = "Invalid characters in input.";
        } else {
            catalog.add(new String[]{artist.trim(), song.trim(), genre.trim()});
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Music Catalog</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .error { color: red; }
        form { margin-bottom: 10px; }
        input, select { padding: 5px; margin-right: 8px; }
    </style>
</head>
<body>
    <h2>Music Catalog</h2>

    <% if (!error.isEmpty()) { %>
        <p class="error"><%= error %></p>
    <% } %>

    <form method="post" action="MusicCatalog.jsp">
        <input type="text" name="artist" placeholder="Artist" required>
        <input type="text" name="song" placeholder="Song Title" required>
        <select name="genre" required>
            <option value="">-- Genre --</option>
            <option>Pop</option>
            <option>Rock</option>
            <option>Hip Hop</option>
            <option>Classical</option>
            <option>Jazz</option>
            <option>Electronic</option>
        </select>
        <button type="submit">Add Song</button>
    </form>

    <table>
        <tr><th>#</th><th>Artist</th><th>Song</th><th>Genre</th></tr>
        <%
            if (catalog.isEmpty()) {
        %>
        <tr><td colspan="4">No songs added yet.</td></tr>
        <%
            } else {
                for (int i = 0; i < catalog.size(); i++) {
                    String[] row = catalog.get(i);
        %>
        <tr>
            <td><%= i + 1 %></td>
            <td><%= row[0] %></td>
            <td><%= row[1] %></td>
            <td><%= row[2] %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
