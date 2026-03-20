package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;

import java.util.List;

public class ReceiptGenerator {

    private ReceiptGenerator() {}

    public static String generate(Order o, List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== BON FISCAL =====\n").append("Comanda #").append(o.getId()).append("\n");

        for (OrderItem i : o.getItems()) {
            var foundProduct = products.stream()
                    .filter(p1 -> i.getProduct().getId() == p1.getId())
                    .findFirst();

            if (foundProduct.isPresent()) {
                Product p = foundProduct.get();
                sb.append(p.getNume()).append(": ")
                        .append(p.getPret()).append(" x ")
                        .append(i.getQuantity()).append(" = ")
                        .append(i.getTotal()).append(" RON\n");
            } else {
                sb.append("[Produs inexistent ID: ").append(i.getProduct().getId()).append("]\n");
            }
        }

        sb.append("---------------------\nTOTAL: ").append(o.getTotalPrice()).append(" RON\n=====================\n");
        return sb.toString();
    }
}