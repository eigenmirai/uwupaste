package xyz.mirai666.uwupaste.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Language {
    private int id;
    private String name;
    private String prettyName;
    private String color;

    public static Map<String, Language> languageMap = new HashMap<String, Language>() {{
        put("plaintext", new Language(99, "plaintext", "Plain Text", "#6C757D"));
        put("c", new Language(0, "c", "C", "#555555"));
        put("csharp", new Language(1, "csharp", "C#", "#178600"));
        put("cpp", new Language(2, "cpp", "C++", "#f34b7d"));
        put("java", new Language(3, "java", "Java", "#b07219"));
        put("py", new Language(4, "py", "Python", "#3572A5"));
        put("rust", new Language(5, "rust", "Rust", "#dea584"));
        put("go", new Language(6, "go", "Go", "#375eab"));
        put("css", new Language(7, "css", "CSS", "#563d7c"));
        put("html", new Language(8, "html", "HTML", "#e44b23"));
        put("json", new Language(9, "json", "JSON", "#40d47e"));
        put("javascript", new Language(10, "javascript", "JavaScript", "#f1e05a"));
        put("typescript", new Language(11, "typescript", "TypeScript", "#2b7489"));
        put("kotlin", new Language(12, "kotlin", "Kotlin", "#F18E33"));
    }};
}
