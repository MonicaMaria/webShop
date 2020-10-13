package main.java.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.Category;
import main.java.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "ProductsServlet")
public class ProductsServlet extends HttpServlet {
    private static final String CATEGORY = "category";
    private static final String DELIMITER = ";";

    private List<Product> productList;

    public void init() throws ServletException {
        productList = new ArrayList<>();
        try {
            InputStream inputStream = getServletContext().getResourceAsStream("resources/products.txt");
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! ");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            productList = bufferedReader.lines()
                    .map(this::mapToItem)
                    .collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        Optional<Product> product = productList.stream()
                .filter(prod -> String.valueOf(prod.getId()).equals(productId))
                .findFirst();

        if (product.isPresent()) {
            String content = "Product " + product.get().getName() + " was added to the cart!\n";

            File file = new File(getServletContext().getRealPath("resources/cart.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(content);

            writer.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder().create();
        String category = request.getParameter(CATEGORY);
        response.getWriter().print(gson.toJson(getProductsForCategory(category)));
    }

    private List<Product> getProductsForCategory(String name) {
        Category category = Category.getCategoryForName(name);
        return productList.stream()
                .filter(product -> product.getCategory() == category)
                .collect(Collectors.toList());
    }

    private Product mapToItem(String line) {
        Product product = new Product();
        String[] p = line.split(DELIMITER);
        product.setId(Integer.valueOf(p[0]));
        product.setCategory(Category.getCategoryForName(p[1]));
        product.setName(p[2]);
        product.setPrice(Double.valueOf(p[3]));
        product.setDescription(p[4]);
        return product;
    }
}
