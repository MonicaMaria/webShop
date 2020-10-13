package main.java.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import main.java.model.Category;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainServlet extends HttpServlet {

    private static JsonObject mapCategory(Category category) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", category.getName());
        return jsonObject;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<JsonObject> categoriesArray = Arrays.stream(Category.values())
                .map(MainServlet::mapCategory)
                .collect(Collectors.toList());

        Gson gson = new GsonBuilder().create();
        response.getWriter().print(gson.toJson(categoriesArray));
    }
}
