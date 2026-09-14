package com.portwire;

public class Router {

    public HttpResponse route(HttpRequest request) {
        String path = request.getPath();

        return switch (path) {
            case "/" -> HttpResponse.ok(
                    "<h1>Yo</h1>"
            );
            case "/hello" -> HttpResponse.ok(
                    "<h1>Yep</h1><p>Server is running.</p>"
            );
            case "/about" -> HttpResponse.ok(
                    "<h1>About</h1><p>Built with pure Java.</p>"
            );
            case "/oreo" -> HttpResponse.ok(
                    "<h1>Oreo</h1><p>It's my cat, and I love him.</p>"
            );
            default -> HttpResponse.notFound();
        };
    }
}
