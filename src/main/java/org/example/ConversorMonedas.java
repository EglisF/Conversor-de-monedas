package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorMonedas {

    // API KEY
    private static final String API_KEY = "34cc58ff329c45382b44e064";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        int opcion = 0;

        System.out.println("*************************************************");
        System.out.println("   BIENVENIDO AL CONVERSOR DE MONEDAS JAVA");
        System.out.println("*************************************************");

        while (opcion != 9) {
            System.out.println("\nIngresa la conversión que deseas realizar:");
            System.out.println("1. Dólar => Peso Argentino");
            System.out.println("2. Peso Argentino => Dólar");
            System.out.println("3. Dólar => Real Brasileño");
            System.out.println("4. Real Brasileño => Dólar");
            System.out.println("5. Dólar => Peso Colombiano");
            System.out.println("6. Peso Colombiano => Dólar");
            System.out.println("7. Dólar => Peso Dominicano"); // ¡Agregado!
            System.out.println("8. Peso Dominicano => Dólar"); // ¡Agregado!
            System.out.println("9. Salir");
            System.out.print("Elija una opción válida: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
                        convertir(client, "USD", "ARS", scanner);
                        break;
                    case 2:
                        convertir(client, "ARS", "USD", scanner);
                        break;
                    case 3:
                        convertir(client, "USD", "BRL", scanner);
                        break;
                    case 4:
                        convertir(client, "BRL", "USD", scanner);
                        break;
                    case 5:
                        convertir(client, "USD", "COP", scanner);
                        break;
                    case 6:
                        convertir(client, "COP", "USD", scanner);
                        break;
                    case 7:
                        convertir(client, "USD", "DOP", scanner);
                        break;
                    case 8:
                        convertir(client, "DOP", "USD", scanner);
                        break;
                    case 9:
                        System.out.println("¡Gracias por usar el conversor! Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: Ingresa solo números enteros.");
                scanner.nextLine(); // Limpiar buffer si hay error
            }
        }
    }

    // Método auxiliar
    private static void convertir(HttpClient client, String monedaBase, String monedaDestino, Scanner scanner) {
        System.out.println("--- Conversión de " + monedaBase + " a " + monedaDestino + " ---");
        System.out.print("Ingrese la cantidad a convertir: ");
        double cantidad = scanner.nextDouble();

        String direccion = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + monedaBase;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (jsonObject.get("result").getAsString().equals("success")) {
                double tasa = jsonObject.getAsJsonObject("conversion_rates").get(monedaDestino).getAsDouble();
                double resultado = cantidad * tasa;

                System.out.printf(">>> El valor %.2f [%s] corresponde al valor final de =>>> %.2f [%s]%n",
                        cantidad, monedaBase, resultado, monedaDestino);
            } else {
                System.out.println("Error al obtener la tasa de cambio.");
            }

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}