package br.com.adautomoises;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(
                            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                            System.getenv("MODEL_ID"),
                            System.getenv("API_KEY")
                    )))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(getJsonRequestBody()))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response -> \n" + httpResponse.body());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getJsonRequestBody() throws IOException, URISyntaxException {
        Locale.setDefault(Locale.US);
        return String.format("""
                        {
                            "contents": [
                                {
                                    "parts": [
                                        {
                                            "text": "%s"
                                        }
                                    ]
                                }
                            ],
                            "generationConfig": {
                                "temperature": %.1f,
                                "topK": %d,
                                "topP": %.1f,
                                "candidateCount": %d
                            }
                        }
                        """,
                getPrompt().replace("\"", "\\\""),
                Double.parseDouble(System.getenv("TEMPERATURE")),
                Integer.parseInt(System.getenv("TOP-K")),
                Double.parseDouble(System.getenv("TOP-P")),
                Integer.parseInt(System.getenv("CANDIDATE_COUNT"))
        );
    }

    private static String getPrompt() {
        System.out.println("Digite um comando:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}